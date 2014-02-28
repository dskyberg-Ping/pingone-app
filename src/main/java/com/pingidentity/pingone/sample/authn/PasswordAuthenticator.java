package com.pingidentity.pingone.sample.authn;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pingidentity.pingone.sample.model.DataManager;
import com.pingidentity.pingone.sample.model.Principal;

public class PasswordAuthenticator extends Authenticator{
	private static final Logger logger = Logger.getLogger(PasswordAuthenticator.class.getName());
	

	public PasswordAuthenticator() {
		
	}
	
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
		DataManager dataManager = DataManager.getInstance();
		HttpSession session = request.getSession(true);
		String user = credentials.get(Authenticator.USERNAME_CRED);
		String password = credentials.get(Authenticator.PASSWORD_CRED);
		logger.info(String.format("PasswordAuthenticator.authenticate called with user[%s] password[%s]", user, password));
		
		Principal principal = dataManager.getPrincipal(user, false);
		// User does not exist
		if (principal == null) {
			session.setAttribute(AUTHENTICATED_ATTR, false);
			session.removeAttribute(PRINCIPAL_ATTR);
			session.setAttribute(LOGIN_ERROR_ATTR, "Login Failed.  Try again");	
			logger.info("Could not find the user in the database");
			return false;
		}
		// Password is bad
		if (!principal.getPassword().equals(password)) {
			session.setAttribute(AUTHENTICATED_ATTR, false);
			session.removeAttribute(PRINCIPAL_ATTR);
			session.setAttribute(LOGIN_ERROR_ATTR, "Login Failed.  Try again");
			logger.info(String.format("PasswordAuthenticator: passwords did not match [%s] [%s]", password, principal.getPassword()));
			return false;
		} 
		// authentication succeeded
		session.setAttribute(AUTHENTICATED_ATTR, new Boolean(true));
		session.setAttribute(PRINCIPAL_ATTR, principal);
		session.removeAttribute(LOGIN_ERROR_ATTR);
		logger.info(String.format("PasswordAuthenticator.authenticate: user was authenticated", user, password));
		return true;		
	}
}
