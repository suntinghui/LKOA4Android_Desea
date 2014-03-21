package com.lkoa.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lkoa.R;
import com.lkoa.adapter.ProcessWorkListAdapter;
import com.lkoa.business.ProcessWorkManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.ProcessItem;

public class ProcessWorkListActivity extends CenterMsgBaseActivity implements OnItemClickListener {
	
	private ListView mListView = null;
	private ProcessWorkListAdapter mAdapter;
	
	private int mTitleResId;
	private String mType;	//流程列表参数
	private String mInnerType;
	
	private ProcessWorkManager mProcessWorkMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_work_list);
		
		mProcessWorkMgr = new ProcessWorkManager();
		Intent intent = getIntent();
		mType = String.valueOf(intent.getIntExtra("type", 0));
		mTitleResId = intent.getIntExtra("titleResId", -1);
		mInnerType = intent.getStringExtra("innerType");
		
		findViews();
		setupViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		mProcessWorkMgr.getLCList(mType, mApp.getUserId(), new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				ArrayList<ProcessItem> list = (ArrayList<ProcessItem>)obj;
				String title = getResources().getString(mTitleResId);
				StringBuffer buffer = new StringBuffer(title);
				buffer.append("（").append(list.size()).append("）");
				mTvTitle.setText(buffer.toString());
				if(mAdapter == null) {
					mAdapter = new ProcessWorkListAdapter(
							ProcessWorkListActivity.this, 0, list);
					mListView.setAdapter(mAdapter);
				} else {
					mAdapter.setData(list);
					mAdapter.notifyDataSetInvalidated();
					mAdapter.notifyDataSetChanged();
				}
			}
		});
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
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		List<ProcessItem> list = mAdapter.getData();
		ProcessItem item = list.get(position);
		String infoId = item.id;
		
		ProcessWorkHandleActivity.start(this, mApp.getUserId(), 
				infoId, mInnerType, mType);
	}
	
	/**
	 * @param ctx	上下文
	 * @param titleResId	标题名称资源id
	 * @param type	流程列表类型参数
	 * @param innerType	获取表单从表时，类型参数
	 */
	public static void start(Context ctx, int titleResId, int type, String innerType) {
		Intent intent = new Intent(ctx, ProcessWorkListActivity.class);
		intent.putExtra("titleResId", titleResId);
		intent.putExtra("type", type);
		intent.putExtra("innerType", innerType);
		ctx.startActivity(intent);
	}
}
