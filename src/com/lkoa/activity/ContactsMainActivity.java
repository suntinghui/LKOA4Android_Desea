package com.lkoa.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.adapter.CenterMsgNewsAdapter;
import com.lkoa.adapter.ContactsAdapter;
import com.lkoa.business.CenterMsgManager;
import com.lkoa.business.ContactsManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.CenterMsgNewsItem;
import com.lkoa.model.ContactItem;
import com.lkoa.util.LogUtil;
import com.lkoa.util.Pinyin4j;

/**
 * 通讯录-首页
 */
public class ContactsMainActivity extends CenterMsgBaseActivity 
	implements OnClickListener, OnItemClickListener {
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
	private ContactsAdapter mSortByNameAdapter, mSortByDeptAdapter;
	
	private boolean mSortByNameLoaded, mSortByDeptLoaded;
	
	private ContactsManager mContactsMgr;
	
	private String mId;

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
		mSortByNameLv = (ListView)mLayoutInflater.inflate(R.layout.layout_list_view, null);
		mSortByDeptLv = (ListView)mLayoutInflater.inflate(R.layout.layout_list_view, null);
		mSortByNameLv.setOnItemClickListener(this);
		mSortByDeptLv.setOnItemClickListener(this);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(R.string.contacts_title);
		
		mParentLatestNews.setOnClickListener(this);
		mParentMoreNews.setOnClickListener(this);
		
		List<View> list = new ArrayList<View>();
		list.add(mSortByNameLv);
		list.add(mSortByDeptLv);
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
		}
	}
	
	private LKAsyncHttpResponseHandler getResponseHandler(final int index) {
		return new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), " + obj.toString());
				
				if(index == INDEX_SORT_BY_NAME) {
					//按名称排序
					List<ContactItem> list = (ArrayList<ContactItem>)obj;
					initAlphaU(list);
					mSortByNameLoaded = true;
					if(mSortByNameAdapter == null) {
						mSortByNameAdapter = new ContactsAdapter(ContactsMainActivity.this, 0, list);
						mSortByNameLv.setAdapter(mSortByNameAdapter);
					}
					
				} else {
					//按部门排序
					mSortByDeptLoaded = true;
				}
			}
		};
	}
	
	private void initAlphaU(List<ContactItem> list) {
		for(ContactItem item : list) {
			item.alphaU = String.valueOf(Pinyin4j.getHanyuPinyin(item.userName).charAt(0));
		}
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
	}
	
}
