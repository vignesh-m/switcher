package com.example.switcher;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class ChatheadService extends Service{
	private WindowManager windowManager;
	  private ImageView chatHead;
	  final static int imgsize=65;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public void onCreate(){
		super.onCreate();

	    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

	    chatHead = new ImageView(this);
	    chatHead.setBackgroundColor(Color.parseColor("#00000000"));
	    chatHead.setImageResource(R.drawable.icon);
	    try {
	    	Drawable b=getPackageManager().getApplicationInfo(MainActivity.APP2packagename,0).loadIcon(getPackageManager());
			chatHead.setImageBitmap(Bitmap.createScaledBitmap(drawableToBitmap(b), dpToPx(imgsize), dpToPx(imgsize), true));	
			Intent intent=getPackageManager().getLaunchIntentForPackage(MainActivity.APP1packagename);
    		startActivity(intent);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Intent intent=getPackageManager().getLaunchIntentForPackage(MainActivity.APP1packagename);
		startActivity(intent);
		MainActivity.currApp=MainActivity.APP2;
	     final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
	        WindowManager.LayoutParams.WRAP_CONTENT,
	        WindowManager.LayoutParams.WRAP_CONTENT,
	        WindowManager.LayoutParams.TYPE_PHONE,
	        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
	        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
	        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
	        PixelFormat.TRANSLUCENT);

	    params.gravity = Gravity.TOP | Gravity.LEFT;
	    params.x = 0;
	    params.y = 100;

	    windowManager.addView(chatHead, params);
	    chatHead.setOnTouchListener(new View.OnTouchListener() {
	    	  private int initialX;
	    	  private int initialY;
	    	  private float initialTouchX;
	    	  private float initialTouchY;
	    	  private int f=0,cx,cy;

	    	  @Override public boolean onTouch(View v, MotionEvent event) {
	    		  if(event.getActionMasked()!=MotionEvent.ACTION_OUTSIDE)
	    		  {
	    			  switch (event.getAction()) {
		    	      case MotionEvent.ACTION_DOWN:
		    	        initialX = params.x;
		    	        initialY = params.y;
		    	        initialTouchX = event.getRawX();
		    	        initialTouchY = event.getRawY();
		    	        return true;
		    	      
		    	      case MotionEvent.ACTION_MOVE:
		    	    	cx=(int) (event.getRawX() - initialTouchX);
		    	    	cy=(int) (event.getRawY() - initialTouchY);
		    	        params.x = initialX + cx;
		    	        params.y = initialY + cy;
		    	        windowManager.updateViewLayout(chatHead, params);
		    	        if(Math.abs(cx)>3 && Math.abs(cy)>3)
		    	        	f=1;
		    	        return true;
		  
		    	    }
	    			  
	    		  }
	    		  if(event.getAction()==MotionEvent.ACTION_UP)
	    		  {
	    			  if(f==0) switchapps();
	    			  else f=0;
	    			  return false;
	    		  }
	    	    
	    		  
	    	    return false;
	    	  }
	    	});

	}
	public void switchapps(){
    	Intent intent;
 	    Drawable b;
 	    
    	if(MainActivity.currApp==MainActivity.APP1) {
    		intent=getPackageManager().getLaunchIntentForPackage(MainActivity.APP1packagename);
    		startActivity(intent);
    		try {
				b=getPackageManager().getApplicationInfo(MainActivity.APP2packagename,0).loadIcon(getPackageManager());
				chatHead.setImageBitmap(Bitmap.createScaledBitmap(drawableToBitmap(b), dpToPx(imgsize), dpToPx(imgsize), true));
				
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		MainActivity.currApp=MainActivity.APP2;
    	}else{
    		intent=getPackageManager().getLaunchIntentForPackage(MainActivity.APP2packagename);
    		startActivity(intent);
    		try {
    			b=getPackageManager().getApplicationInfo(MainActivity.APP1packagename,0).loadIcon(getPackageManager());
				chatHead.setImageBitmap(Bitmap.createScaledBitmap(drawableToBitmap(b), dpToPx(70), dpToPx(70), true));
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		MainActivity.currApp=MainActivity.APP1;
    	}
    	System.out.println(MainActivity.currApp);
	}
	@Override
	  public void onDestroy() {
	    super.onDestroy();
	    if (chatHead != null) windowManager.removeView(chatHead);
	  }
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
	public int dpToPx(int dp)
	 {
	     float density = getApplicationContext().getResources().getDisplayMetrics().density;
	     return Math.round((float)dp * density);
	 }
}
