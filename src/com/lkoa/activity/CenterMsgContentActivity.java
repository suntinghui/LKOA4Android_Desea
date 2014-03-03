package com.lkoa.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.adapter.CenterMsgNewsAdapter;
import com.lkoa.business.CenterMsgManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.CenterMsgNewsItem;
import com.lkoa.util.LogUtil;

/**
 * 信息中心-新闻内容
 */
public class CenterMsgContentActivity extends CenterMsgBaseActivity {
	
	private static final String TAG = "CenterMsgContentActivity";
	
	private CenterMsgManager mNewsMgr;
	private String mInfoId;
	private int mTitleResId;
	
	private TextView mTitleView;
	private ImageView mIconView;
	private TextView mDateView;
	private TextView mContentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center_msg_content);
		
		Intent intent = getIntent();
		mInfoId = intent.getStringExtra("sId");
		mTitleResId = intent.getIntExtra("titleResId", 0);
		mNewsMgr = new CenterMsgManager();
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mTitleView = (TextView) findViewById(R.id.title);
		mDateView = (TextView) findViewById(R.id.date);
		mIconView = (ImageView) findViewById(R.id.icon);
		mContentView = (TextView) findViewById(R.id.content);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(mTitleResId);
		
		mNewsMgr.getXX(mInfoId, MainActivity.USER_ID, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), " + obj.toString());
				CenterMsgNewsItem item = (CenterMsgNewsItem)obj;
				mTitleView.setText(item.title);
				mDateView.setText(item.date);
				mContentView.setText(Html.fromHtml(item.content));
			}
		});
	}
}
