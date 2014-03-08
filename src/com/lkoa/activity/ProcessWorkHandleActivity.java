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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.ProcessWorkManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.Attachment;
import com.lkoa.model.ProcessContentInfo;
import com.lkoa.model.ProcessContentInfo.Field;
import com.lkoa.model.ProcessContentInfo.Field.ContentType;
import com.lkoa.util.LogUtil;

/**
 * 流程办理
 */
public class ProcessWorkHandleActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = ProcessWorkHandleActivity.class.getName();
	
	public static final String KEY_PROCESS_WORK_TYPE = "key_process_work_type";
	
	public static final String TYPE_SAVE = "0";
	public static final String TYPE_COMMIT = "1";
	
	private static int [] mTabNameResIds = new int [] {
		R.string.process_work_handle_forms,
		R.string.process_work_handle_text,
		R.string.process_work_handle_attachment,
	};
	
	public static final int INDEX_FORMS = 0;
	public static final int INDEX_TEXT = 1;
	public static final int INDEX_ATTACHMENT = 2;
	
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
	
	private String mInfoId;	//流程序号
	private String mType;
	
	private ProcessWorkManager mProcessWorkMgr;
	
	private LinearLayout mLinearForms, mLinearAttachments;
	private WebView mWebViewText;
	private TextView mAttachmentCount;
	
	private boolean mFormsDataLoaded, mTextDataLoaded, mAttachmentLoaded;
	
	private ProcessContentInfo mContentInfo;
	
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
		//导航栏
		mTvTitle.setText(R.string.process_work_handle_title);
		mLinearRight.setVisibility(View.VISIBLE);
		mTvRight1.setText(R.string.process_work_handle_save);
		mTvRight2.setText(R.string.process_work_handle_commit);
		mTvRight1.setOnClickListener(this);
		mTvRight2.setOnClickListener(this);
		
		//初始化ViewPager
		LayoutInflater inflater = LayoutInflater.from(this);
		List<View> views = new ArrayList<View>();
		View view = inflater.inflate(R.layout.process_work_handle_content_forms, null);
		mLinearForms = (LinearLayout)view.findViewById(R.id.forms_parent);
		views.add(view);
		
		//正文
		mWebViewText = (WebView)inflater.inflate(R.layout.process_work_handle_content_text, null); 
		views.add(mWebViewText);
		
		//附件
		view = inflater.inflate(R.layout.process_work_handle_content_attachment, null);
		mLinearAttachments = (LinearLayout)view.findViewById(R.id.attachments);
		mAttachmentCount = (TextView)view.findViewById(R.id.attachment_count);
		views.add(view);
		mContentPager.setAdapter(new MyPagerAdapter(views));
		mContentPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		//初始化cursor
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		mCursorW = screenW / 3;
		mOffset = (screenW / 3 - mCursorW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(mOffset, 0);
		
		mFormsDataLoaded = true;
		setActiveTab(INDEX_FORMS, true);
	}
	
	/**
	 * 构建表单
	 */
	private void buildBD(ProcessContentInfo contentInfo) {
		List<Field> list = contentInfo.filedList;
		for(Field field : list) {
			//TODO: 构建表单内容
			setupFormItem(field);
		}
	}
	
	/**
	 * 构建附件页面
	 */
	private void buildAttachment(List<Attachment> list) {
		for(Attachment att : list) {
			//TODO: 构建附件页面
			View view = mLayoutInflater.inflate(R.layout.process_work_handle_content_attachment_item, null);
			TextView name = (TextView)view.findViewById(R.id.tv_attachment_name);
			name.setText(att.title);
			
			mLinearAttachments.addView(view);
		}
	}
	
	private void setupFormItem(Field field) {
		View view = mLayoutInflater.inflate(R.layout.process_work_handle_content_forms_item, null);
		TextView title = (TextView)view.findViewById(R.id.title);
		TextView contentText = (TextView)view.findViewById(R.id.content_text);
		TextView contentEdit = (TextView)view.findViewById(R.id.content_edit);
		
		title.setText(field.name);
		ContentType contentType = field.getContentType();
		if(contentType == ContentType.EDITTEXT) {
			contentEdit.setText(field.showContent);
			contentEdit.setVisibility(View.VISIBLE);
			contentText.setVisibility(View.GONE);
			
		} else if(contentType == ContentType.TEXT) {
			contentEdit.setVisibility(View.GONE);
			contentText.setText(field.showContent);
			contentText.setVisibility(View.VISIBLE);
		} else {
			//TODO: 处理弹出选择dialog
		}
		mLinearForms.addView(view);
	}
	
	private void setActiveTab(int index, boolean loadData) {
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
		
		if(loadData) {
			loadData(index);
		}
	}
	
	private void loadData(int index) {
		switch (index) {
		case INDEX_FORMS:
			//表单数据
			mProcessWorkMgr.getLCBD(mType, mInfoId, MainActivity.USER_ID, new LKAsyncHttpResponseHandler() {
				
				@Override
				public void successAction(Object obj) {
					LogUtil.i(TAG, "successAction(), obj="+obj);
					mContentInfo = (ProcessContentInfo)obj;
					buildBD(mContentInfo);
					mFormsDataLoaded = true;
				}
			});
			break;
			
		case INDEX_TEXT:
			//正文数据
			mProcessWorkMgr.getLCZW(mInfoId, MainActivity.USER_ID, new LKAsyncHttpResponseHandler() {
				
				@Override
				public void successAction(Object obj) {
					LogUtil.i(TAG, obj.toString());
				}
			});
			break;
			
		case INDEX_ATTACHMENT:
			//附件数据
			mProcessWorkMgr.getAttList(mInfoId, new LKAsyncHttpResponseHandler() {
				
				@Override
				public void successAction(Object obj) {
					LogUtil.i(TAG, obj.toString());
					List<Attachment> list = (ArrayList<Attachment>)obj;
					mAttachmentCount.setText(getResources().getString(
							R.string.process_work_handle_attachment_added, list.size()));
					buildAttachment(list);
				}
			});
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right_1:
			//保存
			try {
				mProcessWorkMgr.setGLBD(MainActivity.USER_ID, TYPE_SAVE, 
						mContentInfo.buildXml(), new LKAsyncHttpResponseHandler() {

							@Override
							public void successAction(Object obj) {
								LogUtil.i(TAG, "setGLBD(), successAction obj="+obj);
							}
					
				});
			} catch (Exception e) {
				Log.e(TAG, "setGLBD(), successAction Error: "+e);
				e.printStackTrace();
			}
			
			break;
			
		case R.id.tv_right_2:
			//提交
			break;
			
		default:
			for(int i=0; i<mTabViews.length; i++) {
				if(v == mTabViews[i]) {
					setActiveTab(i, true);
				}
			}
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
			boolean loadData = false;
			switch (index) {
			case INDEX_FORMS:
				loadData = !mFormsDataLoaded;
				break;
				
			case INDEX_TEXT:
				loadData = !mTextDataLoaded;
				break;
				
			case INDEX_ATTACHMENT:
				loadData = !mAttachmentLoaded;
				break;

			default:
				break;
			}
			
			setActiveTab(index, loadData);
		}
		
	}
}
