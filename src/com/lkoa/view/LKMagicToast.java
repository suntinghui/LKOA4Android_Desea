package com.lkoa.view;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.lkoa.R;
import com.lkoa.client.ApplicationEnvironment;

/**
 * 
 * @author sth
 * 
 * http://xiaozilong88.iteye.com/blog/1518472
 *
 */

public class LKMagicToast {
	
	private static Toast toast = null;
	private static Method showMethod = null;
	private static Method hideMethod = null;
	private static Object obj = null;
	
	private static void createToast(){
		toast = new Toast(ApplicationEnvironment.getInstance().getApplication());
		
		LayoutInflater inflate = (LayoutInflater)ApplicationEnvironment.getInstance().getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	    View v = inflate.inflate(R.layout.layout_progress_dialog, null);
	    toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(v);
		
		try {
			Field[] filesList = toast.getClass().getDeclaredFields();
			for (int i=0; i<filesList.length; i++){
				Log.e("field ", filesList[i].getName());
			}
			
			Method[] methodList = toast.getClass().getDeclaredMethods();
			for (int i=0; i<methodList.length; i++){
				Log.e("method ", methodList[i].getName());
			}
			
	    	Field field = toast.getClass().getDeclaredField("mTN");
	    	field.setAccessible(true);
	    	obj = field.get(toast);
	    	showMethod = obj.getClass().getDeclaredMethod("show", null);
	    	hideMethod = obj.getClass().getDeclaredMethod("cancel", null);
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public static void showToast(String message){
		createToast();
		
		//调用TN对象的show()方法，显示toast
		try {
			showMethod.invoke(obj, null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void hideToast(){
		//调用TN对象的hide()方法，关闭toast
		
		try {
			hideMethod.invoke(obj, null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
	}

}
