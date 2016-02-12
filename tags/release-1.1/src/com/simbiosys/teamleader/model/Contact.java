package com.simbiosys.teamleader.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class Contact extends RESTGUIObject implements Serializable {

	public Contact(String resourceXML) {
		super(resourceXML);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8703629230232917993L;
//	protected static HashMap<String, String> UIFieldNameToAPINameMap;
//	protected static HashMap<String, String> APINameToUIFieldNameMap;
//	protected static String[][] commonNameToAPIName;
//	protected static HashMap<String, String> commonNameToAPINameMap;
//	protected static HashMap<String, String> UINameToCommonNameMap;


	public String name;
	public String emailAddress;
	public String telephoneNumber;
	public String country;
	public String zipCode;
	public String city;
	public String street;
	public String number;
	public int id;

	// everything is based on the UI Fieldname.
	// We get the API string from a map of (UIFieldName, API string)
	// We get the Value string from a map of (UIFieldName, value string)


	// contains UI Name, value so all set<> methods contain the UI Field Name
	// also contains APIName, value for the api calls
//	protected LinkedHashMap<String, String> linkedHashMap;


	// because we're lazy and haven't implemented reflection for this class yet, 
	//  we're using the hashMap to see whether the variable was set!
	public LinkedHashMap<String, String> getValues() {
		return(values);
	}

	public String getName() {
		return name;
	}


	public String getEmailAddress() {
		return emailAddress;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public String getCountry() {
		return country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public String getNumber() {
		return number;
	}

	public int getId() {
		return id;
	}

	// for ALL sets we put the API name as the key.
	// 

	public void setName(String name) {
		values.put("forename",name);
		values.put("Name", name);
		values.put("name", name);
		this.name = name;
	}

	public void setEmailAddress(String emailAddress) {
		values.put("Email Address", emailAddress);
		values.put("email", emailAddress);
		this.emailAddress = emailAddress;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		values.put("Telephone Number",telephoneNumber);
		values.put("telephone", telephoneNumber);
		this.telephoneNumber = telephoneNumber;
	}

	public void setCountry(String country) {
		values.put("Country",country);
		values.put("country", country);
		this.country = country;
	}

	public void setZipCode(String zipCode) {
		values.put("Zip Code",zipCode);
		values.put("zipcode", zipCode);
		this.zipCode = zipCode;
	}

	public void setCity(String city) {
		values.put("City", city);
		values.put("city", city);
		this.city = city;
	}

	public void setStreet(String street) {
		values.put("Street", street);
		values.put("street",  street);
		this.street = street;
	}

	public void setNumber(String number) {
		values.put("Number",number);
		values.put("number", number);
		this.number = number;
	}

	public void setId(int id) {
		values.put("id", Integer.valueOf(id).toString());
		this.id = id;
	}


	public HashMap<String, String> getAPINameToUIFieldNameMap() {
		return(APINameToUIFieldNameMap);
	}

	public LinkedHashMap<String, String> getFieldValues() {
		return values;
	}

	public HashMap<String, String> getUINameToAPINameMap() {
		return UIFieldNameToAPINameMap;
	}

	public HashMap<String, String> getAPINamesValuesOnly() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		for(String key:UIFieldNameToAPINameMap.keySet()) {
			String APIName = UIFieldNameToAPINameMap.get(key);
			String tempString = values.get(key);
			if (tempString != null)
				hashMap.put(APIName, tempString);
		}
		for(String key:commonNameToAPINameMap.keySet()) {
			String APIName = commonNameToAPINameMap.get(key);
			String tempString = values.get(key);
			if (tempString != null && !hashMap.containsKey(APIName)) {
				hashMap.put(APIName, tempString);
			}
		}
		return(hashMap);
	}

//	public String set(String key, String value) {
//
//		// go through this class to see if there's a matching member we can set and do it.
//		Class<? extends Contact> c = this.getClass();
//		Log.v("TeamLeader","cannonicalName: " + c.getCanonicalName());
//		Method[] methods = c.getMethods();
//
//		if (key.equals("id")) {
//			setId(Integer.valueOf(value));
//		} else 
//
//			for(Method method:methods) {
//				// if the key matches an existing field, call its getter
//				String commonName = UINameToCommonNameMap.get(key);
//				if (commonName == null) 
//					commonName = key;
//
//				String morphedFieldName = "set" + commonName.substring(0,1).toUpperCase(Locale.US) + commonName.substring(1); 
//
//				if (method.getName().equals(morphedFieldName)) {
//					Log.v("TeamLeader", "using reflection to set: " + key + ", " + value);
//					try {
//						method.invoke(this, value);
//					} catch (InvocationTargetException e) {
//						Log.v("TeamLeader","InvocationTargetException");
//						e.printStackTrace();
//					}
//					//		Log.v("TeamLeader","generic toString: " + this.toString());
//					catch (IllegalAccessException e) {
//						Log.v("TeamLeader", "IllegalAccessException");
//						e.printStackTrace();
//					} catch (IllegalArgumentException e) {
//						Log.v("TeamLeader", "IllegalArgumentException");
//						e.printStackTrace();
//					}
//				}
//			}
//
//		Log.v("TeamLeader", "setting: key-" + key + " value-" + value);
//		// go through the UI names and translate the UI name to the API name as that's what's used when
//		// using the REST API
//		if (UIFieldNameToAPINameMap.get(key) != null) {
//			Log.v("TeamLeader","adding key, value: " + UIFieldNameToAPINameMap.get(key) + "," + value);
//			linkedHashMap.put(UIFieldNameToAPINameMap.get(key), value);
//		}
//		return(linkedHashMap.put(key, value));
//	}



	public String get(String key) {
		return(values.get(key));
	}
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(String key:values.keySet()) {
			stringBuilder.append(key + ":");
			stringBuilder.append(values.get(key));
			stringBuilder.append("\n");
		}
		return(stringBuilder.toString());
	}



}
