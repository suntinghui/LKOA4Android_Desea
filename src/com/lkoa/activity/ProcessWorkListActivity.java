package com.lkoa.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.adapter.BaseListAdapter;
import com.lkoa.adapter.ProcessWorkListAdapter;
import com.lkoa.business.ProcessWorkManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.ProcessItem;

public class ProcessWorkListActivity extends CenterMsgBaseListActivity<ProcessItem> implements OnItemClickListener {
	
	private static final String TYPE_TODO = "0";
	
	private ListView mListView = null;
	
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
	
	private void loadData() {
		mProcessWorkMgr.getLCList(mType, mApp.getUserId(), new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				ArrayList<ProcessItem> list = (ArrayList<ProcessItem>)obj;
				resetPageState(list);
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
				if(list.size() < 1) {
					mTvRight2.setClickable(false);
				} else {
					mTvRight2.setClickable(true);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData();
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
		
		if(TextUtils.equals(mType, TYPE_TODO)) {
			mLinearRight.setVisibility(View.VISIBLE);
			mTvRight1.setVisibility(View.INVISIBLE);
			mTvRight2.setText(R.string.process_work_one_key);
			mTvRight2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//一键接收
					mProcessWorkMgr.allReceive(mApp.getUserId(), new LKAsyncHttpResponseHandler() {
						
						@Override
						public void successAction(Object obj) {
							String result = (String) obj;
							if(TextUtils.equals(result, "1")) {
								//接收成功
								showToast("一键接收成功！");
								mAdapter.clear();
							} else {
								//接收失败
								showToast("一键接收失败！");
							}
						}
					});
				}
			});
			mTvRight2.setClickable(false);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		List<ProcessItem> list = mAdapter.getData();
		ProcessItem item = list.get(getRealPosition(position));
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
