package com.lkoa.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lkoa.R;
import com.lkoa.adapter.ProcessWorkNodeSelectAdapter;
import com.lkoa.model.ProcessContentInfo;
import com.lkoa.model.ProcessContentInfo.Activity;

/**
 * 流程管理-节点选择
 */
public class ProcessWorkNodeSelectActivity extends CenterMsgBaseActivity implements OnItemClickListener {
	
	private ListView mListView = null;
	private ProcessWorkNodeSelectAdapter mAdapter;
	
	private int mTitleResId;
	
	private ProcessContentInfo mContentInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_work_list);
		
		Intent intent = getIntent();
		mContentInfo = (ProcessContentInfo)intent.getSerializableExtra("bundle");
		
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
		
		mAdapter = new ProcessWorkNodeSelectAdapter(this, 
				R.layout.contacts_list_item_select, mContentInfo.activityList);
		mTvTitle.setText(R.string.process_work_next_node_select);
		mListView.setAdapter(mAdapter);
	}
	
	@Override
	public void finish() {
		Intent intent = new Intent();
		intent.putExtra("bundle", mContentInfo);
		setResult(RESULT_OK, intent);
		
		super.finish();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		mAdapter.clearSelected();
		
		List<Activity> list = mAdapter.getData();
		Activity item = list.get(position);
		item.select = 1;
		mAdapter.notifyDataSetChanged();
	}
}
