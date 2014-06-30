package com.lkoa.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.model.DocItem;
import com.lkoa.model.WindowDepartmentItem;


/**
 * 信息中心-文档中心-二级目录
 */
public class CenterMsgDocTwoActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = "CenterMsgDocTwoActivity";
	
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
	
	private ArrayList<DocItem> mDataList;
	
	private LinearLayout mLinearColumns;
	
	private DocItem mOneItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_center_msg_window_department_two);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		mOneItem = (DocItem)bundle.getSerializable("one");
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		mLinearColumns = (LinearLayout)findViewById(R.id.columns);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		mTvTitle.setText(mOneItem.name);
		
		List<DocItem> list = mOneItem.list;
		buildList(list);
	}
	
	private void setupItem(View view, int index, DocItem two) {
		ImageView icon = (ImageView)view.findViewById(R.id.iv_center_msg_icon);
		TextView title = (TextView)view.findViewById(R.id.tv_center_msg_title);
		TextView number = (TextView)view.findViewById(R.id.iv_center_msg_number);
		
		view.setBackgroundResource(mBackgroundResIds[index]);
		icon.setImageResource(mIconResIds[index]);
		title.setText(two.name);
		
		if(Integer.parseInt(two.count) < 1) {
			number.setVisibility(View.GONE);
		} else {
			number.setVisibility(View.VISIBLE);
			number.setText(String.valueOf(two.count));
		}
		number.setOnClickListener(this);
		number.setTag(two);
		view.setOnClickListener(this);
		view.setTag(two);
	}
	
	private void buildList(List<DocItem> list) {
		if(mLinearColumns != null) mLinearColumns.removeAllViews();
		
		DocItem item = null;
		for(int i=0; i<list.size(); i++) {
			item = list.get(i);
			View view = mLayoutInflater.inflate(R.layout.center_msg_home_item, mLinearColumns, false);
			setupItem(view, i, item);
			mLinearColumns.addView(view);
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.iv_center_msg_number) {
			DocItem two = (DocItem)v.getTag();
			Intent intent = new Intent(this, CenterMsgNewsActivity.class);
			intent.putExtra("listType", CenterMsgNewsActivity.LIST_TYPE_DOC);
			intent.putExtra("sId", two.id);
			intent.putExtra("title", two.name);
			startActivity(intent);
			return;
		}
		
		DocItem two = (DocItem)v.getTag();
		if(two.list != null && two.list.size() > 0) {
			//TODO: 构建选项列表
			buildList(two.list);
			
		} else {
			Intent intent = new Intent(this, CenterMsgNewsActivity.class);
			intent.putExtra("listType", CenterMsgNewsActivity.LIST_TYPE_DOC);
			intent.putExtra("sId", two.id);
			intent.putExtra("title", two.name);
			startActivity(intent);
		}
	}
}
