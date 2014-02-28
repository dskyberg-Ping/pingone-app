package com.pingidentity.pingone.sample.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.pingidentity.pingone.sample.model.Account;
import com.pingidentity.pingone.sample.model.DataManager;

import java.util.logging.Logger;

@Path("company/{email}")
public class CompanyByEmail {
	private static final Logger logger = Logger.getLogger(CompanyByEmail.class.getName());
	private DataManager dataManager = DataManager.getInstance();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Account get(@PathParam("email") String email) {
    	logger.info(String.format("Called CompanyByEmail with [%s]",email));
    	// Parse the domain from the email
    	String domain = parseDomainFromEmail(email);
    	if(domain == null) {
    		String result = "parseDomainFromEmail returned null";
    		logger.fine(result);
    		//return Response.status(400).entity(result).build();
    		return null;
    	}
    	Account account = dataManager.getAccountByDomain(domain);
    	if(account == null) {
    		String result = "account was not found";
    		logger.fine(result);
    		//return Response.status(404).entity(result).build();
    		return null;
    		
    	}
        // return Response.status(201).entity(account).build();
    	return account;
    	//put(Entity.json(new GenericEntity<JAXBElement<Customer>>(factory.createCustomer(customer)) {}))
    }
	
    private String parseDomainFromEmail(String email) {
    	int atIdx = email.indexOf('@');
    	if(atIdx == -1) {
    		logger.warning("Could not parse email.  No '@' symbol found");
    		return null;
    	}
    	return email.substring(atIdx+1);
    }
}
