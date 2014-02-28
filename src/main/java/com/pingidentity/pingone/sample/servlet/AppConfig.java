package com.pingidentity.pingone.sample.servlet;

//import java.util.Properties;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import util.ResourceUtils;
import jodd.props.Props;

public class AppConfig {
	private static AppConfig instance=null;
	//private Properties props = new Properties();
	private Props props = new Props();
	
    public static final String REST_API_CLIENT_ID="pingone.rest.client";
    public static final String REST_API_CLIENT_SECRET = "pingone.rest.secret";
    public static final String REST_API_BASE_URL = "pingone.rest.baseurl";
	public static final String LOG_LEVEL = "app.logger.level";
	public static final String APP_SSSO_SUCCESS_REDIRECT="app.sso.success.redirect";
	public static final String APP_SSO_FAILURE_REDIRECT="app.sso.failure.redirect";
	
	private AppConfig() {
		// Privatize for singleton behavior
		
		String localPath = null;
		// First try to load from the class path
		//String appName = ResourceUtils.getAppName();
		String appName = "PingOneApp";
		InputStream configPath = ResourceUtils.getInstance().find(appName+".properties");
		if(configPath == null ) {
			// Oh well.  No app.properties found.
			
			System.out.println(String.format("AppConfig error: No %s.properties found on class path, in ./%s.properties, or in ./resources/%s.properties, or in the env variable %s.resources",
					appName,appName,appName,appName));
			return;
		}
		try{
			load(configPath);
		}catch(IOException ex){
			System.out.println("Found app.properties, but could not load the file.");
		}
	}
	
	public static AppConfig getInstance() {
		if(instance == null) {
			// Initialize singletone
			instance = new AppConfig();
		}
		return instance;
	}
	
	public String getProperty(String key) {
		if(key != null)
			return props.getValue(key);
		else
			return null;
	}
	public String getProperty(String key, String defaultValue) {
		return props.getValue(key, defaultValue);
	}
	public void setProperty(String key, String value) {
		props.setValue(key, value);
	}
	
	public void load(String path) throws IOException {
		//File file = new File(path);
		props.load(path);
	}
	public void load(File file) throws IOException {
		props.load(file);
	}
	public void load(InputStream stream) throws IOException {
		props.load(stream);
	}
}
