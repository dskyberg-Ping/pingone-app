<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<html lang="en">
  <head>
  	<%@ include file="templates/standard_head.jsp" %>
    <title>My Little Pony - SSO ERROR</title>
  </head>
<%@ include file="templates/standard_body_start.jsp"%>
	<div class="alert alert-warning">
  		<strong>SSO Failure: </strong>
	<%
		String error = (String)session.getAttribute("login_error");
		if(error != null && error.length() > 0) {
			session.removeAttribute("login_error");
	%>
		<%=error %>
	<% 
		} 
	%>
	</div>
	
<%@ include file="templates/standard_body_end.jsp" %>
</html>