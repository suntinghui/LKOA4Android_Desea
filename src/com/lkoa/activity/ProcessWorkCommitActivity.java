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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK) {
			mContentInfo = (ProcessContentInfo)data.getSerializableExtra("bundle");
			buildPageWarper();
		}
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
		mNodeNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProcessWorkCommitActivity.this, ProcessWorkNodeSelectActivity.class);
				intent.putExtra("bundle", mContentInfo);
				startActivityForResult(intent, 0);
			}
		});
		
		buildPageWarper();
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
		setItem(mNodeNext, R.string.process_work_node_next, activity.name);
		setItem(mNodeDealtime, R.string.process_work_node_dealtime, activity.dealTime);
		setItem(mNodeZbr, R.string.process_work_node_zbr, activity.zbr.userName);
		
		setItem(mNodeMode, R.string.process_work_node_mode, getModeName(activity.mode));
		setItem(mNodeCyr, R.string.process_work_node_cyr, getCyrs(activity.cyrs));
	}
	
	private void setItem(View view, int titleResId, String value) {
		TextView title = (TextView)view.findViewById(R.id.title);
		TextView text = (TextView)view.findViewById(R.id.content_text);
		if(view == mNodeNext) {
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
		for(User u : list) {
			builder.append(u.toString());
		}
		return builder.toString();
	}
	
	private void commit() {
		mCurrActivity.dealTime = "2";
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
			
		default:
			break;
		}
	}
}
