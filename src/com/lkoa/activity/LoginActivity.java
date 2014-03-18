package com.lkoa.activity;

import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
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
import com.lkoa.client.Constant;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.client.LKHttpRequest;
import com.lkoa.client.LKHttpRequestQueue;
import com.lkoa.client.LKHttpRequestQueueDone;
import com.lkoa.client.TransferRequestTag;
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

	private boolean mIsRemeberPwd;
	private boolean mIsAutoLogin;

	private TextWithIconView tv_username;
	private TextWithIconView tv_pwd;
	
	private ApplicationEnvironment mApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mApp = ApplicationEnvironment.getInstance();

		tv_username = (TextWithIconView) this.findViewById(R.id.tv_username);
		tv_username.setHintString("用户名");
		tv_pwd = (TextWithIconView) this.findViewById(R.id.tv_pwd);
		tv_pwd.setHintString("密码");
		// tv_username.setText("test");
		// tv_pwd.setText("68528888qg");
		tv_pwd.setIcon(R.drawable.icon_pwd);
		tv_pwd.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		ipET = (EditText) this.findViewById(R.id.ipET);
		ipET.setText(mApp.getPreferences()
				.getString(Constant.kHOSTNAME, Constant.DEFAULTHOST));

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

		Button btn_login = (Button) this.findViewById(R.id.loginbutton);
		btn_login.setOnClickListener(listener);

		// 记住密码按钮
		remeberIV = (ImageView) this.findViewById(R.id.selectIV_left);
		remeberIV.setOnClickListener(listener);
		SharedPreferences pref = mApp.getPreferences();
		mIsRemeberPwd = pref.getBoolean(Constant.kREMEBERPWD, false);
		if (mIsRemeberPwd) {
			remeberIV.setBackgroundResource(R.drawable.select_button_s);
			fillUserAndPassword();
		} else {
			remeberIV.setBackgroundResource(R.drawable.select_button_n);
		}
		
		// remberTV
		remberTV = (TextView) this.findViewById(R.id.remeber_pwdTV);

		// 自动登录按钮
		autoLoginIV = (ImageView) this.findViewById(R.id.selectIV_right);
		autoLoginIV.setOnClickListener(listener);
		mIsAutoLogin = pref.getBoolean(Constant.kAUTOLOGIN, false);
		if (mIsAutoLogin) {
			fillUserAndPassword();
			autoLoginIV.setBackgroundResource(R.drawable.select_button_s);
			this.disableRemberPwdButton();
			doLogin();
		} else {
			autoLoginIV.setBackgroundResource(R.drawable.select_button_n);
		}
	}
	
	private void fillUserAndPassword() {
		String userName = mApp.getUserName();
		String password = mApp.getPassword();
		if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
			tv_username.setText(userName);
			tv_pwd.setText(password);
		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.setIpLayout:
			case R.id.setIpButton:
				Animation animation = (Animation) AnimationUtils.loadAnimation(
						LoginActivity.this, R.anim.push_up_in);
				LayoutAnimationController lac = new LayoutAnimationController(
						animation);
				lac.setOrder(LayoutAnimationController.ORDER_RANDOM);
				lac.setDelay(0.3f);
				ipLayout.setLayoutAnimation(lac);
				if (ipLayout.getVisibility() == View.GONE) {
					ipLayout.setVisibility(View.VISIBLE);
				} else {
					ipLayout.setVisibility(View.GONE);
				}
				break;

			case R.id.setButton:
				ipLayout.setVisibility(View.GONE);

				if (ipET.length() == 0) {
					// LoginActivity.this.showToast("服务器地址不能为空！");
				} else {
					Editor editor = mApp.getPreferences().edit();
					editor.putString(Constant.kHOSTNAME, ipET.getText()
							.toString());
					editor.commit();

					// LoginActivity.this.showToast("服务器地址已更改并生效!");
				}
				break;

			case R.id.cancelButton:
				ipLayout.setVisibility(View.GONE);
				break;

			case R.id.loginbutton:
				doLogin();
				break;

			case R.id.selectIV_left:
				//记住密码
				mIsRemeberPwd = !mIsRemeberPwd;
				if (mIsRemeberPwd) {
					remeberIV.setBackgroundResource(R.drawable.select_button_s);
				} else {
					remeberIV.setBackgroundResource(R.drawable.select_button_n);
				}
				mApp.setRememberPassword(mIsRemeberPwd);
				break;

			case R.id.selectIV_right:
				//自动登陆
				mIsAutoLogin = !mIsAutoLogin;
				if (mIsAutoLogin) {
					autoLoginIV.setBackgroundResource(R.drawable.select_button_s);
					LoginActivity.this.disableRemberPwdButton();
				} else {
					autoLoginIV
							.setBackgroundResource(R.drawable.select_button_n);
					remeberIV.setEnabled(true);
					remberTV.setTextColor(getResources()
							.getColor(R.color.black));
				}
				mApp.setAutoLogin(mIsAutoLogin);
				break;
			}
		}
	};

	private void disableRemberPwdButton() {
		remberTV.setTextColor(getResources().getColor(R.color.gray));
		remeberIV.setBackgroundResource(R.drawable.select_button_s);
		remeberIV.setEnabled(false);
		mIsRemeberPwd = true;
	}

	private Boolean checkValue() {
		 if(tv_username.getText().length() == 0){
		 this.showToast("用户名不能为空！");
		 return false;
		 }else if(tv_pwd.getText().length() == 0){
		 this.showToast("密码不能为空！");
		 return false;
		 }
		return true;
	}

	private void doLogin() {

		if (this.checkValue()) {

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
			map.put(Constant.kMETHODNAME, TransferRequestTag.LOGIN);

			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("LoginName", tv_username.getText().toString());
			paramMap.put("Pwd", tv_pwd.getText().toString());
			map.put(Constant.kPARAMNAME, paramMap);

			LKHttpRequest req1 = new LKHttpRequest(map, loginHandler());

			new LKHttpRequestQueue().addHttpRequest(req1).executeQueue(
					"正在登录...", new LKHttpRequestQueueDone() {

						@Override
						public void onComplete() {
							super.onComplete();

						}

					});
		}

	}

	private LKAsyncHttpResponseHandler loginHandler() {
		return new LKAsyncHttpResponseHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				String str = (String) obj;
				String[] results = str.split("[;]");
				String status = results[0];
				if (status.equals("0")) {// 登录成功
					ApplicationEnvironment app = ApplicationEnvironment
							.getInstance();
					app.saveToPreference(Constant.kUSERID, results[1]);
					app.saveToPreference(Constant.KUSERNAM, results[2]);
					app.saveToPreference(Constant.kPASSWORD, tv_pwd.getText().toString());

					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					LoginActivity.this.startActivity(intent);
				} else if (status.equals("1")) {// 密码错误
					LoginActivity.this.showToast("密码错误！");
				} else if (status.equals("2")) {// 此帐号已删除
					LoginActivity.this.showToast("此帐号已删除！");
				} else if (status.equals("3")) {// 此帐号不存在
					LoginActivity.this.showToast("此帐号不存在！");
				}

			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
}
