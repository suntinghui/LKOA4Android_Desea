package com.lkoa.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.model.ProcessItem;
import com.lkoa.model.SmsMessage;

public class SmsMessageListAdapter extends ArrayAdapter<SmsMessage> {

	private LayoutInflater mLayoutInflater;
	private List<SmsMessage> mDataList;
	
	private Resources mRes;

	public SmsMessageListAdapter(Context context, int resource,
			List<SmsMessage> objects) {
		super(context, resource, objects);
		
		mRes = context.getResources();
		
		mLayoutInflater = LayoutInflater.from(context);
		mDataList = objects;
	}
	
	@Override
	public SmsMessage getItem(int position) {
		return mDataList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.my_sms_list_item, parent, false);
			holder = new ViewHolder();
			holder.jsrAndPhone = (TextView)convertView.findViewById(R.id.jsr_and_phone);
			holder.content = (TextView)convertView.findViewById(R.id.content);
			holder.date = (TextView)convertView.findViewById(R.id.date);
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
		
		SmsMessage item = getItem(position);
		holder.jsrAndPhone.setText(mRes.getString(
				R.string.my_sms_jsr_and_phone, item.jsr, item.jsrPhone));
		holder.content.setText(item.content);
		holder.date.setText(item.date);
		
		return convertView;
	}
	
	public void setData(List<SmsMessage> data) {
		this.mDataList = data;
	}
	
	public List<SmsMessage> getData() {
		return mDataList;
	}
	
	private class ViewHolder {
		TextView jsrAndPhone;
		TextView content;
		TextView date;
	}

}
