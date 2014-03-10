package com.lkoa.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.model.ContactItem;
import com.lkoa.model.ProcessContentInfo.Activity;

/**
 * 流程办理节点选择Adapter
 */
public class ProcessWorkNodeSelectAdapter extends ArrayAdapter<Activity> {

	private LayoutInflater mLayoutInflater;
	private List<Activity> mDataList;

	public ProcessWorkNodeSelectAdapter(Context context, int resource,
			List<Activity> objects) {
		super(context, resource, objects);
		
		mLayoutInflater = LayoutInflater.from(context);
		mDataList = objects;
	}
	
	@Override
	public Activity getItem(int position) {
		return mDataList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.contacts_list_item_select, parent, false);
			holder = new ViewHolder();
			holder.name = (CheckedTextView)convertView.findViewById(R.id.name);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Activity item = getItem(position);
		holder.name.setText(item.name);
		holder.name.setChecked(item.select == 1);
		
		return convertView;
	}
	
	public void setData(List<Activity> data) {
		this.mDataList = data;
	}
	
	public List<Activity> getData() {
		return mDataList;
	}
	
	public void clearSelected() {
		for(Activity item : mDataList) {
			item.select = 0;
		}
	}
	
	public List<Activity> getSelectedContacts() {
		List<Activity> list = new ArrayList<Activity>();
		for(Activity item : mDataList) {
			if(item.select == 1) list.add(item);
		}
		return list;
	}
	
	private final class ViewHolder {
		CheckedTextView name;
	}

}
