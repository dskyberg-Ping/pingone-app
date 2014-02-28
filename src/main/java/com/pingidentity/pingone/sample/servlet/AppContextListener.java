package com.pingidentity.pingone.sample.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class AppContextListener implements ServletContextListener{
	private static final Logger logger = Logger.getLogger(AppContextListener.class.getName());
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// Set up the config
		logger.fine("AppContextListener is being applied");

		AppConfig config = AppConfig.getInstance();
		
		
		//config.load(configPath);
		logger.fine("config file loaded. ");
		
		// Set the log level from config file
		setLogLevel();
		
		// Rectify the database file path in the database url
		String dbPath = event.getServletContext().getRealPath(config.getProperty("database.path"));
		config.setProperty("database.path",dbPath);
		String dbUrl = config.getProperty("database.url").replace("{path}", dbPath);
		config.setProperty("database.url", dbUrl);
		dbUrl = config.getProperty("database.url");
		logger.fine("Database path set in config: " + dbUrl);
	}

	private void setLogLevel() {
		String logLevel = AppConfig.getInstance().getProperty(AppConfig.LOG_LEVEL);
		Logger root = Logger.getLogger("com.pingidentity.pingone.sample");
		
		if(logLevel == null || logLevel.length() == 0) {
			root.setLevel(Level.FINE);
			return;
		}	
		 if(logLevel.toUpperCase().equals("FINE"))
			root.setLevel(Level.FINE);
		else if(logLevel.toUpperCase().equals("FINER"))
			root.setLevel(Level.FINER);
		else if(logLevel.toUpperCase().equals("FINEST"))
			root.setLevel(Level.FINEST);
		else if(logLevel.toUpperCase().equals("INFO"))
			root.setLevel(Level.INFO);
		else if(logLevel.toUpperCase().equals("OFF"))
			root.setLevel(Level.OFF);
		else if(logLevel.toUpperCase().equals("SEVERE"))
			root.setLevel(Level.SEVERE);
		else if(logLevel.toUpperCase().equals("WARNING"))
			root.setLevel(Level.WARNING);
		else
			root.setLevel(Level.ALL);

	}
}
