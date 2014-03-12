package com.lkoa.activity;

import java.net.URLEncoder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lkoa.R;
import com.lkoa.business.MyMailManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.MailContentItemInfo;
import com.lkoa.model.RCContentItem;
import com.lkoa.model.RCListItem;
import com.lkoa.util.LogUtil;

/**
 * 我的邮件-写邮件
 */
public class MyMailWriteActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = "MyMailWriteActivity";

	public static final String STATE_DRAFT = "0";
	public static final String STATE_SEND = "1";
	
	private MyMailManager mMailMgr;
	private String mMailId;

	private TextView mSjr;
	private EditText mTitle, mContent;
	private ImageView mSjrAdd;
	
	private String sUserIds = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_mail_write_mail);

		mMailMgr = new MyMailManager();
		Intent intent = getIntent();
		mMailId = intent.getStringExtra("mailId");
		if(mMailId == null) mMailId = "";

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

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK) {
			sUserIds = data.getStringExtra("value");
			String content = data.getStringExtra("showContent");
			mSjr.setText(content);
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
			Intent intent = new Intent(this, ContactsSelectorActivity.class);
			intent.putExtra(ContactsSelectorActivity.KEY_SELECT_MODE, 
					ContactsSelectorActivity.SELECT_MODE_MULTI);
			intent.putExtra(ContactsSelectorActivity.KEY_SELECTED_CONTACT, 
					sUserIds);
			
			startActivityForResult(intent, 0);
		}
	}

}
