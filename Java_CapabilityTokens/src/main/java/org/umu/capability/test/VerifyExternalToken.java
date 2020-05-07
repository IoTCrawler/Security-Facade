/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.umu.capability.test;



import org.umu.capability.evaluator.CapabilityEvaluator;
import org.umu.capability.evaluator.CapabilityVerifierCode;
import org.umu.capability.token.CapabilityToken;

import com.google.gson.stream.MalformedJsonException;


//


public class VerifyExternalToken {

	public static void main(String[] args) throws MalformedJsonException {
		
		// Evaluating external capability tokens.
		System.out.println("Evaluating external capability token");
		
		String CapTokenString  = "{   \"del\": \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjnzYGNReMlgRfiOU/QSqR7f1m1zoaa47fytKnoMZSCElEgpjWRn6lK+xxXVp5d5+QoqfcpbLnwN440hCV8+lUDAe2pfA3gaooyJimq30dYGEWevTzBvhIwZvhKZmpib2V25I/1NvfzIlFpTKtfBc3F1bhzUEMDblyxjWQ1Q0S48PrthD9NiBQKgmwwQXVVgvkOVFRFDOI2oFVR7JxtTDi5bh4cUyIiXyCZpp7QDSq4ixkji6c0wR/ZCeOcZDXNDf4R93wOrMdLFCnyG5GuUccB1CMaMjg7K/wOaOFitcD1ZFEtbxomOOGczxuZDcNuenPMj9vAtb3huEhXtXiyH7iQIDAQAB\",   \"id\": \"o53lfdisdkja1omhlnn1l0sjq8\",   \"ii\": 1557226577,   \"is\": \"capabilitymanager@um.es\",   \"su\": \"fcarrez\",   \"de\": \"device\",   \"si\": \"JTP80jGU4g3o/Pl5CgYJBLZxFqcGsnKIiP/iipzPczNajaASdewxyhZqKGWFzpdr74h62ppx7uOhA+PZD5fm92YVJqRpZnP8x9ZoZi/aamTZ5n7CT3gGXjNQxKZoeuzILGprVvRAs8xnpFE7wyVE92G+9m6qO93Q7ReGyFb5m4Nt575ncXU+8iBqSBgW7YTQyYkjl6W07vPVHKFsWGxpa2ee4THZnMQ0hBLsscWAew3xzgs+l7/TFON82jFGuQS0nJ3HSQvCWFASELyzDimxp1P7quwVeGWyPA7yRqVETMyIYuholWDsKhAd1dxC1KRlrFaGo1AHj0oFZR7Lr4vZIg==\",   \"ar\": [     {       \"ac\": \"action\",       \"re\": \"resource\"     }   ],   \"nb\": 1557226577,   \"na\": 2557230177 }";
		//String capTokenString2 = "{\"id\":\"1aicfl851it914t7a81ne4q1l4\",\"ii\":1572530570,\"is\":\"capabilitymanager@odins.es\",\"su\":\"user\",\"de\":\"device\",\"si\":\"MEYCIQCgU69kYB6qoK+kPChXq6CUvtYOtmj2oBfY6f7uh/bc7wIhAJBjYl6XngPGQoQYQTtrFk3V7ymWD7CRHZSc/jQDGCfH\",\"ar\":[{\"ac\":\"action\",\"re\":\"resource\"}],\"nb\":1572531570,\"na\":1572541570}";

		CapabilityToken ct = CapabilityToken.getCapabilityTokenFromString(CapTokenString);

		    	
    	System.out.println("This is the generated Capability Token from CT Object --- ");
    	System.out.println(ct.toString());
    	System.out.println("------------------");		
		
		System.out.println("Evaluating the token generated");
		

		String action 	= "action";
		String device 	= "device";
		String resource = "resource";
		
		// Evaluating external capability tokens.
		CapabilityEvaluator ce = new CapabilityEvaluator("config/configuration.json", ct);
		CapabilityVerifierCode code  = ce.evaluateCapabilityToken(action, resource, device);
		System.out.println("Is token valid?" + code);

		
		
	}
	
	
	
	
}
