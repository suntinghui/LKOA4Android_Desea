package com.lkoa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.ScheduleManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.JTHDContentItem;
import com.lkoa.model.RCContentItem;
import com.lkoa.util.LogUtil;

/**
 * 日程安排-活动内容页
 */
public class ScheduleActiveContentActivity extends CenterMsgBaseActivity {
	private static final String TAG = "ScheduleActiveContentActivity";
	
	private ScheduleManager mScheduleMgr;
	
	private String mInfoId;
	private int mTitleResId;
	
	private LinearLayout mLinearContent;
	private View [] mViews = new View[8];
	private String [] mNames = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_active_content);
		
		mScheduleMgr = new ScheduleManager();
		Intent intent = getIntent();
		mInfoId = intent.getStringExtra("InfoId");
		mTitleResId = intent.getIntExtra("titleResId", -1);
		
		mNames = getResources().getStringArray(R.array.active_content_item_names);
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		mLinearContent = (LinearLayout)findViewById(R.id.content);
		
		mViews[0] = findViewById(R.id.active_title);
		mViews[1] = findViewById(R.id.active_hddd);
		mViews[2] = findViewById(R.id.active_fqbm);
		mViews[3] = findViewById(R.id.active_date);
		mViews[4] = findViewById(R.id.active_state);
		mViews[5] = findViewById(R.id.active_cyr);
		mViews[6] = findViewById(R.id.active_cjld);
		mViews[7] = findViewById(R.id.active_nr);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(mTitleResId);
		mScheduleMgr.getJTHD(mApp.getUserId(), mInfoId, new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), obj="+obj);
				JTHDContentItem item = (JTHDContentItem) obj;
				mLinearContent.setVisibility(View.VISIBLE);
				setViews(item);
			}
		});
	}
	
	private void setViews(JTHDContentItem item) {
		String[] contentVals = new String[8];
		contentVals[0] = item.title;
		contentVals[1] = item.hddd;
		contentVals[2] = item.fqbm;
		contentVals[3] = item.date;
		contentVals[4] = item.state;
		contentVals[5] = item.cyr;
		contentVals[6] = item.cjld;
		contentVals[7] = item.nr;
		
		TextView title, content;
		for(int i=0; i<mViews.length; i++) {
			title = (TextView)mViews[i].findViewById(R.id.title);
			content = (TextView)mViews[i].findViewById(R.id.content_text);
			
			title.setText(mNames[i]);
			content.setText(contentVals[i]);
		}
	}
}
