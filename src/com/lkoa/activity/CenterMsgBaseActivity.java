package com.lkoa.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;

public class CenterMsgBaseActivity extends BaseActivity{
	protected ImageView mIvBack;
	protected TextView mTvTitle;
	
	protected LayoutInflater mLayoutInflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayoutInflater = LayoutInflater.from(this);
	}
	
	private OnClickListener mBackOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	protected void findViews() {
		mIvBack = (ImageView) findViewById(R.id.iv_back);
		mTvTitle = (TextView) findViewById(R.id.tv_title);
	}
	
	protected void setupViews() {
		mIvBack.setOnClickListener(mBackOnClickListener);
	}
}
