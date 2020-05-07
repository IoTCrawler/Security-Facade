/*

Copyright Odin Solutions S.L. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0

*/

package org.odins.util.filemanagement;

import java.io.File;

public class FileExists {

	
	public static boolean path(String path) { return new File(path).exists(); }
}
