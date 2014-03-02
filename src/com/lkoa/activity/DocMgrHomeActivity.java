package com.lkoa.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;

/**
 * 公文管理-首页
 */
public class DocMgrHomeActivity extends CenterMsgBaseActivity implements OnClickListener {
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_doc_mgr_home);
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
		
		setupItem(mReceivedToday, 0);
		setupItem(mReceivedManagement, 1);
		setupItem(mSendManagement, 2);
	}
	
	private void setupItem(View view, int index) {
		ImageView icon = (ImageView)view.findViewById(R.id.iv_center_msg_icon);
		TextView title = (TextView)view.findViewById(R.id.tv_center_msg_title);
		TextView number = (TextView)view.findViewById(R.id.iv_center_msg_number);
		
		view.setBackgroundResource(mBackgroundResIds[index]);
		icon.setImageResource(mIconResIds[index]);
		title.setText(mTitleResIds[index]);
		number.setText("6");
		
		if(index > 1) number.setVisibility(View.GONE);
		view.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.doc_mgr_received_today:
			//今日收文
			break;
			
		case R.id.doc_mgr_received_management:
			//收文管理
			break;
			
		case R.id.doc_mgr_sent_management:
			//发文管理
			break;

		default:
			break;
		}
	}
}
