package com.lkoa.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.adapter.ContactsAdapter;
import com.lkoa.business.ContactsManager;
import com.lkoa.model.ContactItem;
import com.lkoa.model.DepartmentItem;
import com.lkoa.util.LogUtil;
import com.lkoa.util.Pinyin4j;
import com.lkoa.view.AlphaView;
import com.lkoa.view.AlphaView.OnAlphaChangedListener;

/**
 * 通讯录-部门联系人列表
 */
public class ContactsDeptListActivity extends CenterMsgBaseActivity 
	implements OnItemClickListener, OnAlphaChangedListener {
	private static final String TAG = "ContactsDeptListActivity";
	
	private ListView mListView;
	private ContactsAdapter mAdapter;
	
	private List<ContactItem> mContactItemList;
	
	private TextView mAlphaOverlay;
	private WindowManager mWindowMgr;
	
	//存放存在的汉语拼音首字母和与之对应的列表位置
	private HashMap<String, Integer> mContactAlphaIndexer;
	private OverlayThread mOverlayThread;
	
	private String mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts_dept_list);
		
		Intent intent = getIntent();
		DepartmentItem dept = (DepartmentItem)intent.getSerializableExtra("dept");
		mTitle = dept.deptName;
		mContactItemList = dept.contacts;
		
		mContactAlphaIndexer = new HashMap<String, Integer>();
		mOverlayThread = new OverlayThread();
		initOverlay();
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mListView = (ListView)findViewById(android.R.id.list);
		initAlphaView();
		mListView.setOnItemClickListener(this);
	}
	
	private void initAlphaView() {
		AlphaView alphaV = (AlphaView) findViewById(R.id.alphaView);
		alphaV.setOnAlphaChangedListener(this);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(mTitle);
		
		//获取数据
		loadData();
	}
	
	private void loadData() {
		//按名称排序
		initAlphaU(mContactItemList);
		Collections.sort(mContactItemList);
		initContactAlphaIndexer();
		if(mAdapter == null) {
			mAdapter = new ContactsAdapter(ContactsDeptListActivity.this, 0, mContactItemList);
			mListView.setAdapter(mAdapter);
		}
	}
	
	private void initContactAlphaIndexer() {
		final List<ContactItem> list = mContactItemList;
		for (int i = 0; i < list.size(); i++) {
			String currentAlpha = list.get(i).alphaU;
			String previewAlpha = (i - 1) >= 0 ? list.get(i - 1).alphaU : " ";
			if (!TextUtils.equals(currentAlpha, previewAlpha)) {
				String alpha = list.get(i).alphaU;
				mContactAlphaIndexer.put(alpha, i);
			}
		}
	}
	
	private void initAlphaU(List<ContactItem> list) {
		for(ContactItem item : list) {
			item.alphaU = String.valueOf(Pinyin4j.getHanyuPinyin(item.userName).charAt(0));
		}
	}
	
	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		mAlphaOverlay = (TextView) mLayoutInflater.inflate(R.layout.overlay, null);
		mAlphaOverlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		mWindowMgr = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		mWindowMgr.addView(mAlphaOverlay, lp);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
	}
	
	private Handler handler = new Handler();
	
	@Override
	public void OnAlphaChanged(String s, int index) {
		if (s != null && s.trim().length() > 0) {
			mAlphaOverlay.setText(s);
			mAlphaOverlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(mOverlayThread);
			handler.postDelayed(mOverlayThread, 700);
			if (mContactAlphaIndexer.get(s.toLowerCase()) != null) {
				LogUtil.i(TAG, "set list view pos, s="+s);
				int position = mContactAlphaIndexer.get(s.toLowerCase());
				mListView.setSelection(position);
			} else {
				LogUtil.i(TAG, "not operator, s="+s);
			}
		}
	}
	
	// 设置overlay不可见
	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			mAlphaOverlay.setVisibility(View.GONE);
		}

	}
	
}
