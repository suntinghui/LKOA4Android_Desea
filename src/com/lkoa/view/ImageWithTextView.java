package com.lkoa.view;

import com.lkoa.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageWithTextView extends LinearLayout {
	
	private ImageView imageView;
	private TextView textView;
	private FrameLayout framLayout;
	private ImageView badgeImageView;
	private TextView badgeTextView;
	
	public ImageWithTextView(Context context) {
		super(context);
		
		init(context);
	}

	public ImageWithTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init(context);
	}
	
	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.image_with_text, this);
		
		imageView = (ImageView) this.findViewById(R.id.imageView);
		textView = (TextView) this.findViewById(R.id.textView);
		badgeImageView = (ImageView)this.findViewById(R.id.badgeImageView);
		badgeTextView = (TextView)this.findViewById(R.id.badgeNumText);
		framLayout = (FrameLayout)this.findViewById(R.id.framLayout);
	}
	
	public void setSelectedBackground(){
		framLayout.setBackgroundResource(R.drawable.slidebg);
	}
	
	public void DefaultBackground(){
		framLayout.setBackgroundResource(R.drawable.slid_touming_bg);
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public TextView getTextView() {
		return textView;
	}
	
	public TextView getBadgeTextView(){
		return badgeTextView;
	}
	
	public ImageView getBadgeImageView(){
		return badgeImageView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}
	
}
