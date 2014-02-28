<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.pingidentity.pingone.sample.authn.Authenticator" %>
<%
	if(Authenticator.isAuthenticated(request) == false){
		pageContext.forward("/login.jsp");	   
	}
%>    
    