package com.lkoa.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
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
	
	private TextView mTvWelcome, mTvLogout;
	private ImageView mIvPhoto;
	private LinearLayout mLayoutCenterMgr;
	private ImageView mIvCenterMgr, mIvCenterMsg, mIvProcessWork;
	private ImageView mIvDocHanding, mIvSchedule, mIvContacts;
	private ImageView mIvMyEmail, mIvMyMsg, mIvMgrPeople;
	
	private TextView [] mCenterMgrTextViews;
	
	private MainManager mMainMgr;
	
	private String mUserName;	//用户名称，非登录名

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		mUserName = intent.getStringExtra("userName");
		
		mMainMgr = new MainManager();
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadGLZXCount();
	}
	
	private void findViews() {
		mTvWelcome = (TextView)findViewById(R.id.tv_welcome);
		mTvLogout = (TextView) findViewById(R.id.tv_logout);
		mTvLogout.setOnClickListener(this);
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
			mCenterMgrTextViews[i].setOnClickListener(this);
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
		String latestTime = app.getLatestTime();
		String welcome = null;
		if(TextUtils.isEmpty(latestTime)) {
			//首次登陆
			welcome = getResources().getString(R.string.hello_user_and_time_first, mUserName);
			
		} else {
			//非首次
			welcome = getResources().getString(R.string.hello_user_and_time, mUserName, latestTime);
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
	}
	
	private void loadGLZXCount() {
		final Resources res = getResources();
		mMainMgr.getGLZXCount(mApp.getUserId(), new LKAsyncHttpResponseHandler() {

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
			
		case R.id.tv_center_msg:
		case R.id.iv_center_msg:
			//信息中心
			startActivity(new Intent(this, CenterMsgHomeActivity.class));
			break;
			
		case R.id.tv_my_todo:
		case R.id.iv_process_work:
			//流程办理
			ProcessWorkHomeActivity.start(this, null, 
					R.string.process_work_title, true);
			break;
			
		case R.id.tv_my_received_today:
		case R.id.iv_doc_handing:
			//公文办理
			startActivity(new Intent(this, DocMgrHomeActivity.class));
			break;
			
		case R.id.tv_schedule:
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
			
		case R.id.tv_my_email:
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
			intent = new Intent(this, RSMgrHomeActivity.class);
			startActivity(intent);
			break;
			
		case R.id.tv_logout:
			//退出，回到登陆界面
			this.finish();
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
        		destory();
        		android.os.Process.killProcess(android.os.Process.myPid());
        	}
			
        }
		return false;  
    }
	
	private void destory() {
		List<BaseActivity> list = getAllActiveActivity();
		for(BaseActivity a : list) {
			a.finish();
		}
	}
	
	public static void start(Context ctx, String userName) {
		Intent intent = new Intent(ctx, MainActivity.class);
		intent.putExtra("userName", userName);
		ctx.startActivity(intent);
	}
}
