package com.baiduvolunteer.http;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.SearchRequest.SearchType;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.Publisher;

public class SearchAllRequest extends SearchRequest {
	private ArrayList<Object> resultList = new ArrayList<Object>();

	public static abstract class SearchAllResponseHandler {
		public abstract void handleSuccess(ArrayList<Object> results);

		public void handleError(int statusCode, String errorMsg) {

		}
	}

	private SearchAllResponseHandler responseHandler;

	public void setResponseHandler(SearchAllResponseHandler responseHandler) {
		this.responseHandler = responseHandler;
	}

	/**
	 * @deprecated wont call this for searchAll
	 */
	@Override
	public BaseRequest setHandler(ResponseHandler handler) {
		// TODO Auto-generated method stub
		return this;// ignore handler here
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		super.generateParams(map);
		map.put("type", "all");
	}

	public void start() {
		resultList.clear();
		super.setHandler(new ResponseHandler() {

			@Override
			public void handleResponse(BaseRequest request, int statusCode,
					String errorMsg, String response) {
				try {
					JSONObject obj = new JSONObject(response);
					obj = obj.optJSONObject("result");
					if (obj != null) {
						JSONArray array = obj.optJSONArray("search");
						if (array != null) {
							for (int i = 0; i < array.length(); i++) {
								JSONObject dataobj = array.getJSONObject(i);
								if (dataobj.optString("activityId", null) != null) {
									ActivityInfo info = ActivityInfo
											.createFromJson(dataobj);
									resultList.add(info);
								} else if (dataobj.optString("publisherId",
										null) != null) {
									Publisher publisher = Publisher
											.createFromJson(dataobj);
									resultList.add(publisher);
								}

							}
						}

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				returnResult();
			}

			@Override
			public void handleError(BaseRequest request, int statusCode,
					String errorMsg) {
				returnResult();
				// TODO Auto-generated method stub
				super.handleError(request, statusCode, errorMsg);
			}
		});
		super.start();

	}

	private void returnResult() {
		Log.d("test", "result:" + resultList.size());
		if (responseHandler == null)
			return;
		if (resultList != null) {
			responseHandler.handleSuccess(resultList);
		} else {
			responseHandler.handleError(-1, null);
		}
	}
}
