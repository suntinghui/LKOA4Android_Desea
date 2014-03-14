package com.lkoa.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.model.ContactItem;
import com.lkoa.util.PhoneUtil;

/**
 * 通讯录-联系人详情
 */
public class ContactDetailsActivity extends CenterMsgBaseActivity 
	implements OnClickListener {
	private static final String TAG = "ContactDetailsActivity";
	
	private ContactItem mContactInfo;
	
	private TextView mNameTv, mDeptTv;
	private TextView mBgdhTv;
	private TextView mPhoneTv;
	private ImageView mToSmsIv, mToPhoneIv;
	private TextView mEmail1Tv, mEmail2Tv;
	private ImageView mEmail1Iv, mEmail2Iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_details);
		
		Intent intent = getIntent();
		mContactInfo = (ContactItem)intent.getSerializableExtra("contact");
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mNameTv = (TextView) findViewById(R.id.tv_name);
		mDeptTv = (TextView) findViewById(R.id.tv_department);
		
		mBgdhTv = (TextView) findViewById(R.id.tv_bgdh);
		
		mPhoneTv = (TextView) findViewById(R.id.tv_phone);
		mToSmsIv = (ImageView) findViewById(R.id.iv_message);
		mToPhoneIv = (ImageView) findViewById(R.id.iv_phone);
		
		mEmail1Tv = (TextView) findViewById(R.id.tv_email1);
		mEmail1Iv = (ImageView) findViewById(R.id.iv_email1);
		mEmail2Tv = (TextView) findViewById(R.id.tv_email2);
		mEmail2Iv = (ImageView) findViewById(R.id.iv_email2);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(R.string.contact_details);
		
		mNameTv.setText(mContactInfo.userName);
		mDeptTv.setText(mContactInfo.deptName);
		
		mBgdhTv.setText(mContactInfo.bgdh);
		
		mPhoneTv.setText(mContactInfo.yddh);
		mToSmsIv.setOnClickListener(this);
		mToPhoneIv.setOnClickListener(this);
		
		mEmail1Tv.setText(mContactInfo.email1);
		mEmail1Iv.setOnClickListener(this);
		if(TextUtils.isEmpty(mContactInfo.email1)) {
			mEmail1Iv.setVisibility(View.GONE);
		}
		
		mEmail2Tv.setText(mContactInfo.email2);
		mEmail2Iv.setOnClickListener(this);
		if(TextUtils.isEmpty(mContactInfo.email2)) {
			mEmail2Iv.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_message:
			//发短信
			MyMessageWriteActivity.start(this, mContactInfo);
			break;
			
		case R.id.iv_phone:
			//打电话
			PhoneUtil.dial(this, mContactInfo.yddh);
			break;
			
		case R.id.iv_email1:
			//发邮件1
			MyMailWriteActivity.startForResult(this, mContactInfo);
			break;
			
		case R.id.iv_email2:
			//发邮件2
			MyMailWriteActivity.startForResult(this, mContactInfo);
			break;

		default:
			break;
		}
	}
	
	public static void start(Context context, ContactItem contactInfo) {
		Intent intent = new Intent(context, ContactDetailsActivity.class);
		intent.putExtra("contact", contactInfo);
		context.startActivity(intent);
	}
	
}
