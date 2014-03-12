package com.lkoa.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lkoa.R;
import com.lkoa.model.LinkManModel;
import com.lkoa.util.Pinyin4j;
import com.lkoa.view.AlphaView;
import com.lkoa.view.AlphaView.OnAlphaChangedListener;

public class AddressListActivity extends Activity implements OnAlphaChangedListener {
	private ListView listView;
	private AlphaView alphaView;
	private TextView overlay;

	private WindowManager windowManager;
	private AsyncQueryHandler queryHandler;
	private List<LinkManModel> list;
	private ListAdapter adapter;
	private HashMap<String, Integer> alphaIndexer; // 存放存在的汉语拼音首字母和与之对应的列表位置
	private OverlayThread overlayThread;

	private static final Uri uri = Uri
			.parse("content://com.android.contacts/data/phones");
	private static final String[] projection = { "_id", "display_name",
			"data1", "sort_key" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_adresslist);

		list = new ArrayList<LinkManModel>();
		
		
		alphaIndexer = new HashMap<String, Integer>();
		overlayThread = new OverlayThread();
		intitWidget();
		initOverlay();
	}

	@Override
	protected void onResume() {
		super.onResume();
		new GetBankTask().execute();
		
		}

	@Override
	protected void onStop() {
		try {
			windowManager.removeViewImmediate(overlay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onStop();
	}

	// 初始化控件
	private void intitWidget() {
		listView = (ListView) findViewById(R.id.list_view);
		alphaView = (AlphaView) findViewById(R.id.alphaView);
		alphaView.setOnAlphaChangedListener(this);
	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private void setAdapter() {
		if (adapter == null) {
			adapter = new ListAdapter();
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	private class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public ListAdapter() {
			this.inflater = LayoutInflater.from(AddressListActivity.this);
			for (int i = 0; i < list.size(); i++) {
				// 当前汉语拼音首字母
				String currentAlpha = list.get(i).getAlpha();
				// 上一个汉语拼音首字母，如果不存在为“ ”
				String previewAlpha = (i - 1) >= 0 ? list.get(i - 1).getAlpha()
						: " ";
				if (!previewAlpha.equals(currentAlpha)) {
					String alpha = list.get(i).getAlpha();
					alphaIndexer.put(alpha, i);
				}
			}
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.contacts_list_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			LinkManModel item = list.get(position);
			holder.name.setText(item.getName());
			holder.number.setText(item.getPhone());

			String currentAlpha = list.get(position).getAlpha();
			String previewAlpha = (position - 1) >= 0 ? list.get(position - 1)
					.getAlpha() : " ";
			if (!previewAlpha.equals(currentAlpha)) {
				holder.alpha.setVisibility(View.VISIBLE);
				holder.alpha.setText(currentAlpha);
			} else {
				holder.alpha.setVisibility(View.GONE);
			}
			return convertView;
		}

	}

	class GetBankTask extends AsyncTask<Object, Object, Object>{

		@Override
		protected void onPostExecute(Object result) {

			setAdapter();
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			initData();
			return null;
		}
		
	}
	private final class ViewHolder {
		TextView alpha;
		TextView name;
		TextView number;

		public ViewHolder(View v) {
			alpha = (TextView) v.findViewById(R.id.alpha_text);
			name = (TextView) v.findViewById(R.id.name);
			number = (TextView) v.findViewById(R.id.number);
		}
	}

	private Handler handler = new Handler();

	// 设置overlay不可见
	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}

	@Override
	public void OnAlphaChanged(String s, int index) {
		if (s != null && s.trim().length() > 0) {
			overlay.setText(s);
			overlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(overlayThread);
			handler.postDelayed(overlayThread, 700);
			if (alphaIndexer.get(s.toLowerCase()) != null) {
				int position = alphaIndexer.get(s.toLowerCase());
				listView.setSelection(position);
			}
		}
	}

	private void initData(){
		LinkManModel model0 = new LinkManModel();
		model0.setName("啊佳");
		model0.setPhone("15972009300");
		model0.setAlpha(String.valueOf(Pinyin4j.getHanyuPinyin(model0.getName()).charAt(0)));
		LinkManModel model1 = new LinkManModel();
		model1.setName("彬佳");
		model1.setPhone("15972009300");
		model1.setAlpha(String.valueOf(Pinyin4j.getHanyuPinyin(model1.getName()).charAt(0)));
		LinkManModel model2 = new LinkManModel();
		model2.setName("才佳");
		model2.setPhone("15972009300");
		model2.setAlpha(String.valueOf(Pinyin4j.getHanyuPinyin(model2.getName()).charAt(0)));
		LinkManModel model3 = new LinkManModel();
		model3.setName("大佳");
		model3.setPhone("15972009300");
		model3.setAlpha(String.valueOf(Pinyin4j.getHanyuPinyin(model3.getName()).charAt(0)));
		LinkManModel model4 = new LinkManModel();
		model4.setName("方佳");
		model4.setPhone("15972009300");
		model4.setAlpha(String.valueOf(Pinyin4j.getHanyuPinyin(model4.getName()).charAt(0)));
		LinkManModel model5 = new LinkManModel();
		model5.setName("房佳");
		model5.setPhone("15972009300");
		model5.setAlpha(String.valueOf(Pinyin4j.getHanyuPinyin(model5.getName()).charAt(0)));
		LinkManModel model6 = new LinkManModel();
		model6.setName("官佳");
		model6.setPhone("15972009300");
		model6.setAlpha(String.valueOf(Pinyin4j.getHanyuPinyin(model6.getName()).charAt(0)));
		LinkManModel model7 = new LinkManModel();
		model7.setName("猛佳");
		model7.setPhone("15972009300");
		model7.setAlpha(String.valueOf(Pinyin4j.getHanyuPinyin(model7.getName()).charAt(0)));
		LinkManModel model8 = new LinkManModel();
		model8.setName("牛佳");
		model8.setPhone("15972009300");
		model8.setAlpha(String.valueOf(Pinyin4j.getHanyuPinyin(model8.getName()).charAt(0)));
		LinkManModel model9 = new LinkManModel();
		model9.setName("欧佳");
		model9.setPhone("15972009300");
		model9.setAlpha(String.valueOf(Pinyin4j.getHanyuPinyin(model9.getName()).charAt(0)));
		LinkManModel model10 = new LinkManModel();
		model10.setName("皮");
		model10.setPhone("15972009300");
		model10.setAlpha(String.valueOf(Pinyin4j.getHanyuPinyin(model10.getName()).charAt(0)));
		
		LinkManModel model11 = new LinkManModel();
		model11.setName("齐佳");
		model11.setPhone("15972009300");
		model11.setAlpha(String.valueOf(Pinyin4j.getHanyuPinyin(model11.getName()).charAt(0)));
		
		
		list.add(model0);
		list.add(model1);
		list.add(model2);
		list.add(model3);
		list.add(model4);
		list.add(model5);
		list.add(model6);
		list.add(model7);
		list.add(model8);
		list.add(model9);
		list.add(model10);
		list.add(model11);
		
	}
}