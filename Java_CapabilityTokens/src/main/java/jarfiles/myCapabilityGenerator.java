/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package jarfiles;

import org.umu.capability.generator.CapabilityGenerator;
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
public class myCapabilityGenerator {

	
	
	
	public static void main(String[] args) throws MalformedJsonException {
	
		String user 	= args[0];
		String action 	= args[1];
		String device 	= args[2];
		String resource = args[3];
		
	
		// 1º - We create a Capability Token Request Object
		
		CapabilityTokenRequest ctr = 
				new CapabilityTokenRequest(user, action, device, resource);
	

		String token = null;
		CapabilityGenerator cg = new CapabilityGenerator("config/configuration.json");
		token = cg.generate_capability_token(ctr);

		System.out.println(token);
		return;


	}
	
	
}
