package com.lkoa.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.adapter.RSMgrListAdapter;
import com.lkoa.business.RSManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.RSInfors;

/**
 * 人事管理-首页
 */
public class RSMgrHomeActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = "RSMgrHomeActivity";

	private Button mDateStart, mDateEnd, mUserName, mSearch;
	private TextView mDeptName;
	private ListView mListView;
	private Button mCurrDatePending;
	private TextView mNoDataView;

	private RSMgrListAdapter mAdapter;

	private Calendar mCalendar = Calendar.getInstance();

	private String mLoginUserId;
	private String mUserId;
	private String mUserNameStr;

	private RSManager mRSMgr = null;

	private RSInfors mRSInfors;

	private OnDateSetListener mOnDateSetListener = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(year).append("-");
			monthOfYear += 1;
			String my = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
			buffer.append(my).append("-");
			String dayOfM = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
			buffer.append(dayOfM);
			mCurrDatePending.setText(buffer.toString());
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rs_mgr_home);

		mLoginUserId = mApp.getUserId();
		mUserId = mLoginUserId;
		mUserNameStr = mApp.getUserName();
		mRSMgr = new RSManager();

		findViews();
		setupViews();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			mUserId = data.getStringExtra("value");
			mUserNameStr = data.getStringExtra("showContent");
			mUserName.setText(mUserNameStr);
		}
	}

	@Override
	protected void findViews() {
		super.findViews();

		mTvTitle.setText(R.string.rs_mgr_title);
		mDateStart = (Button) findViewById(R.id.date_start);
		mDateEnd = (Button) findViewById(R.id.date_end);
		mUserName = (Button) findViewById(R.id.user_name);
		mSearch = (Button) findViewById(R.id.search_start);
		mNoDataView = (TextView) findViewById(R.id.no_data_view);

		mDeptName = (TextView) findViewById(R.id.dept);
		mListView = (ListView) findViewById(android.R.id.list);
	}

	@Override
	protected void setupViews() {
		super.setupViews();

		mTvTitle.setText(R.string.rs_mgr_title);

		mDateStart.setOnClickListener(this);
		mDateEnd.setOnClickListener(this);
		mUserName.setOnClickListener(this);
		mSearch.setOnClickListener(this);

		mUserName.setText(mUserNameStr);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		mDateStart.setText(sdf.format(date));
		mDateEnd.setText(sdf.format(date));

		getRecord();
	}

	private void showDatePickerDialog() {
		String[] date = mCurrDatePending.getText().toString().split("-");
		DatePickerDialog dialog = new DatePickerDialog(this, mOnDateSetListener, Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
		dialog.show();
	}

	private boolean isAdmin() {
		if (mRSInfors == null)
			return false;

		if (TextUtils.equals(mRSInfors.adminFlag, "1"))
			return true;
		
		return false;
	}

	private void getRecord() {
		String startDate = mDateStart.getText().toString();
		String endDate = mDateEnd.getText().toString();
		mRSMgr.getKaoQin(mLoginUserId, mUserId, startDate, endDate, new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				mRSInfors = (RSInfors) obj;
				if (mRSInfors.list.size() > 0) {
					mDeptName.setText(mRSInfors.list.get(0).detpName);
					mNoDataView.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
				} else {
					mDeptName.setText("");
					mNoDataView.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
				}

				if (mAdapter == null) {
					mAdapter = new RSMgrListAdapter(RSMgrHomeActivity.this, 0, mRSInfors.list);
					mAdapter.setCountPerPage(mRSInfors.list.size());
					mListView.setAdapter(mAdapter);
				} else {
					mAdapter.setCountPerPage(mRSInfors.list.size());
					mAdapter.setData(mRSInfors.list);
				}
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.date_start:
			// 开始日期
			mCurrDatePending = mDateStart;
			showDatePickerDialog();
			break;

		case R.id.date_end:
			// 结束日期
			mCurrDatePending = mDateEnd;
			showDatePickerDialog();
			break;

		case R.id.user_name:
			// 用户名称
			// !TextUtils.equals(mLoginUserId, mUserId) && !isAdmin()
			if(!isAdmin()) {
				showDialog(MODAL_DIALOG, "对不起，您没有权限查询其他人信息！");
				return;
			}
			
			ContactsSelectorActivity.startForResult(this, ContactsSelectorActivity.SELECT_MODE_SINGLE, mUserId);
			break;

		case R.id.search_start:
			// 开始检索
			getRecord();
			break;

		default:
			break;
		}
	}
}
