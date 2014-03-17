package com.lkoa.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.MainManager;
import com.lkoa.client.ApplicationEnvironment;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.util.LogUtil;

public class MainActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "MainActivity";
	private int back_counts = 0;
	private static int [] mCenterMgrResIds = new int [] {
		R.string.my_todo,
		R.string.received_today,
		R.string.center_msg,
		R.string.schedule,
		R.string.my_email
	};
	
	public static final String USER_ID = "1";
	
	private TextView mTvWelcome;
	private ImageView mIvPhoto;
	private LinearLayout mLayoutCenterMgr;
	private ImageView mIvCenterMgr, mIvCenterMsg, mIvProcessWork;
	private ImageView mIvDocHanding, mIvSchedule, mIvContacts;
	private ImageView mIvMyEmail, mIvMyMsg, mIvMgrPeople;
	
	private TextView [] mCenterMgrTextViews;
	
	private MainManager mMainMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mMainMgr = new MainManager();
		
		findViews();
		setupViews();
	}
	
	private void findViews() {
		mTvWelcome = (TextView)findViewById(R.id.tv_welcome);
		mIvPhoto = (ImageView)findViewById(R.id.iv_photo);
		
		mLayoutCenterMgr = (LinearLayout)findViewById(R.id.linear_center_mgr);
		mIvCenterMsg = (ImageView)findViewById(R.id.iv_center_msg);
		mIvProcessWork = (ImageView)findViewById(R.id.iv_process_work);
		
		mIvDocHanding = (ImageView)findViewById(R.id.iv_doc_handing);
		mIvSchedule = (ImageView)findViewById(R.id.iv_schedule);
		mIvContacts = (ImageView)findViewById(R.id.iv_contacts);
		
		mIvMyEmail = (ImageView)findViewById(R.id.iv_my_email);
		mIvMyMsg = (ImageView)findViewById(R.id.iv_my_msg);
		mIvMgrPeople = (ImageView)findViewById(R.id.iv_mgr_people);
		
		LinearLayout linear = (LinearLayout)findViewById(R.id.linear_center_mgr);
		mCenterMgrTextViews = new TextView[linear.getChildCount()];
		for(int i=0; i<linear.getChildCount(); i++) {
			mCenterMgrTextViews[i] = (TextView)linear.getChildAt(i);
		}
	}
	
	private void saveLatestTime(ApplicationEnvironment app) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
		String currTime = formatter.format(curDate);
		app.saveToPreference("latestTime", currTime);
	}
	
	private void setupViews() {
		ApplicationEnvironment app = ApplicationEnvironment.getInstance();
		String userName = app.getUser();
		String latestTime = app.getLatestTime();
		String welcome = null;
		if(TextUtils.isEmpty(latestTime)) {
			//首次登陆
			welcome = getResources().getString(R.string.hello_user_and_time_first, userName);
			
		} else {
			//非首次
			welcome = getResources().getString(R.string.hello_user_and_time, userName, latestTime);
		}
		saveLatestTime(app);
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
		
		final Resources res = getResources();
		mMainMgr.getGLZXCount("1", new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), " + obj.toString());
				String str = (String)obj;
				if(!TextUtils.isEmpty(str)) {
					String [] subs = str.split("[;]");
					for(int i=0; i<mCenterMgrTextViews.length; i++) {
						mCenterMgrTextViews[i].setText(res.getString(
								mCenterMgrResIds[i], subs[i]));
						}
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				
				for(int i=0; i<mCenterMgrTextViews.length; i++) {
				mCenterMgrTextViews[i].setText(res.getString(
						mCenterMgrResIds[i], 0));
				}
			}
			
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linear_center_mgr:
			//管理中心
			break;
			
		case R.id.iv_center_msg:
			//信息中心
			startActivity(new Intent(this, CenterMsgHomeActivity.class));
			break;
			
		case R.id.iv_process_work:
			//流程办理
			ProcessWorkHomeActivity.start(this, null, 
					R.string.process_work_title, true);
			break;
			
		case R.id.iv_doc_handing:
			//公文办理
			startActivity(new Intent(this, DocMgrHomeActivity.class));
			break;
			
		case R.id.iv_schedule:
			//日程安排
			startActivity(new Intent(this, ScheduleHomeActivity.class));
			break;

		case R.id.iv_contacts:
			//通讯录
			startActivity(new Intent(this, ContactsMainActivity.class));
			/*Intent intent5 = new Intent(MainActivity.this, AddressListActivity.class);
			MainActivity.this.startActivity(intent5);*/
			break;
			
		case R.id.iv_my_email:
			//我的邮件
			startActivity(new Intent(this, MyMailHomeActivity.class));
			break;
			
		case R.id.iv_my_msg:
			//我的短信
			Intent intent = new Intent(this, MyMessageHomeActivity.class);
			startActivity(intent);
			break;
			
		case R.id.iv_mgr_people:
			//人事管理
			break;
			
		default:
			break;
		}
	}
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        {  
        	back_counts++;
        	if(back_counts == 1){
        		this.showToast("再次点击将退出程序！");
        	}else {
        		finish();
        		android.os.Process.killProcess(android.os.Process.myPid());
        	}
			
        }
		return false;  
    }
}
