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

public class ProcessWorkListAdapter extends BaseListAdapter<ProcessItem> {

	public ProcessWorkListAdapter(Context context, int resource,
			List<ProcessItem> objects) {
		super(context, resource, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.process_work_list_item, parent, false);
			holder = new ViewHolder();
			holder.title = (TextView)convertView.findViewById(R.id.title);
			holder.type = (TextView)convertView.findViewById(R.id.type);
			holder.task = (TextView)convertView.findViewById(R.id.task);
			holder.sender = (TextView)convertView.findViewById(R.id.sender);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		position = getRealPosition(position);
		
		if(position % 2 == 0) {
			//135
			convertView.setBackgroundResource(R.drawable.center_msg_news_item_bg_135);
		} else {
			//246
			convertView.setBackgroundResource(R.drawable.center_msg_news_item_bg_246);
		}
		
		ProcessItem item = getItem(position);
		holder.title.setText((position+1) +". "+item.title);
		holder.type.setText(mRes.getString(R.string.process_work_list_type, item.type));
		holder.task.setText(mRes.getString(R.string.process_work_list_task, item.task));
		holder.sender.setText(mRes.getString(R.string.process_work_list_sender, item.sender));
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView title;
		TextView type;
		TextView task;
		TextView sender;
	}

}
