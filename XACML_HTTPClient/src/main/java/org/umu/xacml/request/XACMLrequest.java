/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.umu.xacml.request;

public class XACMLrequest {

    //urn:oasis:names:tc:xacml:2.0:subject:role
    //urn:oasis:names:tc:xacml:1.0:subject:subject-id

    public static String getXACMLRequest(String subjectId, String xacml_resource, String action) {
        return "<Request xmlns=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\">\n" +
                "   <Subject SubjectCategory=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">\n" +
                "       <Attribute AttributeId=\"urn:oasis:names:tc:xacml:2.0:subject:role\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">\n" +
                "           <AttributeValue>"+subjectId+"</AttributeValue>\n" +
                "       </Attribute>  \n" +
                "   </Subject>\n" +
                "   \n" +
                "   <Resource>\n" +
                "       <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">\n" +
                "           <AttributeValue>"+xacml_resource+"</AttributeValue>\n" +
                "       </Attribute>\n" +
                "   </Resource> \n" +
                "\n" +
                "   <Action>\n" +
                "       <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">\n" +
                "           <AttributeValue>"+action+"</AttributeValue>\n" +
                "       </Attribute>  \n" +
                "   </Action>\n" +
                "\n" +
                "   <Environment/>\n" +
                "</Request>";
    }

}
