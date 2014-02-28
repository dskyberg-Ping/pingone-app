<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ page import="com.pingidentity.pingone.sample.model.Principal" %>  
 <%@ page import="com.pingidentity.pingone.sample.authn.Authenticator" %>  
 <% 
 	Principal principal = (Principal) session.getAttribute(Authenticator.PRINCIPAL_ATTR);
 %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
   <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar">3</span>
          </button>
          <a class="navbar-brand" href="#">My Little Pony</a>
        </div>
        <div class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li><a href="index.jsp">Home</a></li>
            <li><a href="admin.jsp">Admin</a></li>
            <li><a href="super.jsp">Super Admin</a></li>
          </ul>
         <% 
         	if(principal != null){
         %>          
          <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <%= principal.getFirstName() %> <%= principal.getLastName()%>
               <span class="glyphicon glyphicon-cog"></span><b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li><a href="profile.jsp">Manage your profile</a></li>
                <li class="divider"></li>
                <li><a href="Logout">Sign Off</a></li>
              </ul>
            </li>
          </ul>
        <%} %>
          
        </div><!--/.nav-collapse -->
      </div>
    </div>
