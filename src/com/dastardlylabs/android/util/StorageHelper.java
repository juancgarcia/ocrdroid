package com.dastardlylabs.android.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

//import org.appcelerator.kroll.common.Log;
//import org.appcelerator.titanium.TiApplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;

public class StorageHelper {
	
	private static String LCAT = "StorageHelper";	
	private Context ctx;
	
	public StorageHelper(Context context){
		ctx = context;
	}
	
	/*
	 * Instance Methods (Depend on Application Context)
	 */
	public File copyAssetToTmp(String assetName) { 
	    File tmpFile;
	    InputStream in = null;
	    OutputStream out = null;
	    
	    try {
	    	in = ctx.getAssets().open(assetName);
			tmpFile = File.createTempFile("asset_", ".tmp", null);
			
			android.util.Log.d(LCAT, "tmpFile.getAbsolutePath(): " + tmpFile.getAbsolutePath());
			android.util.Log.d(LCAT, "tmpFile.getCanonicalPath(): " + tmpFile.getCanonicalPath());
			
			out = new FileOutputStream(tmpFile);
	        StorageHelper.copyFile(in, out);
	        in.close();
	        out.flush(); out.close();
	        return tmpFile;	    	
	    } catch(IOException e) {
	    	android.util.Log.e(LCAT, "copyAsset() error: " + e.getMessage());	    	
	    }
	    
	    return null;
	}
	
	public void copyAssets() { 
	    AssetManager assetManager = ctx.getAssets(); 
	    String[] files = null; 
	    try { 
	        files = assetManager.list(""); 
	    } catch (IOException e) { 
	        android.util.Log.e("tag", e.getMessage()); 
	    } 
	    for(String filename : files) { 
	        InputStream in = null; 
	        OutputStream out = null; 
	        try { 
	          in = assetManager.open(filename); 
	          out = new FileOutputStream("/sdcard/" + filename); 
	          copyFile(in, out); 
	          in.close(); 
	          in = null; 
	          out.flush(); 
	          out.close(); 
	          out = null; 
	        } catch(Exception e) { 
	            android.util.Log.e(LCAT, e.getMessage()); 
	        }        
	    }
	}
	
	public File getStorageDirectory() throws SecurityException{
		File outputDir;
		Map<String, Boolean> state = getExternalStorageState();
		boolean writeable = state.get("writeable"),
				available = state.get("available");
		//String outputDirPath; 
		
		android.util.Log.d(LCAT, "External Storage Available: " + (available? "true": "false"));
		android.util.Log.d(LCAT, "External Storage Writeable: " + (writeable? "true": "false"));
		
		 if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
			 if( writeable ){
					outputDir = ctx.getExternalFilesDir(null);
				} else {
					outputDir = Environment.getDataDirectory();
				}
		 } else {
			 // This is older than Android 1.6
			if( writeable ){
				outputDir = Environment.getExternalStorageDirectory();
				outputDir = new File(
								outputDir.getAbsolutePath() + "/Android/data/" +
								ctx.getPackageName() + "/files/"
							);
			} else {
				outputDir = Environment.getDataDirectory();
			}
	 	}
		android.util.Log.d(LCAT, "Output Dir (absolute path): " + outputDir.getAbsolutePath());
		
		return outputDir.getAbsoluteFile();
	}	
	
	/*
	 * Static methods
	 */
	public static String stripFileUri(String uriPath){
		String uriPrefix = "file://";
		int plen = uriPrefix.length();
		if(uriPath.startsWith(uriPrefix))
			return uriPath.substring(plen);
		else
			return uriPath;
	}

	public static void copyFile(InputStream in, OutputStream out) throws IOException { 
	    byte[] buffer = new byte[1024]; 
	    int read; 
	    while((read = in.read(buffer)) != -1){ 
	      out.write(buffer, 0, read); 
	    } 
	}

	public static Map<String, Boolean> getExternalStorageState(){
		Map<String, Boolean> state = new HashMap<String, Boolean>();
		state.put("available", false);
		state.put("writeable", false);
		
		String externalStorageState = Environment.getExternalStorageState();

		if ( externalStorageState.equals(Environment.MEDIA_MOUNTED) ) {
		    // We can read and write the media
			state.put("available", true);
			state.put("writeable", true);
		} else if ( externalStorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY) ) {
		    // We can only read the media
			state.put("available", true);
			state.put("writeable", false);
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
			state.put("available", false);
			state.put("writeable", false);
		}
		
		return state;
	}

	public static String listDirectory(String _path)
	{
		String fileList = new String();
		
		File dir = new File(_path);
		File[] files = dir.listFiles();
		
		for (File file : files){
			fileList += file.getPath() + "; ";
		}
		
		return fileList;
	}
}
