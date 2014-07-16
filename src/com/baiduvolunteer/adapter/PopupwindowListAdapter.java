package com.baiduvolunteer.adapter;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PopupwindowListAdapter extends BaseAdapter {
	private Activity activity;
	private NodeList nodeList;

	public PopupwindowListAdapter(Activity activity, NodeList nodeList) {
		this.activity = activity;
		this.nodeList = nodeList;
	}

	@Override
	public int getCount() {
		return nodeList.getLength();
	}

	@Override
	public Node getItem(int position) {
		return nodeList.item(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Node node = getItem(position);
		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(
					android.R.layout.simple_list_item_1, null);
		}
		TextView name = (TextView) convertView.findViewById(android.R.id.text1);
		name.setTextColor(0xff000000);
		name.setTextSize(16);
		name.setText(node.getChildNodes().item(7).getTextContent());
		return convertView;
	}
}
