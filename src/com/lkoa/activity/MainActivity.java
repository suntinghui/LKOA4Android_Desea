package com.lkoa.activity;

import com.lkoa.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	
	private TextView mTvWelcome;
	private ImageView mIvPhoto;
	private LinearLayout mLayoutCenterMgr;
	private ImageView mIvCenterMgr, mIvCenterMsg, mIvProcessWork;
	private ImageView mIvDocHanding, mIvSchedule, mIvContacts;
	private ImageView mIvMyEmail, mIvMyMsg, mIvMgrPeople;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		setupViews();
	}
	
	private void findViews() {
		mTvWelcome = (TextView)findViewById(R.id.tv_welcome);
		mIvPhoto = (ImageView)findViewById(R.id.iv_photo);
		
		mLayoutCenterMgr = (LinearLayout)findViewById(R.id.iv_center_mgr);
		mIvCenterMsg = (ImageView)findViewById(R.id.iv_center_msg);
		mIvProcessWork = (ImageView)findViewById(R.id.iv_process_work);
		
		mIvDocHanding = (ImageView)findViewById(R.id.iv_doc_handing);
		mIvSchedule = (ImageView)findViewById(R.id.iv_schedule);
		mIvContacts = (ImageView)findViewById(R.id.iv_contacts);
		
		mIvMyEmail = (ImageView)findViewById(R.id.iv_my_email);
		mIvMyMsg = (ImageView)findViewById(R.id.iv_my_msg);
		mIvMgrPeople = (ImageView)findViewById(R.id.iv_mgr_people);
	}
	
	private void setupViews() {
		String welcome = getResources().getString(R.string.hello_user_and_time, "佳佳", "2013-12-12 12:12");
		mTvWelcome.setText(welcome);
		
		mLayoutCenterMgr.setOnClickListener(this);
		mIvCenterMsg.setOnClickListener(this);
		mIvProcessWork.setOnClickListener(this);
		
		mIvDocHanding.setOnClickListener(this);
		mIvSchedule.setOnClickListener(this);
		mIvContacts.setOnClickListener(this);
		
		mIvMyEmail.setOnClickListener(this);
		mIvMyMsg.setOnClickListener(this);
		mIvMgrPeople.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_center_mgr:
			//管理中心
			break;
			
		case R.id.iv_center_msg:
			//信息中心
			break;
			
		case R.id.iv_process_work:
			//流程办理
			break;
			
		case R.id.iv_doc_handing:
			//公文办理
			break;
			
		case R.id.iv_schedule:
			//日程安排
			break;

		case R.id.iv_contacts:
			//通讯录
			break;
			
		case R.id.iv_my_email:
			//我的邮件
			break;
			
		case R.id.iv_my_msg:
			//我的短信
			break;
			
		case R.id.iv_mgr_people:
			//人事管理
			break;
			
		default:
			break;
		}
	}
}
