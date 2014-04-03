package com.lkoa.business;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewConfig {
	
	private Context mContext;
	
	public WebViewConfig(Context context) {
		mContext = context;
	}

	public void config(WebView webView) {
		webView.setWebViewClient(new AppWebViewClients());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
		webView.getSettings().setSupportZoom(true);
		
		webView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype, long contentLength) {
				Uri uri = Uri.parse(url);  
	            Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
	            mContext.startActivity(intent);   
			}
		});
	}
	
	private class AppWebViewClients extends WebViewClient {

	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }

	    @Override
	    public void onPageFinished(WebView view, String url) {
	        super.onPageFinished(view, url);

	    }
	}
}
