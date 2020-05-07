/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package es.odins.https.simpleclient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import es.odins.util.IOmanagement;

public class NetworkEntity {

		String address, port, protocol;
		Map<String,String> services = null;

		public NetworkEntity( String JSONfile, String entityNameEntry) {
			JsonObject entity = IOmanagement.readJSONFile(JSONfile).get(entityNameEntry).getAsJsonObject();
			
			this.protocol = entity.get("protocol").getAsString();
			this.address = entity.get("address").getAsString();
			this.port = entity.get("port").getAsString();
			
			if(entity.has("services")){
				JsonArray entityServices = entity.get("services").getAsJsonArray();
				this.services = new HashMap<String, String>(entityServices.size());
				
				for (int i=0; i < entityServices.size(); i++) {
					JsonObject service = (JsonObject) entityServices.get(i);
					String path = service.get("path").getAsString();
					if(! path.startsWith("/") ) {
						path = "/" + path;
					}
					this.services.put(service.get("name").getAsString(), path);
				}

			}
		}
		
		public URL getURL() throws MalformedURLException {
			return new URL(this.protocol + "://" + this.address + ":" + this.port);
			
		}
		
		public URL getURL(String service) throws MalformedURLException {
			
			if(this.services != null && this.services.containsKey(service)) {
				System.out.println("Service = " + this.services.get(service));
				return new URL(this.protocol + "://" + this.address + ":" + this.port + this.services.get(service));
			}else {
				System.err.println("This entity does not have the service " + service);
				return getURL();
			}
		}
		
		
		public String toString() 
		{
			if(services != null) {
				String servicesNames = "[";
				for (Map.Entry<String, String> entry : this.services.entrySet()) {
					servicesNames += "(" + entry.getKey()+ "," +entry.getValue()+ "),";
				}	
				servicesNames = servicesNames.substring(0,servicesNames.length()-1) + "]";
				return "[ NetworkEntity [ protocol=" + protocol + ", port=" + port + ", address="+ address + ", services="  +servicesNames+  "]";
			}
			return "[ NetworkEntity [ protocol=" + protocol + ", port=" + port + ", address="+ address +  "]";
		}

		
		public static void main(String[] args) throws MalformedURLException {
			
			
			NetworkEntity idemix = new NetworkEntity("./configuration/network_configuration.json", "idemix");
			System.out.println(idemix);
			System.out.println(idemix.getURL("yeah"));
			
			NetworkEntity fiwarePEP = new NetworkEntity("./configuration/network_configuration.json", "fiwarePEP");
			System.out.println(fiwarePEP);
			System.out.println(fiwarePEP.getURL("yeah"));

			NetworkEntity capmanager = new NetworkEntity("./configuration/network_configuration.json", "capmanager");
			System.out.println(capmanager);
			System.out.println(capmanager.getURL("verify"));
			System.out.println(capmanager.getURL("generate"));
			System.out.println(capmanager.getURL("yeah"));

			
		}
		
	
}
