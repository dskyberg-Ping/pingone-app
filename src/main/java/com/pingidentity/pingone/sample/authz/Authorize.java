package com.pingidentity.pingone.sample.authz;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.pingidentity.pingone.sample.model.Principal;
/**
 * Lots of ways to re-invent this wheel.  But I just want an easy way to 
 * centralize authorization so that it is not container specific.  That
 * way, I can throw the app under any container and it will just work.
 * 
 * I want the authorization capability to work off the Principal 
 * rather than off realms, etc..
 * 
 * For now, I'm just going to hard code the rules table.  Easy to
 * migrate to a database table later.
 * @author dskyberg
 *
 */
public class Authorize {

	@SuppressWarnings("serial")
	private static Map<String,List<String>> rules = new HashMap<String,List<String>>(){{
		// The rules
		put("index",new ArrayList<String>() {{add("Users");add("User");}});
		put("login",new ArrayList<String>() {{add("All");}});
		put("profile",new ArrayList<String>() {{add("All");}});
		put("admin",new ArrayList<String>() {{add("Admin");add("Domain Administrators");}});
		put("super",new ArrayList<String>() {{add("Super");}});
	}};
	
	public static boolean authorized(String resource, Principal principal) {
		
		if(!rules.containsKey(resource)) {
			return false;
		}
		
		List<String> authZGrants = rules.get(resource);
		if(authZGrants.contains("All")) {
			return true;
		}
		
		for(String role : principal.getRoles()) {
			for(String grant : authZGrants) {
				if(role.toLowerCase().equals(grant.toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}
	
}
