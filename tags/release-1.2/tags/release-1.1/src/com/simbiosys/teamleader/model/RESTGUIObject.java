package com.simbiosys.teamleader.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.simbiosys.teamleader.Globals;

import android.content.res.AssetManager;
import android.util.Log;

public abstract class RESTGUIObject implements Serializable {

	private static final long serialVersionUID = 3450881451731706381L;
	
	protected HashMap<String, String> UIFieldNameToAPINameMap = null;
	protected HashMap<String, String> APINameToUIFieldNameMap = null;
	protected HashMap<String, String> commonNameToAPINameMap = null;
	protected HashMap<String, String> UINameToCommonNameMap = null;

	protected LinkedHashMap<String, String> values;

	protected String[][] commonNameToAPIName = null;
	
	public String resourceXML;
	
	protected String name;
	
	public abstract void setCommonNameToAPIName();
	
	public LinkedHashMap<String, String> getValues() {
		return(values);
	}
	
	public RESTGUIObject(String resourceXML) {
		setCommonNameToAPIName();
		this.resourceXML = resourceXML;
		values = new LinkedHashMap<String, String>();
//		commonNameToAPIName;

		// parse the resource xml and get the corresponding array item that belongs to
		if (UINameToCommonNameMap == null) {
			UINameToCommonNameMap = new HashMap<String, String>();
			
			AssetManager assetManager = Globals.application.getResources().getAssets();
			try {
				
				InputStream personXMLStream = assetManager.open(resourceXML);
				InputSource inputSource = new InputSource(personXMLStream);
				XPath xpath = XPathFactory.newInstance().newXPath();
				String expression = "/resources/string-array/item";
				NodeList nodeList = (NodeList)xpath.evaluate(expression, inputSource, XPathConstants.NODESET);


				if (nodeList != null && nodeList.getLength() > 0) {
					for (int i = 0; i < nodeList.getLength(); i++) {
						Log.v("TeamLeader", nodeList.item(i).getTextContent());
						String key = nodeList.item(i).getTextContent();
						NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();
						Node attributeNode = namedNodeMap.getNamedItem("name");
						String value = attributeNode.getNodeValue();
						// for every fieldName in the EnterPerson Screen, we need to map to an api name.
						// This isn't a resource because at this point, you need to be editing code, not a resource.
						// key: resource (from text content) -> value: api (from name attribute)
						// UIFieldName -> generalized/Common name -> apiString
						// So there are 2 maps, one from UIFieldName to generalized fieldName (not showing in UI), 
						//   and one from generalizedFieldName to api String
						// the generalizedFieldName to api String is in the static array above
						UINameToCommonNameMap.put(key, value);
					}
				}

				// In order to get the API name for a UI value:
				// get the UI Name, find the common name from the static array map, then find the api name with the found common name with the 
				// commonNameToAPIName map  then assign the UI Name to the API Name and store this
				// for every key:
				// I.E. UIName -> Common Name (map Built)
				// Common name -> api Name (map built)
				// assign UIName -> api Name in building this map
				// First we have to create the static array map though
				commonNameToAPINameMap = new HashMap<String, String>();
				for (String[] tuple:commonNameToAPIName) {
					commonNameToAPINameMap.put(tuple[0], tuple[1]);
				}
				
				UIFieldNameToAPINameMap = new HashMap<String, String>();
				for(String key:UINameToCommonNameMap.keySet()) {
					String value = UINameToCommonNameMap.get(key);
					UIFieldNameToAPINameMap.put(key,commonNameToAPINameMap.get(value));
				}

				Log.v("TeamLeader","UI -> api mapping:");

				for (String key:UIFieldNameToAPINameMap.keySet()) {
					Log.v("TeamLeader","key:" + key + " value:" + UIFieldNameToAPINameMap.get(key));
				}

				Log.v("TeamLeader", "complete.");

				// This is really memory intensive... but, maybe we can optimize it later if we need to.
				APINameToUIFieldNameMap = new HashMap<String, String>();
				for(String key:UIFieldNameToAPINameMap.keySet()) {
					APINameToUIFieldNameMap.put(UIFieldNameToAPINameMap.get(key), key);
				}

			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		} // end initialization of maps which are static.
		// This can't be done in static code, because we don't know what the Activity is yet

	}
	
	public boolean checkInitOK() {
		return( UIFieldNameToAPINameMap != null &&
		APINameToUIFieldNameMap != null &&
		commonNameToAPINameMap != null &&
		UINameToCommonNameMap != null &&
		commonNameToAPIName != null);
	}
	
	public void set(String key, String value) {

		Class<? extends RESTGUIObject> c = this.getClass();
		Log.v("TeamLeader", "cannonicalName: " + c.getCanonicalName());
		Method[] methods = c.getMethods();

		for(Method method:methods) {
			// if the key matches an existing field, call its getter
			String commonName = UINameToCommonNameMap.get(key);
			if (commonName == null) 
				commonName = key;

			String morphedFieldName = "set" + commonName.substring(0,1).toUpperCase(Locale.US) + commonName.substring(1); 

			Class<?>[] parameterTypes = method.getParameterTypes();
			boolean stringParam=false;
			if (parameterTypes.length == 1 && parameterTypes[0].getName().equals("java.lang.String") && parameterTypes.length == 1) stringParam = true;

			if (method.getName().equals(morphedFieldName) && stringParam) {
				Log.v("TeamLeader", "using reflection to set: " + key + ", " + value);
				try {
					method.invoke(this, value);
				} catch (InvocationTargetException e) {
					
					e.printStackTrace();
				}
				//		Log.v("TeamLeader","generic toString: " + this.toString());
				catch (IllegalAccessException e) {
					
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	

	public String getName() {
		if (!checkInitOK()) {
			Log.v("TeamLeader", "RESTGUIObject now initialized correctly");
			return null;
		}
		return name;
	}

	public void setName(String name) {
		if (!checkInitOK()) {
			Log.v("TeamLeader", "RESTGUIObject now initialized correctly");
			return;
		}
		String UIName = "Name";
		values.put(UIName, name);
		values.put("name", name);
		this.name = name;
	}
	
	public void logMaps() {
		Log.v("TeamLeader","fields:");
		for(String key:values.keySet()) {
			Log.v("TeamLeader",key+":"+values.get(key));
		}
		Log.v("TeamLeader","UI -> API");
		for(String key:UIFieldNameToAPINameMap.keySet()) {
			Log.v("TeamLeader",key+":"+UIFieldNameToAPINameMap.get(key));
		}
		Log.v("TeamLeader","API -> FieldName");
		for(String key:APINameToUIFieldNameMap.keySet()) {
			Log.v("TeamLeader",key+":"+APINameToUIFieldNameMap.get(key));
		}
	}
	
	public String get(String key) {
		return(values.get(key));
	}

	public HashMap<String, String> getAPINamesValuesOnly() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		for(String key:UIFieldNameToAPINameMap.keySet()) {
			String APIName = UIFieldNameToAPINameMap.get(key);
			String tempString = values.get(key);
			if (tempString != null && !tempString.equals(""))
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

}
