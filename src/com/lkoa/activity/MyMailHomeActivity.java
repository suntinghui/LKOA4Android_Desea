package com.lkoa.activity;

import android.content.Intent;
import android.net.Uri;
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
 * 我的邮件-首页
 */
public class MyMailHomeActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = "MyEmailHomeActivity";
	
	private static final int [] mTitleResIds = new int[] {
		R.string.my_email_external,
		R.string.my_email_internal
	};
	
	private static final int [] mIconResIds = new int[] {
		R.drawable.ic_center_msg_news,
		R.drawable.ic_center_msg_public
	};
	
	private static final int [] mBackgroundResIds = new int [] {
		R.drawable.center_msg_news_item_bg,
		R.drawable.center_msg_public_item_bg,
	};
	
	private View [] mItemViews = new View[2];
	
	private MyMailManager mMainMgr;
	
	private int mMailCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_my_email_home);
		
		mMainMgr = new MyMailManager();
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		mItemViews[0] = findViewById(R.id.my_email_external);
		mItemViews[1] = findViewById(R.id.my_email_internal);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(R.string.my_email_title);
		
		for(int i=0; i<mItemViews.length; i++) {
			setupItem(mItemViews[i], i, 0);
		}
		
		mMainMgr.getMailCount(MainActivity.USER_ID, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, obj.toString());
				mMailCount = (Integer)obj;
				setupItem(mItemViews[1], 1, mMailCount);
			}
		});
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
		switch (v.getId()) {
		case R.id.my_email_internal:
			//内部邮件
			Intent intent = new Intent(this, MyMailInternalMainActivity.class);
			startActivity(intent);
			break;
			
		case R.id.my_email_external:
			//外部邮件
			Uri uri = Uri.parse("mailto:"); 
			String[] email = {""};
			intent = new Intent(Intent.ACTION_SENDTO, uri);
			intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
			intent.putExtra(Intent.EXTRA_SUBJECT, ""); // 主题
			intent.putExtra(Intent.EXTRA_TEXT, ""); // 正文
			startActivity(Intent.createChooser(intent, ""));
			break;

		default:
			break;
		}
	}
}
