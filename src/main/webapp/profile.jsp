<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.List" %>
   
<%@ include file="templates/authenticate.jsp" %>

<html lang="en">
  <head>
  	<%@ include file="templates/standard_head.jsp" %>
    <title>My Little Pony - Manage your Profile</title>
  </head>
<%@ include file="templates/standard_body_start.jsp" %>	
<%
	if(principal != null){
%>

	      <div class="app-body">
	      
	        <h1>Profile</h1>
	        
	        <form class="form-horizontal" role="form">
	        	<div class="form-group"> <!-- First name -->
	        		<label for="inputFName" class="col-sm-2 control-label">First Name</label>
	        		<div class="col-sm-10">
	        			<input type="text" class="form-control" id="inputFName" value="<%=principal.getFirstName() %>" />
	        		</div>
	        	</div> <!-- First name -->

	        	<div class="form-group"> <!-- Last name -->
	        		<label for="inpuLName" class="col-sm-2 control-label">Last Name</label>
	        		<div class="col-sm-10">
	        			<input type="text" class="form-control" id="inpuLName" value="<%=principal.getLastName() %>" />
	        		</div>
	        	</div> <!-- Last name -->

	        	<div class="form-group"> <!-- Last email -->
	        		<label for="inputEmail" class="col-sm-2 control-label">Email</label>
	        		<div class="col-sm-10">
	        			<input type="email" class="form-control" id="inputEmail" value="<%=principal.getEmail() %>" />
	        		</div>
	        	</div> <!-- Last email -->
 
 				<%
					Set<String> keys = principal.getAttributeKeys();
					for(String key : keys){
						if(principal.predefined(key)){
							continue;
						}
						StringBuilder sb = new StringBuilder();
						boolean multiple=false;
						List<String>values = principal.getAttributes(key);
						for(String value : values){
							if(multiple) {
								sb.append("; ");
							}
							sb.append(value);
							multiple=true;
						}
						
				%>
	        	<div class="form-group"> 
	        		<label for="input_<%=key %>" class="col-sm-2 control-label"><%=key %></label>
	        		<div class="col-sm-10">
	        			<input type="text" class="form-control" id="input_<%=key %>" value="<%=sb.toString() %>" readonly />
	        		</div>
	        	</div> <!-- First name -->
				
				<% 		
					}
				
				%>	        
 
				<div class="form-group"> <!-- Change password button -->
    				<div class="col-sm-offset-2 col-sm-10">
      					<button type="buton" class="btn btn-primary" data-toggle="modal" data-target="#myModal">Change your password</button>
    				</div>    			
  				</div>	 <!-- Change password button -->    				    	
	        </form> <!-- form-horizontal -->
	        
			 <!-- Modal Password change box -->
			<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			  <div class="modal-dialog">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			        <h4 class="modal-title" id="myModalLabel">Change your password</h4>
			      </div>
			      <div class="modal-body" id="changePasswordBody">
			       <form class="form-horizontal" role="form" id="passwordForm">
			        	<div class="form-group"> <!-- Current password -->
			        		<label for="inputCurrentPwd" class="col-sm-2 control-label">Current Password</label>
			        		<div class="col-sm-8">
			        			<input type="password" class="form-control" id="inputCurrentPwd"  />
			        		</div>
			        	</div> <!-- Current password -->
			        	<div class="form-group"> <!-- New password -->
			        		<label for="inputNewPwd" class="col-sm-2 control-label">New Password</label>
			        		<div class="col-sm-8">
			        			<input type="password" class="form-control" id="inputNewPwd"  />
			        		</div>
			        	</div> <!-- New Password -->
			        	<div class="form-group"> <!-- Repeat password -->
			        		<label for="inputRepeatwPwd" class="col-sm-2 control-label">Re-enter Password</label>
			        		<div class="col-sm-8">
			        			<input type="password" class="form-control" id="inputRepeatwPwd" />
			        		</div>
			        	</div> <!-- Repeat Password -->			       
			       </form>
			       <div id = "alert_placeholder"></div>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			        <button type="button" class="btn btn-primary" onClick='submitForm()'">Change Password</button>
			      </div>
			    </div><!-- /.modal-content -->
			  </div><!-- /.modal-dialog -->
			</div><!-- /.modal -->       
	        
	      </div> <!-- app-body -->
	      <script language ="javascript" type = "text/javascript" >
	      function submitForm() {
	    	  if ( $( "input#inputCurrentPwd" ).val() !== "<%=principal.getPassword()%>" ) {	    		  
	    		$('#alert_placeholder').html('<div class="alert alert-warning"><a class="close" data-dismiss="alert">×</a><span>'+'Your password was not correct'+'</span></div>')
	    	 	$( "input#inputCurrentPwd" ).val("");
	    		return;
	    	  }
	    	  var pwd = $("#inputNewPwd").val();
	    	  
	    	  if($("#inputNewPwd").val() !== $("#inputRepeatwPwd").val()){
	    		 $('#alert_placeholder').html('<div class="alert alert-warning"><a class="close" data-dismiss="alert">×</a><span>'+'New passwords did not match'+'</span></div>')	    		  
	    	  	 $("#inputNewPwd").val("");
	    		 $("#inputRepeatwPwd").val("");
	    		 return;
	    	  }
	    	  $("#myModal").modal("hide");
	      };
	      </script>
<%
	} else { 
%>
	<div class="app-body">	
		<div class="alert alert-info">You don not have a profile</div>
	</div>
<%
	}
%>
<%@ include file="templates/standard_body_end.jsp" %>	
</html>