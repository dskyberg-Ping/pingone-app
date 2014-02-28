<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
  	<%@ include file="templates/standard_head.jsp" %>
    <title>My Little Pony Login</title>
	</head>  
<%@ include file="templates/standard_body_start.jsp"%>
	<%
		String error = (String)session.getAttribute("login_error");
		if(error != null && error.length() > 0) {
			session.removeAttribute("login_error");
	%>
	<div class="alert alert-warning">
  		<strong>Login Failure: </strong> Either the user name or password is incorrect	
	</div>
	
	<%} %>
		<!-- Modal -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		        <h4 class="modal-title" id="myModalLabel">Federated SSO</h4>
		      </div>
		      <div class="modal-body" id="modelBody">
		       
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		        <button type="button" class="btn btn-primary">Sign In</button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div><!-- /.modal -->	
		
      <form id="loginForm" class="form-signin" action="Login" method="post">
        <h2 class="form-signin-heading">Please sign in</h2>
		<div class="alert alert-info">
		Enter your email.  We will check to see if you are a federated user.  If not, you can enter your password.  
		If so, we will send you to your IdP for authentication.
		</div>
        <input class="form-control" id="username" name="username" placeholder="Your Email address" required="" autofocus="" type="text">
        <input class="form-control" id="password" name="password"  placeholder="Password" required="" type="password" >
        <label class="checkbox">
          <input value="remember-me" type="checkbox"> Remember me
        </label>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      </form> 
      <p/>
      <p/>
	<div class="alert alert-info">
	<center><strong>Try one of these</strong></center>
	<strong>Admin:</strong> joe@customer.com, 2Federate<br/>
	<strong>User:</strong> sue@customer.com, 2Federate<br/>
	</div>
<%@ include file="templates/standard_body_end.jsp" %>
  	<script type="text/javascript">
	$(document).ready(function() {
		$("#username").on("change",function(){
			$.get( "rest/company/" + encodeURIComponent($("#username").val()) , function( data ) {

				if(data.ssoEnabled == false){
					$("#modelBody").html("You are federated");
					$('#myModal').modal('show')
					
				}
			})
			 .fail(function(  jqXHR,  textStatus,  errorThrown ) {
				 alert("Failed to get info");
				 $("password").removeAttr('disabled');
			});
		});
		
		$('#myModal').on('hidden.bs.modal', function (e) {
			  alert("Sending you to your IdP");
			})

	});
	</script>
</html>