package com.lkoa.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;

/**
 * 日程管理-首页
 */
public class ScheduleHomeActivity extends CenterMsgBaseActivity implements OnClickListener {
	
	private static final int [] mTitleResIds = new int[] {
		R.string.schedule_my,
		R.string.schedule_department,
		R.string.schedule_leader,
		R.string.schedule_active
	};
	
	private static final int [] mIconResIds = new int[] {
		R.drawable.ic_schedule_my,
		R.drawable.ic_schedule_department,
		R.drawable.ic_schedule_leader,
		R.drawable.ic_schedule_active,
	};
	
	private static final int [] mBackgroundResIds = new int [] {
		R.drawable.center_msg_news_item_bg,
		R.drawable.center_msg_public_item_bg,
		R.drawable.center_msg_notice_item_bg,
		R.drawable.center_msg_window_dep_item_bg
	};
	
	private View mReceivedToday, mReceivedManagement, mSendManagement;
	private View [] mViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_schedule_home);
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mViews = new View[mTitleResIds.length];
		mViews[0] = findViewById(R.id.schedule_my);
		mViews[1] = findViewById(R.id.schedule_department);
		mViews[2] = findViewById(R.id.schedule_leader);
		mViews[3] = findViewById(R.id.schedule_active);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		mTvTitle.setText(R.string.schedule_title);
		
		for(int i=0; i<mViews.length; i++) {
			setupItem(mViews[i], i);
		}
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
