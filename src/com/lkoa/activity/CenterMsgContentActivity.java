package com.lkoa.activity;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
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
	private WebView mContentView;
	
	private int mListType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center_msg_content);
		
		Intent intent = getIntent();
		mInfoId = intent.getStringExtra("sId");
		mTitleResId = intent.getIntExtra("titleResId", 0);
		mListType = intent.getIntExtra("listType", CenterMsgNewsActivity.LIST_TYPE_NEWS);
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
		mContentView = (WebView) findViewById(R.id.content);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		mTvTitle.setText(R.string.news_detail);
		loadData();
	}
	
	private void loadData() {
		if(mListType == CenterMsgNewsActivity.LIST_TYPE_NOTICE) {
			mNewsMgr.getTZ(mInfoId, MainActivity.USER_ID, getResponseHandler());
		} else {
			mNewsMgr.getXX(mInfoId, MainActivity.USER_ID, getResponseHandler());
		}
	}
	
	private LKAsyncHttpResponseHandler getResponseHandler() {
		return new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), " + obj.toString());
				CenterMsgNewsItem item = (CenterMsgNewsItem)obj;
				mTitleView.setText(item.title);
				mDateView.setText(item.date);
				String encoding = "utf-8";
				mContentView.getSettings().setDefaultTextEncodingName(encoding);
				mContentView.loadDataWithBaseURL(null, item.content, "text/html",  encoding, null);
			}
		};
	}
}
