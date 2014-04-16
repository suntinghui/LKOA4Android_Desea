package com.lkoa.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.model.MailItemInfo;
import com.lkoa.model.ProcessItem;
import com.lkoa.model.SmsMessage;
import com.lkoa.util.LogUtil;

public class MyMailListAdapter extends BaseListAdapter<MailItemInfo> {
	
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
		
		MailItemInfo item = getItem(getRealPosition(position));
		holder.sender.setText(item.sender);
		holder.subject.setText(item.subject);
		holder.date.setText(item.date);
		
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
			holder.fj.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView sender;
		TextView subject;
		TextView date;
		ImageView fj;
	}

}
