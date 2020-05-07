/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.umu.capability.token;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/***
 * What does this class do?
 * 
 * This class depends from what class or component?
 * What classes or components depend from this class?
 * 
 * 
 * This class Generates a 
 * 
 * @author dgarcia@odins.es
 *
 */

public class CapabilityTokenRequest {
	
	private String subject; 
	private String device;
	private String action;
	private String resource;
	
	
	
	/**
	 * 
	 * This constructor generates the object with that contains the strings passed as parameters. 
	 * We can call it directly, or use the getCapabilityTokenRequest function that given a JSON
	 * 
	 * @param subject
	 * @param action
	 * @param device
	 * @param resource
	 */
	public CapabilityTokenRequest (String subject, String action, String device, String resource)
	{
		this.subject  = subject;
		this.action   = action;
		this.device   = device;
		this.resource = resource;
	}
	
	
	/**
	 * 
	 * Given the function parameter, a String containing a JSON document that contains the subject, device, action and resource,
	 * we return a CapabilityTokenRequest object with said information. 
	 * 
	 * 
	 * Idea of the JSON document
	 * 
	 * {
	 * 	"su" : "Identity of the Subject -- (The format of this field is subject to change)",
	 * 	"de" : "Generally, the URL of the device -- e.g, coaps://CAFE:DCAF:8080",
	 *  "ac" : "Generally, a CRUD command, POST, PUT, GET, DELETE, etc.",
	 *  "re" : "Generally, the LOCATION path or URI path or the resource"
	 * }
	 * 
	 * Example of JSON document passed through  capabilitytokenrequest
	 * 
	 * {
	 * 	"su" : "(aBVL}}7X7@Gv?Z*",
	 * 	"de" : "coaps://CAFE:DCAF:8080",
	 *  "ac" : "POST",
	 *  "re" : "temperature"
	 * }
	 * 
	 * This would translate to a 
	 * 
	 * 		POST coaps://CAFE:DCAF:8080/temperature 
	 * 
	 * that is authorized by the capability token.
	 * 
	 * @param String capabilitytokenrequest
	 * @return CapabilityTokenRequest
	 */
	public static CapabilityTokenRequest getCapabilityTokenRequestFromJSON (String capabilitytokenrequest)
	{
		JsonParser parser = new JsonParser();
		JsonElement jelement = parser.parse(capabilitytokenrequest);
		JsonObject token_json = jelement.getAsJsonObject();

		JsonElement id_json = token_json.get("su");
		String su = id_json.getAsString();

		JsonElement ii_json = token_json.get("de");
		String de = ii_json.getAsString();

		JsonElement is_json = token_json.get("ac");
		String ac = is_json.getAsString();

		JsonElement su_json = token_json.get("re");
		String re = su_json.getAsString();
		
		return new CapabilityTokenRequest(su, ac, de, re);
	}

	
	
	// Getters
	public String getSubject() {
		return subject;
	}
	
	public String getDevice() {
		return device;
	}

	public String getAction() {
		return action;
	}
	
	public String getResource() {
		return resource;
	}
	
	// Setters
	public void setResource(String resource) {
		this.resource = resource;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	// ToString
	public String toString(){
		String request = "{\"su\": \""
				+ this.getSubject() + "\",\"de\":\""
				+ this.getDevice() + "\",\"ac\":\""
				+ this.getAction() + "\",\"re\":\""
				+ this.getResource() + "\"}";
		return request;
	}
	
}
