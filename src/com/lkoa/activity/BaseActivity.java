package com.lkoa.activity;

import java.util.ArrayList;
import java.util.Stack;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.widget.Toast;

import com.lkoa.client.ApplicationEnvironment;
import com.lkoa.client.LKHttpRequestQueue;
import com.lkoa.view.LKAlertDialog;
import com.lkoa.view.LKProgressDialog;

public class BaseActivity extends Activity {
	
	private static Stack<BaseActivity> stack = new Stack<BaseActivity>();
	
	public static final int PROGRESS_DIALOG 	= 0; // 带滚动条的提示框 
	public static final int MODAL_DIALOG		= 1; // 带确定按纽的提示框，需要用户干预才能消失
	public static final int ALL_DIALOG			= 3; 
	public static final int DOUBLE_MODAL_DIALOG		= 3; // 带确定取消按钮
	
	protected ApplicationEnvironment mApp;
	
	// 要命的static
	private static LKProgressDialog progressDialog = null;
	protected LKAlertDialog mAlertDialog = null;
	protected LKAlertDialog mDoubleAlertDialog = null;
	
	private String mMessage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mApp = ApplicationEnvironment.getInstance();
		
		stack.push(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		//stack.push(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
//		StatService.onResume(this);
	}

	
	@Override
	protected void onPause() {
		super.onPause();
		
//		StatService.onPause(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*if (resultCode == Activity.RESULT_OK){
			this.setResult(Activity.RESULT_OK);
			this.finish();
		}*/
	}
	
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		// 然后会调用 startActivityForResult();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void finish() {
		if (!stack.empty()){
			stack.pop();
		}
		
		super.finish();
	}
	
	public static BaseActivity getTopActivity(){
		try{
			return stack.peek();
			
		} catch(Exception e){
			// 重启系统
			ApplicationEnvironment.getInstance().restartApp();
			
			return null;
		}
		
	}
	
	public static ArrayList<BaseActivity> getAllActiveActivity() {
		if (null == stack || stack.isEmpty()){
			return null;
		}
		
		ArrayList<BaseActivity> list = new ArrayList<BaseActivity>();
		for (int i=0; i<stack.size(); i++){
			list.add(stack.get(i));
		}
		
		return list;
	}
	
	public static void popActivity(){
		try{
			stack.pop();
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void showDialog(final int type, String message){
		this.mMessage = message;
		
		this.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				showDialog(type);
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id){
		case PROGRESS_DIALOG:
			this.showProgressDialog();
			break;
			
		case MODAL_DIALOG:
			this.showAlertDialog();
			break;
			
		case DOUBLE_MODAL_DIALOG:
			showDoubleAlertDialog();
			break;
		}
		
		return super.onCreateDialog(id);
	}
	
	private void showProgressDialog(){
		try{
			// 这里应该关闭其它提示型的对话框
			this.hideDialog(ALL_DIALOG);
			
			this.createProgressDialog();
			
			if(!TextUtils.isEmpty(mMessage)) {
				//提示信息不为空时才显示进度dialog
				progressDialog.setMessage(null==mMessage?"":mMessage);
				progressDialog.create().show();
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void showAlertDialog(){
		try{
			// 这里应该关闭其它提示型的对话框
			this.hideDialog(ALL_DIALOG);
			
			this.createAlertDialog();
			mAlertDialog.setMessage(null == mMessage ? "" : mMessage);
			mAlertDialog.create().show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void showDoubleAlertDialog(){
		try{
			// 这里应该关闭其它提示型的对话框
			this.hideDialog(ALL_DIALOG);
			
			this.createDoubleAlertDialog();
			mDoubleAlertDialog.setMessage(null == mMessage ? "" : mMessage);
			mDoubleAlertDialog.create().show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void hideDialog(int type){
		switch(type){
		case PROGRESS_DIALOG:
			if (null != progressDialog && progressDialog.isShowing()){
				progressDialog.dismiss();
			}
			break;
			
		case MODAL_DIALOG:
			if (null != mAlertDialog && mAlertDialog.isShowing()){
				mAlertDialog.dismiss();
			}
			break;
			
		case DOUBLE_MODAL_DIALOG:
			if (null != mDoubleAlertDialog && mDoubleAlertDialog.isShowing()){
				mDoubleAlertDialog.dismiss();
			}
			break;
			
		default:
			if (null != progressDialog && progressDialog.isShowing()){
				progressDialog.dismiss();
			}
			if (null != mAlertDialog && mAlertDialog.isShowing()){
				mAlertDialog.dismiss();
			}
			if (null != mDoubleAlertDialog && mDoubleAlertDialog.isShowing()){
				mDoubleAlertDialog.dismiss();
			}
			break;
		}
		
	}
	
	private void createProgressDialog(){
//		if (this.getParent() instanceof MainActivityGroup){
//			progressDialog = new LKProgressDialog(this.getParent());
//		} else {
//			progressDialog = new LKProgressDialog(this);
//		}
		progressDialog = new LKProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setTitle("请稍候");
		
		progressDialog.setNegativeButton("取消",
		new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				if (!LKHttpRequestQueue.queueList.isEmpty()){
					LKHttpRequestQueue.queueList.get(LKHttpRequestQueue.queueList.size()-1).cancel();
				}
				
			}
		});
	}
	
	private void createAlertDialog(){
		mAlertDialog = new LKAlertDialog(this);
		
		mAlertDialog.setTitle("提示");
		mAlertDialog.setCancelable(false);
		mAlertDialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}
	
	private void createDoubleAlertDialog(){
		mDoubleAlertDialog = new LKAlertDialog(this);
		
		mDoubleAlertDialog.setTitle("提示");
		mDoubleAlertDialog.setCancelable(false);
		mDoubleAlertDialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				onClickConfirm();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}
	
	public void showToast(String message){
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	protected void onClickConfirm() {
		//do nothing
	}
	
}
