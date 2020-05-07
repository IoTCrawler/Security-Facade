/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.umu.capability.token;

import java.util.ArrayList;

import org.umu.capability.rights.SimpleAccessRight;

public class CapabilityTokenExtended  extends CapabilityToken{

	private String rd;
	
	public CapabilityTokenExtended( String id, long ii, String is, String su, String de, String si, ArrayList<SimpleAccessRight> ar, long nb, long na, String rd) {
		super(id, ii, is, su, de, si, ar, nb, na);
		this.rd = rd;
	}
	
	public String getRd() {
		return rd;
	}
	
	public void setRd(String rd) {
		this.rd = rd;
	}

	public String toString(){
		return super.toString().substring(-1).concat(this.getRd() + "}");
	}
}
