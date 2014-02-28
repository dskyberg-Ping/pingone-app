package com.pingidentity.pingone.sample.authn;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class Authenticator {
	public static final String PASSWORD_CRED ="password";
	public static final String USERNAME_CRED ="username";
	public static final String TOKEN_ID_CRED = "tokenId";
	public final static String AUTHENTICATED_ATTR = "authenticated";
	public final static String PRINCIPAL_ATTR = "principal";
	public final static String LOGIN_ERROR_ATTR = "login_error";
	
	public boolean authenticate(HttpServletRequest request, Map<String,String>credentials) {
		return false;
	}
	
	/**
	 * Tests to see if the current session user is authenticated
	 * 
	 * @param request
	 *            the HttpServletRequest
	 * @return True if the user is authenticated. False otherwise
	 */
	public static boolean isAuthenticated(HttpServletRequest request) {
		Boolean isNotValid = new Boolean(false);

		// Get the session.  Don't create one if it doesn't exist
		HttpSession session = request.getSession(false);
		// If there is no session, the user can't be authenticated
		if (session == null) {
			return isNotValid;
		}
		
		try {
			// Look to see if the session has an AUTHENTICATED attr
			Boolean isAuthenticated = (Boolean) session
					.getAttribute(AUTHENTICATED_ATTR);
			if (isAuthenticated == null) {
				// Nope.  So return false
				return isNotValid;
			}
			// Just return the value of the session attribute
			return isAuthenticated;
		} catch (IllegalStateException e) {
			return isNotValid;
		}
	}
	
	/**
	 * Logs the current user out and revokes the session.
	 * 
	 * @param request
	 */
	public static void invalidate(HttpServletRequest request) {
			HttpSession session = request.getSession(false);
			if (session == null) {
				return;
			}
			try {
				session.removeAttribute(AUTHENTICATED_ATTR);
				session.removeAttribute(PRINCIPAL_ATTR);
				session.invalidate();
			} catch (IllegalStateException e) {
				return;
			}
	}
}
