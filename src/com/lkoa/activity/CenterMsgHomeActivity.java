package com.lkoa.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;

/**
 * 信息中心首页
 */
public class CenterMsgHomeActivity extends Activity implements OnClickListener {
	
	private static final int [] mTitleResIds = new int[] {
		R.string.center_msg_news,
		R.string.center_msg_public,
		R.string.center_msg_notice,
		R.string.center_msg_window_department
	};
	
	private static final int [] mIconResIds = new int[] {
		R.drawable.ic_center_msg_news,
		R.drawable.ic_center_msg_public,
		R.drawable.ic_center_msg_notice,
		R.drawable.ic_center_msg_window_department,
	};
	
	private static final int [] mBackgroundResIds = new int [] {
		R.drawable.center_msg_news_item_bg_n,
		R.drawable.center_msg_public_item_bg_n,
		R.drawable.center_msg_notice_item_bg_n,
		R.drawable.center_msg_window_dep_item_bg_n,
	};
	
	private View mNews, mPublic, mNotice, mWindowDepartment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_center_msg_home);
		findViews();
		setupViews();
	}
	
	private void findViews() {
		mNews = findViewById(R.id.center_msg_news);
		mPublic = findViewById(R.id.center_msg_public);
		mNotice = findViewById(R.id.center_msg_notice);
		mWindowDepartment = findViewById(R.id.center_msg_window_department);
	}
	
	private void setupViews() {
		setupItem(mNews, 0);
		setupItem(mPublic, 1);
		setupItem(mNotice, 2);
		setupItem(mWindowDepartment, 3);
	}
	
	private void setupItem(View view, int index) {
		ImageView icon = (ImageView)view.findViewById(R.id.iv_center_msg_icon);
		TextView title = (TextView)view.findViewById(R.id.tv_center_msg_title);
		TextView number = (TextView)view.findViewById(R.id.iv_center_msg_number);
		
		view.setBackgroundResource(mBackgroundResIds[index]);
		icon.setImageResource(mIconResIds[index]);
		title.setText(mTitleResIds[index]);
		number.setText("6");
		
		if(index > 1) number.setVisibility(View.GONE);
		view.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.center_msg_news:
			//集团新闻
			break;
			
		case R.id.center_msg_public:
			//集团公告
			break;
			
		case R.id.center_msg_notice:
			//集团通知
			break;
			
		case R.id.center_msg_window_department:
			//部门之窗
			break;

		default:
			break;
		}
	}
}
