/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.umu.capability.token;

import java.util.ArrayList;

import org.umu.capability.rights.AccessRight;
import org.umu.capability.rights.SimpleAccessRight;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


/**
 * Objects holding the fields necessary to form a token
 */
public class CapabilityToken {
	private String id;
	private long ii;
	private String is;
	private String su;
	private String de;
	private String si;
	private ArrayList<SimpleAccessRight> ar;
	private long nb;
	private long na;
	
	private static JsonParser parser = new JsonParser();
	private static Gson gson = new Gson();
	private static java.lang.reflect.Type listType = new TypeToken<ArrayList<SimpleAccessRight>>() {}.getType();
	
	/**
	 * Constructor
	 * 
	 * @param id			ID
	 * @param ii			Issuing timestamp
	 * @param is			Issuer
	 * @param su			Subject
	 * @param de			Device
	 * @param si			Signature
	 * @param ar			Access resources
	 * @param nb			Not before
	 * @param na			Not after
	 */
	public CapabilityToken(String id, long ii, String is, String su, String de, String si, ArrayList<SimpleAccessRight> ar, long nb, long na) 
	{
		this.id = id;
		this.ii = ii;
		this.is = is;
		this.su = su;
		this.de = de;
		this.si = si;
		this.ar = ar;
		this.nb = nb;
		this.na = na;
	}
	
	/**
	 * Try to parse a token
	 * 
	 * @param capability_token	The String received
	 * @return					The token (null if the format is incorrect)
	 */
	public static CapabilityToken getCapabilityTokenFromString(String capability_token) {
		if (capability_token == null) return null;
		
		try {
			JsonElement jelement = parser.parse(capability_token);
			JsonObject token_json = jelement.getAsJsonObject();

			JsonElement id_json = token_json.get("id");
			String id = id_json.getAsString();

			JsonElement ii_json = token_json.get("ii");
			long ii = ii_json.getAsLong();

			JsonElement is_json = token_json.get("is");
			String is = is_json.getAsString();

			JsonElement su_json = token_json.get("su");
			String su = su_json.getAsString();

			JsonElement de_json = token_json.get("de");
			String de = de_json.getAsString();

			JsonElement si_json = token_json.get("si");
			String si = si_json.getAsString();

			JsonElement ar_json = token_json.getAsJsonArray("ar");
			
			ArrayList<SimpleAccessRight> ar = gson.fromJson(ar_json, listType);

			JsonElement nb_json = token_json.get("nb");
			long nb = nb_json.getAsLong();
			
			JsonElement na_json = token_json.get("na");
			long na = na_json.getAsLong();
			
			if ((id == null) || (is == null) || (su == null) || (de == null) || (si == null) || (ar == null) || (nb < ii) || (nb >= na)) return null;
			
			return new CapabilityToken(id, ii, is, su, de, si, ar, nb, na);
		} catch(Exception ex) {
			return null;
		}
	}

	////
	// 	GETTERS
	////
	public String getID() {
		return id;
	}

	public long getIssueIns() {
		return ii;
	}

	public String getIss() {
		return is;
	}

	public String getSub() {
		return su;
	}

	public String getDe() {
		return de;
	}

	public String getSig() {
		return si;
	}


	public ArrayList<SimpleAccessRight> getAr() {
		return ar;
	}
	
	public long getNb() {
		return nb;
	}

	public long getNa() {
		return na;
	}

	public boolean IsTimeValid() {
		long ii_field = this.getIssueIns();
		long nb_field = this.getNb();
		long na_field = this.getNa();
		long currentTime = System.currentTimeMillis() / 1000L;
		
		return ((ii_field < currentTime) && (ii_field <= nb_field) && (nb_field < na_field) && (ii_field < na_field) && (na_field >= currentTime));
	}
	
	
    public String getSummary() {
        String result = this.getID() + ":" + this.getIssueIns() + ":" + this.getIss() + ":" + this.getDe() + ":" + this.getSub() + ":" + this.getNb() + ":" + this.getNa();
        for (SimpleAccessRight ac : this.getAr()) {
            result = result + ":" + ac.getPermittedAction() + ":" + ac.getResource();
        }
        return result;
    }
	
	////
	// 	SETTERS
	////
    
    public void setSig(String sig) {
        this.si = sig;
    }
    
    
}

