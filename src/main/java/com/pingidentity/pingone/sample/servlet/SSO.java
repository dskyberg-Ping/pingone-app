package com.pingidentity.pingone.sample.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pingidentity.pingone.sample.authn.Authenticator;
import com.pingidentity.pingone.sample.authn.SSOAuthenticator;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Servlet implementation class SSO
 */
@WebServlet("/sso")
public class SSO extends HttpServlet {
	private static final Logger logger = Logger.getLogger(SSO.class.getName());
	private static final long serialVersionUID = 1L;
	private AppConfig config;
	private static final String TOKEN_ID_PARAM = "tokenid";
	private String errorPath=null;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SSO() {
		super();
		config = AppConfig.getInstance();
		errorPath = config.getProperty(AppConfig.APP_SSO_FAILURE_REDIRECT, "/sso_error.jsp");
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGetOrPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGetOrPost(request, response);
	}

	/**
	 * This Servlet will be called whenever someone is redirected to the app via
	 * IdP-Init. A tokenId will be in the query parameters.
	 * 
	 * The tokenId is actually a reference identifier that points to the
	 * attribute data associated with this event in PingOne. The tokenId is
	 * passed to PingOne via a back-channel, ReST call. The call will return a
	 * JSON structure containing the user attributes of the authenticated user,
	 * along with attributes about who the IdP is and how the user was actually
	 * authenticated.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@SuppressWarnings("serial")
	private void doGetOrPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		final String tokenId = getTokenFromRequest(request, response);

		if (tokenId == null) {
			logger.severe("No tokenId was sent");
			return;
		}
		logger.info("Received token id: " + tokenId);

		Authenticator auth = new SSOAuthenticator();
		if (auth.authenticate(request, new HashMap<String, String>() {
			{
				put(Authenticator.TOKEN_ID_CRED, tokenId);
			}
		}) == true) {
			String path = config.getProperty(AppConfig.APP_SSSO_SUCCESS_REDIRECT, "/index.jsp");
			logger.fine("Authenticator returned success. Redirecting to "+path);
			RequestDispatcher disp = getServletContext().getRequestDispatcher(path);
			disp.forward(request, response);
		} else {
			String err = "Authenticator returned failure.  Redirecting to " + errorPath;
			logger.warning(err);
			sendError(request, response, err);
		}
	}

	private String getTokenFromRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		//HttpSession session = request.getSession(true);
		String tokenId = request.getParameter(TOKEN_ID_PARAM);
		if (tokenId == null) {
			String err = "No token id was provided";
			logger.warning(err);
			sendError(request, response, err);
			return null;
		} else if ("".equals(tokenId)) {
			String err = "Empty token id was recieved";
			logger.warning(err);
			sendError(request, response, err);
			return null;
		}
		return tokenId;
	}

	private void sendError(HttpServletRequest request,
			HttpServletResponse response, String error)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		session.setAttribute(Authenticator.LOGIN_ERROR_ATTR, error);
		RequestDispatcher disp = getServletContext().getRequestDispatcher(errorPath);
		disp.forward(request, response);
	}

}
