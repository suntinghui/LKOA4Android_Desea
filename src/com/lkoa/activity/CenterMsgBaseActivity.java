package com.lkoa.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkoa.R;

public class CenterMsgBaseActivity extends BaseActivity{
	protected ImageView mIvBack;
	protected TextView mTvTitle;
	protected ImageView mIvRight;
	
	protected LinearLayout mLinearRight;
	protected TextView mTvRight1, mTvRight2;
	
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
		mIvRight = (ImageView) findViewById(R.id.iv_right);
		mLinearRight = (LinearLayout) findViewById(R.id.linear_right);
		mTvRight1 = (TextView) findViewById(R.id.tv_right_1);
		mTvRight2 = (TextView) findViewById(R.id.tv_right_2);
	}
	
	protected void setupViews() {
		mIvBack.setOnClickListener(mBackOnClickListener);
		mIvRight.setVisibility(View.GONE);
		mLinearRight.setVisibility(View.GONE);
	}
}
