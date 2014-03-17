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
import com.lkoa.model.RCListItem;

public class ScheduleRCListAdapter extends ArrayAdapter<RCListItem> {

	private LayoutInflater mLayoutInflater;
	private List<RCListItem> mDataList;
	
	private Resources mRes;
	private String mRcTitle, mRcDate, mRcFw, mRcState;
	
	private int mColorTitle, mColorTitleVal, mColorStateVal;
	
	private boolean mShowRCFw = true;	//是否显示日程范围

	public ScheduleRCListAdapter(Context context, int resource,
			List<RCListItem> objects) {
		super(context, resource, objects);
		
		mRes = context.getResources();
		mRcTitle = mRes.getString(R.string.schedule_rc_title);
		mRcDate = mRes.getString(R.string.schedule_rc_date);
		mRcFw = mRes.getString(R.string.schedule_rc_fw);
		mRcState = mRes.getString(R.string.schedule_rc_state);
		
		mColorTitle = mRes.getColor(R.color.rc_title);
		mColorTitleVal = mRes.getColor(R.color.rc_title_value);
		mColorStateVal = mRes.getColor(R.color.rc_state_value);
		
		mLayoutInflater = LayoutInflater.from(context);
		mDataList = objects;
	}
	
	
	
	@Override
	public RCListItem getItem(int position) {
		return mDataList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.schedule_rc_list_item, parent, false);
			holder = new ViewHolder();
			View view = convertView.findViewById(R.id.rc_title);
			holder.rcTitleN = (TextView)view.findViewById(R.id.name);
			holder.rcTitleN.setTextColor(mColorTitle);
			holder.rcTitleVal = (TextView)view.findViewById(R.id.value);
			holder.rcTitleVal.setTextColor(mColorTitleVal);
			
			view = convertView.findViewById(R.id.rc_date);
			holder.rcDateN = (TextView)view.findViewById(R.id.name);
			holder.rcDateN.setTextColor(mColorTitle);
			holder.rcDateVal = (TextView)view.findViewById(R.id.value);
			
			view = convertView.findViewById(R.id.rc_fw);
			holder.rcFwN = (TextView)view.findViewById(R.id.name);
			holder.rcFwN.setTextColor(mColorTitle);
			holder.rcFwVal = (TextView)view.findViewById(R.id.value);
			
			view = convertView.findViewById(R.id.rc_state);
			holder.rcStateN = (TextView)view.findViewById(R.id.name);
			holder.rcStateN.setTextColor(mColorTitle);
			holder.rcStateVal = (TextView)view.findViewById(R.id.value);
			holder.rcStateVal.setTextColor(mColorStateVal);
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
		
		if(!mShowRCFw) {
			holder.rcFwN.setVisibility(View.INVISIBLE);
			holder.rcFwVal.setVisibility(View.INVISIBLE);
		}
		
		RCListItem item = getItem(position);
		holder.rcTitleN.setText(mRcTitle);
		holder.rcTitleVal.setText(item.title);
		
		holder.rcDateN.setText(mRcDate);
		holder.rcDateVal.setText(item.date);
		
		holder.rcFwN.setText(mRcFw);
		holder.rcFwVal.setText(item.fw);
		
		holder.rcStateN.setText(mRcState);
		holder.rcStateVal.setText(item.state);
		
		return convertView;
	}
	
	public void setShowRCFw(boolean show) {
		mShowRCFw = show;
	}
	
	public void setData(List<RCListItem> data) {
		this.mDataList = data;
	}
	
	public List<RCListItem> getData() {
		return mDataList;
	}
	
	private class ViewHolder {
		TextView rcTitleN, rcTitleVal;
		TextView rcDateN, rcDateVal;
		TextView rcFwN, rcFwVal;
		TextView rcStateN, rcStateVal;
	}

}
