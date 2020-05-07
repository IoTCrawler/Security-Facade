/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.odins.util.filemanagement;

import com.google.gson.*;


/**
 * 
 * This class provides functions to exchange formats, e.g., JsonArray to String []
 * 
 * @author dgarcia@odins.es
 *
 */
public class FormatXChange {

	
	
	/**
	 * Returns a String[], given a JsonArray.
	 * @param JsonArray jsonArray
	 * @return String[]
	 */
	public static String[] jsonArray2StringArray(JsonArray jsonArray) {

		if(jsonArray==null)
	        return null;

	    String[] arr = new String[jsonArray.size()];
	    
	    for(int i=0; i<arr.length; i++) {
	        arr[i]=jsonArray.get(i).getAsString();
	    }
	    
	    return arr;
	}
	
	
	
	
	
	
	
}
