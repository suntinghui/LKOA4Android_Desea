package com.lkoa.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.AttachmentManager;
import com.lkoa.business.ProcessWorkManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.Attachment;
import com.lkoa.model.ProcessContentInfo;
import com.lkoa.model.ProcessContentInfo.Field;
import com.lkoa.model.ProcessContentInfo.Field.ContentType;
import com.lkoa.model.ProcessContentInfo.Option;
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
	
	private String mInfoId;	//流程序号
	private String mType;
	
	private ProcessWorkManager mProcessWorkMgr;
	private AttachmentManager mAttachmentMgr;
	
	private LinearLayout mLinearForms, mLinearAttachments;
	private WebView mWebViewText;
	private TextView mAttachmentCount;
	
	private boolean mFormsDataLoaded, mTextDataLoaded, mAttachmentLoaded;
	
	private ProcessContentInfo mContentInfo;
	
	private OnItemSelectedListener mSpinnerOnItemClickListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view, int pos,
				long arg3) {
			Field field = (Field)adapterView.getTag();
			Option opt = field.optionList.get(pos);
			field.name = opt.text;
			field.value = opt.value;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_work_handle);
		
		mProcessWorkMgr = new ProcessWorkManager();
		mAttachmentMgr = new AttachmentManager();
		
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			if(requestCode == ContactsSelectorActivity.SELECT_MODE_SINGLE) {
				String showContent = data.getStringExtra("showContent");
				String value = data.getStringExtra("value");
				
				Field field = mContentInfo.getSinglePeopleField();
				field.showContent = showContent;
				field.value = value;
				
			} else if(requestCode == ContactsSelectorActivity.SELECT_MODE_MULTI) {
				String showContent = data.getStringExtra("showContent");
				String value = data.getStringExtra("value");
				
				Field field = mContentInfo.getMultiPeopleField();
				field.showContent = showContent;
				field.value = value;
				
			} else if(requestCode == DepartmentSelectorActivity.SELECT_MODE_SINGLE) {
				//单选部门
				String showContent = data.getStringExtra("showContent");
				String value = data.getStringExtra("value");
				
				Field field = mContentInfo.getSingleDeptField();
				field.showContent = showContent;
				field.value = value;
				
			} else if(requestCode == DepartmentSelectorActivity.SELECT_MODE_MULTI) {
				//多选部门
				String showContent = data.getStringExtra("showContent");
				String value = data.getStringExtra("value");
				
				Field field = mContentInfo.getMultiDeptField();
				field.showContent = showContent;
				field.value = value;
			}
			
			removeAllFormViews();
			buildBD(mContentInfo);
		}
	}
	
	private void removeAllFormViews() {
		if(mLinearForms != null) mLinearForms.removeAllViews();
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
		//表单
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
		if(mLinearAttachments != null) {
			mLinearAttachments.removeAllViews();
		}
		
		for(Attachment att : list) {
			//TODO: 构建附件页面
			View view = mLayoutInflater.inflate(R.layout.process_work_handle_content_attachment_item, null);
			TextView name = (TextView)view.findViewById(R.id.tv_attachment_name);
			name.setText(att.title);
			final String attId = att.id;
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AttachmentShowActivity.showAttachment(
							ProcessWorkHandleActivity.this, attId);
				}
			});
			
			mLinearAttachments.addView(view);
		}
	}
	
	private void setupFormItem(final Field field) {
		View view = mLayoutInflater.inflate(R.layout.process_work_handle_content_forms_item, null);
		TextView title = (TextView)view.findViewById(R.id.title);
		TextView contentText = (TextView)view.findViewById(R.id.content_text);
		TextView contentEdit = (TextView)view.findViewById(R.id.content_edit);
		ImageView toRightArrow = (ImageView)view.findViewById(R.id.to_right_arrow);
		Spinner contentSpinner = (Spinner) view.findViewById(R.id.content_spinner);
		
		title.setText(field.name);
		ContentType contentType = field.getContentType();
		if(contentType == ContentType.EDITTEXT) {
			contentEdit.setText(field.showContent);
			contentEdit.setVisibility(View.VISIBLE);
			contentText.setVisibility(View.GONE);
			
		} else if(contentType == ContentType.TEXT_EDITTEXT) {
			//先显示showContent的值，再在下面显示文本框让用户输入
			contentText.setText(field.showContent);
			contentText.setVisibility(View.VISIBLE);
			contentEdit.setVisibility(View.VISIBLE);
			
		} else if(contentType == ContentType.PULLDOWNLIST) {
			//TODO: 处理弹出选择dialog
			contentText.setVisibility(View.GONE);
			contentEdit.setVisibility(View.GONE);
			contentSpinner.setVisibility(View.VISIBLE);
			String [] subs = new String[field.optionList.size()];
			Option opt = null;
			for(int i=0; i<field.optionList.size(); i++) {
				opt = field.optionList.get(i);
				subs[i] = opt.text;
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, subs);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			contentSpinner.setAdapter(adapter);
			contentSpinner.setOnItemSelectedListener(mSpinnerOnItemClickListener);
			contentSpinner.setTag(field);
			
		} else if(contentType == ContentType.SINGLE_PEOPLE 
				|| contentType == ContentType.MULTI_PEOPLE) {
			//TODO: 跳转到联系人选择
			contentEdit.setVisibility(View.GONE);
			toRightArrow.setVisibility(View.VISIBLE);
			contentText.setText(field.showContent);
			final ContentType type = contentType;
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					selectContacts(field, type);
				}
			});
			
		} else if(contentType == ContentType.SINGLE_DEPT
				|| contentType == ContentType.MULTI_DEPT) {
			//TODO: 跳转到部门选择
			contentEdit.setVisibility(View.GONE);
			toRightArrow.setVisibility(View.VISIBLE);
			contentText.setText(field.showContent);
			final ContentType type = contentType;
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					selectDepts(field, type);
				}
			});
			
		} else {
			contentEdit.setVisibility(View.GONE);
			contentText.setText(field.showContent);
			contentText.setVisibility(View.VISIBLE);
		}
		view.setTag(field);
		mLinearForms.addView(view);
	}
	
	private void selectContacts(Field field, ContentType type) {
		Intent intent = new Intent(
				ProcessWorkHandleActivity.this, 
				ContactsSelectorActivity.class);
		int mode = -1;
		if(type == ContentType.SINGLE_PEOPLE) {
			ContactsSelectorActivity.startForResult(this, 
					ContactsSelectorActivity.SELECT_MODE_SINGLE,
					field.value);
		} else {
			ContactsSelectorActivity.startForResult(this, 
					ContactsSelectorActivity.SELECT_MODE_MULTI,
					field.value);
		}
	}
	
	private void selectDepts(Field field, ContentType type) {
		Intent intent = new Intent(
				ProcessWorkHandleActivity.this, 
				DepartmentSelectorActivity.class);
		int mode = -1;
		if(type == ContentType.SINGLE_DEPT) {
			mode = DepartmentSelectorActivity.SELECT_MODE_SINGLE;
			intent.putExtra(
					DepartmentSelectorActivity.KEY_SELECT_MODE, 
					mode);
			intent.putExtra(DepartmentSelectorActivity.KEY_SELECTED_DEPT, 
					field.value);
		} else {
			mode = DepartmentSelectorActivity.SELECT_MODE_MULTI;
			intent.putExtra(
					DepartmentSelectorActivity.KEY_SELECT_MODE, 
					mode);
			intent.putExtra(DepartmentSelectorActivity.KEY_SELECTED_DEPT, 
					field.value);
		}
		startActivityForResult(intent, mode);
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
					mTextDataLoaded = true;
				}
			});
			break;
			
		case INDEX_ATTACHMENT:
			//附件数据
			mAttachmentMgr.getAttList(mInfoId, new LKAsyncHttpResponseHandler() {
				
				@Override
				public void successAction(Object obj) {
					LogUtil.i(TAG, obj.toString());
					mAttachmentLoaded = true;
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
	
	private void collectionData() {
		//TODO: 采集数据，保存到mContentInfo对象
		int count = mLinearForms.getChildCount();
		for(int i=0; i<count; i++) {
			View view = mLinearForms.getChildAt(i);
			Field field = (Field)view.getTag();
			setField(field, view);
		}
	}
	
	private void setField(Field field, View view) {
		ContentType type = field.getContentType();
		if(type == ContentType.EDITTEXT) {
			EditText et = (EditText)view.findViewById(R.id.content_edit);
//			field.showContent = et.getText().toString();
			field.value = et.getText().toString();
			
		} else if(type == ContentType.TEXT_EDITTEXT) {
			EditText et = (EditText)view.findViewById(R.id.content_edit);
			field.value = et.getText().toString();
			
		} else if(type == ContentType.SINGLE_PEOPLE
				|| type == ContentType.SINGLE_DEPT
				|| type == ContentType.MULTI_PEOPLE
				|| type == ContentType.MULTI_DEPT) {
			TextView text = (TextView)view.findViewById(R.id.content_text);
			field.showContent = text.getText().toString();
		}
	}
	
	private void save() {
		try {
			collectionData();
			mProcessWorkMgr.setGLBD(MainActivity.USER_ID, TYPE_SAVE, 
					mContentInfo.buildXml(false), new LKAsyncHttpResponseHandler() {

						@Override
						public void successAction(Object obj) {
							LogUtil.i(TAG, "setGLBD(), successAction obj="+obj);
							if(obj != null) {
								showDialog(MODAL_DIALOG, "保存表单成功!");
							}
						}
				
			});
		} catch (Exception e) {
			Log.e(TAG, "setGLBD(), successAction Error: "+e);
			e.printStackTrace();
		}
	}
	
	private void commit() {
		Intent intent = new Intent(this, ProcessWorkCommitActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("processInfo", mContentInfo);
		intent.putExtra("bundle", bundle);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right_1:
			//保存
			save();
			break;
			
		case R.id.tv_right_2:
			//提交
			commit();
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
