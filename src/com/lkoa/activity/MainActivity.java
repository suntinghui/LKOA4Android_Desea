package com.lkoa.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.MainManager;
import com.lkoa.client.ApplicationEnvironment;
import com.lkoa.client.Constant;
import com.lkoa.client.DownloadFileRequest;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.util.FileUtil;
import com.lkoa.util.LogUtil;
import com.lkoa.view.LKAlertDialog;

public class MainActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "MainActivity";
	private int back_counts = 0;
	private static int[] mCenterMgrResIds = new int[] { R.string.my_todo, R.string.received_today, R.string.center_msg, R.string.schedule, R.string.my_email };

	private TextView mTvWelcome, mTvLogout;
	private ImageView mIvPhoto;
	private LinearLayout mLayoutCenterMgr;
	private ImageView mIvCenterMgr, mIvCenterMsg, mIvProcessWork;
	private ImageView mIvDocHanding, mIvSchedule, mIvContacts;
	private ImageView mIvMyEmail, mIvMyMsg, mIvMgrPeople;

	private TextView[] mCenterMgrTextViews;

	private MainManager mMainMgr;

	private String mUserName; // 用户名称，非登录名

	private String downloadAPKURL = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = getIntent();
		mUserName = intent.getStringExtra("userName");

		mMainMgr = new MainManager();

		findViews();
		setupViews();

		new DeleteFileTask().execute();

		new Thread(new Runnable(){
			@Override
			public void run() {
				checkUpdate();
			}}).start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadGLZXCount();
	}

	private void findViews() {
		mTvWelcome = (TextView) findViewById(R.id.tv_welcome);
		mTvLogout = (TextView) findViewById(R.id.tv_logout);
		mTvLogout.setOnClickListener(this);
		mIvPhoto = (ImageView) findViewById(R.id.iv_photo);

		mLayoutCenterMgr = (LinearLayout) findViewById(R.id.linear_center_mgr);
		mIvCenterMsg = (ImageView) findViewById(R.id.iv_center_msg);
		mIvProcessWork = (ImageView) findViewById(R.id.iv_process_work);

		mIvDocHanding = (ImageView) findViewById(R.id.iv_doc_handing);
		mIvSchedule = (ImageView) findViewById(R.id.iv_schedule);
		mIvContacts = (ImageView) findViewById(R.id.iv_contacts);

		mIvMyEmail = (ImageView) findViewById(R.id.iv_my_email);
		mIvMyMsg = (ImageView) findViewById(R.id.iv_my_msg);
		mIvMgrPeople = (ImageView) findViewById(R.id.iv_mgr_people);

		LinearLayout linear = (LinearLayout) findViewById(R.id.linear_center_mgr);
		mCenterMgrTextViews = new TextView[linear.getChildCount()];
		for (int i = 0; i < linear.getChildCount(); i++) {
			mCenterMgrTextViews[i] = (TextView) linear.getChildAt(i);
			mCenterMgrTextViews[i].setOnClickListener(this);
		}
	}

	private void saveLatestTime(ApplicationEnvironment app) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String currTime = formatter.format(curDate);
		app.saveToPreference("latestTime", currTime);
	}

	private void setupViews() {
		ApplicationEnvironment app = ApplicationEnvironment.getInstance();
		String latestTime = app.getLatestTime();
		String welcome = null;
		if (TextUtils.isEmpty(latestTime)) {
			// 首次登陆
			welcome = getResources().getString(R.string.hello_user_and_time_first, mUserName);

		} else {
			// 非首次
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
				String str = (String) obj;
				if (!TextUtils.isEmpty(str)) {
					String[] subs = str.split("[;]");
					for (int i = 0; i < mCenterMgrTextViews.length; i++) {
						mCenterMgrTextViews[i].setText(res.getString(mCenterMgrResIds[i], subs[i]));
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				for (int i = 0; i < mCenterMgrTextViews.length; i++) {
					mCenterMgrTextViews[i].setText(res.getString(mCenterMgrResIds[i], 0));
				}
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linear_center_mgr:
			// 管理中心
			break;

		case R.id.tv_center_msg:
		case R.id.iv_center_msg:
			// 信息中心
			startActivity(new Intent(this, CenterMsgHomeActivity.class));
			break;

		case R.id.tv_my_todo:
		case R.id.iv_process_work:
			// 流程办理
			ProcessWorkHomeActivity.start(this, null, R.string.process_work_title, true);
			break;

		case R.id.tv_my_received_today:
		case R.id.iv_doc_handing:
			// 公文办理
			startActivity(new Intent(this, DocMgrHomeActivity.class));
			break;

		case R.id.tv_schedule:
		case R.id.iv_schedule:
			// 日程安排
			startActivity(new Intent(this, ScheduleHomeActivity.class));
			break;

		case R.id.iv_contacts:
			// 通讯录
			startActivity(new Intent(this, ContactsMainActivity.class));
			/*
			 * Intent intent5 = new Intent(MainActivity.this, AddressListActivity.class); MainActivity.this.startActivity(intent5);
			 */
			break;

		case R.id.tv_my_email:
		case R.id.iv_my_email:
			// 我的邮件
			startActivity(new Intent(this, MyMailHomeActivity.class));
			break;

		case R.id.iv_my_msg:
			// 我的短信
			Intent intent = new Intent(this, MyMessageHomeActivity.class);
			startActivity(intent);
			break;

		case R.id.iv_mgr_people:
			// 人事管理
			intent = new Intent(this, RSMgrHomeActivity.class);
			startActivity(intent);
			break;

		case R.id.tv_logout:
			// 退出，回到登陆界面
			this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back_counts++;
			if (back_counts == 1) {
				this.showToast("再次点击将退出程序！");
			} else {
				destory();
				android.os.Process.killProcess(android.os.Process.myPid());
			}

		}
		return false;
	}

	private void destory() {
		List<BaseActivity> list = getAllActiveActivity();
		for (BaseActivity a : list) {
			a.finish();
		}
	}

	public static void start(Context ctx, String userName) {
		Intent intent = new Intent(ctx, MainActivity.class);
		intent.putExtra("userName", userName);
		ctx.startActivity(intent);
	}

	// ////////////////////////////

	// 删除缓存的附件
	class DeleteFileTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			try {
				FileUtil.deleteFiles();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
	}

	private void checkUpdate() {
		try {
			URL myURL = new URL(ApplicationEnvironment.getInstance().getPreferences().getString(Constant.kHOSTNAME, Constant.DEFAULTHOST) + "androidUpdate.xml");
			URLConnection ucon = myURL.openConnection();
			ucon.setConnectTimeout(20000);
			ucon.setReadTimeout(20000);
			InputStream is = ucon.getInputStream();

			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");

			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:
					if ("version".equals(parser.getName())) {

						int serviceVersion = Integer.parseInt(parser.nextText());
						if (serviceVersion > Constant.VERSION) {
							Log.e("版本更新", "有新版本，需要更新...");
							
							Message msg = new Message();  
				            msg.what = 1;  
				            MainActivity.this.myHandler.sendMessage(msg);
							
						} else {
							Log.e("版本更新", "是最新版本，不需要更新...");
						}

					} else if ("url".equals(parser.getName())) {
						downloadAPKURL = parser.nextText();
					}

					break;
				}

				event = parser.next();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "服务器异常，请稍候再试");

		} catch (IOException e) {
			e.printStackTrace();
			BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "服务器异常，请稍候再试");

		} catch (XmlPullParserException e) {
			e.printStackTrace();
			BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "服务器异常，请稍候再试");

		} catch (Exception e) {
			e.printStackTrace();
			BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "连接服务器超时，请稍候再试。");

		} finally {
		}
	}
	
	private Handler myHandler = new Handler() {  
		  
        @Override  
        public void handleMessage(Message msg) {  
            // //执行接收到的通知，更新UI 此时执行的顺序是按照队列进行，即先进先出  
            super.handleMessage(msg);  
            switch (msg.what) {  
            case 0: // 最新  
            	
                break;
                
            case 1: // 非最新
            	showUpdateDialog();
            	break;
  
            }  
        }  
  
    };  

	private void showUpdateDialog() {
		LKAlertDialog dialog = new LKAlertDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("有新版本，是否下载更新？");
		dialog.setCancelable(false);
		dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				Update();
			}
		});
		dialog.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		dialog.create().show();
	}

	private void Update() {
		DownloadFileRequest.sharedInstance().downloadAndOpen(this, downloadAPKURL, "Desea.apk");
	}
}
