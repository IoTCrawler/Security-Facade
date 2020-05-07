/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package es.odins.http.usecases;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import java.io.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsClient {

	static {
	    //for localhost testing only
	    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
	    new javax.net.ssl.HostnameVerifier(){

	        public boolean verify(String hostname,
	                javax.net.ssl.SSLSession sslSession) {
	            if (hostname.equals("46.101.134.117") ||hostname.equals("localhost") || hostname.equals("xacml") || hostname.equals("keyrock")) {
	                return true;
	            }
	            return false;
	        }
	    });
	}
	
	
	public void disableCertificates() {
	    TrustManager[] trustAllCerts = new TrustManager[]{
	        (TrustManager) new X509TrustManager() {

	            @Override
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }

	            @Override
	            public void checkClientTrusted(
	                    java.security.cert.X509Certificate[] certs, String authType) {
	            }

	            @Override
	            public void checkServerTrusted(
	                    java.security.cert.X509Certificate[] certs, String authType) {
	            }
	        }
	    };

	    // Install the all-trusting trust manager
	    try {
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	    } catch (Exception e) {
	    }
	}

	public String getIdemixAuthToken(String URL, String body){

	
	      URL url;
	      try {
      	     disableCertificates();
		     url = new URL(URL);
		     
		     
		     HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

		     // Add headers
		     connection.setRequestMethod("POST");
		     connection.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON format

		     connection.setDoOutput(true);
		     OutputStream outStream = connection.getOutputStream();
		     OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, "UTF-8");
		     outStreamWriter.write(body);
		     outStreamWriter.flush();
		     outStreamWriter.close();
		     outStream.close();
		     
		     
		     
		     //dumpl all cert info
		     print_https_cert(connection);
				
		     //dump all the content
		     print_content(connection);
				
		     System.out.println(connection.getResponseMessage());
		     System.out.println(connection.getResponseCode());

		 	//get all headers
		 	Map<String, List<String>> map = connection.getHeaderFields();
		 	for (Map.Entry<String, List<String>> entry : map.entrySet()) {
		 		System.out.println("Key : " + entry.getKey() + 
		                  " ,Value : " + entry.getValue());
		 	}
		 	
			String x_subject_token = connection.getHeaderField("X-Subject-Token");
			return x_subject_token;
		     
	      } catch (MalformedURLException e) {
		     e.printStackTrace();
	      } catch (IOException e) {
		     e.printStackTrace();
	      }

	      return null;
	   }
		

    public String getInfo4mToken(String URL,String token){


	      URL url;
	      try {
      	     disableCertificates();
		     url = new URL(URL);
		     
		     
		     HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

		     // Add headers
		     connection.setRequestMethod("GET");
		     //connection.setRequestProperty("Content-Type"	, "application/json"); // We send our data in JSON format
		     connection.setRequestProperty("x-subject-token", token); // We send our data in JSON format
		     connection.setRequestProperty("x-auth-token"	, token); // We send our data in JSON format
		     
		     //dumpl all cert info
		     print_https_cert(connection);
		 	String responseBody = get_content(connection);

		     
		     System.out.println(connection.getResponseMessage());
		     System.out.println(connection.getResponseCode());

		 	//get all headers
		 	Map<String, List<String>> map = connection.getHeaderFields();
		 	for (Map.Entry<String, List<String>> entry : map.entrySet()) {
		 		System.out.println("Key : " + entry.getKey() + 
		                  " ,Value : " + entry.getValue());
		 	}
		 	

		 	return responseBody; 
		     
	      } catch (MalformedURLException e) {
		     e.printStackTrace();
	      } catch (IOException e) {
		     e.printStackTrace();
	      }

	      return null;
	   }
		
	   
	   
	   private void print_https_cert(HttpsURLConnection con){
	     
	    if(con!=null){
				
	      try {
					
		System.out.println("Response Code : " + con.getResponseCode());
		System.out.println("Cipher Suite : " + con.getCipherSuite());
		System.out.println("\n");
					
		Certificate[] certs = con.getServerCertificates();
		for(Certificate cert : certs){
		   System.out.println("Cert Type : " + cert.getType());
		   System.out.println("Cert Hash Code : " + cert.hashCode());
		   System.out.println("Cert Public Key Algorithm : " 
	                                    + cert.getPublicKey().getAlgorithm());
		   System.out.println("Cert Public Key Format : " 
	                                    + cert.getPublicKey().getFormat());
		   System.out.println("\n");
		}
					
		} catch (SSLPeerUnverifiedException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}

	     }
		
	   }
		
	   private void print_content(HttpsURLConnection con){
		if(con!=null){
				
		try {
			
		   System.out.println("****** Content of the URL ********");			
		   BufferedReader br = 
			new BufferedReader(
				new InputStreamReader(con.getInputStream()));
					
		   String input;
					
		   while ((input = br.readLine()) != null){
		      System.out.println(input);
		   }
		   br.close();
					
		} catch (IOException e) {
		   e.printStackTrace();
		}
				
	       }
			
	   }
	
	   private String get_content(HttpsURLConnection con){
		   String result= null;

		   if(con!=null){
				result = "";
			   	try {
					
				   System.out.println("****** Content of the URL ********");			
				   BufferedReader br = 
					new BufferedReader(
						new InputStreamReader(con.getInputStream()));
							
				   String input;
							
				   while ((input = br.readLine()) != null){
				      System.out.println(input);
				      result += input;
				   }
				   br.close();
							
				} catch (IOException e) {
				   e.printStackTrace();
				}
						
	       }
			return result;
	   }

	   
	  /* 
	   public static void main(String[] args)
	   {

		   String body = "{\n" + 
				  		"    \"name\": \"testuser@odins.es\",\n" + 
				  		"    \"password\": \"1234\"\n" + 
				  		"}";
				   
	        String token 	= new HttpsClient().getIdemixAuthToken(body);
	        System.out.println("========================");
	        System.out.println("User Token");
	        System.out.println("========================");
	        System.out.println(token);

		    String userInfo = new HttpsClient().getInfo4mToken(token);
	        
	        System.out.println("========================");
	        System.out.println("User Info");
	        System.out.println("========================");
	        System.out.println(userInfo);

	        JsonParser parser = new JsonParser();
	        JsonElement jsonTree = parser.parse(userInfo);
	        
	        JsonElement jsonObject = jsonTree.getAsJsonObject().get("User").getAsJsonObject();
	        System.out.println(jsonObject);
	        
	   }
	*/

	   
}
