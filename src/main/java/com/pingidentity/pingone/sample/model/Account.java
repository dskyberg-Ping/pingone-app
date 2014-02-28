package com.pingidentity.pingone.sample.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Account implements Serializable{
	private static final long serialVersionUID = -2600527720027451991L;
	private int id;
	private String name;
	private boolean ssoEnabled;
	private String ssoIdpId;	
	private List<Address> addresses = new ArrayList<Address>();
	
	public Account() {
		
	}
	
	public Account(int id, String name, boolean ssoEnabled, String ssoIdpId,
			List<Address> addresses) {
		super();
		this.id = id;
		this.name = name;
		this.ssoEnabled = ssoEnabled;
		this.ssoIdpId = ssoIdpId;
		this.addresses = addresses;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSsoEnabled() {
		return ssoEnabled;
	}

	public void setSsoEnabled(boolean ssoEnabled) {
		this.ssoEnabled = ssoEnabled;
	}

	public String getSsoIdpId() {
		return ssoIdpId;
	}

	public void setSsoIdpId(String ssoIdpId) {
		this.ssoIdpId = ssoIdpId;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
		
}
