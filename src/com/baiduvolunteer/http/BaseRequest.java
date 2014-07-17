package com.baiduvolunteer.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;

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

	protected abstract String method();

	protected abstract void generateParams(HashMap<String, String> map);

	public BaseRequest setHandler(ResponseHandler handler) {
		this.handler = handler;
		return this;
	}

	protected abstract HttpMethod requestMethod();

	protected boolean cachable() {
		return cachable;
	}

	public final String cacheURL() {
		URIBuilder builder = new URIBuilder(String.format("%s%s",
				Config.baseURL, url()));
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
		params.put("method",method());
		String sig = SignatureTool.getSignature(params);
		for (String key : params.keySet()) {
			requestParams.addQueryStringParameter(key, params.get(key));
		}
		requestParams.addQueryStringParameter("signature", sig);
		List<NameValuePair> list = requestParams.getQueryStringParams();
		for (NameValuePair pair : list) {
			Log.d("test",
					"key: " + pair.getName() + ", value: " + pair.getValue());
		}

		String url = String.format("%s%s", Config.baseURL, url());
		if (requestMethod() == HttpMethod.GET && cachable) {

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
		HttpUtilsWrapper.sharedInstance().send(requestMethod(), url, requestParams,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> info) {
						if (cached)
							HttpUtils.sHttpCache.put(cacheURL(), info.result);
						if (handler != null) {
							handler.handleResponse(BaseRequest.this, 200, null,
									info.result);
						}
						Log.d("test", "success: code " + info.statusCode);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						if (handler != null) {
							handler.handleResponse(BaseRequest.this,
									arg0.getExceptionCode(), null, null);
						}
						Log.d("test", "error: code " + arg0.getExceptionCode());
					}
				});
	}

}
