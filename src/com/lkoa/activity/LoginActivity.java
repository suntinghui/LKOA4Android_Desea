package com.lkoa.activity;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.client.ApplicationEnvironment;
import com.lkoa.client.Constants;
import com.lkoa.view.TextWithIconView;

public class LoginActivity extends BaseActivity {
	private LinearLayout ipLayout;

	private LinearLayout setIpLayout;
	private Button setIpButton;
	private Button setButton;
	private Button cancelButton;
	private EditText ipET;

	private TextView remberTV;

	private ImageView remeberIV;
	private ImageView autoLoginIV;

	private Boolean isRemeberPwd;
	private Boolean isAutoLogin;
	
	private TextWithIconView tv_username;
	private TextWithIconView tv_pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		tv_username = (TextWithIconView)this.findViewById(R.id.tv_username);
		tv_username.setHintString("用户名");
		tv_pwd = (TextWithIconView)this.findViewById(R.id.tv_pwd);
		tv_pwd.setHintString("密码");
		tv_pwd.setIcon(R.drawable.icon_pwd);
		
		ipET = (EditText) this.findViewById(R.id.ipET);
		ipET.setText(ApplicationEnvironment.getInstance().getPreferences()
				.getString(Constants.kHOSTNAME, Constants.DEFAULTHOST));

		ipLayout = (LinearLayout) this.findViewById(R.id.toplayout);

		setIpLayout = (LinearLayout) this.findViewById(R.id.setIpLayout);
		setIpLayout.setOnClickListener(listener);

		setIpButton = (Button) this.findViewById(R.id.setIpButton);
		setIpButton.setOnClickListener(listener);

		// ip设置提交按钮
		setButton = (Button) this.findViewById(R.id.setButton);
		setButton.setOnClickListener(listener);

		// ip设置取消按钮
		cancelButton = (Button) this.findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(listener);

		// 记住密码按钮
		remeberIV = (ImageView) this.findViewById(R.id.selectIV_left);
		remeberIV.setOnClickListener(listener);
		isRemeberPwd = ApplicationEnvironment.getInstance().getPreferences()
				.getBoolean(Constants.kREMEBERPWD, false);
		if (isRemeberPwd) {
			remeberIV.setBackgroundResource(R.drawable.select_button_s);
		} else {
			remeberIV.setBackgroundResource(R.drawable.select_button_n);
		}

		// remberTV
		remberTV = (TextView) this.findViewById(R.id.remeber_pwdTV);

		// 自动登录按钮
		autoLoginIV = (ImageView) this.findViewById(R.id.selectIV_right);
		autoLoginIV.setOnClickListener(listener);
		isAutoLogin = ApplicationEnvironment.getInstance().getPreferences()
				.getBoolean(Constants.kAUTOLOGIN, false);
		if (isAutoLogin) {
			autoLoginIV.setBackgroundResource(R.drawable.select_button_s);
			this.disableRemberPwdButton();
		} else {
			autoLoginIV.setBackgroundResource(R.drawable.select_button_n);
		}
	}

private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.setIpLayout:
			case R.id.setIpButton:
				Animation animation = (Animation)AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push_up_in);
				LayoutAnimationController lac = new LayoutAnimationController(animation);
				lac.setOrder(LayoutAnimationController.ORDER_RANDOM);
				lac.setDelay(0.3f);
				ipLayout.setLayoutAnimation(lac);
				if(ipLayout.getVisibility() == View.GONE){
					ipLayout.setVisibility(View.VISIBLE);
				}else{
					ipLayout.setVisibility(View.GONE);
				}
				break;
				
			case R.id.setButton:
				ipLayout.setVisibility(View.GONE);
				
				if(ipET.length() == 0){
//					LoginActivity.this.showToast("服务器地址不能为空！");
				}else{
					Editor editor = ApplicationEnvironment.getInstance().getPreferences().edit();
					editor.putString(Constants.kHOSTNAME, ipET.getText().toString());
					editor.commit();
					
//					LoginActivity.this.showToast("服务器地址已更改并生效!");
				}
				break;
				
			case R.id.cancelButton:
				ipLayout.setVisibility(View.GONE);
				break;
				
			case R.id.loginbutton:
				doLogin();
				break;
				
			case R.id.selectIV_left:
				isRemeberPwd = !isRemeberPwd;
				if(isRemeberPwd){
					remeberIV.setBackgroundResource(R.drawable.select_button_s);
				}else{
					remeberIV.setBackgroundResource(R.drawable.select_button_n);
				}
				break;
				
			case R.id.selectIV_right:
				isAutoLogin = !isAutoLogin;
				if(isAutoLogin){
					autoLoginIV.setBackgroundResource(R.drawable.select_button_s);
					LoginActivity.this.disableRemberPwdButton();
				}else{
					autoLoginIV.setBackgroundResource(R.drawable.select_button_n);
					remeberIV.setEnabled(true);
					remberTV.setTextColor(getResources().getColor(R.color.black));
				}
				break;
			}
		}
	};
	
	private void disableRemberPwdButton(){
		remberTV.setTextColor(getResources().getColor(R.color.gray));
		remeberIV.setBackgroundResource(R.drawable.select_button_s);
		remeberIV.setEnabled(false);
		isRemeberPwd = true;
	}
	
	private Boolean checkValue(){
//		if(userNameET.length() == 0){
//			this.showToast("用户名不能为空！");
//			return false;
//		}else if(pwdET.length() == 0){
//			this.showToast("密码不能为空！");
//			return false;
//		}
		return true;
	}
	
	private void doLogin(){
		if(this.checkValue()){
//			Editor editor = ApplicationEnvironment.getInstance().getPreferences().edit();
//			editor.putString(Constants.kUSERNAME, userNameET.getText().toString());
//			editor.putBoolean(Constants.kAUTOLOGIN, isAutoLogin);
//			editor.putBoolean(Constants.kREMEBERPWD, isRemeberPwd);
//			if(isRemeberPwd){
//				editor.putString(Constants.kPASSWORD, pwdET.getText().toString());
//			}else{
//				editor.putString(Constants.kPASSWORD, "");
//			}
//			editor.commit();
//			
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put(Constants.kWEBSERVICENAME, "LoginService.asmx");
//			map.put(Constants.kMETHODNAME, TransferRequestTag.GETLOGININFOSERVICE);
//			
//			HashMap<String, Object> tempMap = new HashMap<String, Object>();
//			tempMap.put("Function", "LogIn");
//			tempMap.put("UserName", userNameET.getText().toString());
//			tempMap.put("PassWord", pwdET.getText().toString());
//			tempMap.put("LoginType", "WAP");
//			tempMap.put("Function", "LogIn");
//			
//			HashMap<String, Object> qryMap = new HashMap<String, Object>();
//			qryMap.put("Qry", tempMap);
//			
//			HashMap<String, Object> paramMap = new HashMap<String, Object>();
//			paramMap.put("sLoginXML", qryMap);
//			paramMap.put("keyid", PhoneUtil.getIMEI());
//			map.put(Constants.kPARAMNAME, paramMap);
//			
//			LKHttpRequest req1 = new LKHttpRequest(map, getLoginHandler());
//			
//			new LKHttpRequestQueue().addHttpRequest(req1)
//			.executeQueue("正在登录请稍候...", new LKHttpRequestQueueDone(){
//
//				@Override
//				public void onComplete() {
//					super.onComplete();
//					
//				}
//				
//			});	
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}
