package com.lkoa.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.business.MyMailManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.Attachment;
import com.lkoa.model.MailContentItemInfo;
import com.lkoa.util.LogUtil;

/**
 * 我的邮件-内部邮件-邮件内容
 */
public class MyMailContentActivity extends CenterMsgBaseActivity implements OnClickListener {
	private static final String TAG = "MyMailContentActivity";
	
	private MyMailManager mMailMgr;
	
	private String mMailId, mMailJsId;
	
	private LinearLayout mLinearContent;
	private View [] mViews = null;
	private String [] mNames = null;
	
	private LinearLayout mLinearAttachments;
	
	private TextView mTvReply, mTvForwarding, mTvRemove;
	
	private MailContentItemInfo mMailContentInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_mail_content);
		
		mMailMgr = new MyMailManager();
		Intent intent = getIntent();
		mMailId = intent.getStringExtra("mailId");
		mMailJsId = intent.getStringExtra("mailJsId");
		
		mNames = getResources().getStringArray(R.array.my_mail_content_item_names);
		mViews = new View[mNames.length];
		
		findViews();
		setupViews();
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		mLinearContent = (LinearLayout)findViewById(R.id.content);
		
		mViews[0] = findViewById(R.id.mail_sender);
		mViews[1] = findViewById(R.id.mail_sjr);
		mViews[2] = findViewById(R.id.mail_title);
		mViews[3] = findViewById(R.id.mail_content);
		mViews[4] = findViewById(R.id.mail_date);
		mViews[5] = findViewById(R.id.mail_attachment);
		
		mLinearAttachments = (LinearLayout)findViewById(R.id.attachments);
		
		mViews[4].setVisibility(View.GONE);
		
		mTvReply = (TextView)findViewById(R.id.tv_reply);
		mTvForwarding = (TextView)findViewById(R.id.tv_forwarding);
		mTvRemove = (TextView)findViewById(R.id.tv_remove);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(R.string.my_email_details);
		mMailMgr.getMail(mApp.getUserId(), mMailId, new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, "successAction(), obj="+obj);
				mMailContentInfo = (MailContentItemInfo) obj;
				mLinearContent.setVisibility(View.VISIBLE);
				setViews(mMailContentInfo);
			}
		});
		
		mTvReply.setOnClickListener(this);
		mTvForwarding.setOnClickListener(this);
		mTvRemove.setOnClickListener(this);
	}
	
	private void setViews(MailContentItemInfo item) {
		String[] contentVals = new String[7];
		contentVals[0] = item.sender;
		contentVals[1] = item.sjr;
		contentVals[2] = item.title;
		contentVals[3] = item.content;
		contentVals[4] = item.date;
		contentVals[5] = "";
		
		TextView title, content;
		for(int i=0; i<mViews.length - 1; i++) {
			title = (TextView)mViews[i].findViewById(R.id.title);
			content = (TextView)mViews[i].findViewById(R.id.content_text);
			
			title.setText(mNames[i]);
			
			if(i == 3) {
				//邮件内容为html格式数据
				String tmp = contentVals[i].replaceAll("&amp;nbsp;", " ");
				content.setText(Html.fromHtml(tmp));
			} else {
				content.setText(contentVals[i]);
			}
		}
		
		View child = null;
		TextView name = null;
		List<Attachment> list = item.attachments;
		for(Attachment att : list) {
			child = mLayoutInflater.inflate(R.layout.process_work_handle_content_attachment_item, 
					mLinearAttachments);
			name = (TextView)child.findViewById(R.id.tv_attachment_name);
			name.setText(att.title);
			
			child.setTag(att);
			child.setOnClickListener(this);
		}
	}
	
	private LKAsyncHttpResponseHandler getDelResponseHandler() {
		return new LKAsyncHttpResponseHandler() {
			
			@Override
			public void successAction(Object obj) {
				String result = (String) obj;
				if(TextUtils.equals(result, "0")) {
					//删除失败
					showDialog(BaseActivity.MODAL_DIALOG, "邮件删除失败！");
					
				} else {
					//删除成功
					showDialog(BaseActivity.MODAL_DIALOG, "邮件删除成功！");
				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if(id == R.id.tv_reply) {
			//回复
			MyMailWriteActivity.start(this, mMailContentInfo, MyMailWriteActivity.TYPE_REPLY, 
					mMailContentInfo.senderId, mMailContentInfo.sender);
			
		} else if(id == R.id.tv_forwarding) {
			//转发
			MyMailWriteActivity.start(this, mMailContentInfo, MyMailWriteActivity.TYPE_REPLY, 
					null, null);
			
		} else if(id == R.id.tv_remove) {
			//删除
			mMailMgr.delMail(mApp.getUserId(), 
					mMailJsId, getDelResponseHandler());
			
		} else {
			Attachment att = (Attachment)v.getTag();
			loadAttachment(att.title, att.id);
		}
	}
}
