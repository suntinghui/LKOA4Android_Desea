package com.lkoa.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.model.MailItemInfo;
import com.lkoa.model.ProcessItem;
import com.lkoa.model.SmsMessage;

public class MyMailListAdapter extends ArrayAdapter<MailItemInfo> {

	private LayoutInflater mLayoutInflater;
	private List<MailItemInfo> mDataList;
	
	public MyMailListAdapter(Context context, int resource,
			List<MailItemInfo> objects) {
		super(context, resource, objects);
		
		mLayoutInflater = LayoutInflater.from(context);
		mDataList = objects;
	}
	
	@Override
	public MailItemInfo getItem(int position) {
		return mDataList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.my_mail_list_item, parent, false);
			holder = new ViewHolder();
			holder.sender = (TextView)convertView.findViewById(R.id.sender);
			holder.subject = (TextView)convertView.findViewById(R.id.subject);
			holder.date = (TextView)convertView.findViewById(R.id.date);
			holder.fj = (ImageView)convertView.findViewById(R.id.iv_fj);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		if(position % 2 == 0) {
			//135
			convertView.setBackgroundResource(R.drawable.center_msg_news_item_bg_135);
		} else {
			//246
			convertView.setBackgroundResource(R.drawable.center_msg_news_item_bg_246);
		}
		
		MailItemInfo item = getItem(position);
		holder.sender.setText(item.sender);
		holder.subject.setText(item.subject);
		holder.date.setText(item.date);
		
		if(item.fjCount > 0) {
			holder.fj.setVisibility(View.VISIBLE);
		} else {
			holder.fj.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	public void setData(List<MailItemInfo> data) {
		this.mDataList = data;
	}
	
	public List<MailItemInfo> getData() {
		return mDataList;
	}
	
	private class ViewHolder {
		TextView sender;
		TextView subject;
		TextView date;
		ImageView fj;
	}

}
