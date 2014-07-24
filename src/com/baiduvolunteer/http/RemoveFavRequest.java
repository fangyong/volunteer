package com.baiduvolunteer.http;

import java.util.HashMap;

import com.baiduvolunteer.http.AddFavRequest.AddFavType;
import com.baiduvolunteer.http.AddFavRequest.PublisherType;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class RemoveFavRequest extends BaseRequest {

	private PublisherType publisherType;
	private RemoveFavType removeType;
	private String id;

	public RemoveFavRequest setId(String id) {
		this.id = id;
		return this;
	}

	public RemoveFavRequest setRemoveType(RemoveFavType removeType) {
		this.removeType = removeType;
		return this;
	}

	public RemoveFavRequest setPublisherType(PublisherType publisherType) {
		this.publisherType = publisherType;
		return this;
	}

	@Override
	protected String url() {
		// TODO Auto-generated method stub
		return "user";
	}

	@Override
	protected String method() {
		// TODO Auto-generated method stub
		return "uncollections";
	}

	public static enum RemoveFavType {
		RemoveFavTypeActivity, RemoveFavTypePublisher
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		map.put("type",
				removeType == RemoveFavType.RemoveFavTypeActivity ? "activity"
						: "publish");
		if (removeType == RemoveFavType.RemoveFavTypePublisher)
			map.put("publisherType",
					publisherType == PublisherType.PublisherTypeAPP ? "volunteerapp"
							: "gongyixiang");
		map.put(removeType == RemoveFavType.RemoveFavTypeActivity ? "activityId"
				: "publishId", id);

	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
