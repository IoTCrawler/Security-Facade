/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.odins.tokens;

import java.io.IOException;

import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.odins.util.filemanagement.Read4mFile;
import org.umu.capability.generator.CapabilityGenerator;
import org.umu.capability.token.CapabilityTokenRequest;
import org.umu.xacml.pdp.client.PdpClient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import es.odins.http.usecases.HttpsClient;
import es.odins.https.simpleclient.NetworkEntity;
import es.odins.https.simpleclient.SimpleHTTPSClient;


/**
 * Servlet implementation class ObtainCapabilityToken
 */
@WebServlet("/IdemixTokenIdentity")
public class ObtainCapabilityTokenParametrized extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	public static String configurationFolderPath = System.getenv("CAPMANAGER_CONFIG_FOLDER");
	
	private JsonObject 	 configuration;
	
	public  static NetworkEntity idemixEntity 		= null; 
	public  static NetworkEntity pdpEntity 			= null; 
	
	boolean notNull(Object... args) {
	    for (Object arg : args) {
	        if (arg == null) {
	            return false;
	        }
	    }
	    return true;
	}
	
	boolean isNull(Object... args) {
		return !(notNull(args));
	}

	/**
	 * Default constructor. 
	 */
    public ObtainCapabilityTokenParametrized() {
		pdpEntity 	= new NetworkEntity(configurationFolderPath+"/config/network_configuration.json", "pdp");
		idemixEntity= new NetworkEntity(configurationFolderPath+"/config/network_configuration.json", "idemix");

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Serving GET at: ").append(request.getContextPath());
	}

    
    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

    	String configFile = configurationFolderPath+"/config/configuration.json";
    	String app_configFile = configurationFolderPath+"/config/app_configuration.json";
    	
		this.configuration = Read4mFile.readJSONFile(configFile);

		System.out.println( "------- (Configuration information) -------------" );
		System.out.println(this.configuration);
		System.out.println( "--------------------" );

		System.out.println("Serving POST");
		
		/*
		 * First we get the headers and the information we expect to retrieve. 
		 */
		
		String action 			= request.getHeader("action");
		String device 			= request.getHeader("device");
		String resource 		= request.getHeader("resource");
		String idemixIdentity 	= request.getHeader("idemixIdentity");

		// From the IDEMIX identity we get the Idemix Token
		
		SimpleHTTPSClient shc = new SimpleHTTPSClient (configurationFolderPath+"/config/network_configuration.json");	

    	String idemixToken = null;
		try {
			idemixToken = shc.getIdemixToken(idemixIdentity, configurationFolderPath, app_configFile);
			if(idemixToken == null) {
				response.getWriter().append("There was an error getting the Token.");
				return; 
			}
			
		} catch (KeyManagementException | NoSuchAlgorithmException e2) {
			e2.printStackTrace();
			response.getWriter().append("There was an error getting the Token.");
			return; 
		}

		
		if(isNull(action, device, resource, idemixToken) ) {
			response.getWriter().append("We were expecting the following headers: \n"
					+ "action   - action expected to be granted POST, GET, PUT, DELETE\n"
					+ "device   - the device IP, Domain name, etc. plus port to where we intend to gain access\n"
					+ "resource - the resource to which we intend to gain access (e.g., URI Path )\n"
					);
			return; 
		}
		
		System.out.println("Parameters received: ");
		
		System.out.println("action: " + action);
		System.out.println("device: " + device);
		System.out.println("resource: " + resource);
		System.out.println("idemix-token: " + idemixToken);

		System.out.println("Acceding the Idemix Component through the URL " + idemixEntity.getURL("getToken"));
		
	    String userInfo = new HttpsClient(configurationFolderPath+"/config/network_configuration.json").getInfo4mToken(configurationFolderPath, app_configFile, idemixToken);		

	    if(userInfo.equals("") || userInfo == null) {
			response.getWriter().append("There was an error getting the Token.");
			return; 
	    }
        
	    JsonParser parser = new JsonParser();
        JsonElement userInfoTree = parser.parse(userInfo);
        String expirationTimeString = userInfoTree.getAsJsonObject().get("expires").getAsString();

        
        //2020-04-29T14:40:03.000Z
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));   // This line converts the given date into UTC time zone
        Date expiration = null;
		try {
			expiration = sdf.parse( expirationTimeString );
		} catch (ParseException e1) {
			e1.printStackTrace();
			response.getWriter().append("{\"error\":\"There was an error parsing the expired.\"}");
			return; 
		}
        
        Date now = new Date();
        System.out.println("Now: "+ now);
        System.out.println("Expiration: "+ expiration);
        
        if(expiration.compareTo(now) > 0) {
            System.out.println("expiration occurs after now");
         } else if(expiration.compareTo(now) < 0 || expiration.compareTo(now) == 0) {
 			response.getWriter().append("{\"error\":\"There token has expired.\"}");
 			return; 
         }
	    
	    
	    //// Verifying the information about the user.
	    JsonElement jsonObject = userInfoTree.getAsJsonObject().get("User").getAsJsonObject();
        System.out.println(jsonObject);

        String userName4mIdemixToken = jsonObject.getAsJsonObject().get("username").getAsString();
        System.out.println("We got the Username: " + userName4mIdemixToken + " from the Idemix Token");
        
        
        String isUserEnabled = jsonObject.getAsJsonObject().get("enabled").getAsString();
        if(!isUserEnabled.equals("true")) 
        {
			response.getWriter().append("{\"error\":\"The user is not enabled\"}");
			return; 
        }else {
        	System.out.println("The user is enabled.");
        }
        
        
        PdpClient pdpClient= new PdpClient(
                // The / at the end of the URL is FREAKING IMPORTANT!!!! otherwise you get an 302 Message
                new URL( pdpEntity.getURL("getPDPdecision").toString())
        );
        
        System.out.println("PDP URL: "+pdpEntity.getURL("getPDPdecision").toString());
        try {
			boolean result = pdpClient.requestPDPDecision(userName4mIdemixToken, device+resource, action);
			System.out.println(result);
			if(result == false) {
				response.getWriter().append("{\"error\":\"You are not authorized to generate the Capability Token\"}");
				return; 
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().append("{\"error\":\"There was an error contacting the PDP, please contact your admin.\"}");
			return; 
		}
        
        
	    
		
		// 1º - We create a Capability Token Request Object
		CapabilityTokenRequest ctr = 
				new CapabilityTokenRequest(userName4mIdemixToken, action, device, resource);
	
		String token = null;

		String relativeWebPath = "/config/configuration.json";
		CapabilityGenerator cg = new CapabilityGenerator(configurationFolderPath,relativeWebPath);
		token = cg.generate_capability_token(ctr);
		System.out.println(token);

		response.getWriter().append(token);
		
	}

}
