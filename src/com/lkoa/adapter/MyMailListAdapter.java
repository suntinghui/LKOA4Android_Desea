package com.lkoa.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.model.MailItemInfo;
import com.lkoa.model.ProcessItem;
import com.lkoa.model.SmsMessage;
import com.lkoa.util.LogUtil;

public class MyMailListAdapter extends BaseListAdapter<MailItemInfo> {
	
	private List<MailItemInfo> mCheckedItems = new ArrayList<MailItemInfo>();
	
	public MyMailListAdapter(Context context, int resource,
			List<MailItemInfo> objects) {
		super(context, resource, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.my_mail_list_item, parent, false);
			holder = new ViewHolder();
			holder.sender = (TextView)convertView.findViewById(R.id.sender);
			holder.subject = (TextView)convertView.findViewById(R.id.subject);
			holder.date = (TextView)convertView.findViewById(R.id.date);
			holder.fj = (ImageView)convertView.findViewById(R.id.iv_fj);
			holder.check = (CheckBox)convertView.findViewById(R.id.check_box);
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
		
		int realPosition = getRealPosition(position);
		MailItemInfo item = getItem(realPosition);
		holder.check.setOnClickListener(mCheckOnClickListener);
		holder.check.setTag(item);
		holder.sender.setText((realPosition+1)+". "+item.sender);
		holder.subject.setText(item.subject);
		holder.date.setText(item.date);
		if(mCheckedItems.indexOf(item) != -1) {
			holder.check.setChecked(true);
		} else {
			holder.check.setChecked(false);
		}
		
		
		LogUtil.i("MyMailListAdapter", "getView(), item.state="+item.state);
		if(item.state == 0) {
			Paint p = holder.sender.getPaint();
			p.setTypeface(Typeface.DEFAULT_BOLD);
			p.setFakeBoldText(true);
			
			p = holder.subject.getPaint();
			p.setTypeface(Typeface.DEFAULT_BOLD);
			p.setFakeBoldText(true);
			
			p = holder.date.getPaint();
			p.setTypeface(Typeface.DEFAULT_BOLD);
			p.setFakeBoldText(true);
			
		} else {
			Paint p = holder.sender.getPaint();
			p.setTypeface(Typeface.DEFAULT);
			p.setFakeBoldText(false);
			
			p = holder.subject.getPaint();
			p.setTypeface(Typeface.DEFAULT);
			p.setFakeBoldText(false);
			
			p = holder.date.getPaint();
			p.setTypeface(Typeface.DEFAULT);
			p.setFakeBoldText(false);
		}
		
		if(item.fjCount > 0) {
			holder.fj.setVisibility(View.VISIBLE);
		} else {
			holder.fj.setVisibility(View.INVISIBLE);
		}
		
		convertView.setOnClickListener(mOnClickListener);
		convertView.setTag(R.string.key_tag, item);
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView sender;
		TextView subject;
		TextView date;
		ImageView fj;
		CheckBox check;
	}
	
	public List<MailItemInfo> getCheckeIds() {
		return mCheckedItems;
	}
	
	public void clearCheckedItems() {
		List<MailItemInfo> list = new ArrayList<MailItemInfo>();
		for(int i=mDataList.size()-1; i >= 0; i--) {
			MailItemInfo item = mDataList.get(i);
			if(!isChecked(item)) {
				list.add(item);
			} 
		}
		mCheckedItems.clear();
		setData(list);
		notifyDataSetChanged();
	}
	
	private boolean isChecked(MailItemInfo item) {
		if(item == null) return false;
		
		for(MailItemInfo i : mCheckedItems) {
			if(TextUtils.equals(item.id, i.id)) return true;
		}
		return false;
	}
	
	private OnClickListener mCheckOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			MailItemInfo info = (MailItemInfo)v.getTag();
			CheckBox check = (CheckBox) v;
			if(check.isChecked()) {
				mCheckedItems.add(info);
			} else {
				mCheckedItems.remove(info);
			}
		}
	};

}
