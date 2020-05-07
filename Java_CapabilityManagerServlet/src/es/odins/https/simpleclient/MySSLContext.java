/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package es.odins.https.simpleclient;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import es.odins.util.IOmanagement;

public class MySSLContext {

	
	static String clientKeyStorePath 	= null;
	static String clientKeyStorePass 	= null;
	static String clientTrustStorePath = null;
	static String clientTrustStorePass = null;

	
	public MySSLContext (String configurationPath) {

	}

	public static SSLContext createSSLContext(String root, String configurationPath){
       
		 clientKeyStorePath 	= root + IOmanagement.readJSONFile(configurationPath)
				  .get("certificates").getAsJsonObject()
				  .get("clientKeyStorePath").getAsString();

	
		 clientTrustStorePath =  root + IOmanagement.readJSONFile(configurationPath)
					  .get("certificates").getAsJsonObject()
					  .get("clientTrustStorePath").getAsString();
	
		 // Password
		 clientKeyStorePass 	=   IOmanagement.readJSONFile(configurationPath)
				  .get("certificates").getAsJsonObject()
				  .get("clientKeyStorePass").getAsString();

		 clientTrustStorePass =   IOmanagement.readJSONFile(configurationPath)
					  .get("certificates").getAsJsonObject()
					  .get("clientTrustStorePass").getAsString();

		
		try{

        
        	
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream( clientKeyStorePath ),
            		clientKeyStorePass.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, clientKeyStorePass.toCharArray());

            KeyStore trustedStore = KeyStore.getInstance("JKS");
            trustedStore.load(new FileInputStream( clientTrustStorePath), 
            		clientTrustStorePass.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustedStore);

            SSLContext sc = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = tmf.getTrustManagers();
            			
            KeyManager[] keyManagers = kmf.getKeyManagers();
            sc.init(keyManagers, trustManagers, null);

            return sc;
            
        } catch (Exception ex){
            ex.printStackTrace();
        	System.exit(-1);
        }
        return null;
    }
}
