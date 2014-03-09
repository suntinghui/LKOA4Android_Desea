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
import com.lkoa.business.ContactsManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.ContactItem;
import com.lkoa.util.LogUtil;
import com.lkoa.util.Pinyin4j;

/**
 * 通讯录-联系人选择
 */
public class ContactsSelectorActivity extends CenterMsgBaseActivity 
	implements OnItemClickListener {
	private static final String TAG = "ContactsSelectorActivity";
	
	public static final int SELECT_MODE_SINGLE = 0;	//联系人单选模式
	public static final int SELECT_MODE_MULTI = 1;	//联系人多选模式
	
	public static final String KEY_SELECT_MODE = "key_select_mode";
	public static final String KEY_SELECTED_CONTACT = "key_selected_contact";
	
	private ListView mSortByNameLv;
	private ContactsSelectorAdapter mAdapter;
	
	private ContactsManager mContactsMgr;
	
	private String mId;
	
	private int mMode = SELECT_MODE_SINGLE;
	private List<String> mSelectedContacts = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts_selector);
		
		Intent intent = getIntent();
		mMode = intent.getIntExtra(KEY_SELECT_MODE, SELECT_MODE_SINGLE);
		String contacts = intent.getStringExtra(KEY_SELECTED_CONTACT);
		if(!TextUtils.isEmpty(contacts)) {
			for(String s : contacts.split("[;]")) {
				mSelectedContacts.add(s);
			}
		}
		
		mContactsMgr = new ContactsManager();
		
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
				List<ContactItem> list = mAdapter.getSelectedContacts();
				for(ContactItem item : list) {
					showContent.append(item.userName).append(";");
					value.append(item.userId).append(";");
				}
				Intent data = new Intent();
				data.putExtra("showContent", showContent.toString());
				data.putExtra("value", value.toString());
				setResult(RESULT_OK, data);
				
				finish();
			}
		});
		
		//获取数据
		mContactsMgr.getSysAddress_Book(getResponseHandler());
	}
	
	private LKAsyncHttpResponseHandler getResponseHandler() {
		return new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), " + obj.toString());
				List<ContactItem> list = (ArrayList<ContactItem>)obj;
				initAlphaU(list);
				initSelect(list);
				if(mAdapter == null) {
					mAdapter = new ContactsSelectorAdapter(ContactsSelectorActivity.this, 0, list);
					mSortByNameLv.setAdapter(mAdapter);
				}
			}
		};
	}
	
	private void initSelect(List<ContactItem> list) {
		for(ContactItem item : list) {
			if(mSelectedContacts.indexOf(item.userId) > -1) {
				item.checked = true;
			}
		}
	}
	
	private void initAlphaU(List<ContactItem> list) {
		for(ContactItem item : list) {
			item.alphaU = String.valueOf(Pinyin4j.getHanyuPinyin(item.userName).charAt(0));
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		if(mMode == SELECT_MODE_SINGLE) {
			//单选模式
			mAdapter.clearSelected();
		}
		
		List<ContactItem> list = mAdapter.getData();
		ContactItem item = list.get(position);
		item.checked = !item.checked;
		mAdapter.notifyDataSetChanged();
	}
	
}
