package com.lkoa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.activity.ProcessWorkHandleActivity.ProcessWorkType;
import com.lkoa.business.ProcessWorkManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.util.LogUtil;

/**
 * 流程办理首页
 */
public class ProcessWorkHomeActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = "ProcessWorkHomeActivity";
	
	private static int [] mTitleResIds = new int[] {
		R.string.process_work_my_todo,
		R.string.process_work_doing,
		R.string.process_work_record_history,
		R.string.process_work_file_special,
		R.string.process_work_revocation_box,
	};
	
	private static int [] mIconResIds = new int[] {
		R.drawable.ic_process_w_my_todo,
		R.drawable.ic_process_w_dong,
		R.drawable.ic_process_w_record_history,
		R.drawable.ic_process_w_file_special,
		R.drawable.ic_process_w_revocation_box,
	};
	
	private static int [] mBackgroundResIds = new int [] {
		R.drawable.center_msg_news_item_bg,
		R.drawable.center_msg_public_item_bg,
		R.drawable.center_msg_notice_item_bg,
		R.drawable.center_msg_window_dep_item_bg,
		R.drawable.process_w_revocation_box_bg,
	};
	
	private View mMyTodo, mDoing, mRecordHistory, mFileSpecial, mRevocationBox;
	
	private ProcessWorkManager mProcessWorkMgr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_work_home);
		
		mProcessWorkMgr = new ProcessWorkManager();
		
		findViews();
		setupViews();
		
		mProcessWorkMgr.getLCGLCount(MainActivity.USER_ID, new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), "+(String)obj);
				
				String []subs = ((String) obj).split("[;]");
				setViewsCount(subs);
			}
		});
	}
	
	private void setViewsCount(String []subs) {
		setupItem(mMyTodo, 0, Integer.parseInt(subs[0]));
		setupItem(mDoing, 1, Integer.parseInt(subs[1]));
		setupItem(mFileSpecial, 3, Integer.parseInt(subs[2]));
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mTvTitle.setText(R.string.process_work_title);
		
		mMyTodo = findViewById(R.id.process_work_my_todo);
		mDoing = findViewById(R.id.process_work_doing);
		mRecordHistory = findViewById(R.id.process_work_record_history);
		mFileSpecial = findViewById(R.id.process_work_file_special);
		mRevocationBox = findViewById(R.id.process_work_revocation_box);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		setupItem(mMyTodo, 0, 0);
		setupItem(mDoing, 1, 0);
		setupItem(mRecordHistory, 2, 0);
		setupItem(mFileSpecial, 3, 0);
		setupItem(mRevocationBox, 4, 0);
	}
	
	private void setupItem(View view, int index, int count) {
		ImageView icon = (ImageView)view.findViewById(R.id.iv_center_msg_icon);
		TextView title = (TextView)view.findViewById(R.id.tv_center_msg_title);
		TextView number = (TextView)view.findViewById(R.id.iv_center_msg_number);
		
		view.setBackgroundResource(mBackgroundResIds[index]);
		icon.setImageResource(mIconResIds[index]);
		title.setText(mTitleResIds[index]);
		
		if(count < 1) {
			number.setVisibility(View.GONE);
		} else {
			number.setVisibility(View.VISIBLE);
			number.setText(String.valueOf(count));
		}
		view.setOnClickListener(this);
	}
	
	private void startActivity(ProcessWorkType type) {
		Intent intent = new Intent(this, ProcessWorkHandleActivity.class);
		intent.putExtra(ProcessWorkHandleActivity.KEY_PROCESS_WORK_TYPE, type);
		
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.process_work_my_todo:
			//我的待办
			startActivity(ProcessWorkType.TYPE_MY_TODO);
			break;
			
		case R.id.process_work_doing:
			//正在办理
			startActivity(ProcessWorkType.TYPE_DOING);
			break;
			
		case R.id.process_work_record_history:
			//历史记录
			startActivity(ProcessWorkType.TYPE_RECORD_HISTORY);
			break;
			
		case R.id.process_work_file_special:
			//特批文件
			startActivity(ProcessWorkType.TYPE_FILE_SPECIAL);
			break;
			
		case R.id.process_work_revocation_box:
			//撤销箱
			startActivity(ProcessWorkType.TYPE_REVOCATION_BOX);
			break;

		default:
			break;
		}
	}
}
