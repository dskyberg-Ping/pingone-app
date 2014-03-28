package com.pingidentity.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ResourceUtils class will attempt to locate a resource folder somewhere
 * the first time a resource is requested.  The places searched, in order are:
 * 1. Command line
 * 2. Environment: looks for <app name>.resources variable
 * 3. Home Directory: looks for a directory under the user's home called <app name>/resources
 * 4. Resources under CWD: looks for <current working directory>/resources
 * 5. CWD: The above are all tested at startup.  If none exists, the CWD of the jar or class will be
 * tested.
 * 6. ClassPath: If the above all fail, the class path will be tested.
 * 
 * Once a resource is found in one of the above locations/methods, that is remembered,
 * so that future resource requests execute faster.
 * 
 * @author dskyberg
 *
 */
public class ResourceUtils {
	private static final Logger logger = Logger.getLogger(ResourceUtils.class.getPackage()
			.getName());
	private  String appName=null;
	private String foundPath=null;
	private String localPath=null;
	private boolean useClassPath = false;
	
	private static ResourceUtils instance=null;	

	/**
	 * We don't need to synchronize this because the constructor
	 * call is synched in getInstance.
	 */
	private ResourceUtils() {
		// Look in all the usual places for where resources might be found
		findPath();
	}
	
	public static ResourceUtils getInstance(){
			if(instance == null){
				instance = new ResourceUtils();
			}
		return instance;
	}
	
	/**
	 * This method attempts to provide the name of the running application.
	 * If the main app is in a jar file, then the name of the jar file is
	 * returned.  If the main app is running from a bin folder (such as
	 * from within an IDE, then the parent of the bin is returned.
	 * @return
	 */
	public String getAppName(){
		if(appName == null){
			appName = getAppNameFromJar(getMainClass());
			if(appName == null) {
				appName = getAppNameFromWebApp(getMainClass());
			}
		}
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	/**
	 * Bootstrap the discovery process by seeing if the user has either 
	 * identified a location (via environment or commmand line, or if 
	 * a typical location exists.
	 * 
	 * 1. Command line
	 * 2. Environment: looks for <app name>.resources variable
	 * 3. Home Directory: looks for a directory under the user's home called <app name>/resources
	 * 4. Resources under CWD: looks for <current working directory>/resources

	 */
	private void findPath(){
		// This routine looks to see if a path has been declared anywhere
		// and remembers that path.
		
		// Was there anything on the command line?
		String path = lookInCommandLine();
		if(path != null){
			// Found it
			foundPath = path;
			return;
		}
		
		// If not, is there an environment variable?
		path = lookForEnvPath(getAppName() + ".resources");
		if(path != null){
			// Found it
			foundPath = path;
			return;
		}
		
		// If not, is there a resources folder in the current directory?
		path = lookInHome();
		if(path != null){
			// Found it
			foundPath = path;
			return;
		}
		
		// Is there an app resource directory under the user's home directory?
		path = lookInHome();
		if(path != null){
			// Found it
			foundPath = path;
			return;
		}
		
	}

	private String lookInHome(){
		String path = System.getenv("user.home");
		if (path == null) {
			return null;
		}
		// Found the user's home directory. See if an app folder exists
		path = path + File.separator + getAppName() + File.separator + "resources";
		File filePath = new File(path);
		if(!filePath.exists()){
			// Oops! The resources folder doesn't seem to exist
			logger.warning("Found env variable, but path does not exist: " + path);
			return null;
		}
		return path;
	}
	
	/**
	 * See if the env variable commandLine is present. If so, see if there is a
	 * "resource" key. If so, get the value
	 * 
	 * @return
	 */
	private String lookInCommandLine() {
		String cli = System.getenv("commandLine");
		if (cli == null) {
			return null;
		}
		String[] args = stringToArgs(cli);
		if (args == null) {
			logger.warning("Found commandLine in the env, but no data: " + cli);
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("resources")) {
				String path = args[i + 1];
				File file = new File(path);
				if (file.exists()) {
					return path;
				}
			}
		}
		return null;
	}
	
	private InputStream lookInClassPath(String fileName){
		InputStream stream = ResourceUtils.class.getClassLoader()
				.getResourceAsStream(fileName);
		if (stream != null) {
			synchronized(this){
				useClassPath = true;
			}
		}	
		return stream;
	}
	/**
	 * Utility function to determine if a path is specified
	 * in a given env variable.  
	 * @param envName
	 * @return A path string, if it exists.  Else null.
	 */
	private String lookForEnvPath(String envName) {
		String path = System.getenv(envName);
		if(path == null){
			return null;
		}
		// Stated in env.  Does it exist?
		File filePath = new File(path);
		if(filePath.exists()){
			foundPath = path;
			return path;
		}
		return null;
	}

	

	/*
	 * First sees if a resource path was specified at startup. 
	 * If not, then the CWD is attempted.  
	 * Otherwise, the classpath is attempted.
	 */
	public  InputStream find(String fileName) {
		InputStream stream = null;
		String path = null;
		// See if a path was provided at startup
		if(foundPath != null){
			path = foundPath + File.separator + fileName;
			stream = getFileStream(path);
			if(stream != null){
				logger.fine("Found resource: " + path);
				return stream;
			}
		}
		// The resource was not in the discovered folders.
		// Try the local directory
		if(localPath == null){
			localPath = getLocalPath();
		}
		path = localPath + File.separator + fileName;
		stream = getFileStream(path);
		if(stream != null ){
			logger.fine("Found resource: " + path);
			return stream;
		}
		// Finally, look in the classpath
		stream = lookInClassPath(fileName);
		if(stream != null){
			logger.fine("Found resource on class path: " + fileName);
			return stream;
		}
		logger.warning("Did not find the requested resource: " + fileName);
		return null;
	}

	
	/**
	 * Utility function to return the local path that the Jar file
	 * resides in.
	 * @return
	 */
	private static String getLocalPath() {
		String path = ResourceUtils.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		try {
			String decodedPath = URLDecoder.decode(path, "UTF-8");
			int idx = decodedPath.lastIndexOf('/');
			return decodedPath.substring(0, idx);
		} catch (UnsupportedEncodingException e) {
			// Weird. The path returned did not decode correctly. Bounce!
			logger.warning("The local path did not decode properly");
			return null;
		}
	}
	
	/**
	 * Opens an InputStream in the file system
	 * @param fileName
	 * @return
	 */
	private static InputStream getFileStream(String fileName){
		try {
			InputStream stream = new FileInputStream(fileName);
			if (stream != null){
				return stream;
			}
		} catch (FileNotFoundException e) {
		}
		return null;
	}
	

	public static String readFileToString(String fileName){
		try {
			InputStream stream = new FileInputStream(new File(fileName));
			return readFileToString(stream);
		} catch (FileNotFoundException e) {
			logger.warning("Failed to open file: " + fileName);
		}
		return null;
	}
	
	public static String readFileToString(InputStream stream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder out = new StringBuilder();
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
			reader.close();
		} catch (IOException e) {
			logger.warning("Error reading stream");
		}
		return out.toString();
	}

	public static String getMainClass(){
		StackTraceElement[] stack = Thread.currentThread ().getStackTrace ();
		StackTraceElement main = stack[stack.length - 1];
		String mainClass = main.getClassName ();
		return mainClass;
	}
	
	/**
	 * Get the name of the current app based on the name of the jar file
	 * in which the main class resides.  Note, if the app is not being
	 * run from a jar file, then this will return null. 
	 * @param className
	 * @return
	 */
	public static String getAppNameFromJar(String className){
		String jarPath = getClassJarName(className);
		if(jarPath == null) {
			return null;
		}
		int idx = jarPath.lastIndexOf(".jar");
		String appName = jarPath.substring(0, idx);
		return appName;
	}

	/**
	 * In the case that this app is a web app, and running from a web context (such as in Tomcat)
	 * then return the name of the web app that the context is running under.
	 * 
	 * The web app may be in a .war file, or .war directory (if exploded by Tomcat), or may be in 
	 * a directory without the .war extension.  In which case, we need to back up from /classes 
	 * directory to the parent.
	 * 
	 * @return String: war file/directory name.
	 */
	public static String getAppNameFromWebApp(String className) {
		
		String path = getCodePath(className);
		
		// First see if there is a .war in the path
		int idx = path.lastIndexOf(".war");
		if(idx > -1) {
			// We got a hit!  Now look for the last separator before the .war
			int beg = path.lastIndexOf(File.separator, idx);
			if(beg == -1) {
				if(idx ==0) {
					// Weird, but it looks like the app is running from the root dir
					return path.substring(0,idx);
				}
				// Shouldn't ever get here.  
				logger.warning("Could not parse web app path: " + path);
				return null;
			}
			// Found a path in front of the .war file			
			return path.substring(beg+1,idx);
		}

		// If we are here, then there was no .war in the code path.
		// Look to see if there is a "WEB-INF" directory in the path
		idx = path.indexOf("/WEB-INF");
		if(idx > -1) {
			// We found a WEB-INF folder.  Work backward to the parent
			int beg = path.lastIndexOf(File.separator, idx);
			if(beg == -1) {
				if(idx == 0 ) {
				// Weird, but it looks like the app is running from the root dir
				return path.substring(0,idx);	
				
				}
				// Shouldn't ever get here.  
				logger.warning("Could not parse web app path: " + path);
				return null;
			}
			// Found a path in front of the WEB-INF file			
			return path.substring(beg+1,idx);			
		}
		// We got bupkis
		logger.warning("Could not parse web app path: " + path);		
		return null;
	}	
	
	/**
	 * Returns the jar file that contains the specified class
	 * @param className
	 * @return if the class is being run from a jar file, then the jar file name
	 * is returned.  Else null.  Note:  If the class is being run from a folder,
	 * such as a bin directory in Eclipse, then null will be returned.
	 */
	public static String getClassJarName(String className){
		try {
			Class myclass = Class.forName(className);
			return getClassJarName(myclass);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static String getCodePath(String className) {
		try {
			Class myclass = Class.forName(className);
			return getCodePath(myclass);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static String getCodePath(Class className) {
		String path = className.getProtectionDomain()
				  .getCodeSource()
				  .getLocation()
				  .getPath();
		return path;
	}
	
	public static String getClassJarName(Class className){
		
		String path = getCodePath(className);
		
		// See if this is a jar.  If the main class is executed from a folder,
		// it will be the folder name
		int idx = path.lastIndexOf(".jar");
		if(idx < 0){
			// It's not a jar file
			// See if this is a bin directory
			idx = path.lastIndexOf("/bin");
			if(idx < 0){
				// Oh well, we tried
				return null;
			}
			path = path.substring(0,idx);
			path += ".jar";
		}
		// Now get the final portion of the path - the jarfile name
		idx = path.lastIndexOf(File.separator);
		if(idx < 0){
			// That's weird.
			logger.warning("Could not parse jar file path: " + path);
			return null;
		}
		String jarFileName = path.substring(idx+1);
		return jarFileName;		
	}
	
	public static String argsToString(String[] args){
		if(args == null || args.length == 0){
			return "";
		}
		boolean firstTime = true;
		StringBuilder sb = new StringBuilder();
		for(String s : args){
			if(!firstTime){
				sb.append(":");
			}
			sb.append(s);
			firstTime = false;
		}
		return sb.toString();
	}
	
	public static String[] stringToArgs(String args){
		if(args == null){
			return null;
		}
		return args.split(":", -1);	
	}
	
}