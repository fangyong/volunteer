package com.baiduvolunteer.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.baiduvolunteer.config.Config;

public class SignatureTool {
	public final static JSONObject getSignedJson(
			HashMap<String, String> inParams) throws JSONException {

		JSONObject outJson = new JSONObject(inParams);
		outJson.put("sign", getSignature(inParams));
		return outJson;
	}

	public static final String getSignature(HashMap<String, String> inParams) {
		ArrayList<String> sortKeys = new ArrayList<String>();
		HashMap<String, String> signParams = new HashMap<String, String>(
				inParams);
		signParams.put("app_key", Config.APP_SECRET);
		for (String key : signParams.keySet()) {
			if (signParams.get(key) != null) {
				sortKeys.add(key);
			}
		}
		Collections.sort(sortKeys);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sortKeys.size(); i++) {
			String key = sortKeys.get(i);
			if (i > 0)
				sb.append("&");
			// Log.d("test","sorted Key:"+key);
//			sb.append(key);
//			sb.append("=");
			String value;
			try {
				value = URLEncoder.encode(signParams.get(key),"utf-8");
				sb.append(value);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// try {
			// String encodeValue = URLEncoder.encode(
			// String.format("%s", value), "utf-8").replace("+",
			// "%20");
			
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

		}
		String s = MD5.md5(sb.toString());
//		Log.d("test", "base Str:" + sb.toString());
		// sb.append(Config.APP_SECRET);
		// s = MD5.md5(sb.toString());
		return s;
	}

	public static final String getSignedParams(HashMap<String, String> inParams) {
		StringBuilder sb = new StringBuilder();
		for (String key : inParams.keySet()) {
			try {
				String value;
				value = URLEncoder.encode(
						String.format("%s", inParams.get(key)), "utf-8");
				sb.append(String.format("%s=%s&", key, value));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		sb.append(String.format("sign=%s", getSignature(inParams)));
		return sb.toString();
	}
}
