package com.example.applist;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.switcher.R;

public class AppListAdaptor extends BaseAdapter {
   
   private LayoutInflater mInflater;

   private List<App> mApps;
   /** a map which maps the package name of an app to its icon drawable */
   private Map<String, Drawable> mIcons;
   private Drawable mStdImg;
   
   /**
    * Constructor.
    * 
    * @param context the application context which is needed for the layout inflater
    */
   public AppListAdaptor(Context context) {
      // cache the LayoutInflater to avoid asking for a new one each time
      mInflater = LayoutInflater.from(context);
      
      // set the default icon until the actual icon is loaded for an app
      mStdImg = context.getResources().getDrawable(R.drawable.icon);
   }
   
   @Override
   public int getCount() {
      return mApps.size();
   }
   
   @Override
   public Object getItem(int position) {
      return mApps.get(position);
   }
   
   @Override
   public long getItemId(int position) {
      return position;
   }
   
   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      
      AppViewHolder holder;
      if(convertView == null) {
         convertView = mInflater.inflate(R.layout.row, null);
         
         // creates a ViewHolder and stores a reference to the children view we want to bind data to
         holder = new AppViewHolder();
         holder.mTitle = (TextView) convertView.findViewById(R.id.apptitle);
         holder.mIcon = (ImageView) convertView.findViewById(R.id.appicon);
         convertView.setTag(holder);
      } else { 
         // reuse/overwrite the view passed assuming(!) that it is castable!
         holder = (AppViewHolder) convertView.getTag();
      }
      
      App app = mApps.get(position);
      
      holder.setTitle(app.getTitle());
      if (mIcons == null || mIcons.get(app.getPackageName()) == null) {
         holder.setIcon(mStdImg);
      } else {
         holder.setIcon(mIcons.get(app.getPackageName()));
      }
      
      return convertView; 
   }
   
   /**
    * Sets the list of apps to be displayed.
    * 
    * @param list the list of apps to be displayed
    */
   public void setListItems(List<App> list) { 
      mApps = list; 
   }
   
   /**
    * Sets the map containing the icons for each displayed app.
    * 
    * @param icons the map which maps the app's package name to its icon
    */
   public void setIcons(Map<String, Drawable> icons) {
      this.mIcons = icons;
   }
   
   /**
    * Returns the map containing the icons for each displayed app.
    * 
    * @return a map which contains a mapping of package names to icon drawable for all displayed apps
    */
   public Map<String, Drawable> getIcons() {
      return mIcons;
   }
   
   /**
    * A view holder which is used to re/use views inside a list.
    */
   public class AppViewHolder {
      
      private TextView mTitle;
      private ImageView mIcon;
      
      /**
       * Sets the text to be shown as the app's title
       * 
       * @param title the text to be shown inside the list row
       */
      public void setTitle(String title) {
         mTitle.setText(title);
      }
      
      /**
       * Sets the icon to be shown next to the app's title
       * 
       * @param img the icon drawable to be displayed
       */
      public void setIcon(Drawable img) {
         if (img != null) {
            mIcon.setImageDrawable(img);
         }
      }
   }
}

