package com.baiduvolunteer.http;

import java.util.HashMap;

import com.baiduvolunteer.model.Publisher.PublisherType;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class AddFavRequest extends BaseRequest {

	public static enum AddFavType {
		AddFavTypeActivity, AddFavTypePublisher
	}

	

	private PublisherType publisherType;
	private AddFavType addType;
	private String id;

	public AddFavRequest setAddType(AddFavType addType) {
		this.addType = addType;
		return this;
	}

	public AddFavRequest setPublisherType(PublisherType publisherType) {
		this.publisherType = publisherType;
		return this;
	}

	public AddFavRequest setId(String id) {
		this.id = id;
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
		return "collections";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		map.put("type", addType == AddFavType.AddFavTypeActivity ? "activity"
				: "publisher");
		if (addType == AddFavType.AddFavTypePublisher)
			map.put("publishType",
					publisherType == PublisherType.PublisherTypeAPP ? "baiduvolunteer"
							: "gongyixiang");
		map.put(addType == AddFavType.AddFavTypeActivity ? "activityId"
				: "publishId", id);
	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
