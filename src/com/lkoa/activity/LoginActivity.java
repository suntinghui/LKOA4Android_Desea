package com.lkoa.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lkoa.R;

public class LoginActivity extends Activity implements OnClickListener {

	private Button mBtnLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mBtnLogin = (Button)findViewById(R.id.loginbutton);
		mBtnLogin.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginbutton:
			startActivity(new Intent(this, MainActivity.class));
			break;

		default:
			break;
		}
	}

}
