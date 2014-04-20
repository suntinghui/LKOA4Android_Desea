package com.lkoa.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

public class BaseListAdapter<T> extends ArrayAdapter<T> {
	public static final int COUNT_PER_PAGE = 10; 
	protected int mCurrPage;
	protected int mItemCount;
	protected int mPageCount;
	
	protected List<T> mDataList;
	
	protected LayoutInflater mLayoutInflater;
	protected Resources mRes;
	protected OnClickListener mOnClickListener;

	public BaseListAdapter(Context context, int textViewResourceId,
			List<T> objects) {
		super(context, textViewResourceId, objects);
		mItemCount = objects.size();
		mPageCount = (mItemCount + COUNT_PER_PAGE - 1) / COUNT_PER_PAGE;
		mDataList = objects;		
		mRes = context.getResources();
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	public void setOnClickListener(OnClickListener listener) {
		mOnClickListener = listener;
	}
	
	public void showPage(int page) {
		mCurrPage = page;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(mCurrPage < mPageCount - 1) {
			return COUNT_PER_PAGE;
		}
		
		return mItemCount % COUNT_PER_PAGE;
	}
	
	@Override
	public T getItem(int position) {
		if(position > mDataList.size() - 1) return null;
		return mDataList.get(position);
	}

	public void setData(List<T> data) {
		this.mDataList.clear();
		mDataList.addAll(data);
	}
	
	public List<T> getData() {
		return mDataList;
	}
	
	@Override
	public void notifyDataSetChanged() {
		mItemCount = mDataList.size();
		mPageCount = (mItemCount + COUNT_PER_PAGE - 1) / COUNT_PER_PAGE;
		super.notifyDataSetChanged();
	}
	
	protected int getRealPosition(int position) {
		return mCurrPage * COUNT_PER_PAGE + position;
	}
}
