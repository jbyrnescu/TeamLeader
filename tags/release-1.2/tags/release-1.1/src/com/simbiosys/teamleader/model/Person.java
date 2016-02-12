package com.simbiosys.teamleader.model;

import java.io.Serializable;

import android.util.Log;

public class Person extends Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1429230216255017504L;

//	@Override
	public String toString() {
		return "Person [gender=" + gender + ", DOB=" + DOB + ", company="
				+ company + ", gsm=" + gsm + ", language=" + language
				+ ", values=" + values
//				+ ", UIFieldNameToAPINameMap=" + UIFieldNameToAPINameMap
//				+ ", APINameToUIFieldNameMap=" + APINameToUIFieldNameMap
				+ ", getValues()=" + getValues() + ", getName()=" + getName()
				+ ", getSurname()=" + getSurname() + ", getEmailAddress()="
				+ getEmailAddress() + ", getTelephoneNumber()="
				+ getTelephoneNumber() + ", getCountry()=" + getCountry()
				+ ", getZipCode()=" + getZipCode() + ", getCity()=" + getCity()
				+ ", getStreet()=" + getStreet() + ", getNumber()="
				+ getNumber() + ", getId()=" + getId()
//				+ ", getAPINameToUIFieldNameMap()="
//				+ getAPINameToUIFieldNameMap() + ", getFieldValues()="
				+ getFieldValues() + ", getUINameToAPINameMap()="
//				+ getUINameToAPINameMap() + ", toString()=" + super.toString()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}

	private String gender;
	private String DOB;
	private String company;
	private String gsm;
	private String language;
	private String surname;

	static private String[][] commonNameToAPINamePerson = { 
		{"name","forename"}, 
		{"surname","surname"},
		{"emailAddress", "email"},
		{"telephoneNumber", "telephone"},
		{"country", "country"},
		{"zipcode", "zipcode"},
		{"city", "city"},
		{"street", "street"},
		{"number", "number"},
		{"gender", "gender"},
		{"dob", "dob"},
		{"company", "company"},
		{"mobilePhoneNumber", "gsm"},
		{"language", "language"}
	};

	//			<item name="name" >Name</item>
	//	        <item name="surname" >Surname</item>
	//	        <item name="emailAddress" >Email Address</item>
	//	        <item name="telephoneNumber" >Telephone Number</item>
	//	        <item name="country" >Country</item>
	//	        <item name="zipcode" >Zip Code</item>
	//	        <item name="city" >City</item>
	//	        <item name="street" >Street</item>
	//	        <item name="number" >Number</item>
	//	        <item name="gender" >Gender</item>
	//	        <item name="dob" >DOB</item>
	//	        <item name="company" >Company</item>
	//	        <item name="mobilePhoneNumber" >Mobile Phone Number</item>
	//	        <item name="language" >Language</item>

//	private static HashMap<String, String> commonNameToAPINameMapPerson; 

	// add a few resource to api strings in the hash map in the constructor
	public Person() {
		super("EnterPerson.xml");
//		commonNameToAPIName = commonNameToAPINamePerson;
//
//
//		// parse the resource xml and get the corresponding array item that belongs to
//		if (UINameToCommonNameMap == null) {
//			UINameToCommonNameMap = new HashMap<String, String>();
//			AssetManager assetManager = activity.getResources().getAssets();
//			try {
//
//				InputStream personXMLStream = assetManager.open("EnterPerson.xml");
//				InputSource inputSource = new InputSource(personXMLStream);
//				XPath xpath = XPathFactory.newInstance().newXPath();
//				String expression = "/resources/string-array/item";
//				NodeList nodeList = (NodeList)xpath.evaluate(expression, inputSource, XPathConstants.NODESET);
//
//
//				if (nodeList != null && nodeList.getLength() > 0) {
//					for (int i = 0; i < nodeList.getLength(); i++) {
//						Log.v("TeamLeader", nodeList.item(i).getTextContent());
//						String key = nodeList.item(i).getTextContent();
//						NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();
//						Node attributeNode = namedNodeMap.getNamedItem("name");
//						String value = attributeNode.getNodeValue();
//						// for every fieldName in the EnterPerson Screen, we need to map to an api name.
//						// This isn't a resource because at this point, you need to be editing code, not a resource.
//						// key: resource (from text content) -> value: api (from name attribute)
//						// UIFieldName -> generalized/Common name -> apiString
//						// So there are 2 maps, one from UIFieldName to generalized fieldName (not showing in UI), 
//						//   and one from generalizedFieldName to api String
//						// the generalizedFieldName to api String is in the static array above
//						UINameToCommonNameMap.put(key, value);
//					}
//				}
//
//				// In order to get the API name for a UI value:
//				// get the UI Name, find the common name from the static array map, then find the api name with the found common name with the 
//				// commonNameToAPIName map  then assign the UI Name to the API Name and store this
//				// for every key:
//				// I.E. UIName -> Common Name (map Built)
//				// Common name -> api Name (map built)
//				// assign UIName -> api Name in building this map
//				// First we have to create the static array map though
//				commonNameToAPINameMapPerson = new HashMap<String, String>();
//				for (String[] tuple:commonNameToAPINamePerson) {
//					commonNameToAPINameMapPerson.put(tuple[0], tuple[1]);
//				}
//				commonNameToAPINameMap = commonNameToAPINameMapPerson;
//				
//				UIFieldNameToAPINameMap = new HashMap<String, String>();
//				for(String key:UINameToCommonNameMap.keySet()) {
//					String value = UINameToCommonNameMap.get(key);
//					UIFieldNameToAPINameMap.put(key,commonNameToAPINameMapPerson.get(value));
//				}
//
//				Log.v("TeamLeader","UI -> api mapping:");
//
//				for (String key:UIFieldNameToAPINameMap.keySet()) {
//					Log.v("TeamLeader","key:" + key + " value:" + UIFieldNameToAPINameMap.get(key));
//				}
//
//				Log.v("TeamLeader", "complete.");
//
//				// This is really memory intensive... but, maybe we can optimize it later if we need to.
//
//				for(String key:UIFieldNameToAPINameMap.keySet()) {
//					APINameToUIFieldNameMap.put(UIFieldNameToAPINameMap.get(key), key);
//				}
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (XPathExpressionException e) {
//				e.printStackTrace();
//			}
//		} // end initialization of maps which are static.
//		// This can't be done in static code, because we don't know what the Activity is yet

	}

	public String getCompany() {
		return(company);
	}

	public String getGender() {
		return gender;
	}

	public String getDOB() {
		return DOB;
	}

	public String getGSM() {
		return(gsm);
	}

	public String getLanguage() {
		return(language);
	}
	
	public String getSurname() {
		return(surname);
	}

	public void setLanguage(String language) {
		values.put("language", language);
		this.language = language;
	}

	public void setGender(String gender) {
		values.put("gender", gender);
		this.gender = gender;
	}

	public void setDOB(String dOB) {
		values.put("dob", dOB);
		DOB = dOB;
	}

	public void setCompany(String company) {
		values.put("company", company);
		this.company = company;
	}

	public void setGSM(String gsm) {
		values.put("gsm", gsm);
		this.gsm = gsm;
	}
	
	public void setSurname(String surname) {
		String uiName = APINameToUIFieldNameMap.get("surname");
		values.put("surname", surname);
		values.put(uiName, surname);
		this.surname = surname;
	}

	public boolean hasForename() {
		// first get the right UI name, then query the map to see if it exists in the values
		String uiName = APINameToUIFieldNameMap.get("forename");
		boolean isNull = (name == null);
		if (!isNull) Log.v("TeamLeader", values.get(uiName).toString());
		String forename = values.get("forename");
		if (forename != null) Log.v("TeamLeader", "forename in values? - " + values.get("forename"));
		return(forename != null && !forename.equals(""));
	}

	public boolean hasSurname() {
		return(surname != null && !surname.equals(""));
	}

	public boolean hasEmailAddress() {
		boolean isNull = emailAddress == null;
		boolean isEmpty = true;
		if (!isNull)
			isEmpty = (emailAddress.equals(""));
		return(!(isNull || isEmpty));
	}

	@Override
	public void setCommonNameToAPIName() {
		commonNameToAPIName = commonNameToAPINamePerson;
	}

}
