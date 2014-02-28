package com.lkoa.activity;

import java.util.List;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.lkoa.R;
import com.lkoa.adapter.CenterMsgNewsAdapter;
import com.lkoa.business.CenterMsgNewsManager;
import com.lkoa.model.CenterMsgNewsItem;

/**
 * 信息中心-集团新闻
 */
public class CenterMsgNewsActivity extends CenterMsgBaseActivity implements OnClickListener {
	private View mParentLatestNews, mParentMoreNews;
	private TextView mTvLatestNews, mTvMoreNews;
	private View mLineLatestNews, mLineMoreNews;
	
	private int mTextColorSelected;
	private int mTextColorUnselected;
	
	private ViewSwitcher mViewSwitcher;
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
		
		mViewSwitcher = (ViewSwitcher)findViewById(R.id.view_switcher);
		mLatestNewsLv = (ListView)findViewById(R.id.lv_latest_news);
		mMoreNewsLv = (ListView)findViewById(R.id.lv_more_news);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(R.string.center_msg_news);
		mParentLatestNews.setOnClickListener(this);
		mParentMoreNews.setOnClickListener(this);
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
	
	private void switchTo(View active) {
		if(active != getSelectedParentView()) {
			mViewSwitcher.showNext();
		}
		
		TabType type = null;
		if(active == mParentMoreNews) {
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
		
		execAsyncTask(type);
	}
	
	private View getSelectedParentView() {
		if(mLineMoreNews.getVisibility() == View.VISIBLE) {
			return mParentMoreNews;
		} else {
			return mParentLatestNews;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vg_latest_news:
			//最新消息
			switchTo(mParentLatestNews);
			break;
			
		case R.id.vg_more_news:
			//更多消息
			switchTo(mParentMoreNews);
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
}
