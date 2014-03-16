package com.lkoa.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.CenterMsgManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.WindowDepartmentItem;
import com.lkoa.util.LogUtil;


/**
 * 信息中心-部门之窗
 */
public class CenterMsgWindowDepartmentActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = "CenterMsgWindowDepartmentActivity";
	
	private static final int [] mIconResIds = new int[] {
		R.drawable.ic_window_d_office_president,
		R.drawable.ic_window_d_legal_affairs,
		R.drawable.ic_window_d_human_resources,
		R.drawable.ic_window_d_finance_management,
		R.drawable.ic_window_d_business_management,
		R.drawable.ic_window_d_run_management,
		R.drawable.ic_window_d_planning_design,
	};
	
	private static final int [] mBackgroundResIds = new int [] {
		R.drawable.center_msg_news_item_bg,
		R.drawable.center_msg_public_item_bg,
		R.drawable.center_msg_notice_item_bg,
		R.drawable.center_msg_window_dep_item_bg,
		R.drawable.process_w_revocation_box_bg, 
	};
	
	private String [] mItemTitles;
	
	private CenterMsgManager mNewsMgr;
	
	private ArrayList<WindowDepartmentItem> mDataList;
	
	private LinearLayout mLinearItems = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mNewsMgr = new CenterMsgManager();
		
		Resources res = getResources();
		mItemTitles = res.getStringArray(R.array.window_d_items);
		
		setContentView(R.layout.activity_center_msg_window_department);
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		mLinearItems = (LinearLayout) findViewById(R.id.columns);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		mTvTitle.setText(R.string.center_msg_window_department);
		loadData();
	}
	
	private void setupItem(WindowDepartmentItem item, int index) {
		View view = mLayoutInflater.inflate(R.layout.center_msg_home_item, mLinearItems, false);
		ImageView icon = (ImageView)view.findViewById(R.id.iv_center_msg_icon);
		TextView title = (TextView)view.findViewById(R.id.tv_center_msg_title);
		TextView number = (TextView)view.findViewById(R.id.iv_center_msg_number);
		
		view.setBackgroundResource(mBackgroundResIds[index % 5]);
		icon.setImageResource(mIconResIds[index % 5]);
		title.setText(item.name);
		
		if(Integer.parseInt(item.count) < 1) {
			number.setVisibility(View.GONE);
		} else {
			number.setVisibility(View.VISIBLE);
			number.setText(item.count);
		}
		view.setOnClickListener(this);
		view.setTag(item);
		
		mLinearItems.addView(view);
	}
	
	private void loadData() {
		mNewsMgr.getBMZC(MainActivity.USER_ID, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction()");
				mDataList = (ArrayList<WindowDepartmentItem>)obj;
				for(int i=0; i<mDataList.size(); i++) {
					setupItem(mDataList.get(i), i);
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		WindowDepartmentItem item = (WindowDepartmentItem)v.getTag();
		Intent intent = new Intent(this, CenterMsgWinDepartmentTwoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("one", item);;
		intent.putExtra("bundle", bundle);
		startActivity(intent);
	}
}
