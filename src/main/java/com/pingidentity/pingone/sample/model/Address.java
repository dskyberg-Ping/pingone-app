package com.pingidentity.pingone.sample.model;

public class Address {
	private int id;
	private String name;
	private String street1;
	private String street2;
	private String city;
	private String region;
	private String country;
	private String postal;
	
	public Address() {
		
	}

	public Address(int id, String name, String street1, String street2, String city,
			String region, String country, String postal) {
		super();
		this.id = id;
		this.name = name;
		this.street1 = street1;
		this.street2 = street2;
		this.city = city;
		this.region = region;
		this.country = country;
		this.postal = postal;
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

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}
	
}
