package com.lkoa.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.lkoa.R;
import com.lkoa.adapter.SmsMessageListAdapter;
import com.lkoa.business.MySmsManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.SmsMessage;

/**
 * 我的短信-列表页
 */
public class MyMessageListActivity extends CenterMsgBaseActivity {
	
	private ListView mListView = null;
	private SmsMessageListAdapter mAdapter;
	
	private int mTitleResId;
	
	private MySmsManager mSmsMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_work_list);
		
		mSmsMgr = new MySmsManager();
		Intent intent = getIntent();
		mTitleResId = intent.getIntExtra("titleResId", -1);
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		mListView = (ListView)findViewById(android.R.id.list);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(mTitleResId);
		mIvRight.setVisibility(View.VISIBLE);
		mIvRight.setImageResource(R.drawable.my_sms_write);
		
		mIvRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyMessageListActivity.this, MyMessageWriteActivity.class);
				startActivity(intent);
			}
		});
		
		mSmsMgr.getSmsList(MainActivity.USER_ID, new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				ArrayList<SmsMessage> list = (ArrayList<SmsMessage>)obj;
				if(mAdapter == null) {
					mAdapter = new SmsMessageListAdapter(
							MyMessageListActivity.this, 0, list);
					mListView.setAdapter(mAdapter);
				}
			}
		});
	}
}
