package com.baiduvolunteer.http;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.SearchRequest.SearchType;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.Publisher;

public class SearchAllRequest {
	private boolean success = false;
	private ArrayList<Object> resultList = new ArrayList<Object>();

	public static abstract class SearchAllResponseHandler {
		public abstract void handleSuccess(ArrayList<Object> results);

		public void handleError(int statusCode, String errorMsg) {

		}
	}

	public SearchAllRequest setLat(double lat) {
		request1.setLat(lat);
		request2.setLat(lat);
		return this;
	}

	public SearchAllRequest setLng(double lng) {
		request1.setLng(lng);
		request2.setLng(lng);
		return this;
	}

	public SearchAllRequest setSize(int size) {
		request1.setSize(size);
		request2.setSize(size);
		return this;
	}

	public SearchAllRequest setEnd(long end) {
		request1.setEnd(end);
		request2.setEnd(end);
		return this;
	}

	public SearchAllRequest setKey(String key) {
		request1.setKey(key);
		request2.setKey(key);
		return this;
	}

	public SearchAllRequest() {
		// TODO Auto-generated constructor stub
		request1 = new SearchRequest();
		request1.setSearchType(SearchType.SearchTypeActivity);
		request2 = new SearchRequest();
		request2.setSearchType(SearchType.SearchTypePublisher);
	}

	public void start() {
		tasks = 2;
		success = true;
		resultList.clear();
		request1.setHandler(new ResponseHandler() {

			@Override
			public void handleResponse(BaseRequest request, int statusCode,
					String errorMsg, String response) {
				// TODO Auto-generated method stub
				tasks--;
				Log.d("test", "search request " + response);
				try {
					JSONObject obj = new JSONObject(response);
					obj = obj.optJSONObject("result");
					if (obj != null) {
						JSONArray array = obj.optJSONArray("activities");
						if (array != null) {
							for (int i = 0; i < array.length(); i++) {
								ActivityInfo info = ActivityInfo
										.createFromJson(array.getJSONObject(i));
								resultList.add(info);
							}
						}

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (tasks == 0)
					returnResult();
			}

			@Override
			public void handleError(BaseRequest request, int statusCode,
					String errorMsg) {
				tasks--;
				success &= false;
				if (tasks == 0)
					returnResult();
				// TODO Auto-generated method stub
				super.handleError(request, statusCode, errorMsg);
			}
		}).start();
		request2.setHandler(new ResponseHandler() {

			@Override
			public void handleResponse(BaseRequest request, int statusCode,
					String errorMsg, String response) {
				// TODO Auto-generated method stub
				Log.d("test", "search request " + response);
				tasks--;
				try {
					JSONObject obj = new JSONObject(response);
					obj = obj.optJSONObject("result");
					if (obj != null) {
						JSONArray array = obj.optJSONArray("publishers");
						if (array != null) {
							for (int i = 0; i < array.length(); i++) {
								Publisher info = Publisher.createFromJson(array
										.getJSONObject(i));
								resultList.add(info);
							}
						}

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (tasks == 0)
					returnResult();
			}

			@Override
			public void handleError(BaseRequest request, int statusCode,
					String errorMsg) {
				// TODO Auto-generated method stub
				tasks--;
				success &= false;
				if (tasks == 0)
					returnResult();
				super.handleError(request, statusCode, errorMsg);
			}
		}).start();
	}

	private SearchAllResponseHandler handler;

	public SearchAllRequest setHandler(
			SearchAllResponseHandler searchAllResponseHandler) {
		this.handler = searchAllResponseHandler;
		return this;
	}

	private void returnResult() {
		Log.d("test", "result:" + resultList.size());
		if (success && handler != null) {
			handler.handleSuccess(resultList);
		} else {
			handler.handleError(-1, null);
		}
	}

	private int tasks = 2;

	private SearchRequest request1;
	private SearchRequest request2;

}
