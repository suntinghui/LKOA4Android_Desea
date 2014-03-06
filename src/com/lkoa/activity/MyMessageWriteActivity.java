package com.lkoa.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.lkoa.R;
import com.lkoa.business.MySmsManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;

/**
 * 我的短信-列表页
 */
public class MyMessageWriteActivity extends CenterMsgBaseActivity implements OnClickListener {
	
	private MySmsManager mSmsMgr;
	
	private ImageView mBtnAddMore;
	private Button mBtnSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_sms_write);
		
		mSmsMgr = new MySmsManager();
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mBtnAddMore = (ImageView) findViewById(R.id.add_more);
		mBtnSend = (Button) findViewById(R.id.send);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(R.string.my_sms_write_title);
		mIvRight.setVisibility(View.GONE);
		
		mBtnAddMore.setOnClickListener(this);
		mBtnSend.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_more:
			//添加联系人
			break;
			
		case R.id.send:
			//发送短信
			break;

		default:
			break;
		}
	}
}
