package com.mhacks.lunchroulette.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mhacks.lunchroulette.R;

public class LocationListAdapter extends ArrayAdapter<Pair<String, String>> {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<Pair<String, String>> location = new ArrayList<Pair<String, String>>();

	public LocationListAdapter(Context context, int resource,
			List<Pair<String, String>> objects, LayoutInflater layoutInflater) {
		super(context, resource, objects);
		mContext = context;
		mInflater = layoutInflater;
		location = (ArrayList<Pair<String, String>>) objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LocationViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new LocationViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.place_name);
			holder.info =  (TextView) convertView.findViewById(R.id.place_info);
			convertView.setTag(holder);

		} else {
			holder = (LocationViewHolder) convertView.getTag();
		}

		holder.name.setText(((Pair<String, String>) location.get(position)).first);
		holder.info.setText(((Pair<String, String>) location.get(position)).second);
		return convertView;
	}
}

class LocationViewHolder {
	public TextView name;
	public TextView info;
}
