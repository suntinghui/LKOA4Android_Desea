package com.lkoa.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.CenterMsgManager;
import com.lkoa.business.WebViewConfig;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.Attachment;
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
	private LinearLayout mLinearAttachments;
	
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
		
		mLinearAttachments = (LinearLayout)findViewById(R.id.attachments);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		mTvTitle.setText(R.string.news_detail);
		new WebViewConfig(this).config(mContentView);
		loadData();
	}
	
	private void loadData() {
		if(mListType == CenterMsgNewsActivity.LIST_TYPE_NOTICE) {
			mNewsMgr.getTZ(mInfoId, mApp.getUserId(), getResponseHandler());
		} else {
			mNewsMgr.getXX(mInfoId, mApp.getUserId(), getResponseHandler());
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
				String tmp = item.content.replaceAll("&amp;nbsp;", " ");
				mContentView.loadDataWithBaseURL(null, tmp, "text/html",  encoding, null);
				
				buildAttachment(item.attachments);
			}
		};
	}
	
	/**
	 * 构建附件页面
	 */
	private void buildAttachment(List<Attachment> list) {
		if(mLinearAttachments != null) {
			mLinearAttachments.removeAllViews();
		}
		
		LogUtil.i(TAG, "buildAttachment(), list.size="+list.size());
		for(Attachment att : list) {
			//TODO: 构建附件页面
			View view = mLayoutInflater.inflate(R.layout.process_work_handle_content_attachment_item, null);
			TextView name = (TextView)view.findViewById(R.id.tv_attachment_name);
			name.setText(att.title);
			final String attId = att.id;
			final String title = att.title;
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					loadAttachment(title, attId);
				}
			});
			
			mLinearAttachments.addView(view);
		}
	}
}
