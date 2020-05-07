/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.umu.capability.generator;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.odins.util.filemanagement.Read4mFile;
import org.umu.capability.rights.SimpleAccessRight;
import org.umu.capability.token.CapabilityTokenExtended;
import org.umu.capability.token.CapabilityTokenRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class CapabilityGenerator {


    private static  String TRUST_STORE_LOCATION;
    private static  String TRUST_STORE_PASSWORD;
    private static  String TRUST_STORE_ENTRY;
    private static  String KEY_STORE_LOCATION;
    private static  String KEY_STORE_PASSWORD;
    private static  String KEY_STORE_ENTRY;
    private static  String CERT_ALGORITHM_SIGNATURE;
    private static  String DOMAIN;

    
	PrivateKey capabilityPrivateKey = null;
    
	public CapabilityGenerator(String configFile) {
		
		
		System.out.println("CapabilityGenerator started");
		JsonObject config = Read4mFile.readJSONFile(configFile);
		
		System.out.println(config.toString());


	     System.out.println("Working Directory = " +
	     System.getProperty("user.dir"));
	     
	     
			DOMAIN = config.get("DOMAIN").getAsString();

			TRUST_STORE_LOCATION = config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("TRUST_STORE_LOCATION").getAsString();
			TRUST_STORE_PASSWORD = config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("TRUST_STORE_PASSWORD").getAsString();
			TRUST_STORE_ENTRY    = config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("TRUST_STORE_ENTRY").getAsString();
			KEY_STORE_LOCATION 	 = config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("KEY_STORE_LOCATION").getAsString();
			KEY_STORE_PASSWORD 	 = config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("KEY_STORE_PASSWORD").getAsString();
			KEY_STORE_ENTRY    	 = config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("KEY_STORE_ENTRY").getAsString();
			CERT_ALGORITHM_SIGNATURE = config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("CERT_ALGORITHM").getAsJsonArray().get(0).getAsJsonObject().get("SIGNATURE").getAsString();
			
			System.out.println("SIGNATURE: "+CERT_ALGORITHM_SIGNATURE);
	
			KeyStore keyStore = null;
			  try {
		            // load the key store
		            keyStore = KeyStore.getInstance("JKS");
		            InputStream in = new FileInputStream(KEY_STORE_LOCATION);
		            keyStore.load(in, KEY_STORE_PASSWORD.toCharArray());

		            // load the trust store
		            KeyStore 	trustStore 	= KeyStore.getInstance("JKS");
		            InputStream inTrust 	= new FileInputStream(TRUST_STORE_LOCATION);
		            trustStore.load(inTrust, TRUST_STORE_PASSWORD.toCharArray());

		            // You can load multiple certificates if needed
		            Certificate[] trustedCertificates = new Certificate[1];

		            System.out.println("TRUST_STORE_ENTRY: "  +TRUST_STORE_ENTRY);
		            trustedCertificates[0] = trustStore.getCertificate(TRUST_STORE_ENTRY);
		            
		            System.out.println("KEY_STORE_ENTRY: "  +KEY_STORE_ENTRY);
		            System.out.println(keyStore.containsAlias(KEY_STORE_ENTRY));
		            //this.capabilityPublicKey = keyStore.getCertificate(KEY_STORE_ENTRY).getPublicKey();
		            
		            
		            this.capabilityPrivateKey = (PrivateKey) keyStore.getKey(KEY_STORE_ENTRY, KEY_STORE_PASSWORD.toCharArray());
		             
			  } catch (GeneralSecurityException | IOException e) {
		            System.err.println("Could not load the keystore");
		            e.printStackTrace();
		      }
			  
			  System.out.println("CapabilityGenerator completed");
			
	}
	
	 
	public CapabilityGenerator(String rootPath, String configFile) {
			
		System.out.println("CapabilityGenerator started 5");

		System.out.println("The file: " + rootPath+configFile);
		File f = new File(rootPath+configFile); 
		
		if(f.exists() && !f.isDirectory()) {
			System.out.println("The file exists");
		} 
		else {
	        System.out.println(" file DOES NO exists ");
		}
		
		JsonObject config = Read4mFile.readJSONFile(rootPath + configFile);
		System.out.println("JSON File: \n" + config.toString() + "\n" );
		
		System.out.println(config.toString());
	     
     
		DOMAIN = config.get("DOMAIN").getAsString();
		System.out.println("DOMAIN: "+ DOMAIN);
		
		TRUST_STORE_LOCATION = rootPath + config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("TRUST_STORE_LOCATION").getAsString();
		System.out.println("TRUST_STORE_LOCATION:"+TRUST_STORE_LOCATION);
		
		TRUST_STORE_PASSWORD = config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("TRUST_STORE_PASSWORD").getAsString();
		System.out.println("TRUST_STORE_PASSWORD:"+TRUST_STORE_PASSWORD);
		
		TRUST_STORE_ENTRY    = config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("TRUST_STORE_ENTRY").getAsString();
		System.out.println("TRUST_STORE_ENTRY:"+TRUST_STORE_ENTRY);
		
		KEY_STORE_LOCATION 	 = rootPath + config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("KEY_STORE_LOCATION").getAsString();
		System.out.println("KEY_STORE_LOCATION:"+KEY_STORE_LOCATION);
		
		KEY_STORE_PASSWORD 	 = config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("KEY_STORE_PASSWORD").getAsString();
		System.out.println("KEY_STORE_PASSWORD:"+KEY_STORE_PASSWORD);
		
		KEY_STORE_ENTRY    	 = config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("KEY_STORE_ENTRY").getAsString();
		System.out.println("KEY_STORE_ENTRY:"+KEY_STORE_ENTRY);
		
		CERT_ALGORITHM_SIGNATURE = config.get("CREDENTIAL_INFO").getAsJsonArray().get(0).getAsJsonObject().get("CERT_ALGORITHM").getAsJsonArray().get(0).getAsJsonObject().get("SIGNATURE").getAsString();
		System.out.println("CERT_ALGORITHM_SIGNATURE:"+CERT_ALGORITHM_SIGNATURE);
		
		
		System.out.println("The KEY_STORE_LOCATION: " + KEY_STORE_LOCATION);
		f = new File(KEY_STORE_LOCATION); 
		
		if(f.exists() && !f.isDirectory()) {
			System.out.println("The file exists");
		} 
		else {
	        System.out.println(" file DOES NO exists ");
		}
		
		
		System.out.println("The TRUST_STORE_LOCATION: " + TRUST_STORE_LOCATION);
		f = new File(TRUST_STORE_LOCATION); 
		
		if(f.exists() && !f.isDirectory()) {
			System.out.println("The file exists");
		} 
		else {
	        System.out.println(" file DOES NO exists ");
		}
		
		
			KeyStore keyStore = null;
			  try {
		            // load the key store
		            keyStore = KeyStore.getInstance("JKS");
		            InputStream in = new FileInputStream(KEY_STORE_LOCATION);
		            keyStore.load(in, KEY_STORE_PASSWORD.toCharArray());

		            // load the trust store
		            KeyStore 	trustStore 	= KeyStore.getInstance("JKS");
		            InputStream inTrust 	= new FileInputStream(TRUST_STORE_LOCATION);
		            trustStore.load(inTrust, TRUST_STORE_PASSWORD.toCharArray());

		            // You can load multiple certificates if needed
		            Certificate[] trustedCertificates = new Certificate[1];
		            trustedCertificates[0] = trustStore.getCertificate(TRUST_STORE_ENTRY);
		            
		            //this.capabilityPublicKey = keyStore.getCertificate(KEY_STORE_ENTRY).getPublicKey();
		            this.capabilityPrivateKey = (PrivateKey) keyStore.getKey(KEY_STORE_ENTRY, KEY_STORE_PASSWORD.toCharArray());
		             
			  } catch (GeneralSecurityException | IOException e) {
		            System.err.println("Could not load the keystore");
		            e.printStackTrace();
		      }
			  
			  System.out.println("CapabilityGenerator completed");
			
	}
	
	public String generate_capability_token(CapabilityTokenRequest ctr)
	{
		String resourceDescription = null;

		/*
		if(ctr.resourceDescriptionFile != null)
			resourceDescription = this.readResourceDescription(resourceDescriptionFile);
		*/
		
		String  id 	= this.generateTokenID();
		long 	ii 	= System.currentTimeMillis()/1000L - 1000;
		String  iss = "capabilitymanager" + DOMAIN;
		long 	nb 	= System.currentTimeMillis()/1000L;
		long 	na 	= System.currentTimeMillis()/1000L + 10000;

		ArrayList <SimpleAccessRight> accessrights = new ArrayList <SimpleAccessRight>();
		SimpleAccessRight ar = new SimpleAccessRight();
		
		ar.setPermittedAction(ctr.getAction());
		ar.setResource(ctr.getResource());
		accessrights.add(ar);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		CapabilityTokenExtended ct = new CapabilityTokenExtended(id, ii, iss, ctr.getSubject(), ctr.getDevice(), null, accessrights, nb, na, resourceDescription);
		
		String signature = this.generateSignature (ct, capabilityPrivateKey);
		ct.setSig(signature);
		
		String token_firmado = gson.toJson(ct);
		
		System.out.println(gson.toJson(new JsonParser().parse(token_firmado)));


		return token_firmado;
	}


	private String generateSignature(CapabilityTokenExtended ct, PrivateKey serverPrivateKey) {
		System.out.println("*****generateSignature: ");
		String dataToCheck = this.getSignatureString(ct);
		try {
			Signature signature = Signature.getInstance(CERT_ALGORITHM_SIGNATURE);

			signature.initSign(serverPrivateKey);
			signature.update(dataToCheck.getBytes());
			byte[] sigBytes;
			sigBytes = signature.sign();
			byte [] signature_bytes = Base64.getEncoder().encode(sigBytes);
			return new String (signature_bytes);
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e1) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e1);
			return null;
		}
	}
	
	public String generateTokenID() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
	
	public String readResourceDescription (String filePath){
		BufferedReader br = null;
		String description = "";
		try {
			br = new BufferedReader(new FileReader(filePath));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			description = sb.toString();
			br.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		/*
		System.out.println("************************************************************************************");
		System.out.println("************************************************************************************");
		System.out.println("************************************************************************************");
		System.out.println("************************************************************************************");
		System.out.println(description);
		*/
		return description;
	}
	
	private String getSignatureString(CapabilityTokenExtended capability_token) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("CapabilityToken{id=");
		stringBuilder.append(capability_token.getID());
		stringBuilder.append(", ii=");
		stringBuilder.append(capability_token.getIssueIns());
		stringBuilder.append(", is=");
		stringBuilder.append(capability_token.getIss());
		stringBuilder.append(", su=");
		stringBuilder.append(capability_token.getSub());
		stringBuilder.append(", de=");
		stringBuilder.append(capability_token.getDe());
		stringBuilder.append(", ar=");
		
		ArrayList<SimpleAccessRight> ar = capability_token.getAr();
		
		for(int i = 0, size = ar.size(); i < size; i++) {
			stringBuilder.append(ar.get(i).toString());
		}
		
		stringBuilder.append(", nb=");
		stringBuilder.append(capability_token.getNb());
		stringBuilder.append(", na=");
		stringBuilder.append(capability_token.getNa());
		stringBuilder.append("}");

		String dataToCheck = stringBuilder.toString();

		return dataToCheck;
	}
}
