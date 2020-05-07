/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.odins.util.filemanagement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Write2File {

	
	public static void writeByteArray2File(String outputfile, byte[] b) throws IOException {
		OutputStream os = new FileOutputStream(outputfile);
		os.write(b);
		os.close();
	}
}
