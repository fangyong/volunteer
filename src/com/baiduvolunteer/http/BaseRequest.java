package com.baiduvolunteer.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baiduvolunteer.config.Config;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.util.URIBuilder;

public abstract class BaseRequest {

	public static abstract class ResponseHandler {
		public abstract void handleResponse(BaseRequest request,
				int statusCode, String errorMsg, String response);

		public void handleError(BaseRequest request, int statusCode,
				String errorMsg) {
			Log.e("test", "connection error on request " + request.method()
					+ "response:" + errorMsg);
		}
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
		builder.addParameter("method", method());
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

	/**
	 * that's how it really works
	 */
	public void start() {
		RequestParams requestParams = new RequestParams();
		params.clear();
		generateParams(params);
		params.put("method", method());
		if (User.sharedUser().vuid != null
				&& !TextUtils.isEmpty(User.sharedUser().vuid))
			params.put("vuid", User.sharedUser().vuid);
		String sig = SignatureTool.getSignature(params);
		for (String key : params.keySet()) {
			try {
				requestParams.addQueryStringParameter(key,
						URLEncoder.encode(params.get(key), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	private void doSendRequest(final String url, RequestParams requestParams,
			final boolean cached) {
		HttpUtilsWrapper.sharedInstance().send(requestMethod(), url,
				requestParams, new RequestCallBack<String>() {
					@Override
					public void onSuccess(final ResponseInfo<String> info) {
						if (cached)
							HttpUtils.sHttpCache.put(cacheURL(), info.result);
						boolean success = false;
						Log.d("test", "response:" + info.result);
						JSONObject result = null;
						if (info.result != null) {
							try {
								JSONObject obj = new JSONObject(info.result);
								JSONObject responseHead = obj
										.optJSONObject("responseHead");
								success = responseHead != null
										&& responseHead.optString("success",
												null) != null;
								result = obj.optJSONObject("result");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (success && handler != null) {
							// new Thread(new Runnable() {
							//
							// @Override
							// public void run() {
							// TODO Auto-generated method stub
							handler.handleResponse(BaseRequest.this,
									info.statusCode, null, info.result);
							// }
							// }).start();
						} else if (handler != null) {
							// new Thread(new Runnable() {

							// @Override
							// public void run() {
							// TODO Auto-generated method stub
							handler.handleError(BaseRequest.this,
									info.statusCode, info.result);
							// }
							// }).start();
						}

					}

					@Override
					public void onFailure(final HttpException arg0,
							final String arg1) {
						// TODO Auto-generated method stub
						Log.d("test", "url " + url + " error" + arg1);
						if (handler != null) {
							ViewUtils.runInMainThread(new Runnable() {

								@Override
								public void run() {
									ViewUtils.showToast("网络连接出错",
											Toast.LENGTH_LONG);
									// TODO Auto-generated method stub
									// Toast.makeText(ViewUtils.getContext(),
									// "网络连接出错", Toast.LENGTH_LONG).show();
									handler.handleError(BaseRequest.this,
											arg0.getExceptionCode(), null);
								}
							});
						}
						Log.d("test", "error: code " + arg0.getExceptionCode());
					}
				});
	}
}
