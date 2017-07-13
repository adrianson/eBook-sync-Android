package com.skytree.simpledemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	RelativeLayout contentView;
	Button debugButton0;
	Button debugButton2;
	Button debugButton3;
	
	final private String TAG = "EPub";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!this.makeBooksDirectory()) {
        	debug("faild to make books directory");
        }

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;

		contentView = new RelativeLayout(this);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
		contentView.setLayoutParams(rlp);	
		
        RelativeLayout.LayoutParams debugButton0Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
        debugButton0 = new Button(this);
        debugButton0.setText("Book");        
        debugButton0Param.leftMargin = 	(int)(10*density);
        debugButton0Param.topMargin = 	(int)(25*density);
        debugButton0Param.width = 		(int)(90*density);
        debugButton0Param.height = 		(int)(40*density);
        debugButton0.setLayoutParams(debugButton0Param);
        debugButton0.setId(8080);
        debugButton0.setOnClickListener(listener);
        debugButton0.setVisibility(1);        
        contentView.addView(debugButton0);
        
        RelativeLayout.LayoutParams debugButton2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
        debugButton2 = new Button(this);
        debugButton2.setText("Install");        
        debugButton2Param.leftMargin = 	(int)(250*density);
        debugButton2Param.topMargin = 	(int)(25*density);
        debugButton2Param.width = 		(int)(90*density);
        debugButton2Param.height = 		(int)(40*density);
        debugButton2.setLayoutParams(debugButton2Param);
        debugButton2.setId(8082);
        debugButton2.setOnClickListener(listener);
        contentView.addView(debugButton2);
        
        setContentView(contentView);

	}
	
	public void debug(String msg) {
		Log.d("EPub", msg);
	}
	
	public boolean makeBooksDirectory() {
		boolean res;		
		String filePath = new String(Environment.getExternalStorageDirectory().getAbsolutePath() + "/books");
		File file = new File(filePath);
		if (!file.exists()) {
			res = file.mkdirs();
		}else {
			res = false;		
		}
		return res;	
	}
	
	public boolean fileExists(String fileName) {
		boolean res;
		
		String pureName = this.removeExtention(fileName);
		String targetDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/books/"+pureName;

		//	String targetDirectory = getFilesDir().getAbsolutePath() + "/books/"+pureName;
		String targetPath = targetDirectory+"/"+ fileName;
		
		File file = new File(targetPath);
		debug(file.getAbsolutePath());
		
		if (file.exists()) res = true;
		else  res = false;
		return res;		
	}
	
	public boolean  deleteFile(String fileName) {
		boolean res;

		String pureName = this.removeExtention(fileName);
		String targetDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/books/"+pureName;
		String targetPath = targetDirectory+"/"+ fileName;
		
		File file = new File(targetPath);
		res = file.delete();
		return res;		
	}
	
	public String removeExtention(String filePath) {
	    // These first few lines the same as Justin's
	    File f = new File(filePath);

	    // if it's a directory, don't remove the extention
	    if (f.isDirectory()) return filePath;

	    String name = f.getName();

	    // Now we know it's a file - don't need to do any special hidden
	    // checking or contains() checking because of:
	    final int lastPeriodPos = name.lastIndexOf('.');
	    if (lastPeriodPos <= 0)
	    {
	        // No period after first character - return name as it was passed in
	        return filePath;
	    }
	    else
	    {
	        // Remove the last period and everything after it
	        File renamed = new File(f.getParent(), name.substring(0, lastPeriodPos));
	        return renamed.getPath();
	    }
	}

	
	public void copyToDevice(String fileName) {			      
		if (!this.fileExists(fileName)){
	          try
	          {
	        	  String pureName = this.removeExtention(fileName);
	        	  String targetDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/books/"+pureName;
	        	  File dir = new File(targetDirectory);
	        	  dir.mkdirs();
	        	  String targetPath = targetDirectory+"/"+ fileName;
	        	  
	        	  InputStream localInputStream = getAssets().open(fileName);	        	  
	        	  FileOutputStream localFileOutputStream = new FileOutputStream(targetPath);

	        	  byte[] arrayOfByte = new byte[1024];
	        	  int offset;
	        	  while ((offset = localInputStream.read(arrayOfByte))>0)
	        	  {
	        		  localFileOutputStream.write(arrayOfByte, 0, offset);	              
	        	  }
	        	  localFileOutputStream.close();
	        	  localInputStream.close();
	        	  Log.d(TAG, fileName+" copied to phone");	            
	          }
	          catch (IOException localIOException)
	          {
	              localIOException.printStackTrace();
	              Log.d(TAG, "failed to copy");
	              return;
	          }
	      }
	      else {
	          Log.d(TAG, fileName+" already exist");
	      }	         
	}
	
	private void installBook(String fileName) {
        if (this.fileExists(fileName)){
        	Log.d(TAG, fileName+ " already exist. try to delete old file.");
        	this.deleteFile(fileName);
        }
        this.copyToDevice(fileName);		
	}
	
	private void installSamples() {
		this.installBook("Alice.epub");
	}
	
	private void startBookViewActivity() {
		Intent intent = new Intent(MainActivity.this,BookViewActivity.class);
		startActivity(intent);		
	}

	
	private OnClickListener listener=new OnClickListener(){
		@Override
		public void onClick(View arg) {
	        if (arg.getId()==8080) {	        	
	        	startBookViewActivity();
	        }else if (arg.getId()==8082) {
	        	installSamples();	        	
	        }else if (arg.getId()==8083) {
	        	
	        }
			
		}
	};
	
}

