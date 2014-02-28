package com.lkoa.activity;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;

public class CenterMsgBaseActivity extends Activity{
	protected ImageView mIvBack;
	protected TextView mTvTitle;
	
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
