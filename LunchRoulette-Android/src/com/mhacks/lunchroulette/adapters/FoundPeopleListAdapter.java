package com.mhacks.lunchroulette.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mhacks.lunchroulette.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FoundPeopleListAdapter extends ArrayAdapter<Pair<String, String>> {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<Pair<String, String>> people = new ArrayList<Pair<String, String>>();

	public FoundPeopleListAdapter(Context context, int resource,
			List<Pair<String, String>> objects, LayoutInflater layoutInflater) {
		super(context, resource, objects);
		mContext = context;
		mInflater = layoutInflater;
		people = (ArrayList<Pair<String, String>>) objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.user_name);
			holder.image = (ImageView) convertView
					.findViewById(R.id.profile_pic);
			holder.loader = ImageLoader.getInstance();

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name
				.setText(((Pair<String, String>) people.get(position)).first);
		holder.loader.displayImage(
				((Pair<String, String>) people.get(position)).second,
				holder.image);

		return convertView;
	}
}

class ViewHolder {
	public TextView name;
	public ImageView image;
	public ImageLoader loader;
}
