/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.odins.tokens;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.umu.capability.evaluator.CapabilityEvaluator;
import org.umu.capability.evaluator.CapabilityVerifierCode;
import org.umu.capability.token.CapabilityToken;




/**
 * Servlet implementation class ObtainCapabilityToken
 */
@WebServlet("/verify")
public class VerifyCapabilityToken extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String configurationFolderPath = System.getenv("CAPMANAGER_CONFIG_FOLDER");

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
    public VerifyCapabilityToken() {

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
    	
		System.out.println("Serving POST");
		
		/*
		 * First we get the headers and the information we expect to retrieve. 
		 * 
		 * 
		 */
		
		String action 	= request.getHeader("action");
		String device 	= request.getHeader("device");
		String resource = request.getHeader("resource");
		String captoken = request.getHeader("captoken");

		if(isNull(action, device, resource, captoken) ) {
			response.getWriter().append("We were expecting the following headers: \n"
					+ "action   - action expected to be granted POST, GET, PUT, DELETE\n"
					+ "device   - the device IP, Domain name, etc. plus port to where we intend to gain access\n"
					+ "resource - the resource to which we intend to gain access (e.g., URI Path )\n"
					+ "captoken - the Capability Tokent to verify\n"
					);
			return; 
		}
		
		System.out.println("Parameters received: ");
		
		System.out.println("action: " + action);
		System.out.println("device: " + device);
		System.out.println("resource: " + resource);
		System.out.println("captoken: " + captoken);
		

		// Evaluating external capability tokens.
		CapabilityToken ct = CapabilityToken.getCapabilityTokenFromString(captoken);
		CapabilityEvaluator ce = new CapabilityEvaluator(configurationFolderPath,"/config/configuration.json", ct);
		CapabilityVerifierCode code  = ce.evaluateCapabilityToken(action, resource, device);
		
		response.getWriter().append( code.toString() );
		
	}

}
