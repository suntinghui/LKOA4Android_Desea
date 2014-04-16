package com.lkoa.activity;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.lkoa.R;
import com.lkoa.adapter.BaseListAdapter;
import com.lkoa.model.ProcessItem;


public abstract class CenterMsgBaseListActivity<T> extends CenterMsgBaseActivity {
	
	protected Button mPagePrev, mPageNext;
	protected TextView mPageIndicator;
	protected int mCurrPageNo;
	protected int mPageCount;
	protected BaseListAdapter<T> mAdapter;
	
	private OnClickListener mPageOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final int id = v.getId();
			if(id == R.id.page_prev) {
				prevPage();
			} else if(id == R.id.page_next) {
				nextPage();
			}
		}
	};
	
	@Override
	protected void findViews() {
		super.findViews();
		
		mPagePrev = (Button)findViewById(R.id.page_prev);
		mPageNext = (Button)findViewById(R.id.page_next);
		mPageIndicator = (TextView) findViewById(R.id.page_indicator);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		
		mPagePrev.setOnClickListener(mPageOnClickListener);
		mPageNext.setOnClickListener(mPageOnClickListener);
	}
	
	protected void prevPage() {
		mCurrPageNo--;
		if(mCurrPageNo < 0) {
			mCurrPageNo = 0;
		}
		updatePageControlState();
		mAdapter.showPage(mCurrPageNo);
	}
	
	protected void nextPage() {
		mCurrPageNo++;
		if(mCurrPageNo > mPageCount - 1) {
			mCurrPageNo = mPageCount - 1;
		}
		updatePageControlState();
		mAdapter.showPage(mCurrPageNo);
	}
	
	protected void updatePageControlState() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(mCurrPageNo + 1 > mPageCount ? mPageCount : mCurrPageNo + 1);
		buffer.append("/");
		buffer.append(mPageCount);
		mPageIndicator.setText(buffer.toString());
		
		int indexCount = mPageCount - 1;
		if(mCurrPageNo <= 0) {
			mPagePrev.setEnabled(false);
		} else {
			mPagePrev.setEnabled(true);
		}
		
		if(mCurrPageNo >= indexCount) {
			mPageNext.setEnabled(false);
		} else {
			mPageNext.setEnabled(true);
		}
	}
	
	protected int getPageCount(List<T> list) {
		if(list == null || list.size() < 1) return 0;
		
		int size = list.size();
		return (size + BaseListAdapter.COUNT_PER_PAGE - 1) / BaseListAdapter.COUNT_PER_PAGE;
	}
	
	protected int getRealPosition(int position) {
		return mCurrPageNo * BaseListAdapter.COUNT_PER_PAGE + position;
	}
	
	protected void resetPageState(List<T> list) {
		mPageCount = getPageCount(list);
		mCurrPageNo = 0;
		if(mAdapter != null)mAdapter.showPage(mCurrPageNo);
		updatePageControlState();
	}
}
