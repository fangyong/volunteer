package com.baiduvolunteer.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;

import android.util.Log;

import com.baiduvolunteer.config.Config;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.util.URIBuilder;

public abstract class BaseRequest {

	public static interface ResponseHandler {
		public void handleResponse(BaseRequest request, int statusCode,
				String errorMsg, String response);
	}

	private HashMap<String, String> params = new HashMap<String, String>();

	private ResponseHandler handler;

	private boolean cachable;

	public void setCachable(boolean cachable) {
		this.cachable = cachable;
	}

	/**
	 * 子类override该方法用于设置访问url
	 * 
	 * @return
	 */
	protected abstract String url();

	protected abstract void generateParams(HashMap<String, String> map);

	public BaseRequest setHandler(ResponseHandler handler) {
		this.handler = handler;
		return this;
	}

	protected abstract HttpMethod getMethod();

	protected boolean cachable() {
		return cachable;
	}

	public final String cacheURL() {
		URIBuilder builder = new URIBuilder(String.format("%s%s", Config.baseURL,url()));
		for (String key : params.keySet()) {
			builder.addParameter(key, params.get(key));
		}

		try {
			URI uri = builder.build(Charset.defaultCharset());
			return uri.toString();
		} catch (URISyntaxException e) {
			return null;
		}
	}

	public final void start() {
		RequestParams requestParams = new RequestParams();
		params.clear();
		generateParams(params);
		String sig = SignatureTool.getSignature(params);
		for (String key : params.keySet()) {
			requestParams.addQueryStringParameter(key, params.get(key));
		}
		String url = String.format("%s%s", Config.baseURL, url());
		if (getMethod() == HttpMethod.GET && cachable) {
			URIBuilder builder = new URIBuilder();
			for (String key : params.keySet()) {
				builder.addParameter(key, params.get(key));
			}

			String uri = cacheURL();
			String result = HttpUtils.sHttpCache.get(uri);
			if (result != null) {
				if (handler != null) {
					handler.handleResponse(BaseRequest.this, 200, null, result);
				}
			} else {
				doSendRequest(url, requestParams, cachable);
			}
		} else {
			doSendRequest(url, requestParams, false);
		}
	}

	private void doSendRequest(String url, RequestParams requestParams,
			final boolean cached) {
		HttpUtilsWrapper.sharedInstance().send(getMethod(), url, requestParams,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> info) {
						if (cached)
							HttpUtils.sHttpCache.put(cacheURL(), info.result);
						if (handler != null) {
							handler.handleResponse(BaseRequest.this, 200, null,
									info.result);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						if (handler != null) {
							handler.handleResponse(BaseRequest.this,
									arg0.getExceptionCode(), null, null);
						}
					}
				});
	}

}
