package com.lkoa.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
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
		R.drawable.center_msg_news_item_bg,
		R.drawable.center_msg_public_item_bg,
	};
	
	private String [] mItemTitles;
	private View [] mItemViews;
	
	private CenterMsgManager mNewsMgr;
	
	private ArrayList<WindowDepartmentItem> mDataList;
	
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
		mItemViews = new View[mItemTitles.length];
		mItemViews[0] = findViewById(R.id.window_d_office_president);
		mItemViews[1] = findViewById(R.id.window_d_legal_affairs);
		mItemViews[2] = findViewById(R.id.window_d_human_resources);
		mItemViews[3] = findViewById(R.id.window_d_finance_management);
		mItemViews[4] = findViewById(R.id.window_d_business_management);
		mItemViews[5] = findViewById(R.id.window_d_run_management);
		mItemViews[6] = findViewById(R.id.window_d_planning_design);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		mTvTitle.setText(R.string.center_msg_window_department);
		
		for(int i=0; i<mItemViews.length; i++) {
			setupItem(mItemViews[i], i, 0);
		}
		loadData();
	}
	
	private void setupItem(View view, int index, int count) {
		ImageView icon = (ImageView)view.findViewById(R.id.iv_center_msg_icon);
		TextView title = (TextView)view.findViewById(R.id.tv_center_msg_title);
		TextView number = (TextView)view.findViewById(R.id.iv_center_msg_number);
		
		view.setBackgroundResource(mBackgroundResIds[index]);
		icon.setImageResource(mIconResIds[index]);
		title.setText(mItemTitles[index]);
		
		if(count < 1) {
			number.setVisibility(View.GONE);
		} else {
			number.setVisibility(View.VISIBLE);
			number.setText(String.valueOf(count));
		}
		view.setOnClickListener(this);
		if(mDataList != null) view.setTag(mDataList.get(index));
	}
	
	private void loadData() {
		mNewsMgr.getBMZC(MainActivity.USER_ID, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction()");
				mDataList = (ArrayList<WindowDepartmentItem>)obj;
				for(int i=0; i<mItemViews.length; i++) {
					setupItem(mItemViews[i], i, mDataList.get(i).count);
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
