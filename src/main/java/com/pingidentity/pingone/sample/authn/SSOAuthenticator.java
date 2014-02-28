package com.pingidentity.pingone.sample.authn;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pingidentity.pingone.sample.model.Principal;
import com.pingidentity.pingone.sample.servlet.AppConfig;
import com.pingidentity.pingone.sample.sso.SSOConfig;
import com.pingidentity.pingone.sample.sso.SSOManager;

public class SSOAuthenticator extends Authenticator{
	private static final Logger logger = Logger.getLogger(PasswordAuthenticator.class.getName());
	private AppConfig config = AppConfig.getInstance();
	public SSOAuthenticator() {}
	
	/**
	 * Attemps to authenticate the user. If the user successfully authenticates,
	 * a Principal will be added to the session, along with the 'authenticated'
	 * attribute if the user properly authenticates.
	 * 
	 * If the user fails to authenticate, then Logout is called.
	 * 
	 * @param request
	 *            the HttpServletRequest
	 * @param username
	 *            the user name provided by the user
	 * @param password
	 *            the password provided by the user
	 * @return True if the user successfully authenticates. False otherwise
	 */
	@Override
	public boolean authenticate(HttpServletRequest request, Map<String,String>credentials) {
		HttpSession session = request.getSession(true);
		String tokenId = credentials.get(Authenticator.TOKEN_ID_CRED);
		SSOConfig ssoConfig = getSSOConfig();
		SSOManager authN = new SSOManager(ssoConfig);
	
		// A tokenId was passed on the browser re-direct.
		// call PingOne and pass the tokenId on the URL.  
		// PingOne will return the attributes associated
		// with the tokenId and then invalidate the token
		// so that it cannot be re-used.
		Object obj = authN.dereferenceToken(tokenId);
				
		// General failure.  So, no authentication. Be
		// sure to clean up any session that may exist.
		if (obj == null) {
			session.setAttribute(AUTHENTICATED_ATTR, false);
			session.removeAttribute(PRINCIPAL_ATTR);
			session.setAttribute(LOGIN_ERROR_ATTR, "SSO Failed");
			logger.info("Authentication attempt failed to dereference the supplied token");
			return false;
		}
		// Error message returned.  The user is not
		// authenticated.  Put the error in the session so
		// that it can be displayed.  Remove the principal and
		// the AUTHENTICATED session attribute.
		if (obj instanceof String) {
			session.setAttribute(AUTHENTICATED_ATTR, false);
			session.removeAttribute(PRINCIPAL_ATTR);
			session.setAttribute(LOGIN_ERROR_ATTR, (String)obj);
			return false;
		} 
		// Authentication succeeded.  Note in the session that
		// the current user is authenticated.  Store the Principal,
		// and remove any lingering error messages.
		session.setAttribute(AUTHENTICATED_ATTR, new Boolean(true));
		session.setAttribute(PRINCIPAL_ATTR, (Principal)obj);
		session.removeAttribute(LOGIN_ERROR_ATTR);
		return true;			
	}
	
	/**
	 * Grab all the parameters that the Authenticate routine will need
	 * These are currently stored in the AppConfig object.
	 * @return
	 */
	private SSOConfig getSSOConfig() {
		SSOConfig ssoConfig = new SSOConfig();
		
		ssoConfig.setClientId( config.getProperty(AppConfig.REST_API_CLIENT_ID));
		ssoConfig.setClientSecret( config.getProperty(AppConfig.REST_API_CLIENT_SECRET));
		ssoConfig.setBaseUrl( config.getProperty(AppConfig.REST_API_BASE_URL));
		
		return ssoConfig;
	}
}
