package com.lkoa.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.lkoa.adapter.DeptsAdapter;
import com.lkoa.business.ContactsManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.ContactItem;
import com.lkoa.model.DepartmentItem;
import com.lkoa.util.LogUtil;
import com.lkoa.util.Pinyin4j;
import com.lkoa.view.AlphaView;
import com.lkoa.view.AlphaView.OnAlphaChangedListener;

/**
 * 通讯录-首页
 */
public class ContactsMainActivity extends CenterMsgBaseActivity 
	implements OnClickListener, OnItemClickListener, OnAlphaChangedListener {
	private static final String TAG = "ContactsMainActivity";
	
	private static final int INDEX_SORT_BY_NAME = 0;
	private static final int INDEX_SORT_BY_DEPARTMENT = 1;
	
	private View mParentLatestNews, mParentMoreNews;
	private TextView mTvLatestNews, mTvMoreNews;
	private View mLineLatestNews, mLineMoreNews;
	
	private int mTextColorSelected;
	private int mTextColorUnselected;
	
	private ViewPager mViewPager;
	private ListView mSortByNameLv, mSortByDeptLv;
	private View mSortByNameViewParent, mSortByDeptViewParent;
	private ContactsAdapter mSortByNameAdapter;
	private DeptsAdapter mSortByDeptAdapter;
	
	private boolean mSortByNameLoaded, mSortByDeptLoaded;
	
	private ContactsManager mContactsMgr;
	
	private String mId;
	
	private List<ContactItem> mContactItemList;
	
	private TextView mAlphaOverlay;
	private WindowManager mWindowMgr;
	
	//存放存在的汉语拼音首字母和与之对应的列表位置
	private HashMap<String, Integer> mDeptAlphaIndexer;
	private HashMap<String, Integer> mContactAlphaIndexer;
	private OverlayThread mOverlayThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts_main);
		
		mContactsMgr = new ContactsManager();
		
		Resources res = getResources();
		mTextColorSelected = res.getColor(R.color.center_msg_news_tab_text_selected);
		mTextColorUnselected = res.getColor(R.color.center_msg_news_tab_text_unselected);
		
		findViews();
		setupViews();
		
		mDeptAlphaIndexer = new HashMap<String, Integer>();
		mContactAlphaIndexer = new HashMap<String, Integer>();
		mOverlayThread = new OverlayThread();
		initOverlay();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mParentLatestNews = findViewById(R.id.vg_latest_news);
		mParentMoreNews = findViewById(R.id.vg_more_news);
		
		mTvLatestNews = (TextView) findViewById(R.id.tv_latest_news);
		mTvMoreNews = (TextView) findViewById(R.id.tv_more_news);
		
		mLineLatestNews = findViewById(R.id.v_latest_news_line_selected);
		mLineMoreNews = findViewById(R.id.v_more_news_line_selected);
		
		mViewPager = (ViewPager)findViewById(R.id.view_pager);
		mSortByNameViewParent = mLayoutInflater.inflate(R.layout.layout_listview_with_alpha, null);
		mSortByNameLv = (ListView)mSortByNameViewParent.findViewById(android.R.id.list);
		initAlphaView(mSortByNameViewParent);
		
		mSortByDeptViewParent = mLayoutInflater.inflate(R.layout.layout_listview_with_alpha, null);
		mSortByDeptLv = (ListView)mSortByDeptViewParent.findViewById(android.R.id.list);
		initAlphaView(mSortByDeptViewParent);
		
		mSortByNameLv.setOnItemClickListener(this);
		mSortByDeptLv.setOnItemClickListener(this);
	}
	
	private void initAlphaView(View view) {
		AlphaView alphaV = (AlphaView) view.findViewById(R.id.alphaView);
		alphaV.setOnAlphaChangedListener(this);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(R.string.contacts_title);
		
		mParentLatestNews.setOnClickListener(this);
		mParentMoreNews.setOnClickListener(this);
		
		List<View> list = new ArrayList<View>();
		list.add(mSortByNameViewParent);
		list.add(mSortByDeptViewParent);
		mViewPager.setAdapter(new MyPagerAdapter(list));
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		//获取数据
		loadData(INDEX_SORT_BY_NAME);
	}
	
	private void switchTo(int activeIdx) {
		if(activeIdx == INDEX_SORT_BY_DEPARTMENT) {
			mTvLatestNews.setTextColor(mTextColorUnselected);
			mTvMoreNews.setTextColor(mTextColorSelected);
			mLineMoreNews.setVisibility(View.VISIBLE);
			mLineLatestNews.setVisibility(View.GONE);
			if(mSortByDeptLoaded == false) {
				loadData(activeIdx);
			}
			
		} else {
			mTvLatestNews.setTextColor(mTextColorSelected);
			mTvMoreNews.setTextColor(mTextColorUnselected);
			mLineLatestNews.setVisibility(View.VISIBLE);
			mLineMoreNews.setVisibility(View.GONE);
			if(mSortByNameLoaded == false) {
				loadData(activeIdx);
			}
		}
		mViewPager.setCurrentItem(activeIdx);
	}
	
	private void loadData(int index) {
		if(index == INDEX_SORT_BY_NAME) {
			//按姓名排序
			mContactsMgr.getSysAddress_Book(getResponseHandler(index));
			
		} else {
			//按部门排序
			if(mContactItemList != null && mContactItemList.size() > 0) {
				if(mSortByDeptAdapter == null) {
					List<DepartmentItem> list = mContactsMgr.getDepts(mContactItemList);
					initDeptAlphaU(list);
					Collections.sort(list);
					initDeptAlphaIndexer(list);
					mSortByDeptAdapter = new DeptsAdapter(this, 0, list);
					mSortByDeptLv.setAdapter(mSortByDeptAdapter);
				}
			}
		}
	}
	
	private LKAsyncHttpResponseHandler getResponseHandler(final int index) {
		return new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), " + obj.toString());
				
				if(index == INDEX_SORT_BY_NAME) {
					//按名称排序
					mContactItemList = (ArrayList<ContactItem>)obj;
					initAlphaU(mContactItemList);
					Collections.sort(mContactItemList);
					initContactAlphaIndexer();
					mSortByNameLoaded = true;
					if(mSortByNameAdapter == null) {
						mSortByNameAdapter = new ContactsAdapter(ContactsMainActivity.this, 0, mContactItemList);
						mSortByNameLv.setAdapter(mSortByNameAdapter);
					}
					
				} else {
					//按部门排序
					mSortByDeptLoaded = true;
				}
			}
		};
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
	
	private void initDeptAlphaIndexer(final List<DepartmentItem> list) {
		for (int i = 0; i < list.size(); i++) {
			String currentAlpha = list.get(i).alpha;
			String previewAlpha = (i - 1) >= 0 ? list.get(i - 1).alpha : " ";
			if (!TextUtils.equals(currentAlpha, previewAlpha)) {
				String alpha = list.get(i).alpha;
				mDeptAlphaIndexer.put(alpha, i);
			}
		}
	}
	
	private void initDeptAlphaU(List<DepartmentItem> list) {
		for(DepartmentItem item : list) {
			if(TextUtils.isEmpty(item.deptName)) {
				item.alpha = "#";
			} else {
				item.alpha = String.valueOf(
						Pinyin4j.getHanyuPinyin(item.deptName).charAt(0));
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vg_latest_news:
			//最新消息
			switchTo(INDEX_SORT_BY_NAME);
			loadData(INDEX_SORT_BY_NAME);
			
			break;
			
		case R.id.vg_more_news:
			//更多消息
			switchTo(INDEX_SORT_BY_DEPARTMENT);
			loadData(INDEX_SORT_BY_DEPARTMENT);
			break;

		default:
			break;
		}
	}
	
	private class MyPagerAdapter extends PagerAdapter {
		List<View> mViews = null;

		public MyPagerAdapter(List<View> views) {
			mViews = views;
		}
		
		@Override
		public int getCount() {
			return mViews.size();
		}
		
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			View view = mViews.get(arg1);
			mViewPager.addView(view, 0);
			return mViews.get(arg1);
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			mViewPager.removeView(mViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
	
	private class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
			switchTo(index);
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		int index = mViewPager.getCurrentItem();
		if(index == 0) {
			ContactItem item = mSortByNameAdapter.getData().get(position);
			ContactDetailsActivity.start(this, item);
		} else {
			DepartmentItem item = mSortByDeptAdapter.getData().get(position);
			Intent intent = new Intent(this, ContactsDeptListActivity.class);
			intent.putExtra("dept", item);
			startActivity(intent);
		}
	}
	
	private ListView getCurrListView() {
		int index = mViewPager.getCurrentItem();
		if(index == 0) {
			return mSortByNameLv;
		} else {
			return mSortByDeptLv;
		}
	}
	
	private HashMap<String, Integer> getCurrAlphaIndexer() {
		int index = mViewPager.getCurrentItem();
		if(index == 0) {
			return mContactAlphaIndexer;
		} else {
			return mDeptAlphaIndexer;
		}
	}
	
	private Handler handler = new Handler();
	
	@Override
	public void OnAlphaChanged(String s, int index) {
		if (s != null && s.trim().length() > 0) {
			mAlphaOverlay.setText(s);
			mAlphaOverlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(mOverlayThread);
			handler.postDelayed(mOverlayThread, 700);
			if (getCurrAlphaIndexer().get(s.toLowerCase()) != null) {
				LogUtil.i(TAG, "set list view pos, s="+s);
				int position = getCurrAlphaIndexer().get(s.toLowerCase());
				getCurrListView().setSelection(position);
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
