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
import com.lkoa.business.CenterMsgManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.CenterMsgNewsItem;
import com.lkoa.util.LogUtil;

/**
 * 信息中心-集团新闻
 */
public class CenterMsgNewsActivity extends CenterMsgBaseActivity 
	implements OnClickListener, OnItemClickListener {
	private static final String TAG = "CenterMsgNewsActivity";
	
	private static final int INDEX_LATEST_NEWS = 0;
	private static final int INDEX_MORE_NEWS = 1;
	
	private static final String NEWS_UNREAD = "0";
	private static final String NEWS_READED = "1";
	
	public static final int LIST_TYPE_NEWS = 0;
	public static final int LIST_TYPE_PUBLIC = 1;
	public static final int LIST_TYPE_NOTICE = 2;
	public static final int LIST_TYPE_WIN_DEPARTMENT = 3;
	
	private int mListType = LIST_TYPE_NEWS;
	
	private View mParentLatestNews, mParentMoreNews;
	private TextView mTvLatestNews, mTvMoreNews;
	private View mLineLatestNews, mLineMoreNews;
	
	private int mTextColorSelected;
	private int mTextColorUnselected;
	
	private ViewPager mViewPager;
	private ListView mLatestNewsLv, mMoreNewsLv;
	private CenterMsgNewsAdapter mLatestNewsAdapter, mMoreNewsAdapter;
	
	private CenterMsgManager mNewsMgr;
	private boolean mLatestNewsLoaded, mMoreNewsLoaded;
	
	enum TabType {
		TabLatestNews, TabMoreNews
	}
	
	private String mId;
	private int mTitleResId;
	
	private String mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center_msg_news);
		
		Intent intent = getIntent();
		mTitleResId = intent.getIntExtra("titleResId", -1);
		mId = intent.getStringExtra("sId");
		mListType = intent.getIntExtra("listType", LIST_TYPE_NEWS);
		mTitle = intent.getStringExtra("title");
		
		mNewsMgr = new CenterMsgManager();
		
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
		mLatestNewsLv = (ListView)mLayoutInflater.inflate(R.layout.layout_list_view, null);
		mMoreNewsLv = (ListView)mLayoutInflater.inflate(R.layout.layout_list_view, null);
		mLatestNewsLv.setOnItemClickListener(this);
		mMoreNewsLv.setOnItemClickListener(this);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		if(mTitleResId == -1) {
			mTvTitle.setText(mTitle);
		} else {
			mTvTitle.setText(mTitleResId);
		}
		
		mParentLatestNews.setOnClickListener(this);
		mParentMoreNews.setOnClickListener(this);
		
		List<View> list = new ArrayList<View>();
		list.add(mLatestNewsLv);
		list.add(mMoreNewsLv);
		mViewPager.setAdapter(new MyPagerAdapter(list));
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		//获取数据
		loadNewsList(NEWS_UNREAD);
	}
	
	private void switchTo(int activeIdx) {
		if(activeIdx == INDEX_MORE_NEWS) {
			mTvLatestNews.setTextColor(mTextColorUnselected);
			mTvMoreNews.setTextColor(mTextColorSelected);
			mLineMoreNews.setVisibility(View.VISIBLE);
			mLineLatestNews.setVisibility(View.GONE);
			if(mMoreNewsLoaded == false) {
				loadNewsList(NEWS_READED);
			}
			
		} else {
			mTvLatestNews.setTextColor(mTextColorSelected);
			mTvMoreNews.setTextColor(mTextColorUnselected);
			mLineLatestNews.setVisibility(View.VISIBLE);
			mLineMoreNews.setVisibility(View.GONE);
			if(mLatestNewsLoaded == false) {
				loadNewsList(NEWS_UNREAD);
			}
		}
		mViewPager.setCurrentItem(activeIdx);
	}
	
	private void loadNewsList(String state) {
		final String finalState = state;
		//获取数据
		if(mListType == LIST_TYPE_NOTICE) {
			mNewsMgr.getTZList(finalState, mApp.getUserId(), getResponseHandler(state));
		} else {
			mNewsMgr.getXXList(finalState, mId, mApp.getUserId(), getResponseHandler(state));
		}
	}
	
	private LKAsyncHttpResponseHandler getResponseHandler(final String state) {
		return new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), " + obj.toString());
				ArrayList<CenterMsgNewsItem> list = (ArrayList<CenterMsgNewsItem>)obj;
				
				if(TextUtils.equals(state, "0")) {
					//最新消息
					mLatestNewsLoaded = true;
					if(mLatestNewsAdapter == null) {
						mLatestNewsAdapter = new CenterMsgNewsAdapter(
								CenterMsgNewsActivity.this, 0, list);
						mLatestNewsLv.setAdapter(mLatestNewsAdapter);
					} else {
						mLatestNewsAdapter.setData(list);
					}
					setupAdapter();
					mLatestNewsAdapter.notifyDataSetChanged();
					
				} else {
					//更多消息
					mMoreNewsLoaded = true;
					if(mMoreNewsAdapter == null) {
						mMoreNewsAdapter = new CenterMsgNewsAdapter(
								CenterMsgNewsActivity.this, 0, list);
						mMoreNewsLv.setAdapter(mMoreNewsAdapter);
					} else {
						mMoreNewsAdapter.setData(list);
					}
					setupAdapter();
					mMoreNewsAdapter.notifyDataSetChanged();
				}
			}
		};
	}
	
	private void setupAdapter() {
		if(mListType == LIST_TYPE_PUBLIC 
				|| mListType == LIST_TYPE_WIN_DEPARTMENT) {
			//集团公告和部门之窗不显示图标
			if(mLatestNewsAdapter != null) mLatestNewsAdapter.setShowIconFlag(false);
			if(mMoreNewsAdapter != null) mMoreNewsAdapter.setShowIconFlag(false);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vg_latest_news:
			//最新消息
			switchTo(INDEX_LATEST_NEWS);
			loadNewsList(NEWS_UNREAD);
			
			break;
			
		case R.id.vg_more_news:
			//更多消息
			switchTo(INDEX_MORE_NEWS);
			loadNewsList(NEWS_READED);
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
		CenterMsgNewsItem item = null;
		if(adapterView == mLatestNewsLv) {
			item = mLatestNewsAdapter.getData().get(position);
		} else if(adapterView == mMoreNewsLv) {
			item = mMoreNewsAdapter.getData().get(position);
		}
		
		if(item != null) {
			String id = item.id;
			Intent intent = new Intent(this, CenterMsgContentActivity.class);
			intent.putExtra("sId", id);
			intent.putExtra("titleResId", R.string.center_msg_news);
			intent.putExtra("listType", mListType);
			startActivity(intent);
		}
	}
	
}
