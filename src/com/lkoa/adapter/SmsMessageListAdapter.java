package com.lkoa.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.model.ProcessItem;
import com.lkoa.model.SmsMessage;

public class SmsMessageListAdapter extends BaseListAdapter<SmsMessage> {
	
	public SmsMessageListAdapter(Context context, int resource,
			List<SmsMessage> objects) {
		super(context, resource, objects);
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
		
		position = getRealPosition(position);
		SmsMessage item = getItem(position);
		if(TextUtils.isEmpty(item.jsrPhone)) {
			holder.jsrAndPhone.setText((position+1) + ". "+item.jsr);
		} else {
			holder.jsrAndPhone.setText((position+1) + ". "+mRes.getString(
					R.string.my_sms_jsr_and_phone, item.jsr, item.jsrPhone));
		}
		holder.content.setText(item.content);
		holder.date.setText(item.date);
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView jsrAndPhone;
		TextView content;
		TextView date;
	}

}
