package com.lkoa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.lkoa.R;

/**
 * 流程办理
 */
public class ProcessWorkHandleActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = ProcessWorkHandleActivity.class.getName();
	
	public static final String KEY_PROCESS_WORK_TYPE = "key_process_work_type";
	
	public enum ProcessWorkType {
		TYPE_MY_TODO,
		TYPE_DOING,
		TYPE_RECORD_HISTORY,
		TYPE_FILE_SPECIAL,
		TYPE_REVOCATION_BOX
	}
	
	private ProcessWorkType mWorkType = ProcessWorkType.TYPE_MY_TODO;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_work_home);
		
		Intent intent = getIntent();
		if(intent != null) {
			mWorkType = (ProcessWorkType) intent.
					getSerializableExtra(KEY_PROCESS_WORK_TYPE);
			Log.i(TAG, "onCreate(), mWorkType=" + mWorkType);
		}
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		mTvTitle.setText(R.string.process_work_handle_title);
	}

	@Override
	public void onClick(View v) {
	}
}
