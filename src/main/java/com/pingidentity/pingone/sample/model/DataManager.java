package com.pingidentity.pingone.sample.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pingidentity.pingone.sample.servlet.AppConfig;

public class DataManager {
	private static final Logger logger = Logger.getLogger(DataManager.class.getName());
	private static DataManager instance=null;
	private static AppConfig config;
	private static final String DB_DRIVER = "database.driver";
	private static final String DB_URL = "database.url";
	private static final String DB_USER = "database.user";
	private static final String DB_PWD = "database.password";
	private Connection conn = null;

	// Various prepared statements
	private PreparedStatement selectUser = null;
	private PreparedStatement selectRoles = null;
	private PreparedStatement selectAccountByDomain = null;

	protected DataManager() {
		config = AppConfig.getInstance();
		try {
			String dbDriver = config.getProperty(DB_DRIVER);
			String dbUrl = config.getProperty(DB_URL);
			String dbUser = config.getProperty(DB_USER);
			String dbPwd = config.getProperty(DB_PWD);
			Class.forName(dbDriver);
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			
			// Create prepared statements
			selectAccounts = conn.prepareStatement(selectAccountsSQL);
		} catch (SQLException se) {
			logger.log(Level.SEVERE,null,se);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE,null, e);
		}
		
	}
	
	public static DataManager getInstance() {
		if(instance == null) {
			instance = new DataManager();
		}
		return instance;
	}
	
	public Connection getConnection() {
		return this.conn;
	}

	private final String selectAccountsSQL="SELECT * FROM Account";
	private PreparedStatement selectAccounts = null;

	public List<Account> getAccountsList() {
		List<Account> accounts = new ArrayList<Account>();
	
		try {
			ResultSet rs = selectAccounts.executeQuery();
			//ResultSetMetaData meta = rs.getMetaData();
			if (rs != null) {
				while(rs.next()) {
					Account account = new Account();
					account.setId(rs.getInt("id"));
					account.setName(rs.getString("name"));
					account.setSsoEnabled(rs.getBoolean("ssoEnabled"));
					if(account.isSsoEnabled()) {
						account.setSsoIdpId(rs.getString("ssoIdpId"));
					}
					accounts.add(account);
				}
			}
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE,null,e);
		}
		return accounts;
	}

	private static final String userSql = "SELECT User.id as id, User.firstName as firstName,"
			+ " User.lastName as lastName, User.email as email, User_Password.password as password,"
			+ " Account.name as company"
			+ " FROM User JOIN Account ON User.accountId=Account.id"
			+ " JOIN User_Password ON User_Password.userId=User.id"
			+ " WHERE email=?";
	private static final String rolesSql = "select Role.name FROM User_Role"
			+ " JOIN Role ON User_Role.roleId=Role.id"
			+ " WHERE User_Role.userId=?";
	
	public Principal getPrincipal(String email, boolean isSso) {
		Principal principal = null;
		if(conn == null) {
			logger.severe("Connection is null");
			return null;
		}
		try {
			// First time this is called, the prepared statements
			// will be created.  They will be reused after that.
			if(selectUser == null)
				selectUser = conn.prepareStatement(userSql);
			if(selectRoles == null)
			selectRoles = conn.prepareStatement(rolesSql);
			
			// Set up the select statement and run the query
			selectUser.setString(1, email);			
			ResultSet rs = selectUser.executeQuery();
			if (rs.next()) {
				// Found the user
				principal = new Principal();
				// Get the id so we can look up roles
				selectRoles.setInt(1, rs.getInt("id"));
				ResultSet roles_rs = selectRoles.executeQuery();
				principal.setFirstName(rs.getString("firstName"));
				principal.setLastName(rs.getString("lastName"));
				principal.setCompany(rs.getString("company"));
				principal.setEmail(rs.getString("email"));
				if(!isSso) {
					principal.setPassword(rs.getString("password"));
				}
				while (roles_rs.next()) {
					principal.addRole(roles_rs.getString("name"));
				}
			}			
			
		} catch (SQLException se) {
			se.printStackTrace();
		} 
		return principal;
	}
	
	private final String accountByDomainsSql=
			"select Account.name as name, Account.ssoEnabled as ssoEnabled, Account.ssoIdpId as ssoIdpId FROM Account_Domain"
			+ " JOIN Account ON Account_Domain.accountId=Account.id"
			+ " WHERE Account_Domain.domain=?";
	
	public Account getAccountByDomain(String domain) {
		Account account = null;
		if(conn == null) {
			logger.severe("Connection is null");
			return null;
		}
		try {
			if(selectAccountByDomain == null) {
				selectAccountByDomain = conn.prepareStatement(accountByDomainsSql);
			}
			
			selectAccountByDomain.setString(1, domain);			
			ResultSet rs = selectAccountByDomain.executeQuery();
			if (rs.next()) {
				logger.info("Found Account info");
				account = new Account();
				account.setName(rs.getString("name"));
				account.setSsoEnabled(rs.getBoolean("ssoEnabled"));
				account.setSsoIdpId(rs.getString("ssoIdpId"));
			}
			
		} catch (SQLException se) {
			if(account != null) {
				logger.info(String.format("Found account [%s], but error occurred.",account.getName()));
			}
			se.printStackTrace();
		} 
		return account;
	}
}
