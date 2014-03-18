package com.lkoa.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.lkoa.R;
import com.lkoa.adapter.ProcessWorkSpinnerAdapter;
import com.lkoa.business.ProcessWorkManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.Attachment;
import com.lkoa.model.ProcessContentInfo;
import com.lkoa.model.ProcessContentInfo.Activity;
import com.lkoa.model.ProcessContentInfo.Field;
import com.lkoa.model.ProcessContentInfo.Field.ContentType;
import com.lkoa.model.ProcessContentInfo.Option;
import com.lkoa.model.ProcessContentInfo.User;
import com.lkoa.util.LogUtil;

/**
 * 流程办理-操作提交
 */
public class ProcessWorkCommitActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = ProcessWorkCommitActivity.class.getName();
	
	public static final String KEY_PROCESS_WORK_TYPE = "key_process_work_type";
	
	public static final String TYPE_SAVE = "0";
	public static final String TYPE_COMMIT = "1";
	
	private ProcessWorkManager mProcessWorkMgr;
	
	private LinearLayout mLinearContent;
	
	private ProcessContentInfo mContentInfo;
	
	private View mNodeNext, mNodeDealtime, mNodeMode, mNodeZbr, mNodeCyr;
	private Activity mCurrActivity;	//当前节点
	
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
		setContentView(R.layout.activity_process_work_commit_operator);
		
		mProcessWorkMgr = new ProcessWorkManager();
		
		Resources res = getResources();
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		mContentInfo = (ProcessContentInfo)bundle.getSerializable("processInfo");
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mNodeNext = findViewById(R.id.node_next);
		mNodeDealtime = findViewById(R.id.node_dealtime);
		mNodeMode = findViewById(R.id.node_mode);
		mNodeZbr = findViewById(R.id.node_zbr);
		mNodeCyr = findViewById(R.id.node_cyr);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		//导航栏
		mTvTitle.setText(R.string.process_work_commit_title);
		mLinearRight.setVisibility(View.VISIBLE);
		mTvRight1.setVisibility(View.GONE);
		mTvRight2.setText(R.string.process_work_handle_commit);
		mTvRight2.setOnClickListener(this);
		mNodeNext.setOnClickListener(this);
		mNodeZbr.setOnClickListener(this);
		mNodeCyr.setOnClickListener(this);
		
		buildPageWarper();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK) {
			if(requestCode == 8) {
				ProcessContentInfo info = (ProcessContentInfo)data.getSerializableExtra("bundle");
				if(info != null) {
					mContentInfo = info;
					buildPageWarper();
				}
			} else {
				int mode = data.getIntExtra(ContactsSelectorActivity.RESULT_KEY_MODE, 
						ContactsSelectorActivity.SELECT_MODE_SINGLE);
				String showContent = data.getStringExtra(
						ContactsSelectorActivity.RESULT_KEY_SHOWCONTENT);
				String userId = data.getStringExtra(
						ContactsSelectorActivity.RESULT_KEY_VALUE);
				
				if(mode == ContactsSelectorActivity.SELECT_MODE_SINGLE) {
					if(mCurrActivity.zbr == null) {
						mCurrActivity.zbr = mContentInfo.newUser();
					}
					User user = mCurrActivity.zbr;
					user.userId = userId;
					user.userName = showContent;
					
				} else {
					List<User> list = mCurrActivity.cyrs;
					User user = null;
					String [] userIds = userId.split(",");
					String [] userNames = showContent.split(",");
					for(int i=0; i<userIds.length; i++) {
						user = mContentInfo.newUser();
						user.userId = userIds[i];
						user.userName = userNames[i];
						list.add(user);
					}
				}
				buildPageWarper();
			}
		}
	}
	
	private void buildPageWarper() {
		List<Activity> list = mContentInfo.activityList;
		boolean flag = false;
		for(Activity a : list) {
			if(a.select == 1) {
				mCurrActivity = a;
				buildPage(a);
				flag = true;
			}
		}
		if(!flag && list.size() > 0) {
			mCurrActivity = list.get(0);
			buildPage(list.get(0));
		}
	}
	
	private void buildPage(final Activity activity) {
		switch (activity.mode) {
		case 0:
			//主办人模式：主办人必须有，相关办理人可以没有。
			break;
			
		case 1:
			//会签人模式：主办人的选择要隐去，相关办理人必须有，且可以多选
			mNodeZbr.setVisibility(View.GONE);
			break;
			
		case 2:
			//单人签发模式：主办人的选择要隐去，相关办理人必须有，且可以多选
			mNodeZbr.setVisibility(View.GONE);
			break;

		default:
			break;
		}
		
		setItem(mNodeNext, R.string.process_work_node_next, activity.name);
		setItem(mNodeDealtime, R.string.process_work_node_dealtime, activity.dealTime);
		setItem(mNodeZbr, R.string.process_work_node_zbr, activity.zbr.userName);
		
		setItem(mNodeMode, R.string.process_work_node_mode, getModeName(activity.mode));
		setItem(mNodeCyr, R.string.process_work_node_cyr, getCyrs(activity.cyrs));
	}
	
	private void setItem(View view, int titleResId, String value) {
		TextView title = (TextView)view.findViewById(R.id.title);
		TextView text = (TextView)view.findViewById(R.id.content_text);
		EditText edit = (EditText)view.findViewById(R.id.content_edit);
		if(view == mNodeNext || view == mNodeCyr || view == mNodeZbr) {
			ImageView toRightArrow = (ImageView)view.findViewById(R.id.to_right_arrow);
			toRightArrow.setVisibility(View.VISIBLE);
		}
		
		title.setText(titleResId);
		text.setText(value);
	}
	
	private String getModeName(int mode) {
		Resources res = getResources();
		if(mode == 0) {
			return res.getString(R.string.process_work_node_mode_zbr);
		} else if(mode == 1) {
			return res.getString(R.string.process_work_node_mode_hqr);
		} else {
			return res.getString(R.string.process_work_node_mode_dr);
		}
	}
	
	private String getCyrs(List<User> list) {
		StringBuilder builder = new StringBuilder();
		User u = null;
		for(int i=0; i<list.size(); i++) {
			u = list.get(i);
			if(i != list.size()-1) {
				builder.append(u.toString()).append(",");
			} else {
				builder.append(u.toString());
			}
		}
		return builder.toString();
	}
	
	private boolean checkValid() {
		EditText edit = (EditText)mNodeDealtime.findViewById(R.id.content_edit);
		String dealTime = edit.getText().toString();
		if(TextUtils.isEmpty(dealTime)) return false;
		try {
			Float.parseFloat(dealTime);
		} catch(Exception e) {
			return false;
		}
		
		switch (mCurrActivity.mode) {
		case 0:
			//主办人模式：主办人必须有，相关办理人可以没有。
			User zbr = mCurrActivity.zbr;
			if(zbr == null || TextUtils.isEmpty(zbr.userId)) return false;
			break;
			
		case 1:
		case 2:
			//会签人模式：主办人的选择要隐去，相关办理人必须有，且可以多选
			//单人签发模式：主办人的选择要隐去，相关办理人必须有，且可以多选
			mNodeZbr.setVisibility(View.GONE);
			List<User> list = mCurrActivity.cyrs;
			if(list.size() < 1) return false;
			break;

		default:
			break;
		}
		return true;
	}
	
	private void commit() {
		if(!checkValid()) {
			Toast.makeText(this, "请检查输入是否正确！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		mCurrActivity.select = 1;
		try {
			mProcessWorkMgr.setGLBD(MainActivity.USER_ID, TYPE_COMMIT, 
					mContentInfo.buildXml(true), new LKAsyncHttpResponseHandler() {
				
				@Override
				public void successAction(Object obj) {
					LogUtil.i(TAG, "setGLBD(), successAction obj="+obj);
					if(obj != null) {
						showDialog(MODAL_DIALOG, "提交表单成功!");
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right_2:
			//提交
			commit();
			break;
			
		case R.id.node_next:
			//下级节点选择
			Intent intent = new Intent(ProcessWorkCommitActivity.this, ProcessWorkNodeSelectActivity.class);
			intent.putExtra("bundle", mContentInfo);
			startActivityForResult(intent, 8);
			break;
			
		case R.id.node_zbr:
			//主办人选择
			ContactsSelectorActivity.startForResult(this,
					ContactsSelectorActivity.SELECT_MODE_SINGLE, 
					mCurrActivity.zbr.userId);
			break;
			
		case R.id.node_cyr:
			//参与人选择
			StringBuilder builder = new StringBuilder();
			int size = mCurrActivity.cyrs.size();
			for(int i=0; i<size; i++) {
				User u = mCurrActivity.cyrs.get(i);
				if(i != size-1) {
					builder.append(u.userId).append(",");
				} else {
					builder.append(u.userId);
				}
			}
			ContactsSelectorActivity.startForResult(this,
					ContactsSelectorActivity.SELECT_MODE_MULTI, 
					builder.toString());
			break;
			
		default:
			break;
		}
	}
}
