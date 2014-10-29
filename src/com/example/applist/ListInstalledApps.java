package com.example.applist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.switcher.MainActivity;
import com.example.switcher.R;
public class ListInstalledApps extends Activity implements OnItemClickListener {
   final int imgsize=60;
   /* whether or not to include system apps */
   private static final boolean INCLUDE_SYSTEM_APPS = true;
   App app1=null,app2=null;
   private ListView mAppsList;
   private AppListAdaptor mAdapter;
   private List<App> mApps;
   Map<String, Drawable> icons = new HashMap<String, Drawable>();
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      
      mAppsList = (ListView) findViewById(R.id.appslist);
      mAppsList.setOnItemClickListener(this);
   
      mApps = loadInstalledApps(INCLUDE_SYSTEM_APPS);
      
      mAdapter = new AppListAdaptor(getApplicationContext());
      mAdapter.setListItems(mApps);
      mAppsList.setAdapter(mAdapter);
      
      new LoadIconsTask().execute(mApps.toArray(new App[]{}));
   }
   
   @Override
   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      final App app = (App) parent.getItemAtPosition(position);
      if(app1==null){
    	  app1=app;
      }else if(app2==null){
    	  app2=app;
      }else if(app1.equals(app)){
    	  app1=app2;
    	  app2=null;
      }else if(app2.equals(app)){
    	  app2=null;
      }else{
    	  app2=app;
      }
      if(app1!=null) System.out.print(app1.getTitle()+" ");
      else System.out.print("NONE ");
      if(app2!=null) System.out.println(app2.getTitle());
      else System.out.println("NONE");
      updateicons(app1,app2);
   }
   
   /**
    * Uses the package manager to query for all currently installed apps which are put into beans and returned
    * in form of a list.
    * 
    * @param includeSysApps whether or not to include system applications
    * @return a list containing an {@code App} bean for each installed application 
    */
   private List<App> loadInstalledApps(boolean includeSysApps) {
      List<App> apps = new ArrayList<App>();
      
      // the package manager contains the information about all installed apps
      PackageManager packageManager = getPackageManager();
      
      List<PackageInfo> packs = packageManager.getInstalledPackages(0); //PackageManager.GET_META_DATA 
      
      for(int i=0; i < packs.size(); i++) {
         PackageInfo p = packs.get(i);
         ApplicationInfo a = p.applicationInfo;
         // skip system apps if they shall not be included
         if ((!includeSysApps) && ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1)) {
            continue;
         }
         App app = new App();
         app.setTitle(p.applicationInfo.loadLabel(packageManager).toString());
         app.setPackageName(p.packageName);
         app.setVersionName(p.versionName);
         app.setVersionCode(p.versionCode);
         CharSequence description = p.applicationInfo.loadDescription(packageManager);
         app.setDescription(description != null ? description.toString() : "");
         app.icon=icons.get(p.packageName);
         apps.add(app);
      }
      return apps;
   }
   
   /**
    * An asynchronous task to load the icons of the installed applications.
    */
   private class LoadIconsTask extends AsyncTask<App, Void, Void> {
      @Override
      protected Void doInBackground(App... apps) {
         
         
         PackageManager manager = getApplicationContext().getPackageManager();
         
         for (App app : apps) {
            String pkgName = app.getPackageName();
            Drawable ico = null;
            try {
               Intent i = manager.getLaunchIntentForPackage(pkgName);
               if (i != null) {
                  ico = manager.getActivityIcon(i);
               }
               else ico=getResources().getDrawable(R.drawable.android);
            } catch (NameNotFoundException e) {
               Log.e("ERROR", "Unable to find icon for package '" + pkgName + "': " + e.getMessage());
            }
            icons.put(app.getPackageName(), ico);
         }
         mAdapter.setIcons(icons);
         
         return null;
      }
      
      @Override
      protected void onPostExecute(Void result) {
         mAdapter.notifyDataSetChanged();
      }
      
  }
   public void updateicons(App app1,App app2){
	    ImageView appicon1=(ImageView) findViewById(R.id.app1icon);
	   	ImageView appicon2=(ImageView) findViewById(R.id.app2icon);
	   	if(app1!=null)
	   		appicon1.setImageBitmap(Bitmap.createScaledBitmap(drawableToBitmap(icons.get(app1.getPackageName())), dpToPx(imgsize), dpToPx(imgsize), true));
	   	else
	   		appicon1.setImageBitmap(Bitmap.createScaledBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.android)), dpToPx(imgsize), dpToPx(imgsize), true));
	   	//appicon1.setImageResource(R.drawable.android);
	   	if(app2!=null)
	   		appicon2.setImageBitmap(Bitmap.createScaledBitmap(drawableToBitmap(icons.get(app2.getPackageName())), dpToPx(imgsize), dpToPx(imgsize), true));
	   	else
	   		appicon1.setImageBitmap(Bitmap.createScaledBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.android)), dpToPx(imgsize), dpToPx(imgsize), true));
   }
   public void send(View view){
   	Intent intent=new Intent(this,MainActivity.class);
   	intent.putExtra("APP1name", app1.getPackageName());
   	intent.putExtra("APP2name", app2.getPackageName());
   	/*ImageView appicon1=(ImageView) findViewById(R.id.app1icon);
   	ImageView appicon2=(ImageView) findViewById(R.id.app2icon);
   	appicon1.setImageDrawable(icons.get(app1.getPackageName()));
   	appicon2.setImageDrawable(icons.get(app2.getPackageName()));*/
   	startActivity(intent);
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
