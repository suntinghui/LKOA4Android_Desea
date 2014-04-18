package com.lkoa.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lkoa.R;
import com.lkoa.adapter.MyMailListAdapter;
import com.lkoa.business.MyMailManager;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.model.MailItemInfo;
import com.lkoa.util.LogUtil;

/**
 * 我的邮件-内部邮件-列表页
 */
public class MyMailInternalListActivity 
	extends CenterMsgBaseListActivity<MailItemInfo> 
	implements OnItemClickListener, OnClickListener {
	
	private static final String TAG = "MyMailInternalListActivity";
	
	public static final int STATE_UNREAD = 0;
	public static final int STATE_READED = 1;
	public static final int STATE_ALL = 2;
	
	private MyMailManager mMailMgr;
	private int mTitleResId;
	
	private ListView mListView;
	
	private int mState = STATE_ALL;
	private int mType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		mType = intent.getIntExtra("type", MyMailInternalMainActivity.TYPE_INBOX);
		mTitleResId = intent.getIntExtra("titleResId", 0);
		
		setContentView(R.layout.activity_my_email_internal_list);
		
		mMailMgr = new MyMailManager();
		findViews();
		setupViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		loadData();
	}
	
	private void loadData() {
		mMailMgr.getMailList(String.valueOf(mState), String.valueOf(mType), mApp.getUserId(), new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				LogUtil.i(TAG, obj.toString());
				ArrayList<MailItemInfo> list = (ArrayList<MailItemInfo>)obj;
				resetPageState(list);
				if(mAdapter == null) {
					mAdapter = new MyMailListAdapter(MyMailInternalListActivity.this, 0, list);
					mListView.setAdapter(mAdapter);
					((MyMailListAdapter)mAdapter).setOnClickListener(mOnItemClickLis);
				} else {
					mAdapter.setData(list);
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setOnItemClickListener(this);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mTvTitle.setText(mTitleResId);
		
		if(mType == MyMailInternalMainActivity.TYPE_DELETED) {
			//已删除列表页，显示清空按钮
			mLinearRight.setVisibility(View.VISIBLE);
			mTvRight1.setVisibility(View.INVISIBLE);
			mTvRight2.setVisibility(View.VISIBLE);
			mTvRight2.setText(R.string.my_email_clear);
			mTvRight2.setOnClickListener(this);
			
		} else {
			//收件箱、发件箱、草稿箱，显示删除和清空按钮
			mLinearRight.setVisibility(View.VISIBLE);
			mTvRight1.setVisibility(View.VISIBLE);
			mTvRight2.setVisibility(View.VISIBLE);
			mTvRight1.setText(R.string.my_email_delete);
			mTvRight1.setOnClickListener(this);
			mTvRight2.setText(R.string.my_email_clear);
			mTvRight2.setOnClickListener(this);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		MailItemInfo item = mAdapter.getData().get(getRealPosition(position));
		String mailId = item.id;
		Intent intent = new Intent(this, MyMailContentActivity.class);
		intent.putExtra("mailId", mailId);
		intent.putExtra("mailJsId", item.jsId);
		startActivity(intent);
	}
	
	private OnClickListener mOnItemClickLis = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			MailItemInfo item = (MailItemInfo)v.getTag(R.string.key_tag);
			String mailId = item.id;
			Intent intent = new Intent(MyMailInternalListActivity.this, MyMailContentActivity.class);
			intent.putExtra("mailId", mailId);
			intent.putExtra("mailJsId", item.jsId);
			startActivity(intent);
		}
	};
	
	private boolean checkValid() {
		String s = buildIds();
		return !TextUtils.isEmpty(s);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right_1:
			//删除
			if(checkValid()) {
				mPendingId = R.id.tv_right_1;
				showDialog(DOUBLE_MODAL_DIALOG, "确定删除选中项吗？");
			} else {
				showDialog(MODAL_DIALOG, "请选择后再操作！");
			}
			break;
			
		case R.id.tv_right_2:
			//清空
			if(checkValid()) {
				mPendingId = R.id.tv_right_2;
				showDialog(DOUBLE_MODAL_DIALOG, "确定清空选中项吗？");
			} else {
				showDialog(MODAL_DIALOG, "请选择后再操作！");
			}
			break;

		default:
			break;
		}
	}
	
	private int mPendingId = -1;
	
	@Override
	protected void onClickConfirm() {
		super.onClickConfirm();
		switch (mPendingId) {
		case R.id.tv_right_1:
			//删除
			String ids = buildJsIds();
			if(mType == MyMailInternalMainActivity.TYPE_DRAFTS) {
				//如果是草稿箱删除，sType=1 ,Ids=邮件序号
				ids = buildIds();
				mMailMgr.clearMail(mApp.getUserId(), ids, getClearResHandler());
				
			} else {
				mMailMgr.delMail(mApp.getUserId(), ids, getDelResHandler());
			}
			break;
			
		case R.id.tv_right_2:
			//清空
			ids = buildIds();
			mMailMgr.clearMail(mApp.getUserId(), ids, getClearResHandler());
			break;

		default:
			break;
		}
		
		mPendingId = -1;
	}
	
	/**
	 * 构建邮件接收序号
	 */
	private String buildJsIds() {
		MyMailListAdapter adapter = (MyMailListAdapter)mAdapter;
		List<MailItemInfo> list = adapter.getCheckeIds();
		if(list == null || list.size() < 1) return null;
		
		StringBuffer buffer = new StringBuffer();
		for(MailItemInfo item : list) {
			buffer.append(item.jsId);
			buffer.append(",");
		}
		buffer.delete(buffer.length() - 1, buffer.length());
		
		return buffer.toString();
	}
	
	/**
	 * 构建邮件序号
	 */
	private String buildIds() {
		MyMailListAdapter adapter = (MyMailListAdapter)mAdapter;
		List<MailItemInfo> list = adapter.getCheckeIds();
		if(list == null || list.size() < 1) return null;
		
		StringBuffer buffer = new StringBuffer();
		for(MailItemInfo item : list) {
			buffer.append(item.id);
			buffer.append(",");
		}
		buffer.delete(buffer.length() - 1, buffer.length());
		
		return buffer.toString();
	}
	
	private LKAsyncHttpResponseHandler getDelResHandler() {
		return new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				String result = (String) obj;
				if(TextUtils.equals(result, "1")) {
					//删除成功
					showDialog(BaseActivity.MODAL_DIALOG, "邮件删除成功！");
					((MyMailListAdapter)mAdapter).clearCheckedItems();
					
				} else {
					//删除失败
					showDialog(BaseActivity.MODAL_DIALOG, "邮件删除失败！");
				}
			}
		};
	}
	
	private LKAsyncHttpResponseHandler getClearResHandler() {
		return new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				String result = (String) obj;
				if(TextUtils.equals(result, "1")) {
					//清空成功
					showDialog(BaseActivity.MODAL_DIALOG, "邮件清空成功！");
					((MyMailListAdapter)mAdapter).clearCheckedItems();
					
				} else {
					//清空失败
					showDialog(BaseActivity.MODAL_DIALOG, "邮件清空失败！");
				}
			}
		};
	}
	
}
