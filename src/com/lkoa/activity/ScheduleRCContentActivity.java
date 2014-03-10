package com.lkoa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.adapter.ScheduleRCListAdapter;
import com.lkoa.business.ScheduleManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.RCContentItem;
import com.lkoa.model.RCListItem;
import com.lkoa.util.LogUtil;

/**
 * 日程安排-内容页
 */
public class ScheduleRCContentActivity extends CenterMsgBaseActivity implements OnItemClickListener {
	private static final String TAG = "ScheduleRCContentActivity";
	
	private ScheduleRCListAdapter mAdapter;
	
	private ScheduleManager mScheduleMgr;
	
	private String mInfoId;
	private int mTitleResId;
	
	private LinearLayout mLinearContent;
	private View [] mViews = new View[7];
	private String [] mNames = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_rc_content);
		
		mScheduleMgr = new ScheduleManager();
		Intent intent = getIntent();
		mInfoId = intent.getStringExtra("InfoId");
		mTitleResId = intent.getIntExtra("titleResId", -1);
		
		mNames = getResources().getStringArray(R.array.rc_content_item_names);
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		mLinearContent = (LinearLayout)findViewById(R.id.content);
		
		mViews[0] = findViewById(R.id.rc_title);
		mViews[1] = findViewById(R.id.rc_zxr);
		mViews[2] = findViewById(R.id.rc_date);
		mViews[3] = findViewById(R.id.rc_fw);
		mViews[4] = findViewById(R.id.rc_state);
		mViews[5] = findViewById(R.id.rc_cyz);
		mViews[6] = findViewById(R.id.rc_content);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(mTitleResId);
		mScheduleMgr.getRC(MainActivity.USER_ID, mInfoId, new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), obj="+obj);
				RCContentItem item = (RCContentItem) obj;
				mLinearContent.setVisibility(View.VISIBLE);
				setViews(item);
			}
		});
	}
	
	private void setViews(RCContentItem item) {
		String[] contentVals = new String[7];
		contentVals[0] = item.title;
		contentVals[1] = item.zxr;
		contentVals[2] = item.date;
		contentVals[3] = item.fw;
		contentVals[4] = item.state;
		contentVals[5] = item.cyr;
		contentVals[6] = item.nr;
		
		TextView title, content;
		for(int i=0; i<mViews.length; i++) {
			title = (TextView)mViews[i].findViewById(R.id.title);
			content = (TextView)mViews[i].findViewById(R.id.content_text);
			
			title.setText(mNames[i]);
			content.setText(contentVals[i]);
		}
	}
	

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		RCListItem item = mAdapter.getData().get(position);
	}
}
