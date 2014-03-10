package com.lkoa.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lkoa.R;
import com.lkoa.adapter.ContactsSelectorAdapter;
import com.lkoa.adapter.DepartmentSelectorAdapter;
import com.lkoa.business.ProcessWorkManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.ContactItem;
import com.lkoa.model.DepartmentItem;
import com.lkoa.util.LogUtil;
import com.lkoa.util.Pinyin4j;

/**
 * 流程管理-部门选择
 */
public class DepartmentSelectorActivity extends CenterMsgBaseActivity 
	implements OnItemClickListener {
	private static final String TAG = "ContactsSelectorActivity";
	
	public static final int SELECT_MODE_SINGLE = 5;	//联系人单选模式
	public static final int SELECT_MODE_MULTI = 6;	//联系人多选模式
	public static final String KEY_SELECT_MODE = "key_select_mode";	//选择模式
	
	public static final String KEY_SELECTED_DEPT = "key_selected_contact";//已选择的部门
	
	private ListView mSortByNameLv;
	private DepartmentSelectorAdapter mAdapter;
	
	private ProcessWorkManager mContactsMgr;
	
	private String mId;
	
	private int mMode = SELECT_MODE_SINGLE;
	private List<String> mSelectedContacts = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts_selector);
		
		Intent intent = getIntent();
		mMode = intent.getIntExtra(KEY_SELECT_MODE, SELECT_MODE_SINGLE);
		String contacts = intent.getStringExtra(KEY_SELECTED_DEPT);
		if(!TextUtils.isEmpty(contacts)) {
			for(String s : contacts.split("[,]")) {
				mSelectedContacts.add(s);
			}
		}
		
		mContactsMgr = new ProcessWorkManager();
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mSortByNameLv = (ListView) findViewById(android.R.id.list);
		mSortByNameLv.setOnItemClickListener(this);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(R.string.contacts_selector_title);
		mLinearRight.setVisibility(View.VISIBLE);
		mTvRight1.setVisibility(View.GONE);
		mTvRight2.setVisibility(View.VISIBLE);
		mTvRight2.setText(R.string.confirm);
		mTvRight2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StringBuilder showContent = new StringBuilder();
				StringBuilder value = new StringBuilder();
				List<DepartmentItem> list = mAdapter.getSelectedContacts();
				DepartmentItem item = null;
				int count = list.size();
				for(int i=0; i<count; i++) {
					item = list.get(i);
					if(i == count - 1) {
						showContent.append(item.deptName);
						value.append(item.deptId);
					} else {
						showContent.append(item.deptName).append(",");
						value.append(item.deptId).append(",");
					}
				}
				Intent data = new Intent();
				data.putExtra("showContent", showContent.toString());
				data.putExtra("value", value.toString());
				setResult(RESULT_OK, data);
				
				finish();
			}
		});
		
		//获取数据
		mContactsMgr.getDept(getResponseHandler());
	}
	
	private LKAsyncHttpResponseHandler getResponseHandler() {
		return new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), " + obj.toString());
				List<DepartmentItem> list = (ArrayList<DepartmentItem>)obj;
				initAlphaU(list);
				initSelect(list);
				if(mAdapter == null) {
					mAdapter = new DepartmentSelectorAdapter(DepartmentSelectorActivity.this, 0, list);
					mSortByNameLv.setAdapter(mAdapter);
				}
			}
		};
	}
	
	private void initSelect(List<DepartmentItem> list) {
		for(DepartmentItem item : list) {
			if(mSelectedContacts.indexOf(item.deptId) > -1) {
				item.checked = true;
			}
		}
	}
	
	private void initAlphaU(List<DepartmentItem> list) {
		String py = null;
		for(DepartmentItem item : list) {
			py = Pinyin4j.getHanyuPinyin(item.deptName);
			if(!TextUtils.isEmpty(py)) {
				item.alpha = String.valueOf(py.charAt(0));
			} else {
				item.alpha = "#";
			}
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		if(mMode == SELECT_MODE_SINGLE) {
			//单选模式
			mAdapter.clearSelected();
		}
		
		List<DepartmentItem> list = mAdapter.getData();
		DepartmentItem item = list.get(position);
		item.checked = !item.checked;
		mAdapter.notifyDataSetChanged();
	}
	
}
