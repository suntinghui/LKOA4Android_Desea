package com.lkoa.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lkoa.R;
import com.lkoa.business.AttachmentManager;
import com.lkoa.business.WebViewConfig;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.util.LogUtil;

/**
 * 附件显示界面
 */
public class AttachmentShowActivity extends CenterMsgBaseActivity {
	private static final String TAG = "AttachmentShowActivity";
	
	public static final String KEY_ATT_ID = "key_att_id";
	
	private WebView mContentWebView;
	
	private AttachmentManager mAttachmentMgr;
	
	private String mAttId;	//附件序号
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attachment_show);
		
		mAttachmentMgr = new AttachmentManager();
		
		Intent intent = getIntent();
		mAttId = intent.getStringExtra(KEY_ATT_ID);
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mContentWebView = (WebView)findViewById(R.id.webview); 
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		mTvTitle.setText(R.string.attachment_show_title);
		
		new WebViewConfig(this).config(mContentWebView);
		
		mAttachmentMgr.getAtt(mAttId, getAttachmentResponseHandler());
	}
	
	private LKAsyncHttpResponseHandler getAttachmentResponseHandler() {
		return new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), attachment response. obj="+obj);

				if(obj == null) return;
				String url = mAttachmentMgr.getUrl((String)obj);
				LogUtil.i(TAG, "successAction(), url="+url);
				
				mContentWebView.loadUrl(url);
			}
		};
	}
	
	public static void showAttachment(Context ctx, String attId) {
		Intent intent = new Intent(ctx, AttachmentShowActivity.class);
		intent.putExtra(AttachmentShowActivity.KEY_ATT_ID, attId);
		ctx.startActivity(intent);
	}
}
