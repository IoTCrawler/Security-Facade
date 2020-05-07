/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.umu.xacml.pdp.client;

import org.umu.xacml.request.XACMLrequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.IOException;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public  class PdpClient {

    private static  Logger  theLogger = Logger.getLogger(PdpClient.class.getName());
    private         URL     PDP_URL;

    public static   Logger getLogger(){
        return theLogger;
    }
    public PdpClient(URL url){  this.PDP_URL = url;  }

    public boolean
    requestPDPDecision(String subject_attributes, String targetResource, String action) throws Exception {

        theLogger.log(Level.INFO, "\nSubject attributes: " + subject_attributes + "\n" +
                "Resource: " + targetResource + "\n" +
                "Action: " + action + "\n");

        theLogger.log(Level.INFO, "\n" +  XACMLrequest.getXACMLRequest(subject_attributes, targetResource, action));

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

            HttpPost post       = new HttpPost(this.PDP_URL.toString());
            String xacml_req    = XACMLrequest.getXACMLRequest(subject_attributes, targetResource, action);
            ByteArrayEntity req = new ByteArrayEntity(xacml_req.getBytes("UTF-8"));

            post.setEntity(req);

            HttpResponse httpresponse = httpClient.execute(post);

            String result = EntityUtils.toString(httpresponse.getEntity());
            theLogger.log(Level.INFO, "PDP result: \n\n\n" + result);
            return result.contains("Permit");

        } catch (IOException e) {

            // handle
            e.printStackTrace();
            theLogger.log(Level.SEVERE, "Error during the connection to the PDP");
            throw new Exception("Error during the connection to the PDP", e);        }


    }


}
