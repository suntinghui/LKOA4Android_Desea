package com.lkoa.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lkoa.R;
import com.lkoa.business.MySmsManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.ContactItem;
import com.lkoa.util.LogUtil;

/**
 * 我的短信-写短信
 */
public class MyMessageWriteActivity extends CenterMsgBaseActivity implements OnClickListener {
	public static final String TAG = "MyMessageWriteActivity";
	
	public static final String SPILITER = ",";
	
	private MySmsManager mSmsMgr;
	
	private ImageView mBtnAddMore;
	private Button mBtnSend;
	private TextView mSjrTv;
	private EditText mContactSelected;
	private EditText mContentEt;
	
	private String mUserIds;
	private String mUserNames;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_sms_write);
		
		Intent intent = getIntent();
		ContactItem item = (ContactItem)intent.getSerializableExtra("contact");
		if(item != null) {
			mUserNames = item.userName;
			mUserIds = item.userId;
		}
		
		mSmsMgr = new MySmsManager();
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mBtnAddMore = (ImageView) findViewById(R.id.add_more);
		mBtnSend = (Button) findViewById(R.id.send);
		mSjrTv = (TextView) findViewById(R.id.tv_sjr);
		mContactSelected = (EditText) findViewById(R.id.contacts_selected);
		mContentEt = (EditText) findViewById(R.id.content);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(R.string.my_sms_write_title);
		mIvRight.setVisibility(View.GONE);
		
		mBtnAddMore.setOnClickListener(this);
		mBtnSend.setOnClickListener(this);
		mContactSelected.setText(mUserNames);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK) {
			mUserIds = data.getStringExtra("value");
			mUserNames = data.getStringExtra("showContent");
			mContactSelected.setText(mUserNames);
		}
	}
	
	private LKAsyncHttpResponseHandler getResponseHandler() {
		return new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), obj="+obj);
				String result = (String)obj;
				if(TextUtils.equals("1", result)) {
					showDialog(MODAL_DIALOG, "发送短信成功！");
				} else {
					showDialog(MODAL_DIALOG, "发送短信失败！");
				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_more:
			//添加联系人
			ContactsSelectorActivity.startForResult(this, 
					ContactsSelectorActivity.SELECT_MODE_MULTI, mUserIds);
			break;
			
		case R.id.send:
			//发送短信
			String content = mContentEt.getText().toString();
			if(TextUtils.isEmpty(content) || TextUtils.isEmpty(mUserIds)) {
				Toast.makeText(this, "收件人和短信内容都不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}
			mSmsMgr.writeSMS(MainActivity.USER_ID, mUserIds, 
					mUserNames, content, getResponseHandler());
			break;

		default:
			break;
		}
	}
	
	public static void start(Context ctx, ContactItem item) {
		Intent intent = new Intent(ctx, MyMessageWriteActivity.class);
		intent.putExtra("contact", item);
		ctx.startActivity(intent);
	}
}
