package com.lkoa.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lkoa.R;
import com.lkoa.adapter.ProcessWorkListAdapter;
import com.lkoa.business.AttachmentManager;
import com.lkoa.business.ProcessWorkManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.Attachment;
import com.lkoa.model.BDCBTable;
import com.lkoa.model.ProcessItem;
import com.lkoa.model.BDCBTable.Body;
import com.lkoa.model.BDCBTable.Column;
import com.lkoa.model.BDCBTable.Head;
import com.lkoa.model.BDCBTable.Row;
import com.lkoa.model.ProcessContentInfo;
import com.lkoa.model.ProcessContentInfo.Field;
import com.lkoa.model.ProcessContentInfo.Field.ContentType;
import com.lkoa.model.ProcessContentInfo.Option;
import com.lkoa.util.LogUtil;
import com.lkoa.view.DateTimePickerDialog;

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
		R.string.process_work_handle_cb,
		R.string.process_work_handle_gllc_list
	};
	
	public static final int INDEX_FORMS = 0;
	public static final int INDEX_TEXT = 1;
	public static final int INDEX_ATTACHMENT = 2;
	public static final int INDEX_CB = 3;	//从表
	public static final int INDEX_GLLC_LIST = 4;	//关联流程
	
	private View [] mTabViews = new View[5];
	
	public enum ProcessWorkType {
		TYPE_MY_TODO,
		TYPE_DOING,
		TYPE_RECORD_HISTORY,
		TYPE_FILE_SPECIAL,
		TYPE_REVOCATION_BOX
	}
	
	private ProcessWorkType mWorkType = ProcessWorkType.TYPE_MY_TODO;
	
	private static final String [] TYPES_SHOW_SAVE_COMMIT = new String [] {"0", "1", "3", "7", "8", "10", "12", "13", "15"};
	
	private int mTextColorSelected;
	private int mTextColorUnselected;
	
	private ViewPager mContentPager;
	
	private String mInfoId;	//流程序号
	private String mType;
	private String mInnerType;
	
	private ProcessWorkManager mProcessWorkMgr;
	private AttachmentManager mAttachmentMgr;
	
	private LinearLayout mLinearForms, mLinearAttachments;
	private WebView mWebViewText;
	private TextView mTvNoData;
	private TextView mAttachmentCount;
	
	private LinearLayout mLinearTables;
	
	private ListView mGLLCListView;
	
	private boolean mFormsDataLoaded, mTextDataLoaded, mAttachmentLoaded, mCBLoaded, mGLLCLoaded;
	
	private ProcessContentInfo mContentInfo;
	
	private ProcessWorkListAdapter mGLLCListAdapter;	//关联流程列表适配器
	
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
	
	private boolean mShowSaveCommit = false; 
	
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
		mInnerType = intent.getStringExtra("innerType");
		
		for(String str : TYPES_SHOW_SAVE_COMMIT) {
			if(TextUtils.equals(mType, str)) {
				mShowSaveCommit = true;
			}
		}
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mTabViews[0] = findViewById(R.id.process_work_handle_forms);
		mTabViews[1] = findViewById(R.id.process_work_handle_text);
		mTabViews[2] = findViewById(R.id.process_work_handle_attachment);
		mTabViews[3] = findViewById(R.id.process_work_handle_cb);
		mTabViews[4] = findViewById(R.id.process_work_handle_gllc_list);
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
				//单选联系人
				String showContent = data.getStringExtra("showContent");
				String value = data.getStringExtra("value");
				
				Field field = mContentInfo.getSinglePeopleField();
				field.showContent = showContent;
				field.value = value;
				
			} else if(requestCode == ContactsSelectorActivity.SELECT_MODE_MULTI) {
				//多选联系人
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
		if(mShowSaveCommit) {
			mTvTitle.setText(R.string.process_work_handle_title);
			mLinearRight.setVisibility(View.VISIBLE);
			mTvRight1.setText(R.string.process_work_handle_save);
			mTvRight2.setText(R.string.process_work_handle_commit);
			mTvRight1.setOnClickListener(this);
			mTvRight2.setOnClickListener(this);
			
		}
		
		//初始化ViewPager
		//表单
		LayoutInflater inflater = LayoutInflater.from(this);
		List<View> views = new ArrayList<View>();
		View view = inflater.inflate(R.layout.process_work_handle_content_forms, null);
		mLinearForms = (LinearLayout)view.findViewById(R.id.forms_parent);
		views.add(view);
		
		//正文
		view = inflater.inflate(R.layout.process_work_handle_content_text, null);
		mWebViewText = (WebView)view.findViewById(R.id.webview);
		mTvNoData = (TextView) view.findViewById(R.id.tv_no_data);
		configWebView();
		views.add(view);
		
		//附件
		view = inflater.inflate(R.layout.process_work_handle_content_attachment, null);
		mLinearAttachments = (LinearLayout)view.findViewById(R.id.attachments);
		mAttachmentCount = (TextView)view.findViewById(R.id.attachment_count);
		views.add(view);
		
		//从表
		//TODO: 获取从表 View
		view = inflater.inflate(R.layout.process_work_handle_content_bdcb, null);
		mLinearTables = (LinearLayout)view.findViewById(R.id.linear_bdcb);
		views.add(view);
		
		//管理流程
		view = inflater.inflate(R.layout.layout_list_view, null);
		mGLLCListView = (ListView)view.findViewById(android.R.id.list);
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
		removeAllFormViews();
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
		TextView noEmpty = (TextView)view.findViewById(R.id.tv_no_empty);
		ImageView toRightArrow = (ImageView)view.findViewById(R.id.to_right_arrow);
		Spinner contentSpinner = (Spinner) view.findViewById(R.id.content_spinner);
		
		if(field.editMode == ProcessContentInfo.Field.EDIT_MODE_MUST) {
			//必填
			noEmpty.setVisibility(View.VISIBLE);
		}
		
		if(TextUtils.equals(field.id, "0")) {
			//fiedl.id为0，居中显示contentText的内容
			title.setVisibility(View.GONE);
			toRightArrow.setVisibility(View.GONE);
			contentText.setGravity(Gravity.CENTER);
		}
		
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
			contentEdit.setText(field.value);
			
		} else if(contentType == ContentType.PULLDOWNLIST) {
			//TODO: 处理弹出选择dialog
			contentText.setVisibility(View.GONE);
			contentEdit.setVisibility(View.GONE);
			contentSpinner.setVisibility(View.VISIBLE);
			String [] subs = new String[field.optionList.size()];
			Option opt = null;
			int selection = 0;
			for(int i=0; i<field.optionList.size(); i++) {
				opt = field.optionList.get(i);
				subs[i] = opt.text;
				if(TextUtils.equals(opt.value, field.value)) {
					selection = i;
				}
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, subs);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			contentSpinner.setAdapter(adapter);
			contentSpinner.setSelection(selection);
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
			
		} else if(contentType == ContentType.DATE_PICKER /*field.type == Field.DATA_TYPE_DATE*/) {
			//日期选择
			contentEdit.setVisibility(View.VISIBLE);
			contentEdit.setText(field.showContent);
			contentText.setVisibility(View.GONE);
			contentEdit.setTag(field);
			contentEdit.setOnClickListener(mDatePickerListener);
			
		} else if(contentType == ContentType.TIME_PICKER /*field.type == Field.DATA_TYPE_TIME*/) {
			//时间选择
			contentEdit.setVisibility(View.VISIBLE);
			contentEdit.setText(field.showContent);
			contentText.setVisibility(View.GONE);
			contentEdit.setTag(field);
			contentEdit.setOnClickListener(mTimePickerListener);
			
		} else if(contentType == ContentType.DATE_TIME_PICKER /*field.type == Field.DATA_TYPE_DATE_AND_TIME*/) {
			//日期时间选择
			contentEdit.setVisibility(View.VISIBLE);
			contentEdit.setText(field.showContent);
			contentText.setVisibility(View.GONE);
			contentEdit.setTag(field);
			DateTimeOnClickListener listener = new DateTimeOnClickListener();
			listener.mDateTimeET = contentEdit;
			contentEdit.setOnClickListener(listener);
			
		} else if(contentType == ContentType.MONTH_AND_YEAR_PICKER) {
			//年月选择
			contentEdit.setVisibility(View.VISIBLE);
			contentEdit.setText(field.showContent);
			contentText.setVisibility(View.GONE);
			contentEdit.setTag(field);
			contentEdit.setOnClickListener(mMonthAndYearPickerListener);
			
		} else {
			contentEdit.setVisibility(View.GONE);
			contentText.setText(field.showContent);
			contentText.setVisibility(View.VISIBLE);
		}
		view.setTag(field);
		mLinearForms.addView(view);
	}
	
	private OnClickListener mDatePickerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final Field field = (Field)v.getTag();
			//TODO: 获取年月日
			String date = field.showContent;
			String subs [] = null;
			if(!TextUtils.isEmpty(date)) {
				subs = date.split("-");
			}
			if(subs == null || subs.length != 3) {
				return;
			}
			
			DatePickerDialog datePicker = new DatePickerDialog(ProcessWorkHandleActivity.this, 
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							StringBuffer buffer = new StringBuffer();
							buffer.append(year).append("-");
							buffer.append(monthOfYear+1).append("-");
							buffer.append(dayOfMonth);
							field.showContent = buffer.toString();
							field.value = buffer.toString();
							buildBD(mContentInfo);
						}
				}, Integer.parseInt(subs[0]), Integer.parseInt(subs[1]) - 1, Integer.parseInt(subs[2]));
			datePicker.show();
		}
	};
	
	private OnClickListener mMonthAndYearPickerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final Field field = (Field)v.getTag();
			//TODO: 获取年月
			String date = field.showContent;
			String subs [] = null;
			if(!TextUtils.isEmpty(date)) {
				subs = date.split("-");
			}
			if(subs == null || subs.length != 2) {
				return;
			}
			
			DatePickerDialog datePicker = new DatePickerDialog(ProcessWorkHandleActivity.this, 
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							StringBuffer buffer = new StringBuffer();
							buffer.append(year).append("-");
							buffer.append(monthOfYear+1);
							field.showContent = buffer.toString();
							field.value = buffer.toString();
							buildBD(mContentInfo);
						}
				}, Integer.parseInt(subs[0]), Integer.parseInt(subs[1]) - 1, 1);
			datePicker.show();
		}
	};
	
	private OnClickListener mTimePickerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final Field field = (Field)v.getTag();
			//TODO: 获取年月日
			String date = field.showContent;
			String subs [] = null;
			if(!TextUtils.isEmpty(date)) {
				subs = date.split(":");
			}
			if(subs == null || subs.length != 2) {
				return;
			}
			
			TimePickerDialog datePicker = new TimePickerDialog(ProcessWorkHandleActivity.this, 
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							StringBuffer buffer = new StringBuffer();
							buffer.append(hourOfDay).append(":");
							buffer.append(minute);
							field.showContent = buffer.toString();
							field.value = buffer.toString();
							buildBD(mContentInfo);
						}
					}, Integer.parseInt(subs[0]), Integer.parseInt(subs[1]), true);
			datePicker.show();
		}
	};
	
	private void selectContacts(Field field, ContentType type) {
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
			mProcessWorkMgr.getLCBD(mType, mInfoId, mApp.getUserId(), new LKAsyncHttpResponseHandler() {
				
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
			mProcessWorkMgr.getLCZW(mInfoId, mApp.getUserId(), new LKAsyncHttpResponseHandler() {
				
				@Override
				public void successAction(Object obj) {
					LogUtil.i(TAG, obj.toString());
					String path = (String) obj;
					if(TextUtils.isEmpty(path)) {
						//TODO: 提示用户，没有正文数据
						mTvNoData.setVisibility(View.VISIBLE);
						mWebViewText.setVisibility(View.GONE);
						return;
					}
					
					mWebViewText.setVisibility(View.VISIBLE);
					mTextDataLoaded = true;
					//TODO: 加载流程正文链接文档
					String url = mAttachmentMgr.getUrl(path);
					mWebViewText.loadUrl(url);
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
			
		case INDEX_CB:
			//表单从表 
			mProcessWorkMgr.getLCBDCB(mInfoId, mApp.getUserId(), mInnerType, 
					new LKAsyncHttpResponseHandler() {
				
				@Override
				public void successAction(Object obj) {
					mCBLoaded = true;
					LogUtil.i(TAG, "obj="+obj);
					buildBDCB((ArrayList<BDCBTable>)obj);
				}
			});
			break;
			
		case INDEX_GLLC_LIST:
			//关联流程
			mProcessWorkMgr.getGLLCList(mInfoId, new LKAsyncHttpResponseHandler() {
				@Override
				public void successAction(Object obj) {
					mGLLCLoaded = true;
					LogUtil.i(TAG, "getGLLCList: obj="+obj);
					buildGLLCList((ArrayList<ProcessItem>)obj);
				}
			});
			break;

		default:
			break;
		}
	}
	
	private void buildBDCB(List<BDCBTable> list) {
		if(mLinearTables != null) mLinearTables.removeAllViews();
		
		//TODO: 构建从表
		for(BDCBTable table : list) {
			TableLayout layout = buildBDCB(table);
			mLinearTables.addView(layout);
		}
	}
	
	private void buildGLLCList(List<ProcessItem> list) {
		//TODO: 构建关联流程列表
		if(mGLLCListAdapter == null) {
			mGLLCListAdapter = new ProcessWorkListAdapter(
					ProcessWorkHandleActivity.this, 0, list);
			mGLLCListView.setAdapter(mGLLCListAdapter);
		}
	}
	
	private TableLayout buildBDCB(BDCBTable table) {
		TableLayout tableL = new TableLayout(this);

		Head head = table.head;
		Body body = table.body;
		TableRow rowHead = buildRow(head.row);
		rowHead.setBackgroundColor(Color.GRAY);
		tableL.addView(rowHead);
		
		for(Row row : body.rows) {
			TableRow rowBody = buildRow(row);
			tableL.addView(rowBody);
		}
		
		return tableL;
	}
	
	private TableRow buildRow(Row row) {
		TableRow tableRow = new TableRow(this);
		for(Column c : row.columns) {
			TextView tv = new TextView(this);
			tv.setText(c.des);
			tv.setPadding(5, 5, 5, 5);
			tableRow.addView(tv);
		}
		
		return tableRow;
	}
	
	/**
	 * @return 标志采集数据是否成功
	 */
	private boolean collectionData() {
		//TODO: 采集数据，保存到mContentInfo对象
		int count = mLinearForms.getChildCount();
		for(int i=0; i<count; i++) {
			View view = mLinearForms.getChildAt(i);
			Field field = (Field)view.getTag();
			if(!setField(field, view)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param field	字段数据	
	 * @param value	待验证的值
	 * @return	验证是否通过	true-通过	false-不通过
	 */
	private boolean checkValid(Field field, String value) {
		if(field.editMode == ProcessContentInfo.Field.EDIT_MODE_MUST
				&& TextUtils.isEmpty(value)) {
			return false;
		}
		
		return true;
	}
	
	private boolean setField(Field field, View view) {
		ContentType type = field.getContentType();
		if(type == ContentType.EDITTEXT) {
			EditText et = (EditText)view.findViewById(R.id.content_edit);
			String value = et.getText().toString();
			
			if(!checkValid(field, value)) {
				return false;
			}
			
			field.value = value;
			
		} else if(type == ContentType.TEXT_EDITTEXT) {
			EditText et = (EditText)view.findViewById(R.id.content_edit);
			String value = et.getText().toString();
			
			if(!checkValid(field, value)) {
				return false;
			}
			
			field.value = value;
			
		} else if(type == ContentType.SINGLE_PEOPLE
				|| type == ContentType.SINGLE_DEPT
				|| type == ContentType.MULTI_PEOPLE
				|| type == ContentType.MULTI_DEPT) {
			TextView text = (TextView)view.findViewById(R.id.content_text);
			field.showContent = text.getText().toString();
		}
		
		return true;
	}
	
	private void save() {
		try {
			if(!collectionData()) {
				//采集数据失败，必填项没有填
				Toast.makeText(this, "请检查必填项是否为空！", Toast.LENGTH_SHORT).show();
				return;
			}
			mProcessWorkMgr.setGLBD(mApp.getUserId(), TYPE_SAVE, 
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
		if(!collectionData()) {
			Toast.makeText(this, "请检查必填项是否为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		ProcessWorkCommitActivity.start(this, mType, mInnerType, mContentInfo);
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
	
	/**
	 * @param ctx	上下文
	 * @param userId	用户id
	 * @param infoId	
	 * @param innerType	获取表单从表时，需要的类型id
	 * @param sType		获取表单数据需要的类型id
	 */
	public static void start(Context ctx, String userId, 
			String infoId, String innerType, String sType) {
		Intent intent = new Intent(ctx, ProcessWorkHandleActivity.class);
		intent.putExtra("InfoId", infoId);
		intent.putExtra("sType", sType);
		intent.putExtra("innerType", innerType);
		ctx.startActivity(intent);
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
				
			case INDEX_CB:
				loadData = !mCBLoaded;
				break;
				
			case INDEX_GLLC_LIST:
				loadData = !mGLLCLoaded;
				break;

			default:
				break;
			}
			
			setActiveTab(index, loadData);
		}
	}
	
	private class DateTimeOnClickListener implements OnClickListener {
		public TextView mDateTimeET;

		@Override
		public void onClick(View v) {
			DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(ProcessWorkHandleActivity.this);
			dateTimePicKDialog.dateTimePicKDialog((EditText)mDateTimeET, 0);
		}
	}
	
	private void configWebView() {
		mWebViewText.setWebViewClient(new AppWebViewClients());
		mWebViewText.getSettings().setJavaScriptEnabled(true);
		mWebViewText.getSettings().setUseWideViewPort(true);
		mWebViewText.getSettings().setSupportZoom(true);
		
		mWebViewText.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype, long contentLength) {
				Uri uri = Uri.parse(url);  
	            Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
	            startActivity(intent);   
			}
		});
	}
	
	private class AppWebViewClients extends WebViewClient {

	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }

	    @Override
	    public void onPageFinished(WebView view, String url) {
	        super.onPageFinished(view, url);

	    }
	}
}
