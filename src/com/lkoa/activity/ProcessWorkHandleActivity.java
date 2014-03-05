package com.lkoa.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.ProcessWorkManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.util.LogUtil;

/**
 * 流程办理
 */
public class ProcessWorkHandleActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = ProcessWorkHandleActivity.class.getName();
	
	public static final String KEY_PROCESS_WORK_TYPE = "key_process_work_type";
	
	private static int [] mTabNameResIds = new int [] {
		R.string.process_work_handle_forms,
		R.string.process_work_handle_text,
		R.string.process_work_handle_attachment,
	};
	
	private View [] mTabViews = new View[3];
	
	public enum ProcessWorkType {
		TYPE_MY_TODO,
		TYPE_DOING,
		TYPE_RECORD_HISTORY,
		TYPE_FILE_SPECIAL,
		TYPE_REVOCATION_BOX
	}
	
	private ProcessWorkType mWorkType = ProcessWorkType.TYPE_MY_TODO;
	
	private int mTextColorSelected;
	private int mTextColorUnselected;
	
	private ViewPager mContentPager;
	private int mCursorW, mOffset;
	
	private String mInfoId, mType;
	
	private ProcessWorkManager mProcessWorkMgr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_work_handle);
		
		mProcessWorkMgr = new ProcessWorkManager();
		
		Resources res = getResources();
		mTextColorSelected = res.getColor(R.color.center_msg_news_tab_text_selected);
		mTextColorUnselected = res.getColor(R.color.center_msg_news_tab_text_unselected);
		
		Intent intent = getIntent();
		mInfoId = intent.getStringExtra("InfoId");
		mType = intent.getStringExtra("sType");
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mTabViews[0] = findViewById(R.id.process_work_handle_forms);
		mTabViews[1] = findViewById(R.id.process_work_handle_text);
		mTabViews[2] = findViewById(R.id.process_work_handle_attachment);
		for(int i=0; i<mTabViews.length; i++) {
			TextView name = (TextView)mTabViews[i].findViewById(R.id.tv_tab_name);
			name.setText(mTabNameResIds[i]);
			mTabViews[i].setOnClickListener(this);
		}
		
		mContentPager = (ViewPager) findViewById(R.id.process_work_handle_content);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		mTvTitle.setText(R.string.process_work_handle_title);
		
		//初始化ViewPager
		LayoutInflater inflater = LayoutInflater.from(this);
		List<View> views = new ArrayList<View>();
		views.add(inflater.inflate(R.layout.process_work_handle_content_forms, null));
		views.add(inflater.inflate(R.layout.process_work_handle_content_text, null));
		views.add(inflater.inflate(R.layout.process_work_handle_content_attachment, null));
		mContentPager.setAdapter(new MyPagerAdapter(views));
		mContentPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		mProcessWorkMgr.getLCBD(mType, mInfoId, MainActivity.USER_ID, new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), obj="+obj);
			}
		});
		
		//初始化cursor
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		mCursorW = screenW / 3;
		mOffset = (screenW / 3 - mCursorW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(mOffset, 0);
		
		setActiveTab(0);
	}
	
	private void setActiveTab(int index) {
		for(int i=0; i<mTabViews.length; i++) {
			TextView name = (TextView)mTabViews[i].findViewById(R.id.tv_tab_name);
			View line = mTabViews[i].findViewById(R.id.v_line_selected);
			if(i == index) {
				line.setVisibility(View.VISIBLE);
				name.setTextColor(mTextColorSelected);
			} else {
				line.setVisibility(View.GONE);
				name.setTextColor(mTextColorUnselected);
			}
		}
		
		mContentPager.setCurrentItem(index);
	}

	@Override
	public void onClick(View v) {
		for(int i=0; i<mTabViews.length; i++) {
			if(v == mTabViews[i]) {
				setActiveTab(i);
			}
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
			if(view instanceof WebView) {
				((WebView)view).loadUrl("www.baidu.com"); 
			}
			
			mContentPager.addView(view, 0);
			return mViews.get(arg1);
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			mContentPager.removeView(mViews.get(position));
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
			Log.i(TAG, "onPageSelected(), index="+index);
			setActiveTab(index);
		}
		
	}
}
