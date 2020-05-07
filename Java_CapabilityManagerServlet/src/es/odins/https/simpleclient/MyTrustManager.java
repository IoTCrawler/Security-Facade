/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package es.odins.https.simpleclient;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;



 
class MyTrustManager implements X509TrustManager {
	
	
	
	// For an empty TrustManager
	public void checkClientTrusted(X509Certificate[] chain, String
			authType) {
	}

	public void checkServerTrusted(X509Certificate[] chain, String
			authType) {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}

}
