/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/


import org.umu.xacml.pdp.client.PdpClient;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;


public class PdpClientTest {


    public static void main(String[] args) throws MalformedURLException {

        PdpClient pdpClient= new PdpClient(
                // The / at the end of the URL is FREAKING IMPORTANT!!!! otherwise you get an 302 Message
                new URL("http://localhost:8080/XACMLServletPDP/")
        );

        try {
            pdpClient.requestPDPDecision(
                    "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEAhPN60KBnrwsANw0dP6W11AAU60VUVed/Mpj8R1OOtC/HS1n+3TZ2xxPmvHmhdu2RxnZF6IgpqgWXzmBH0WVxA==",
                    "/api/message/secure_bootstrapping",
                    "POST"
            );
        }
        catch (Exception e){
            PdpClient.getLogger().log(Level.SEVERE,"Something whent wrong!");
            PdpClient.getLogger().log(Level.SEVERE,"Error: " + e);
        }

    }
}
