package com.lkoa.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.CenterMsgManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.IdCountItem;
import com.lkoa.util.LogUtil;

/**
 * 信息中心首页
 */
public class CenterMsgHomeActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = "CenterMsgHomeActivity";
	
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
		R.drawable.center_msg_news_item_bg,
		R.drawable.center_msg_public_item_bg,
		R.drawable.center_msg_notice_item_bg,
		R.drawable.center_msg_window_dep_item_bg,
	};
	
	private CenterMsgManager mCenterMsgMgr;
	private View [] mItemViews = new View[4];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_center_msg_home);
		
		mCenterMsgMgr = new CenterMsgManager();
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		mItemViews[0] = findViewById(R.id.center_msg_news);
		mItemViews[1] = findViewById(R.id.center_msg_public);
		mItemViews[2] = findViewById(R.id.center_msg_notice);
		mItemViews[3] = findViewById(R.id.center_msg_window_department);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		for(int i=0; i<mItemViews.length; i++) {
			setupItem(mItemViews[i], i, 0, null);
		}
		
		//获取数据
		mCenterMsgMgr.getXXZXCount("1", new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), " + obj.toString());
				HashMap<String, IdCountItem> map = (HashMap<String, IdCountItem>)obj;
				for(Map.Entry<String, IdCountItem> entry : map.entrySet()) {
					String key = entry.getKey();
					IdCountItem item = entry.getValue();
					if(TextUtils.equals(key, "JDXW")) {
						setupItem(mItemViews[0], 0, item.count, item.id);
						
					} else if(TextUtils.equals(key, "JDGG")) {
						setupItem(mItemViews[1], 1, item.count, item.id);
						
					} else if(TextUtils.equals(key, "TZXX")) {
						setupItem(mItemViews[2], 2, item.count, item.id);
					}
				}
			}
		});
	}
	
	private void setupItem(View view, int index, int count, String id) {
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
		view.setTag(id);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.center_msg_news:
			//集团新闻
			Intent intent = new Intent(this, CenterMsgNewsActivity.class);
			intent.putExtra("sId", (String)v.getTag());
			startActivity(intent);
			break;
			
		case R.id.center_msg_public:
			//集团公告
			break;
			
		case R.id.center_msg_notice:
			//集团通知
			break;
			
		case R.id.center_msg_window_department:
			//部门之窗
			startActivity(new Intent(this, CenterMsgWindowDepartmentActivity.class));
			break;

		default:
			break;
		}
	}
}
