/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.umu.capability.token;

import java.util.ArrayList;

import org.umu.capability.rights.SimpleAccessRight;

public class CapabilityTokenDelegated extends CapabilityToken {

	/*
	 * This extended version of the Capability Token adds a new value, "del"
	 * to express that this token has been delegated from the original owner
	 * of the token to another entity for their use.
	 * 
	 * This has several implications, 
	 * 	- The owner has to expedite the delegated token.
	 *  - 
	 *  
	 */
	
	public CapabilityTokenDelegated(String del, String id, long ii, String is, String su, String de, String si,
			ArrayList<SimpleAccessRight> ar, long nb, long na) {
		
		super(id, ii, is, su, de, si, ar, nb, na);

		this.del = del;
	}

	private String del;

    public String getDel() {
        return this.del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    @Override
    public String getSummary() {
        return super.getSummary() + ":" + this.getDel();
    }
}

