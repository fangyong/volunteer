package com.baiduvolunteer.task;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;

public class LoadProvinceListTask extends AsyncTask<String, Void, Integer> {
	private Activity activity;
	private NodeList provinceList;
	private OnTaskFinishListener onTaskFinishListener;
	private Dialog mProgressDialog;

	public LoadProvinceListTask(OnTaskFinishListener onTaskFinishListener,
			Activity activity) {
		this.onTaskFinishListener = onTaskFinishListener;
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		// mProgressDialog = new Dialog(this.activity,
		// R.style.theme_dialog_alert);
		// mProgressDialog.setContentView(R.layout.progress_dialog);
		// mProgressDialog.show();
	}

	@Override
	protected Integer doInBackground(String... params) {

		try {
			InputStream inputstream = activity.getAssets().open(
					"province_city_district.plist");
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inputstream);
			Element root = document.getDocumentElement();
			Log.i("root", root.getTagName());
			// 获得省市列表
			provinceList = root.getChildNodes().item(1).getChildNodes();
			// 获得城市列表
			// NodeList citylist = provincelist.item(0).getChildNodes().item(15)
			// .getChildNodes();
			// 获得地区列表
			// NodeList districtlist = citylist.item(0).getChildNodes().item(15)
			// .getChildNodes();
			// Log.i("node", districtlist.getLength() + "");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 1;
	}

	public interface OnTaskFinishListener {
		void onTaskFinish(NodeList nodeList);
	}

	@Override
	protected void onPostExecute(Integer result) {
		int state = result.intValue();
		if (state == 1) {
			onTaskFinishListener.onTaskFinish(provinceList);
		}
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	public String read() {
		InputStream inputStream = null;
		BufferedInputStream bis = null;
		String myString = null;
		try {
			InputStream inputstream = activity.getAssets().open(
					"province_city_district.plist");
			bis = new BufferedInputStream(inputstream);

			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			myString = EncodingUtils.getString(baf.toByteArray(), "UTF-8");
		} catch (IOException e) {
			Log.i("file is exist", "no");
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}

				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.i("myString", myString);
		return myString;
	}

	public NodeList getProvinceList() {
		return provinceList;
	}

}
