package com.lkoa.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.model.CenterMsgNewsItem;
import com.lkoa.model.ContactItem;
import com.lkoa.model.DepartmentItem;

public class DeptsAdapter extends ArrayAdapter<DepartmentItem> {

	private LayoutInflater mLayoutInflater;
	private List<DepartmentItem> mDataList;

	public DeptsAdapter(Context context, int resource,
			List<DepartmentItem> objects) {
		super(context, resource, objects);
		
		mLayoutInflater = LayoutInflater.from(context);
		mDataList = objects;
	}
	
	@Override
	public DepartmentItem getItem(int position) {
		return mDataList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.contacts_list_item, parent, false);
			holder = new ViewHolder();
			holder.alpha = (TextView)convertView.findViewById(R.id.alpha_text);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.number = (TextView)convertView.findViewById(R.id.number);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		DepartmentItem item = getItem(position);
		holder.name.setText(item.deptName);
		String cAlpha = item.alpha;
		String pAlpha = (position - 1) >= 0 ? getItem(position - 1).alpha : " ";
		if (!TextUtils.equals(pAlpha, cAlpha)) {
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(cAlpha);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	public void setData(List<DepartmentItem> data) {
		this.mDataList = data;
	}
	
	public List<DepartmentItem> getData() {
		return mDataList;
	}
	
	private final class ViewHolder {
		TextView alpha;
		TextView name;
		TextView number;
	}

}
