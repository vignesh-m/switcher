	package com.example.applist;

import android.graphics.drawable.Drawable;
	public class App {
	   private String title;//title
	   private String packageName;
	   private String versionName;
	   private int versionCode;
	   private String description;
	   public Drawable icon;
	   // ordinary getters and setters
	   
	   public String getTitle() {
	      return title;
	   }

	   public void setTitle(String title) {
	      this.title = title;
	   }

	   public String getPackageName() {
	      return packageName;
	   }

	   public void setPackageName(String packageName) {
	      this.packageName = packageName;
	   }

	   public String getVersionName() {
	      return versionName;
	   }

	   public void setVersionName(String versionName) {
	      this.versionName = versionName;
	   }

	   public int getVersionCode() {
	      return versionCode;
	   }

	   public void setVersionCode(int versionCode) {
	      this.versionCode = versionCode;
	   }
	   
	   public String getDescription() {
	      return description;
	   }

	   public void setDescription(String description) {
	      this.description = description;
	   }

	}
