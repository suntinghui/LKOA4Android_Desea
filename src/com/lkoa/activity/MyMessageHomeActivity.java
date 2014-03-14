package com.lkoa.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;

/**
 * 我的短信-首页
 */
public class MyMessageHomeActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = "MyMessageHomeActivity";
	
	private static int [] mTitleResIds = new int[] {
		R.string.my_sms_inbox,
		R.string.my_sms_outbox,
		R.string.my_sms_weixin,
	};
	
	private static int [] mIconResIds = new int[] {
		R.drawable.ic_inbox,
		R.drawable.ic_outbox,
		R.drawable.ic_weixin,
	};
	
	private static int [] mBackgroundResIds = new int [] {
		R.drawable.center_msg_news_item_bg,
		R.drawable.center_msg_public_item_bg,
		R.drawable.center_msg_notice_item_bg,
	};
	
	private View mInbox, mOutbox, mWeixin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_message_home);
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mTvTitle.setText(R.string.process_work_title);
		
		mInbox = findViewById(R.id.my_message_inbox);
		mOutbox = findViewById(R.id.my_message_outbox);
		mWeixin = findViewById(R.id.my_message_weixin);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(R.string.my_sms);
		setupItem(mInbox, 0);
		setupItem(mOutbox, 1);
		setupItem(mWeixin, 2);
	}
	
	private void setupItem(View view, int index) {
		ImageView icon = (ImageView)view.findViewById(R.id.iv_center_msg_icon);
		TextView title = (TextView)view.findViewById(R.id.tv_center_msg_title);
		TextView number = (TextView)view.findViewById(R.id.iv_center_msg_number);
		
		number.setVisibility(View.GONE);
		view.setBackgroundResource(mBackgroundResIds[index]);
		icon.setImageResource(mIconResIds[index]);
		title.setText(mTitleResIds[index]);
		view.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_message_inbox:
			//收件箱
			MyMessageListActivity.start(this, 
					String.valueOf(0), R.string.my_sms_inbox);
			break;
			
		case R.id.my_message_outbox:
			//发件箱
			MyMessageListActivity.start(this, 
					String.valueOf(1), R.string.my_sms_outbox);
			break;
			
		case R.id.my_message_weixin:
			//我的微信
			startWeixin();
			break;

		default:
			break;
		}
	}
	
	private void startWeixin() {
		try {
			Intent weixin = new Intent(Intent.ACTION_MAIN); 
			weixin.addCategory(Intent.CATEGORY_LAUNCHER);             
			ComponentName cn = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");             
			weixin.setComponent(cn); 
			weixin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(weixin);
		} catch(Exception e) {
			showDialog(MODAL_DIALOG, "您还未安装微信，请安装后再试！");
		}
		
	}
	
	public static void start(Context ctx, int[] types, int titleResId, boolean loadCount) {
		Intent intent = new Intent(ctx, MyMessageHomeActivity.class);
		intent.putExtra("types", types);
		intent.putExtra("titleResId", titleResId);
		intent.putExtra("loadCount", loadCount);
		ctx.startActivity(intent);
	}
}
