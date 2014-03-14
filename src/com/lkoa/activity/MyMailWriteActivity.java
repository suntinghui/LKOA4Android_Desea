package com.lkoa.activity;

import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.MyMailManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.ContactItem;

/**
 * 我的邮件-写邮件
 */
public class MyMailWriteActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = "MyMailWriteActivity";

	public static final String STATE_DRAFT = "0";	//来自草稿箱
	public static final String STATE_SEND = "1";	//新建邮件
	
	private MyMailManager mMailMgr;
	private String mMailId;

	private TextView mSjr;
	private EditText mTitle, mContent;
	private ImageView mSjrAdd;
	
	private String sUserIds = null;
	private String sUserNames = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_mail_write_mail);

		mMailMgr = new MyMailManager();
		Intent intent = getIntent();
		mMailId = intent.getStringExtra("mailId");
		if(mMailId == null) mMailId = "";
		ContactItem item = (ContactItem)intent.getSerializableExtra("contact");
		if(item != null) {
			sUserIds = item.userId;
			sUserNames = item.userName;
		}

		findViews();
		setupViews();
	}

	@Override
	protected void findViews() {
		super.findViews();

		mSjr = (TextView) findViewById(R.id.mail_sjr);
		mTitle = (EditText) findViewById(R.id.mail_title);
		mContent = (EditText) findViewById(R.id.mail_content);
		mSjrAdd = (ImageView) findViewById(R.id.mail_sjr_add);
	}

	@Override
	protected void setupViews() {
		super.setupViews();

		mTvTitle.setText(R.string.my_email_details);
		mLinearRight.setVisibility(View.VISIBLE);
		mTvRight1.setVisibility(View.GONE);
		mTvRight2.setText(R.string.my_email_send);
		mTvRight2.setOnClickListener(this);
		mSjrAdd.setOnClickListener(this);
		
		mSjr.setText(sUserNames);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK) {
			sUserIds = data.getStringExtra("value");
			sUserNames = data.getStringExtra("showContent");
			mSjr.setText(sUserNames);
		}
	}
	
	private LKAsyncHttpResponseHandler getResponseHandler() {
		return new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				String result = (String) obj;
				if(!TextUtils.equals(result, "1")) {
					//发送成功
					showDialog(BaseActivity.MODAL_DIALOG, "邮件发送成功！");
					
				} else {
					//发送失败
					showDialog(BaseActivity.MODAL_DIALOG, "邮件发送失败！");
				}
			}
		};
	}
	
	private boolean check(String title, String content) {
		if(TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
			return false;
		}
		
		if(TextUtils.isEmpty(sUserIds)) return false;
		
		return true;
	}

	@Override
	public void onClick(View v) {
		if(v == mTvRight2) {
			//发送
			String title = mTitle.getText().toString();
			String content = mContent.getText().toString();
			
			if(check(title, content)) {
				mMailMgr.writeMail(MainActivity.USER_ID, sUserIds, mMailId, 
						URLEncoder.encode(title), URLEncoder.encode(content), 
						STATE_SEND, getResponseHandler());
			}
			
		} else if(v == mSjrAdd) {
			//添加收件人
			ContactsSelectorActivity.startForResult(this, 
					ContactsSelectorActivity.SELECT_MODE_MULTI, sUserIds);
		}
	}
	
	public static void startForResult(Context ctx, ContactItem item) {
		Intent intent = new Intent(ctx, MyMailWriteActivity.class);
		intent.putExtra("contact", item);
		((Activity)ctx).startActivityForResult(intent, 0);
	}

}
