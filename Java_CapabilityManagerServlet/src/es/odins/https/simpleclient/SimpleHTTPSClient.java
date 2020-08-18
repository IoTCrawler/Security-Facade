/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package es.odins.https.simpleclient;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import java.util.List;
import java.util.Map;
import java.io.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import es.odins.util.IOmanagement;

public class SimpleHTTPSClient
{

	//private String configurationPath = null;
	public  static NetworkEntity idemixEntity 		= null; 
	public  static NetworkEntity fiwarePEPEntity 	= null; 
	public  static NetworkEntity capmanagerEntity 	= null; 

	static {
	
	}


	public SimpleHTTPSClient(String configurationPath) {	
	
		//this.configurationPath 	= configurationPath;	
		System.out.println("Creating SimpleHTTPSClient: Configuration file - " + configurationPath);
		idemixEntity 		= new NetworkEntity(configurationPath, "idemix");
		fiwarePEPEntity 	= new NetworkEntity(configurationPath, "fiwarePEP");
		capmanagerEntity 	= new NetworkEntity(configurationPath, "capmanager");
	
		//for localhost testing only
	    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
	    new javax.net.ssl.HostnameVerifier(){
	        public boolean verify(String hostname,
	                javax.net.ssl.SSLSession sslSession) {
	            if (hostname.equals(idemixEntity.address) || 
	            	hostname.equals(fiwarePEPEntity.address)||
	            	hostname.equals(capmanagerEntity.address)) {
	                return true;
	            }
	            return false;
	        }
	    });
	}
	
	public String getIdemixToken(String UserInfo, String root, String configurationPath) throws IOException, NoSuchAlgorithmException, KeyManagementException {
		
    System.out.println("Idemix URL: " + idemixEntity.getURL("getToken") );
    System.out.println("after URL: ");
		URL url = idemixEntity.getURL("getToken");
		System.out.println("Before create ssl: ");
		SSLContext sslctx = MySSLContext.createSSLContext(root, configurationPath);
		HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

		con.setRequestMethod("POST");
		con.setDoOutput(true);		
		con.setRequestProperty("Content-Type", "application/json");
		
    System.out.println("Before output stream : " + con.getSSLSocketFactory());
		  try(OutputStream os = con.getOutputStream()) {
			    byte[] input = UserInfo.getBytes("utf-8");
			    os.write(input, 0, input.length);           
			}  
      System.out.println("Before connection factory: " + con.getSSLSocketFactory());
		  con.connect();
      System.out.println("After connection res code: " + con.getResponseCode());
		  if(con.getResponseCode() == 401) {
			  System.out.println("There was an error getting the token: Response code - 401");
			  return null;
		  }
		  
		  
		  Map<String, List<String>> map = con.getHeaderFields();

		  for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			  	System.out.println("Key : " + entry.getKey() + 
		                 " ,Value : " + entry.getValue());
			}

		  String token = con.getHeaderField("X-Subject-Token");
		  		System.out.println( "Token: " + token);
  
		  
		  try(BufferedReader br = new BufferedReader(
				  new InputStreamReader(con.getInputStream(), "utf-8"))) {
				    StringBuilder response = new StringBuilder();
				    String responseLine = null;
				    while ((responseLine = br.readLine()) != null) {
				        response.append(responseLine.trim());
				    }
				    System.out.println(response.toString());
				}

		  
		con.disconnect();

		return token;
		
	}
	
	public static String getCapabilityToken(String root, String configurationPath, String idemixToken, String action, String resource, String device) throws KeyManagementException, NoSuchAlgorithmException, IOException 
	{
		
		System.out.println("getcapabilityManagerURL: " + capmanagerEntity.getURL("generate") );

		SSLContext sslctx = MySSLContext.createSSLContext(root, configurationPath);
		HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
		HttpsURLConnection con = (HttpsURLConnection) capmanagerEntity.getURL("generate").openConnection();

		
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		
		con.setRequestProperty("action"			, action);
		con.setRequestProperty("resource"		, resource);
		con.setRequestProperty("device"			, device);
		con.setRequestProperty("idemix-token"	, idemixToken);
		

		  con.connect();
		  
		  
		  String token = "";  
		  try(BufferedReader br = new BufferedReader(
				  new InputStreamReader(con.getInputStream(), "utf-8"))) {
				    StringBuilder response = new StringBuilder();
				    String responseLine = null;
				    while ((responseLine = br.readLine()) != null) {
				        response.append(responseLine.trim());
				    }
				    //System.out.println(response.toString());
				    token += response.toString();
				}

		  
		con.disconnect();
		return token;
	}
	
	public static String verifyCapabilityToken(String capToken, String action, String resource, String device) throws KeyManagementException, NoSuchAlgorithmException, IOException 
	{
		
		System.out.println("verifycapabilityManagerURL: " + capmanagerEntity.getURL("verify") );
		HttpURLConnection con = (HttpURLConnection) capmanagerEntity.getURL("verify").openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		
		con.setRequestProperty("action"			, action);
		con.setRequestProperty("resource"		, resource);
		con.setRequestProperty("device"			, device);
		con.setRequestProperty("captoken"		, capToken);
		

		  con.connect();
		  
		  
		  String token = "";  
		  try(BufferedReader br = new BufferedReader(
				  new InputStreamReader(con.getInputStream(), "utf-8"))) {
				    StringBuilder response = new StringBuilder();
				    String responseLine = null;
				    while ((responseLine = br.readLine()) != null) {
				        response.append(responseLine.trim());
				    }
				    //System.out.println(response.toString());
				    token += response.toString();
				}

		  
		con.disconnect();
		return token;
	}
	
	
	public static String sendInformationToPEPProxy(String capabilityToken, String NGSI, String NGSI_URI_Path) throws KeyManagementException, NoSuchAlgorithmException {

        int responseCode = 0;

        try {
        	String pepProxyURL = fiwarePEPEntity.getURL() + NGSI_URI_Path; 
        	System.out.println("PEP proxy URL: "+ pepProxyURL);
            
        	URL url = new URL( pepProxyURL );
            
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setHostnameVerifier(new HostnameVerifier(){

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("fiware-service", "PlugNHarvest");
            conn.setRequestProperty("fiware-servicepath", "/pilot1");
            conn.setRequestProperty("x-auth-token", capabilityToken);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            OutputStream os = conn.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            System.out.println(NGSI);
            bw.write(NGSI);
            bw.flush();
            
            StringBuilder response = new StringBuilder();

            responseCode = conn.getResponseCode();
            if (responseCode == 200) 
            { 
            	InputStream is = conn.getInputStream();
                if (is != null) 
                { 
                	BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    
                    String output;
                    while ((output = rd.readLine()) != null) {
                      response.append(output);
                    }
                }
                is.close();
            } 
            else if (responseCode == 401)
            { 
            	System.out.println("Not authorized to access the resource");

            } else {
            	System.out.println("An error HTTP " + responseCode + " occured");

            }
            
            if (response != null) {
                System.out.println("Response-->" + response.toString());
                return response.toString();
            }
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "An error HTTP " + responseCode + " occured";
    }
	
	
	
    public static void main(String[] args) throws Exception {
      /*  
    	SimpleHTTPSClient shc = new SimpleHTTPSClient("./configuration/user_information.json");
    	
    	String userInfo = IOmanagement.readJSONFile("./configuration/user_information.json").toString();
    	String token = shc.getIdemixToken(userInfo);
    	

    	String capabilityToken = getCapabilityToken(token, "POST", "46.101.134.117", "/updateContext");
    	System.out.println("Token: " + capabilityToken);

    	String result = verifyCapabilityToken(capabilityToken, "POST", "46.101.134.117","/updateContext");
    	System.out.println("Verification: " + result);

    	*/

    }

}


