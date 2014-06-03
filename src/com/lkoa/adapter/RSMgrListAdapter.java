package com.lkoa.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.model.ProcessItem;
import com.lkoa.model.RSItem;

public class RSMgrListAdapter extends BaseListAdapter<RSItem> {

	public RSMgrListAdapter(Context context, int resource,
			List<RSItem> objects) {
		super(context, resource, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.rs_mgr_list_item, parent, false);
			holder = new ViewHolder();
			holder.cqTime = (TextView)convertView.findViewById(R.id.cq_time);
			holder.cqState = (TextView)convertView.findViewById(R.id.cq_state);
			holder.cqTimePos = (TextView)convertView.findViewById(R.id.cq_time_pos);
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
		
		RSItem item = getItem(position);
		if(item != null) {
			holder.cqTimePos.setText((position+1) +". 出勤时间");
			holder.cqTime.setText(item.dkTime);
			holder.cqState.setText(item.dkType);
		}
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView cqTime;	//出勤时间
		TextView cqState;	//出勤状态
		TextView cqTimePos;	//考勤时间文本
	}

}
