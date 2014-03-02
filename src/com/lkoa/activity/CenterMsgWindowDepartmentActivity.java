package com.lkoa.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;


/**
 * 信息中心-部门之窗
 */
public class CenterMsgWindowDepartmentActivity extends CenterMsgBaseActivity implements OnClickListener {
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
			setupItem(mItemViews[i], i);
		}
	}
	
	private void setupItem(View view, int index) {
		ImageView icon = (ImageView)view.findViewById(R.id.iv_center_msg_icon);
		TextView title = (TextView)view.findViewById(R.id.tv_center_msg_title);
		TextView number = (TextView)view.findViewById(R.id.iv_center_msg_number);
		
		view.setBackgroundResource(mBackgroundResIds[index]);
		icon.setImageResource(mIconResIds[index]);
		title.setText(mItemTitles[index]);
		number.setText("6");
		
		if(index > 1) number.setVisibility(View.GONE);
		view.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.window_d_office_president:
			//总裁办公室
			break;
			
		case R.id.window_d_legal_affairs:
			//法律事务部
			break;
			
		case R.id.window_d_human_resources:
			//人力资源部
			break;
			
		case R.id.window_d_finance_management:
			//财务管理部
			break;
			
		case R.id.window_d_business_management:
			//企业管理部
			break;
			
		case R.id.window_d_run_management:
			//经营管理部
			break;
			
		case R.id.window_d_planning_design:
			//规划设计部
			break;

		default:
			break;
		}
	}
}
