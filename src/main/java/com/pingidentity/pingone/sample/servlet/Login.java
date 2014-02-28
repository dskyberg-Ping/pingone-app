package com.pingidentity.pingone.sample.servlet;


import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pingidentity.pingone.sample.authn.Authenticator;
import com.pingidentity.pingone.sample.authn.PasswordAuthenticator;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Login.class.getName());
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("serial")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
		logger.info(String.format("Login called with username=[%s] password=[%s]", username, password));
        Authenticator auth = new PasswordAuthenticator();
        if(auth.authenticate(request,new HashMap<String,String>(){
        	{
        		put(Authenticator.USERNAME_CRED,username);
        		put(Authenticator.PASSWORD_CRED,password);
        	}})== true){
    		RequestDispatcher disp = getServletContext(  ).getRequestDispatcher("/index.jsp");
    		disp.forward(request, response);       	
        } else{
			RequestDispatcher disp = getServletContext(  ).getRequestDispatcher("/login.jsp");
			disp.forward(request, response);        	
        }
	}
}
