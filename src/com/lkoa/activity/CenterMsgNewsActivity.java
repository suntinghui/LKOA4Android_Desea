package com.lkoa.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.adapter.CenterMsgNewsAdapter;
import com.lkoa.business.CenterMsgNewsManager;
import com.lkoa.model.CenterMsgNewsItem;

/**
 * 信息中心-集团新闻
 */
public class CenterMsgNewsActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final int INDEX_LATEST_NEWS = 0;
	private static final int INDEX_MORE_NEWS = 1;
	
	private View mParentLatestNews, mParentMoreNews;
	private TextView mTvLatestNews, mTvMoreNews;
	private View mLineLatestNews, mLineMoreNews;
	
	private int mTextColorSelected;
	private int mTextColorUnselected;
	
	private ViewPager mViewPager;
	private ListView mLatestNewsLv, mMoreNewsLv;
	private CenterMsgNewsAdapter mLatestNewsAdapter, mMoreNewsAdapter;
	
	private CenterMsgNewsManager mNewsMgr;
	
	private NewsAsyncTask mNewsTask;
	
	enum TabType {
		TabLatestNews, TabMoreNews
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center_msg_news);
		
		mNewsMgr = new CenterMsgNewsManager();
		
		Resources res = getResources();
		mTextColorSelected = res.getColor(R.color.center_msg_news_tab_text_selected);
		mTextColorUnselected = res.getColor(R.color.center_msg_news_tab_text_unselected);
		
		findViews();
		setupViews();
		execAsyncTask(TabType.TabLatestNews);
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
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(R.string.center_msg_news);
		mParentLatestNews.setOnClickListener(this);
		mParentMoreNews.setOnClickListener(this);
		
		List<View> list = new ArrayList<View>();
		list.add(mLatestNewsLv);
		list.add(mMoreNewsLv);
		mViewPager.setAdapter(new MyPagerAdapter(list));
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	private void execAsyncTask(TabType type) {
		if(mNewsTask == null || mNewsTask.getStatus() == AsyncTask.Status.FINISHED) {
			mNewsTask = new NewsAsyncTask(type);
			mNewsTask.execute();
		} else if(mNewsTask.getStatus() == AsyncTask.Status.RUNNING 
				&& type != mNewsTask.getType()) {
			mNewsTask.cancel(true);
			mNewsTask = new NewsAsyncTask(type);
			mNewsTask.execute();
		}
	}
	
	private void switchTo(int activeIdx) {
		TabType type = null;
		if(activeIdx == INDEX_MORE_NEWS) {
			mTvLatestNews.setTextColor(mTextColorUnselected);
			mTvMoreNews.setTextColor(mTextColorSelected);
			mLineMoreNews.setVisibility(View.VISIBLE);
			mLineLatestNews.setVisibility(View.GONE);
			type = TabType.TabMoreNews;
			
		} else {
			mTvLatestNews.setTextColor(mTextColorSelected);
			mTvMoreNews.setTextColor(mTextColorUnselected);
			mLineLatestNews.setVisibility(View.VISIBLE);
			mLineMoreNews.setVisibility(View.GONE);
			type = TabType.TabLatestNews;
		}
		
		mViewPager.setCurrentItem(activeIdx);
		execAsyncTask(type);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vg_latest_news:
			//最新消息
			switchTo(INDEX_LATEST_NEWS);
			break;
			
		case R.id.vg_more_news:
			//更多消息
			switchTo(INDEX_MORE_NEWS);
			break;

		default:
			break;
		}
	}
	
	private class NewsAsyncTask extends AsyncTask<Void, Void, List<CenterMsgNewsItem>> {
		private TabType type;
		
		NewsAsyncTask(TabType type) {
			this.type = type;
		}

		@Override
		protected List<CenterMsgNewsItem> doInBackground(Void... params) {
			if(this.type == TabType.TabLatestNews) {
				//最新消息
				return mNewsMgr.getLatestNewsData();
			} else {
				//更多消息
				return mNewsMgr.getMoreNewsData();
			}
		}
		
		@Override
		protected void onPostExecute(List<CenterMsgNewsItem> result) {
			super.onPostExecute(result);
			if(this.type == TabType.TabLatestNews) {
				//最新消息
				if(mLatestNewsAdapter == null) {
					mLatestNewsAdapter = new CenterMsgNewsAdapter(
							CenterMsgNewsActivity.this, 0, result);
					mLatestNewsLv.setAdapter(mLatestNewsAdapter);
				} else {
					mLatestNewsAdapter.setData(result);
				}
				mLatestNewsAdapter.notifyDataSetChanged();
				
			} else {
				//更多消息
				if(mMoreNewsAdapter == null) {
					mMoreNewsAdapter = new CenterMsgNewsAdapter(
							CenterMsgNewsActivity.this, 0, result);
					mMoreNewsLv.setAdapter(mMoreNewsAdapter);
				} else {
					mMoreNewsAdapter.setData(result);
				}
				mMoreNewsAdapter.notifyDataSetChanged();
			}
		}
		
		public TabType getType() {
			return this.type;
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
	
}
