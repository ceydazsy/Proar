package com.paar.ch2;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends Activity {
	SurfaceView cameraPreView;
	SurfaceHolder previewHolder;
	Camera camera;
	boolean inPreview;

	//git -- deneme degisiklik
	//git -- deneme degisiklik 2
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		inPreview = false;
		
		cameraPreView = (SurfaceView) findViewById(R.id.cameraPreview);
		previewHolder = cameraPreView.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		camera = Camera.open();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		if(inPreview){
			camera.stopPreview();
		}
		camera.release();
		camera=null;
		inPreview=false;
		
		super.onPause();
	}
	private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters){
		Camera.Size result = null;
		for(Camera.Size size : parameters.getSupportedPreviewSizes()){
			if(size.width <= width && size.height <= height){
				if(result==null){
					result=size;
				}else{
					int resultArea = result.width*result.height;
					int newArea = size.width*size.height;
					
					if(newArea > resultArea){
						result=size;
					}
				}
			}
		}
		return(result);
	}
	
	
		SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				try{
					camera.setPreviewDisplay(previewHolder);
				}catch(Throwable t){
					Log.e("ProAndroidAR2Activity", "Exception in setPreviewDisplay()",t);
				}
				
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				Camera.Parameters parameters = camera.getParameters();
				Camera.Size size = getBestPreviewSize(width, height, parameters);
				
				if(size!=null){
					parameters.setPictureSize(size.width,size.height);
					camera.setParameters(parameters);
					camera.startPreview();
					inPreview=true;
				
				}
			}
			
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		};
@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
