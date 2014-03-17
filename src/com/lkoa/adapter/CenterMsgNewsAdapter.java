package com.lkoa.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.model.CenterMsgNewsItem;

public class CenterMsgNewsAdapter extends ArrayAdapter<CenterMsgNewsItem> {

	private LayoutInflater mLayoutInflater;
	private List<CenterMsgNewsItem> mDataList;

	public CenterMsgNewsAdapter(Context context, int resource,
			List<CenterMsgNewsItem> objects) {
		super(context, resource, objects);
		
		mLayoutInflater = LayoutInflater.from(context);
		mDataList = objects;
	}
	
	@Override
	public CenterMsgNewsItem getItem(int position) {
		return mDataList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.center_msg_news_item, parent, false);
			holder = new ViewHolder();
			holder.icon = (ImageView)convertView.findViewById(R.id.icon);
			holder.title = (TextView)convertView.findViewById(R.id.title);
			holder.date = (TextView)convertView.findViewById(R.id.date);
			holder.details = (TextView)convertView.findViewById(R.id.details);
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
		
		CenterMsgNewsItem item = getItem(position);
		holder.title.setText(item.title);
		/*if(TextUtils.equals(item.state, "0")) {
			holder.title.getPaint().setFakeBoldText(true);
		} else {
			holder.title.getPaint().setFakeBoldText(false);
		}*/
		holder.date.setText(item.date);
		holder.details.setText(item.content);
		if(item.iconUrl == null) {
			holder.icon.setVisibility(View.GONE);
		} else {
			holder.icon.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	public void setData(List<CenterMsgNewsItem> data) {
		this.mDataList = data;
	}
	
	public List<CenterMsgNewsItem> getData() {
		return mDataList;
	}
	
	private class ViewHolder {
		ImageView icon;
		TextView title;
		TextView date;
		TextView details;
	}

}
