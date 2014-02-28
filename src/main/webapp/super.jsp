<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.pingidentity.pingone.sample.authz.Authorize" %>
<%@ page import="com.pingidentity.pingone.sample.model.Account" %> 
<%@ page import="com.pingidentity.pingone.sample.model.DataManager" %>  
<%@ include file="templates/authenticate.jsp" %>

<html lang="en">
  <head>
  	<%@ include file="templates/standard_head.jsp" %>
    <title>My Little Pony - Super Admin</title>
  </head>
<%@ include file="templates/standard_body_start.jsp" %>	
<%
if(principal != null && Authorize.authorized("super",principal)){
%>

<div class="app-body">
	<div class="row">
		<div class="col-xs-3">
				<!-- Left side of the page -->
				<div class="panel">
				<div class="panel-header">Accounts</div>
				<div class="panel-body list-group">
			 	 <%
			  		DataManager dm = DataManager.getInstance();
			  		List<Account> accounts = dm.getAccountsList();
			  		boolean first = true;
			  		for(Account account : accounts){
			  			String classes = "list-group-item";
			  			if(first){
			  				classes += " active";
			  				first = false;
			  			}
			  				
			  	%>
			  		<a href="#" class="<%=classes %>" ><%=account.getName() %></a>
			 	 <% 		
			  		}
			  	%>
				</div>
				</div>
		</div> <!-- col -->
			
			<div class="col-xs-8">
				<!-- Right side of the page -->
				<form class="form-horizontal" role="form">
					<div class="checkbox">
				  		<label>
				   	 		<input type="checkbox" value="">
				   	 		Use SAML based Single Sign-on
				  		</label>
					</div>			
					<div class="form-group">
						<label for="ssoIdpId" class="col-sm-4 control-label">Entity Id</label>
						<input type="text" class="form-control col-sm-offset-4 col-sm-4" placeholder="Text input" name="ssoIdpId" id="ssoIdpId">
				</div>
			</form>
			</div> <!-- col -->
	</div> <!-- Row -->
</div>
<%
	} else { 
%>
	<div class="app-body">	
		<div class="alert alert-info">You must be a super admin to see this page</div>
	</div>
<%
	}
%>
<%@ include file="templates/standard_body_end.jsp" %>	
</html>