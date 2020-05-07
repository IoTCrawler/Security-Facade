/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.umu.capability.test;

import org.umu.capability.evaluator.CapabilityEvaluator;
import org.umu.capability.evaluator.CapabilityVerifierCode;
import org.umu.capability.generator.CapabilityGenerator;
import org.umu.capability.token.CapabilityToken;
import org.umu.capability.token.CapabilityTokenRequest;

import com.google.gson.stream.MalformedJsonException;

/**
 * In this class we intend to develop a series of tests with a two-fold purpose:
 * 	1º - Provide a straightforward code that shows how the library works
 *  2º - Verify that indeed everything is working as expected
 *  Note: Not to confuse with Unit testing which are not performed per se here. 
 * 
 * @author dgarcia
 *
 */
public class CapabilityTest {
	
	
	public static void main(String[] args) throws MalformedJsonException {
	
		
		
		
		String user 	= null;//args[0];
		String action 	= null;//args[1];
		String device 	= null;//args[2];
		String resource = null;//args[3];
		
		user ="user";
		action ="action";
		device ="device";
		resource ="resource";
		// 1º - We create a Capability Token Request Object
		
		CapabilityTokenRequest ctr = 
				new CapabilityTokenRequest(user, action, device, resource);
	

		String token = null;
		{
			CapabilityGenerator cg = new CapabilityGenerator("config/configuration.json");
			token = cg.generate_capability_token(ctr);
		}
		
		// Now we evaluate the Capability Token

		System.out.println("This is the generated Capability Token --- ");
    	System.out.println( token );
    	System.out.println("------------------");		
		
    	CapabilityToken ct = CapabilityToken.getCapabilityTokenFromString( token );

    	
    	System.out.println("This is the generated Capability Token from CT Object --- ");
    	System.out.println(ct.toString());
    	System.out.println("------------------");		
		
		// Evaluating external capability tokens.
		CapabilityEvaluator ce = new CapabilityEvaluator("config/configuration.json", ct);
		CapabilityVerifierCode code  = ce.evaluateCapabilityToken(action, resource, device);
		System.out.println("Is token valid?" + code);

		
	}
	
	
}
