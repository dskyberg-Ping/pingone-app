package com.pingidentity.pingone.sample.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;


public class Principal implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1981682290874672278L;

	private Attributes attributes = new Attributes();
	
	/*
	 * Common attributes
	 */
	public final static String FIRST_NAME="firstName";
	public final static String LAST_NAME="lastName";
	public final static String EMAIL="email";
	public final static String COMPANY="company";
	public final static String AUTHN_METHOD="authnMethod";
	public final static String PASSWORD="password";
	public final static String ROLES="roles";
	
	@SuppressWarnings("serial")
	private List<String> predefines = new ArrayList<String>() {
		{ 
			add(FIRST_NAME);
			add(LAST_NAME);
			add(EMAIL);
			add(PASSWORD);
			}};
; 
	
	public Principal(String firstName, String lastName, String email,
			String password, String company, List<String> roles) {
		super();
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setEmail(email);
		this.setPassword(password);
		this.setCompany(company);
		this.setRoles(roles);
	}
	
	public Principal(){
	}

	public boolean predefined(String key) {
		if(predefines.contains(key))
			return true;
		return false;
	}
	
	public String getFirstName() {
		return attributes.getSingleValue(FIRST_NAME);
	}

	public void setFirstName(String firstName) {
		this.attributes.putSingleValue(FIRST_NAME, firstName);
	}

	public String getLastName() {
		return attributes.getSingleValue(LAST_NAME);
	}

	public void setLastName(String lastName) {
		this.attributes.putSingleValue(LAST_NAME, lastName);
	}

	public String getEmail() {
		return attributes.getSingleValue(EMAIL);
	}

	public void setEmail(String email) {
		this.attributes.putSingleValue(EMAIL, email);
	}

	public String getPassword() {
		return attributes.getSingleValue(PASSWORD);
	}

	public void setPassword(String password) {
		this.attributes.putSingleValue(PASSWORD, password);
	}

	public String getCompany() {
		return attributes.getSingleValue(COMPANY);
	}

	public void setCompany(String company) {
		this.attributes.putSingleValue(COMPANY, company);
	}

	public void addRole(String role) {
		attributes.put(ROLES, role);
	}
	public List<String> getRoles() {
		if(attributes.containsKey(ROLES)) {
			return attributes.get(ROLES);
		} else {
			return new ArrayList<String>();
		}
	}

	public void setRoles(List<String> roles) {
		this.attributes.put(ROLES, roles);
	}	
	
	public List<String> getAttributes(String key){
		return attributes.get(key);
	}
	
	public String getAttribute(String key) {
		if(attributes.containsKey(key)) {
			return attributes.getSingleValue(key);
		}
		return "";
	}
	
	public void setAttributes(String key, List<String> values) {
		this.attributes.put(key, values);
	}
	
	public void setAttribute(String key, String value) {
		this.attributes.put(key, value);
	}
	
	public Set<String> getAttributeKeys() {
		return attributes.keySet();
	}
}
