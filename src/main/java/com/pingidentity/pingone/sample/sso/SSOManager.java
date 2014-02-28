package com.pingidentity.pingone.sample.sso;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.pingidentity.pingone.sample.model.Principal;


/**
 * The SSOManager class can do several things, depending on how SSO activities
 * are handled by the app.  In general, this class can:
 * 	1. Dereference a reference Id (tokenId) sent in the browser from PingOne
 *  2. Associate the SSO user with an internal user in the app's identity store
 *  3. Just In Time (JIT) provision a new user from the attributes that are provided 
 * @author dskyberg
 *
 */
public class SSOManager {
	private static Logger logger = Logger.getLogger(SSOManager.class.getName());
	//private static final String baseUrl = "https://sso.connect.pingidentity.com/sso/TXS/2.0/1/";
	private Client jerseyClient;
	private SSOConfig ssoConfig;
	
	/**
	 * Constructor.  Receives an instance of SSOConfig, which holds the
	 * REST parameters for calling the PingOne REST API.
	 * @param ssoConfig
	 */
	public SSOManager(SSOConfig ssoConfig) {
		this.ssoConfig = ssoConfig;
		jerseyClient = this.createRESTClient(ssoConfig, logger);
	}

	/**
	 * This routine calls the PingOne REST API to swap the reference Id
	 * passed in via the browser for a JSON set of attributes that tell
	 * us who the user is, who authenticated the user (what IdP), and
	 * how the user was authenticated.
	 * @param tokenId
	 * @return if success, then a Principal is returned.  If there was
	 * an error, then a String containing the error message is returned.
	 * 
	 * Note: As a precaution, every tokenId sent to your app
	 * should be dereferenced, even if the user has a valid session.  
	 * Doing this allows for several advantages:
	 *  * It ensures that the tokenId is properly disposed of. TokenIds
	 *    are one-time tokens. So dereferencing automatically invalidates
	 *    the token so that it cannot be re-used.
	 *  * Dereferencing every time allows you to keep local identity in
	 *    sync with the federated identity.  Ie, if attributes or roles are
	 *    updated at the IdP, you have instant knowledge.  
	 */
	public Object  dereferenceToken(String tokenId) {
		// Create a Jersey WebTarget to baseUrl/tokenId.  The baseUrl is in
		// the ssoConfig object.  Note: it must be terminated with '/'
		WebTarget resource = jerseyClient.target(ssoConfig.getBaseUrl()+tokenId);
		
		// Execute the GET, and request the response be in JSON format
		Response response = resource.request(MediaType.APPLICATION_JSON).get(Response.class);
		if(response.getStatus() == 200) {
			// Successful response back from PingOne.  Get the JSON Payload as a map,
			// and convert the map into a Principal
			Principal principal = map2Principal( response.readEntity(new GenericType<Map<String,Object>>() {})) ;
			// The Demo app consumes a Principal object for all authorization decisions.  
			// Create a Principal using the attributes returned from PingOne		
			return principal;
		} else {
			// Didn't get a successful response. So the return should be an error string.
			String string = response.readEntity(String.class);
			return string;
		}
	}
	
	/**
	 * See if the SSO user has a local identity.  If so, map to the principal.
	 * @param ssoPrincipal
	 * @return a new principal, if the SSO identity could be mapped.  Else null.
	 */
	public Principal associateWithLocalPrincipal(Principal ssoPrincipal) {
		Principal principal = null;
		
		return principal;
	}
	
	/**
	 * If the SSO identity does not exist in the local identity store, then
	 * add it.  
	 * @param principal
	 * @return the principal, updated with any local identity id's, etc.
	 */
	public Principal jitProvision(Principal principal) {
		
		return principal;
	}
	
	/****************************************************************
	 * 
	 * Helper functions
	 * 
	 ***************************************************************/

	/**
	 * By default, the app creates an insecure client for REST activity 
	 * with PingOne.  Meaning, the app does not do any SSL/TLS certificate
	 * checking to ensure the PingOne cert is presented.  
	 * 
	 * WARNING:  You should never put insecure code like this into production.
	 * For production, replace this function, and use a keystore that
	 * contains the PingOne certificate.
	 * 
	 * @param config
	 * @param logger
	 * @return an inscecure client for REST activity.
	 */
	private Client createRESTClient(SSOConfig config, Logger logger) {
		
		// Use local routines to establish an insecure REST client
		// that connects to any server.  These routines should NEVER
		// be used in a production app!
		SSLContext ctx = getSSLContext("TLS");
		HostnameVerifier hnv = getHostnameVerifier();
		
		// Create a Jersey client instance that uses TLS (HTTPS)
		Client client = ClientBuilder.newBuilder()
				.sslContext(ctx).hostnameVerifier(hnv).build();
		HttpAuthenticationFeature basicAuth = HttpAuthenticationFeature.basic(ssoConfig.getClientId(),ssoConfig.getClientSecret());
		// If HTTP Basic Auth credentials were provided in the config,
		// then set up the BasicAuth filter
		if (ssoConfig.getClientId() != null && ssoConfig.getClientSecret() != null) {
			client.register(basicAuth);
		}
		
		// Register the logger so we can watch the traffic between
		// PingOne and our app
		client.register(JacksonFeature.class).register(new LoggingFilter(logger, true));
		
		return client;
	}
	
	/**
	 * This routine maps the attributes returned from PingOne into
	 * a Principal object for the app to consume.
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "serial" })
	private Principal map2Principal(Map<String,Object> map) {
		
		Principal principal = new Principal();
		
		for(String key : map.keySet()) {
			Object values = map.get(key);	
			
			// The app looks for a "roles" attribute.  But the 
			// roles may be sent as a list of groups in a memberOf
			// attribute.  If so, just add them all to a "roles" attribute.
			if(key.equals("memberOf")) {
				key = "roles";
			}
			// Handle single valued pairs
			if(values instanceof String) {
				principal.setAttribute(key, (String)values);
			}
			// Handle multi-valued pairs
			if(values instanceof List ) {
				principal.setAttributes(key, (List<String>)values);
			}
		}
		// Our app uses email as the primary user key.  So the subject
		// returned by PingOne should be an email.  Just map it to the
		// email attribute in the Principal
		principal.setEmail((String)map.get("subject"));
		
		// If no roles were sent, add the "User" role so that the Principal
		// has at least the default role.
		if(principal.getRoles() == null || principal.getRoles().size() == 0) {
			principal.setRoles(new ArrayList<String>(){{add("Users");}});
		}
		
		return principal;
	}
	
	/****************************************************************
	 * 
	 * Client SSL handlers.  These routines ensure that the demo
	 * app does not have to deal with loading PingOne SSL server
	 * certs into a keystore.  All server certs are accepted by
	 * the REST client routines.
	 * 
	 ***************************************************************/

	private static SSLContext getSSLContext(String version) {
		TrustManager[] trustAllCerts = getTrustManager();
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance(version);

			// set up a TrustManager that trusts everything
			sslContext.init(null, trustAllCerts, new SecureRandom());

			return sslContext;
		} catch (KeyManagementException ex) {
			logger.log(Level.SEVERE,
					null, ex);
			return null;
		} catch (NoSuchAlgorithmException ex) {
			logger.log(Level.SEVERE,
					null, ex);
			return null;
		}
	}

	private static HostnameVerifier getHostnameVerifier() {
		HostnameVerifier hv = new HostnameVerifier() {

			@Override
			public boolean verify(String hostName, SSLSession sslSession) {
				return true;
			}
		};

		return hv;
	}

	private static TrustManager[] getTrustManager() {
		return new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };
	}

}
