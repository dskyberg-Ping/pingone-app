<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.pingidentity.pingone.sample.authz.Authorize" %>

<%@ include file="templates/authenticate.jsp" %>

<html lang="en">
  <head>
  	<%@ include file="templates/standard_head.jsp" %>
    <title>My Little Pony - Admin</title>
  </head>
<%@ include file="templates/standard_body_start.jsp" %>	
<%
	if(principal != null && Authorize.authorized("admin",principal)){
%>

	      <div class="app-body">
	        <h1>Administrator Page</h1>
	        <p class="lead">This is where you administer your My Little Pony account.<br> 
	        Someday, there will be actual things to administer here!</p>
	      </div>
<%
	} else { 
%>
	<div class="app-body">	
		<div class="alert alert-info">You must be an administrator to see this page</div>
	</div>
<%
	}
%>
<%@ include file="templates/standard_body_end.jsp" %>	
</html>