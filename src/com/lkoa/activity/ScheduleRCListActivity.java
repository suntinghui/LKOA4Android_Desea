package com.lkoa.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.lkoa.R;
import com.lkoa.adapter.ScheduleRCListAdapter;
import com.lkoa.business.ScheduleManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.RCListItem;

/**
 * 日程安排-列表页
 */
public class ScheduleRCListActivity extends CenterMsgBaseActivity implements OnItemClickListener {
	
	private ListView mListView = null;
	private ScheduleRCListAdapter mAdapter;
	
	private int mTitleResId;
	private int mType;	//1=我的日程，2 =部门日程，3=领导日程，4=集团活动
	
	private ScheduleManager mScheduleMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_work_list);
		
		mScheduleMgr = new ScheduleManager();
		Intent intent = getIntent();
		mTitleResId = intent.getIntExtra("titleResId", -1);
		mType = intent.getIntExtra("sType", 1);
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		mListView = (ListView)findViewById(android.R.id.list);
		mListView.setOnItemClickListener(this);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(mTitleResId);
		
		if(mType < 4) {
			//日程列表
			mScheduleMgr.getRCList(String.valueOf(mType), MainActivity.USER_ID, new LKAsyncHttpResponseHandler() {
				
				@Override
				public void successAction(Object obj) {
					List<RCListItem> list = (ArrayList<RCListItem>)obj;
					if(mAdapter == null) {
						mAdapter = new ScheduleRCListAdapter(
								ScheduleRCListActivity.this, 0, list);
						mListView.setAdapter(mAdapter);
					}
				}
			});
			
		} else {
			//集团活动
			mScheduleMgr.getJTHDList(MainActivity.USER_ID, new LKAsyncHttpResponseHandler() {
				
				@Override
				public void successAction(Object obj) {
					List<RCListItem> list = (ArrayList<RCListItem>)obj;
					if(mAdapter == null) {
						mAdapter = new ScheduleRCListAdapter(
								ScheduleRCListActivity.this, 0, list);
						mListView.setAdapter(mAdapter);
					}
				}
			});
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		RCListItem item = mAdapter.getData().get(position);
		Intent intent = null;
		if(mType < 4) {
			intent = new Intent(this, ScheduleRCContentActivity.class);
			intent.putExtra("titleResId", R.string.schedule_rc_content_title);
		} else {
			intent = new Intent(this, ScheduleActiveContentActivity.class);
			intent.putExtra("titleResId", R.string.schedule_active_content_title);
		}
		intent.putExtra("InfoId", item.id);
		
		startActivity(intent);
	}
}
