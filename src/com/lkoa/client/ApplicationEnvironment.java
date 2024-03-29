package com.lkoa.client;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.Preference;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.lkoa.activity.BaseActivity;

public class ApplicationEnvironment {
	
	public static final String LKOA4ANDROID 		= "LKOA4ANDROID";
	
	private static ApplicationEnvironment appEnv 	= null;
	private Application application 				= null;
	private SharedPreferences preferences 			= null;
	
	public static ApplicationEnvironment getInstance(){
		if (null == appEnv){
			appEnv = new ApplicationEnvironment();
			
		}
		
		return appEnv;
	}
	
	public Application getApplication(){
		if (null == this.application){
			this.application = BaseActivity.getTopActivity().getApplication();
		}
		
		return this.application;
	}
	
	public DisplayMetrics getPixels(){
		DisplayMetrics dm = new DisplayMetrics();  
		((WindowManager)getApplication().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		return dm;
	}
	
	public SharedPreferences getPreferences(){
		if (null == preferences)
			preferences = this.getApplication().getSharedPreferences(ApplicationEnvironment.LKOA4ANDROID, Context.MODE_PRIVATE);
		
		return preferences;
	}
	
	public void setRememberPassword(boolean remember) {
		saveToPreference(Constant.kREMEBERPWD, remember);
	}
	
	public void setAutoLogin(boolean autoLogin) {
		saveToPreference(Constant.kAUTOLOGIN, autoLogin);
	}
	
	public String getUserId() {
		return getFromPreference(Constant.kUSERID);
	}
	
	public String getPassword() {
		return getFromPreference(Constant.kPASSWORD);
	}
	
	public String getUserName() {
		return getFromPreference(Constant.KUSERNAM);
	}
	
	public String getLatestTime() {
		return getFromPreference("latestTime");
	}
	
	public void saveToPreference(String key, boolean value) {
		SharedPreferences pref = getPreferences();
		Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public void saveToPreference(String key, String value) {
		SharedPreferences pref = getPreferences();
		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public String getFromPreference(String key) {
		SharedPreferences pref = getPreferences();
		return pref.getString(key, null);
	}
	
	public boolean checkNetworkAvailable() {
		ConnectivityManager manager = (ConnectivityManager) this.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null)
			return false;
		
		NetworkInfo netinfo = manager.getActiveNetworkInfo();
		if (netinfo == null) {
			return false;
		}
		if (netinfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	public void restartApp(){
		Intent intent = getApplication().getBaseContext().getPackageManager()
				.getLaunchIntentForPackage(getApplication().getBaseContext().getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		getApplication().startActivity(intent);
	}

}
