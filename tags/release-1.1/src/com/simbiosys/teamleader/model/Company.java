package com.simbiosys.teamleader.model;

import java.io.Serializable;

public class Company extends Contact implements Serializable {

	@Override
	public String toString() {
		return "Company [vatCode=" + vatCode + ", accountManagerId="
				+ accountManagerId + ", businessType=" + businessType
				+ ", language=" + language + ", name=" + name
				+ ", emailAddress=" + emailAddress + ", telephoneNumber="
				+ telephoneNumber + ", country=" + country + ", zipCode="
				+ zipCode + ", city=" + city + ", street=" + street
				+ ", number=" + number + ", id=" + id + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2963151618286956105L;
	
	private String vatCode;
	private int accountManagerId;
	private String businessType;
	private String language;
	

	static private String[][] commonNameToAPINameCompany = { 
		{"name","name"}, 
		{"emailAddress", "email"},
		{"telephoneNumber", "telephone"},
		{"country", "country"},
		{"zipcode", "zipcode"},
		{"city", "city"},
		{"street", "street"},
		{"number", "number"},
		{"language", "language"},
		{"accountManagerId","account_manager_id"},
		{"businessType","business_type"},
		{"vatCode","vat_code"}
	};



//	private static HashMap<String, String> UINameToCommonNameMap;
//	private static HashMap<String, String> commonNameToAPINameMapCompany; 
	
	public Company() {
		super("EnterCompany.xml");
	}
	
	// add a few resource to api strings in the hash map in the constructor
//	public Company(Activity activity) {
//		
//		commonNameToAPIName = commonNameToAPINameCompany;
//
//		// parse the resource xml and get the corresponding array item that belongs to
//		if (UINameToCommonNameMap == null) {
//			UINameToCommonNameMap = new HashMap<String, String>();
//			AssetManager assetManager = activity.getResources().getAssets();
//			try {
//
//				InputStream personXMLStream = assetManager.open("EnterCompany.xml");
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
//				commonNameToAPINameMapCompany = new HashMap<String, String>();
//				for (String[] tuple:commonNameToAPINameCompany) {
//					commonNameToAPINameMapCompany.put(tuple[0], tuple[1]);
//				}
//				commonNameToAPINameMap = commonNameToAPINameMapCompany;
//
//				UIFieldNameToAPINameMap = new HashMap<String, String>();
//				for(String key:UINameToCommonNameMap.keySet()) {
//					String value = UINameToCommonNameMap.get(key);
//					UIFieldNameToAPINameMap.put(key,commonNameToAPINameMapCompany.get(value));
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
//
//	}

	public String getLanguage() {
		return(language);
	}

	public String getVatCode() {
		return vatCode;
	}

	public int getAccountManagerId() {
		return accountManagerId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setVatCode(String vatCode) {
		values.put("Vat Code", vatCode);
		values.put("vat_code", vatCode);
		this.vatCode = vatCode;
	}

	public void setAccountManagerId(int accountManagerId) {
		values.put("Account Manager", Integer.valueOf(accountManagerId).toString());
		values.put("account_manage_id", (Integer.valueOf(accountManagerId)).toString());
		this.accountManagerId = accountManagerId;
	}

	public void setBusinessType(String businessType) {
		values.put("Business Type", businessType);
		values.put("business_type",businessType);
		this.businessType = businessType;
	}

	public void setLanguage(String language) {
		values.put("Language", language);
		values.put("language", language);
		this.language = language;
	}

	public boolean hasEmailAddress() {
		boolean isNull = (emailAddress == null);
		return(!(isNull || emailAddress.equals("")));
	}
	
	public boolean hasName() {
		boolean isNull = (name == null);
		return(!(isNull || name.equals("")));
	}

	@Override
	public void setCommonNameToAPIName() {
		commonNameToAPIName = commonNameToAPINameCompany;		
	}
	
}
