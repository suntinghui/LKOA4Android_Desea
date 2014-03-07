package com.lkoa.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.lkoa.R;
import com.lkoa.adapter.MyMailListAdapter;
import com.lkoa.business.MyMailManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.MailItemInfo;
import com.lkoa.util.LogUtil;

/**
 * 我的邮件-内部邮件-列表页
 */
public class MyMailInternalListActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = "MyMailInternalListActivity";
	
	public static final int STATE_UNREAD = 0;
	public static final int STATE_READED = 1;
	public static final int STATE_ALL = 2;
	
	private MyMailManager mMainMgr;
	private int mTitleResId;
	
	private ListView mListView;
	private MyMailListAdapter mAdapter;
	
	private int mState = STATE_ALL;
	private int mType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		mType = intent.getIntExtra("type", MyMailInternalMainActivity.TYPE_INBOX);
		mTitleResId = intent.getIntExtra("titleResId", 0);
		
		setContentView(R.layout.activity_my_email_internal_list);
		
		mMainMgr = new MyMailManager();
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mListView = (ListView) findViewById(android.R.id.list);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(mTitleResId);
		
		mMainMgr.getMailList(String.valueOf(mState), String.valueOf(mType), MainActivity.USER_ID, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, obj.toString());
				ArrayList<MailItemInfo> list = (ArrayList<MailItemInfo>)obj;
				if(mAdapter == null) {
					mAdapter = new MyMailListAdapter(MyMailInternalListActivity.this, 0, list);
					mListView.setAdapter(mAdapter);
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_email_internal_inbox:
			//收件箱
			break;
			
		case R.id.my_email_internal_outbox:
			//发件箱
			break;
			
		case R.id.my_email_internal_drafts:
			//草稿箱
			break;
			
		case R.id.my_email_internal_deleted:
			//已删除
			break;

		default:
			break;
		}
	}
}
