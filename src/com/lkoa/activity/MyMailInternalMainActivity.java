package com.lkoa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.MyMailManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.util.LogUtil;

/**
 * 我的邮件-内部邮件
 */
public class MyMailInternalMainActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = "MyMailInternalMainActivity";
	
	public static final int TYPE_DRAFTS = 0;
	public static final int TYPE_INBOX = 1;
	public static final int TYPE_OUTBOX = 2;
	public static final int TYPE_DELETED = 3;
	
	private static final int [] mTitleResIds = new int[] {
		R.string.my_email_internal_inbox,
		R.string.my_email_internal_outbox,
		R.string.my_email_internal_drafts,
		R.string.my_email_internal_deleted
	};
	
	private static final int [] mIconResIds = new int[] {
		R.drawable.ic_center_msg_news,
		R.drawable.ic_center_msg_public,
		R.drawable.ic_center_msg_news,
		R.drawable.ic_center_msg_public
	};
	
	private static final int [] mBackgroundResIds = new int [] {
		R.drawable.center_msg_news_item_bg,
		R.drawable.center_msg_public_item_bg,
		R.drawable.center_msg_notice_item_bg,
		R.drawable.center_msg_window_dep_item_bg,
	};
	
	private View [] mItemViews = new View[4];
	
	private MyMailManager mMainMgr;
	
	private int mMailCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_my_email_internal_main);
		
		mMainMgr = new MyMailManager();
		findViews();
		setupViews();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mMainMgr.getMailCount(mApp.getUserId(), new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, obj.toString());
				mMailCount = (Integer)obj;
				setupItem(mItemViews[0], 0, mMailCount);
			}
		});
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		mItemViews[0] = findViewById(R.id.my_email_internal_inbox);
		mItemViews[1] = findViewById(R.id.my_email_internal_outbox);
		mItemViews[2] = findViewById(R.id.my_email_internal_drafts);
		mItemViews[3] = findViewById(R.id.my_email_internal_deleted);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(R.string.my_email_internal);
		mLinearRight.setVisibility(View.VISIBLE);
		mTvRight1.setVisibility(View.GONE);
		mTvRight2.setText(R.string.my_email_write);
		mTvRight2.setOnClickListener(this);
		
		for(int i=0; i<mItemViews.length; i++) {
			setupItem(mItemViews[i], i, 0);
		}
	}
	
	private void setupItem(View view, int index, int count) {
		ImageView icon = (ImageView)view.findViewById(R.id.iv_center_msg_icon);
		TextView title = (TextView)view.findViewById(R.id.tv_center_msg_title);
		TextView number = (TextView)view.findViewById(R.id.iv_center_msg_number);
		
		view.setBackgroundResource(mBackgroundResIds[index]);
		icon.setImageResource(mIconResIds[index]);
		title.setText(mTitleResIds[index]);
		if(count <= 0) {
			number.setVisibility(View.GONE);
		} else {
			number.setText(String.valueOf(count));
			number.setVisibility(View.VISIBLE);
		}
		
		view.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, MyMailInternalListActivity.class);
		
		switch (v.getId()) {
		case R.id.my_email_internal_inbox:
			//收件箱
			intent.putExtra("type", TYPE_INBOX);
			intent.putExtra("titleResId", R.string.my_email_internal_inbox);
			startActivity(intent);
			break;
			
		case R.id.my_email_internal_outbox:
			//发件箱
			intent.putExtra("type", TYPE_OUTBOX);
			intent.putExtra("titleResId", R.string.my_email_internal_outbox);
			startActivity(intent);
			break;
			
		case R.id.my_email_internal_drafts:
			//草稿箱
			intent.putExtra("type", TYPE_DRAFTS);
			intent.putExtra("titleResId", R.string.my_email_internal_drafts);
			startActivity(intent);
			break;
			
		case R.id.my_email_internal_deleted:
			//已删除
			intent.putExtra("type", TYPE_DELETED);
			intent.putExtra("titleResId", R.string.my_email_internal_deleted);
			startActivity(intent);
			break;
			
		case R.id.tv_right_2:
			//写邮件
			intent = new Intent(this, MyMailWriteActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
