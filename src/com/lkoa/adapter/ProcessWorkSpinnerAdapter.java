package com.lkoa.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.model.CenterMsgNewsItem;
import com.lkoa.model.ContactItem;
import com.lkoa.model.ProcessContentInfo.Option;

public class ProcessWorkSpinnerAdapter extends ArrayAdapter<Option> {

	private LayoutInflater mLayoutInflater;
	private List<Option> mDataList;
	
	public ProcessWorkSpinnerAdapter(Context context, int resource,
			List<Option> objects) {
		super(context, resource, objects);
		
		mLayoutInflater = LayoutInflater.from(context);
		mDataList = objects;
	}
	
	@Override
	public Option getItem(int position) {
		return mDataList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(android.R.layout.simple_spinner_item, parent, false);
			holder = new ViewHolder();
			holder.text = (TextView)convertView.findViewById(android.R.id.text1);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Option item = getItem(position);
		holder.text.setText(item.text);
		
		return convertView;
	}
	
	public void setData(List<Option> data) {
		this.mDataList = data;
	}
	
	public List<Option> getData() {
		return mDataList;
	}
	
	private final class ViewHolder {
		TextView text;
	}

}
