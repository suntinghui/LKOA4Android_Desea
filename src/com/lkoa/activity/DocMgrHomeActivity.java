package com.lkoa.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.DocManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.util.LogUtil;

/**
 * 公文管理-首页
 */
public class DocMgrHomeActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = "DocMgrHomeActivity";
	
	private static final int [] mTitleResIds = new int[] {
		R.string.doc_mgr_received_today,
		R.string.doc_mgr_received_management,
		R.string.doc_mgr_sent_management,
	};
	
	private static final int [] mIconResIds = new int[] {
		R.drawable.ic_doc_mgr_received_today,
		R.drawable.ic_doc_mgr_received_management,
		R.drawable.ic_doc_mgr_send_management,
	};
	
	private static final int [] mBackgroundResIds = new int [] {
		R.drawable.center_msg_news_item_bg,
		R.drawable.center_msg_public_item_bg,
		R.drawable.center_msg_notice_item_bg,
	};
	
	private View mReceivedToday, mReceivedManagement, mSendManagement;
	
	private DocManager mDocMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_doc_mgr_home);
		
		mDocMgr = new DocManager();
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mReceivedToday = findViewById(R.id.doc_mgr_received_today);
		mReceivedManagement = findViewById(R.id.doc_mgr_received_management);
		mSendManagement = findViewById(R.id.doc_mgr_sent_management);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(R.string.doc_mgr_title);
		
		setupItem(mReceivedToday, 0, 0);
		setupItem(mReceivedManagement, 1, 0);
		setupItem(mSendManagement, 2, 0);
		
		mDocMgr.getGWGLCount(MainActivity.USER_ID, new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), obj="+obj);
				String result = (String) obj;
				String [] counts = result.split(";");
				setupItem(mReceivedToday, 0, Integer.parseInt(counts[0]));
				setupItem(mReceivedManagement, 1, Integer.parseInt(counts[1]));
				setupItem(mSendManagement, 2, Integer.parseInt(counts[2]));
			}
		});
	}
	
	private void setupItem(View view, int index, int count) {
		ImageView icon = (ImageView)view.findViewById(R.id.iv_center_msg_icon);
		TextView title = (TextView)view.findViewById(R.id.tv_center_msg_title);
		TextView number = (TextView)view.findViewById(R.id.iv_center_msg_number);
		
		view.setBackgroundResource(mBackgroundResIds[index]);
		icon.setImageResource(mIconResIds[index]);
		title.setText(mTitleResIds[index]);
		if(count > 0) {
			number.setText(count);
			number.setVisibility(View.VISIBLE);
		} else {
			number.setVisibility(View.GONE);
		}
		view.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.doc_mgr_received_today:
			//今日收文
			ProcessWorkListActivity.start(this, R.string.doc_mgr_received_today, 6, "0");
			break;
			
		case R.id.doc_mgr_received_management:
			//收文管理
			ProcessWorkHomeActivity.start(this, new int[] {7, 8, 9, 10, 11}, 
					R.string.doc_mgr_received_management, false);
			break;
			
		case R.id.doc_mgr_sent_management:
			//发文管理
			ProcessWorkHomeActivity.start(this, new int[] {12, 13, 14, 15, 16}, 
					R.string.doc_mgr_sent_management, false);
			break;

		default:
			break;
		}
	}
}
