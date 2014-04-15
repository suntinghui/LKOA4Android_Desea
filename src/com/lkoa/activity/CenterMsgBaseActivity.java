package com.lkoa.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.AttachmentManager;
import com.lkoa.client.ApplicationEnvironment;
import com.lkoa.client.DownloadFileRequest;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.util.LogUtil;

public class CenterMsgBaseActivity extends BaseActivity{
	protected ImageView mIvBack;
	protected TextView mTvTitle;
	protected ImageView mIvRight;
	
	protected LinearLayout mLinearRight;
	protected TextView mTvRight1, mTvRight2;
	
	protected LayoutInflater mLayoutInflater;
	
	protected AttachmentManager mAttachmentMgr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayoutInflater = LayoutInflater.from(this);
		mAttachmentMgr = new AttachmentManager();
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
	
	private LKAsyncHttpResponseHandler getAttachmentResponseHandler(final String fileName) {
		return new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				if(obj == null) return;
				
				String fileUrl = mAttachmentMgr.getUrl((String)obj);
				DownloadFileRequest.sharedInstance().downloadAndOpen(
						CenterMsgBaseActivity.this, fileUrl, fileName);
			}
		};
	}
	
	protected void loadAttachment(String fileName, String attId) {
		mAttachmentMgr.getAtt(attId, getAttachmentResponseHandler(fileName));
	}
}
