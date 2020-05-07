/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package jarfiles;


import org.umu.capability.evaluator.CapabilityEvaluator;
import org.umu.capability.evaluator.CapabilityVerifierCode;
import org.umu.capability.token.CapabilityToken;

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
public class myCapabilityEvaluator {
	
	public static void main(String[] args) throws MalformedJsonException {

		String action 	= args[1];
		String device 	= args[2];
		String resource = args[3];
		String captoken = "{\"id\":\"gdqjrfumh0acqj2pgmlqh1bap8\",\"ii\":1588197411,\"is\":\"capabilitymanager@odins.es\",\"su\":\"testuser@odins.es\",\"de\":\"device\",\"si\":\"R0Vq9FId21ENq4YJse8GxQarHr/VptRZl72rfYl8dxkCE2Qi/aJH9DeyUe8SMx9X+ck7P5EjGKBOO6RN7iQ1jY/fdxbyH71C3eQ83w2LNwUznmL3Los+0leKon65BFsU5zMxd96O8zggEoTqLBHp6mji5jQ3tPjazqS/qYH918lgNRjq0buQxfEGIhfiKBGUIW6iGhqDaxo6xK03yLhVdoaZPXhjHsFnpf1wa+Mem6odETBqRseIYTIH0iVMOtHqriQhq1RibLBDVKUpBLqXUNJxm1ZVq0veuwQW3qaPyTadrcz3rhHj3KaxHcynvEJc7WQwBI3mwoF0igeUBE2MT0wuIFggd7ll6+RxV4ESac5W3tXzMhhA5uHxp9hbBsbEKdNBkzrsUbp8ds7y7CZ+65ozfPHJqVMNnQSfS9RnagenA7dBgXjZg1RepllCOMX/QcvxrwyIUmXmGQUiBQ3h2xkKyZxoIrkB5q3oRcMriBoOnsMReAbLj9ji3XENI22KgakbzodHy46r6/Hemqz5swlpz0tWFNyQ7UnQNVq0I9c3UZqFAohfDzQVpn6jpu08VBigpaa7GKB6bYIApkSdPcikubM5OVxRNgzT+Xg3GfoFkmOIgoax5WQ79htWjLr0nv4jXU9V438/aAkBInxFGhzEUnMtqef+3U4VLzS451o=\",\"ar\":[{\"ac\":\"action\",\"re\":\"resource\"}],\"nb\":1588198411,\"na\":1588208411}";//args[4];

		
		// Evaluating external capability tokens.
		CapabilityToken ct = CapabilityToken.getCapabilityTokenFromString(captoken);
		CapabilityEvaluator ce = new CapabilityEvaluator("","config/configuration.json", ct);
		CapabilityVerifierCode code  = ce.evaluateCapabilityToken(action, resource, device);
		System.out.println("Is token valid?" + code);

		
	}
	
	
}
