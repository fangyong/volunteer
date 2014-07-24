package com.baiduvolunteer.task;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.util.Log;

import com.baiduvolunteer.config.Config;
import com.baiduvolunteer.config.Config.CityInfo;
import com.baiduvolunteer.util.ViewUtils;

public class LoadCitiesTask extends AsyncTask<String, Void, Integer> {

	private OnTaskFinishListener onTaskFinishListener;
	private ArrayList<CityInfo> provinceList;

	public void setOnTaskFinishListener(
			OnTaskFinishListener onTaskFinishListener) {
		this.onTaskFinishListener = onTaskFinishListener;
	}

	public LoadCitiesTask() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onPreExecute() {
		// mProgressDialog = new Dialog(this.activity,
		// R.style.theme_dialog_alert);
		// mProgressDialog.setContentView(R.layout.progress_dialog);
		// mProgressDialog.show();
		provinceList = new ArrayList<Config.CityInfo>();
	}

	@Override
	protected Integer doInBackground(String... params) {

		try {
			InputStream inputstream = ViewUtils.getContext().getAssets()
					.open("cities.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inputstream);
			Element root = document.getDocumentElement();
			Log.i("test", "root " + root.getTagName());
			// 获得省市列表
			NodeList cityList = root.getElementsByTagName("city");
			Log.d("test", "root childs count" + cityList.getLength());
			for (int i = 0; i < cityList.getLength(); i++) {
				Node node = cityList.item(i);
				String name = node.getAttributes().getNamedItem("name")
						.getTextContent();
				int id = Integer.valueOf(node.getAttributes()
						.getNamedItem("value").getTextContent());
				CityInfo info = new CityInfo();
				info.name = name;
				info.id = id;
				provinceList.add(info);
				NodeList subList = node.getChildNodes();
				if (subList == null)
					continue;
				for (int j = 0; j < subList.getLength(); j++) {
					Node subNode = subList.item(j);
					// name = subNode.getAttributes();
					if (subNode.getNodeType() != Node.ELEMENT_NODE)
						continue;
					name = subNode.getAttributes().getNamedItem("name")
							.getTextContent();
					id = Integer.valueOf(subNode.getAttributes()
							.getNamedItem("value").getTextContent());
//					CityInfo info = new CityInfo();
					 CityInfo subCity = new CityInfo();
					
					 subCity.name = name;
					 subCity.id = id;
					 if (info.subCityList == null) {
					 info.subCityList = new ArrayList<Config.CityInfo>();
					 }
					 info.subCityList.add(subCity);
				}
			}
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
		void onTaskFinish(ArrayList<CityInfo> nodeList);
	}

	@Override
	protected void onPostExecute(Integer result) {
		int state = result.intValue();
		if (state == 1) {
			onTaskFinishListener.onTaskFinish(provinceList);
		}
	}

	public String read() {
		InputStream inputStream = null;
		BufferedInputStream bis = null;
		String myString = null;
		try {
			InputStream inputstream = ViewUtils.getContext().getAssets()
					.open("province_city_district.plist");
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

	public ArrayList<CityInfo> getProvinceList() {
		return provinceList;
	}

}
