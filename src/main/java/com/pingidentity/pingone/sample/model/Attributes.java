package com.pingidentity.pingone.sample.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Attributes extends  HashMap<String,List<String>> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2620597226541847552L;

	/**
	 * If the key exists, the first value in the key array is returned
	 * @param key the key
	 * @return the first String value
	 */
	public String getSingleValue(String key) {
		if(containsKey(key) == false) {
			return null;
		}
		return get(key).get(0);
	}
	
	/**
	 * Ensures that only one value is associated with the key.  If any 
	 * value currently exists, it is replaced by the new value.  Use putSingleValue
	 * when you do not want to keep an array of values for the given key.
	 * @param key
	 * @param value
	 */
	public void putSingleValue(String key, String value) {
		List<String> attrs = new ArrayList<String>();
		attrs.add(value);
		put(key,attrs);
	}	
	
	/** 
	 * Add a value to the attribute set.  Calling this multiple times for a single attribute
	 * will result in a multivalued attribute
	 * @param key
	 * @param value
	 */
	public void put(String key, String value) {
		List<String> values;
		if(containsKey(key) == false) {
			values = new ArrayList<String>();
			values.add(value);
			put(key,values);
		}else {
			values = get(key);
			values.add(value);
			put(key,values);
		}
	}
}
