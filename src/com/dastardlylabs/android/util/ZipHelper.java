package com.dastardlylabs.android.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;

public class ZipHelper {

	private static final String LCAT = "AndroZip";

	public static void extract(String sourcefile, String dirout) throws IOException, FileNotFoundException {
		String uriPrefix = "file://";
		int plen = uriPrefix.length();
		if(sourcefile.startsWith(uriPrefix))
			sourcefile = sourcefile.substring(plen);
		if(dirout.startsWith(uriPrefix))
			dirout = dirout.substring(plen);
		if(!new File(sourcefile).exists()){
	    	throw new FileNotFoundException(LCAT + " sourcefile doesn't exist: " + sourcefile);
	    }
	    else {
	    	ZipInputStream in = new ZipInputStream(new FileInputStream(sourcefile));
	    	ZipEntry ze = null;
	    	byte[] buf = new byte[8096];
	      
	    	while((ze = in.getNextEntry()) != null) {
	    		String name = ze.getName();
		    	if(name.length() > 0) {
					if (ze.isDirectory()) {
						File d = new File(dirout, name);
						d.mkdirs();
						d = null;
					} else {
						FileOutputStream fos = null;
						try {
							fos = new FileOutputStream(new File(dirout,name));
							int read = 0;
							while((read = in.read(buf)) != -1) {
								fos.write(buf, 0, read);
							}
						} finally {
							if (fos != null) {
								try {
									fos.close();
								} catch (Throwable t) {
									//Ignore
								}
							}
						}
					}
		    	}
		    	in.closeEntry();
	      }	      
	      in.close();
	    }
	}

	public static List<String> list(String sourcefile) throws IOException, FileNotFoundException {
		
		List<String> zipList = new ArrayList<String>();
		
		int plen = "file://".length();
		sourcefile = sourcefile.substring(plen);

		if(!new File(sourcefile).exists()){
	    	throw new FileNotFoundException(LCAT + " sourcefile doesn't exist: " + sourcefile);
	    } else {
	    	ZipInputStream in = new ZipInputStream(new FileInputStream(sourcefile));
	    	ZipEntry ze = null;
	      
	    	while((ze = in.getNextEntry()) != null) {
	    		String name = ze.getName();
		    	if(name.length() > 0)
					zipList.add(name);
		    	in.closeEntry();
			}	      
			in.close();
	    }
	    return zipList;
	}
}