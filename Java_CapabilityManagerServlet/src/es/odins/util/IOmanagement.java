/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package es.odins.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;



public class IOmanagement {

	
	public static String readFile(String path, Charset encoding) 
			  throws IOException 
	{
	  checkFileProcessing(path);
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
	
	
	public static String[] toStringArray(JsonArray array) {
	    if(array==null)
	        return null;

	    String[] arr=new String[array.size()];
	    for(int i=0; i<arr.length; i++) {
	        arr[i]=array.get(i).getAsString();
	    }
	    return arr;
	}
	
	public static JsonObject readJSONFile(String jsonFile) {
		
		JsonParser parser 		= new JsonParser();
		
		try {
			return (JsonObject) parser.parse(
										IOmanagement.readFile(
												jsonFile, 
												Charset.defaultCharset()
												)
										);
			
		} catch (JsonSyntaxException | IOException e) {			
			System.exit(-1);
			e.printStackTrace();
		}
		return null;
	}
	
	public static String trimJsonStringValue(String JsonValue) {
		return JsonValue.substring(1,JsonValue.length()-1);
	}
	
	public static boolean fileExists(String path) {
		File tempFile = new File( path );
		return tempFile.exists();
	}
	
	public static void checkFileProcessing(String path) throws IOException {

		System.out.println("Retrieving file: " + path );

    	if(IOmanagement.fileExists( path )) {
    		System.out.println("OK!");
    	}else {
    		System.out.println("Error file " + path + " not found");
    		throw new IOException("Error file " + path + " not found");
    		
    	}
 
	}
	
	
}
