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

public class ContactsSelectorAdapter extends ArrayAdapter<ContactItem> {

	private LayoutInflater mLayoutInflater;
	private List<ContactItem> mDataList;

	public ContactsSelectorAdapter(Context context, int resource,
			List<ContactItem> objects) {
		super(context, resource, objects);
		
		mLayoutInflater = LayoutInflater.from(context);
		mDataList = objects;
	}
	
	@Override
	public ContactItem getItem(int position) {
		return mDataList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.contacts_list_item_select, parent, false);
			holder = new ViewHolder();
			holder.alpha = (TextView)convertView.findViewById(R.id.alpha_text);
			holder.name = (CheckedTextView)convertView.findViewById(R.id.name);
			holder.number = (TextView)convertView.findViewById(R.id.number);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		ContactItem item = getItem(position);
		holder.name.setText(item.userName);
		holder.name.setChecked(item.checked);
		String cAlpha = item.alphaU;
		String pAlpha = (position - 1) >= 0 ? getItem(position - 1).alphaU : " ";
		if (!pAlpha.equals(cAlpha)) {
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(cAlpha);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	public void setData(List<ContactItem> data) {
		this.mDataList = data;
	}
	
	public List<ContactItem> getData() {
		return mDataList;
	}
	
	public void clearSelected() {
		for(ContactItem item : mDataList) {
			item.checked = false;
		}
	}
	
	public List<ContactItem> getSelectedContacts() {
		List<ContactItem> list = new ArrayList<ContactItem>();
		for(ContactItem item : mDataList) {
			if(item.checked) list.add(item);
		}
		return list;
	}
	
	private final class ViewHolder {
		TextView alpha;
		CheckedTextView name;
		TextView number;
	}

}
