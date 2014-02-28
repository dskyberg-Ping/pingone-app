package com.pingidentity.pingone.sample.sso;

public class SSOConfig {
	private String clientId;
	private String clientSecret;
	private String baseUrl;
	private String path;
	
	public SSOConfig(String clientId, String clientSecret, String baseUrl,
			String path) {
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.baseUrl = baseUrl; //     // https://sso.connect.pingidentity.com/sso/TXS/2.0/1/<tokenid>
		this.path = path;
	}
	public SSOConfig() {
		
	}
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
