/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.umu.capability.evaluator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import org.odins.util.filemanagement.Read4mFile;
import org.umu.capability.rights.SimpleAccessRight;
import org.umu.capability.token.CapabilityToken;

import com.google.gson.JsonObject;


/**
 * Create the class to evaluate a Capability Token.
 * 
 * @author dangarcia
 *
 */
public class CapabilityEvaluator
{

	X509Certificate certificate;
	CapabilityToken ct;

    private static  String TRUST_STORE_LOCATION;
    private static  String TRUST_STORE_PASSWORD;
    private static  String TRUST_STORE_ENTRY;
    private static  String KEY_STORE_LOCATION;
    private static  String KEY_STORE_PASSWORD;
    private static  String KEY_STORE_ENTRY;
    private static  String CERT_ALGORITHM_SIGNATURE;
    
   	PublicKey  capabilityPublicKey  = null;
	
	public CapabilityEvaluator(String configFile) {
		
		
		System.out.println("CapabilityEvaluator started");
		JsonObject config = Read4mFile.readJSONFile(configFile);
		
		System.out.println(config.toString());


	     System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
	     
	    
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
		            this.capabilityPublicKey = keyStore.getCertificate(KEY_STORE_ENTRY).getPublicKey();

		            		             
			  } catch (GeneralSecurityException | IOException e) {
		            System.err.println("Could not load the keystore");
		            e.printStackTrace();
		      }
			  
			  System.out.println("CapabilityGenerator completed");		
	}
	
	
	public CapabilityEvaluator(String rootPath, String configFile, CapabilityToken ct) {
		
		this.ct = ct;
		
		System.out.println("CapabilityEvaluator started 3");
		JsonObject config = Read4mFile.readJSONFile(rootPath + configFile);
		System.out.println("JSON File: \n" + config.toString() + "\n" );
		
		System.out.println(config.toString());
	     
		
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
		File f = new File(KEY_STORE_LOCATION); 
		
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

		            System.out.println("TRUST_STORE_ENTRY: "  +TRUST_STORE_ENTRY);
		            trustedCertificates[0] = trustStore.getCertificate(TRUST_STORE_ENTRY);
		            
		            System.out.println("KEY_STORE_ENTRY: "  +KEY_STORE_ENTRY);
		            System.out.println(keyStore.containsAlias(KEY_STORE_ENTRY));
		            this.capabilityPublicKey = keyStore.getCertificate(KEY_STORE_ENTRY).getPublicKey();

		            		             
			  } catch (GeneralSecurityException | IOException e) {
		            System.err.println("Could not load the keystore");
		            e.printStackTrace();
		      }
			  
			  System.out.println("CapabilityGenerator completed");		
	}
	

	public CapabilityEvaluator(String configFile, CapabilityToken ct) {
		this(configFile);
		this.ct = ct;
	}
	public CapabilityEvaluator(String configFile, String ct) {
		this(configFile);
		this.ct = CapabilityToken.getCapabilityTokenFromString(ct);
	}
	
	
	/**
	 * Given a capability token, the action (normally a REST command) to perform over a service, 
	 * and the device and resource on which the action is intended to be performed.
	 * @param capability_token
	 * @param action
	 * @param device_resource
	 * @return CapabilityVerifierCode
	 */
	private CapabilityVerifierCode evaluateCapabilityTokenIntrinsicInformation()
	{

        if ( !isTimeValid(this.ct.getIssueIns(), this.ct.getNb(), this.ct.getNa()) ) {
			System.err.println("Error: The time is not Valid");
        	return CapabilityVerifierCode.OUTATIME;
        }

		boolean signatureIsValid = isSignatureValid();

		if (!signatureIsValid) {
			System.err.println("Error: The signature is not Valid");

			return CapabilityVerifierCode.SIGNATURE_NOT_VALID;
		}else {
			System.out.println("INFO: The signature IS Valid");
			
		}

		System.out.println("The action is authorized");
		return CapabilityVerifierCode.AUTHORIZED;
	}

	
	public CapabilityVerifierCode evaluateCapabilityToken(String action, String resource, String device) {
		return this.evaluateCapabilityTokenIntrinsicInformation();
	}
	
	public CapabilityVerifierCode evaluateCapabilityToken() {
		return this.evaluateCapabilityTokenIntrinsicInformation();
	}


	/**
	 * This function returns if the time frame is valid for the token.
	 * @param ii_field
	 * @param nb_field
	 * @param na_field
	 * @return
	 */
	private boolean isTimeValid(long ii_field, long nb_field, long na_field) {
		long currentTime = System.currentTimeMillis() / 1000L;
		return (ii_field < currentTime) && (ii_field <= nb_field) && (nb_field < na_field) && (ii_field < na_field) && (na_field >= currentTime);
	}

	
	/**
	 * This function evaluates if the signature is valid
	 * @param capability_token
	 * @return
	 */
	public boolean isSignatureValid() {
		
		List<String> signatures2Check = new LinkedList<String>();
			 signatures2Check.add(CERT_ALGORITHM_SIGNATURE);
			 // Add all the possible signatures to check them all before rendering the Capability Token invalid.	 
		
		String dataToCheck = getSignatureString();
		System.out.println("***************** " + dataToCheck);
		System.out.println(dataToCheck.length());

		while(signatures2Check.size()>0)
		{
			try
			{
				String currentSignature = signatures2Check.remove(0);
				System.out.println("Checking the signature " + currentSignature);
				Signature signature = Signature.getInstance(currentSignature);
				
				signature.initVerify(this.capabilityPublicKey);
				signature.update(dataToCheck.getBytes("UTF-8"));
	
				byte[] signature_bytes = Base64.getDecoder().decode(this.ct.getSig());
				return signature.verify(signature_bytes);
			
			} catch (InvalidKeyException | NoSuchAlgorithmException| SignatureException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * This function returns a String with the content to check that the signature is correct.
	 * @param capability_token
	 * @return String
	 */
	private String getSignatureString() 
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("CapabilityToken{id=");
		stringBuilder.append(ct.getID());
		stringBuilder.append(", ii=");
		stringBuilder.append(ct.getIssueIns());
		stringBuilder.append(", is=");
		stringBuilder.append(ct.getIss());
		stringBuilder.append(", su=");
		stringBuilder.append(ct.getSub());
		stringBuilder.append(", de=");
		stringBuilder.append(ct.getDe());
		stringBuilder.append(", ar=");

		ArrayList<SimpleAccessRight> ar = ct.getAr();

		int i = 0; for (int size = ar.size(); i < size; i++) {
			stringBuilder.append(((SimpleAccessRight)ar.get(i)).toString());
		}

		stringBuilder.append(", nb=");
		stringBuilder.append(ct.getNb());
		stringBuilder.append(", na=");
		stringBuilder.append(ct.getNa());
		stringBuilder.append("}");

		String dataToCheck = stringBuilder.toString();

		return dataToCheck;
	}
}