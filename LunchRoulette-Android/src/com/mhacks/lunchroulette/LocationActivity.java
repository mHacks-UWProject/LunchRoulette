package com.mhacks.lunchroulette;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ListView;

import com.mhacks.lunchroulette.adapters.LocationListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LocationActivity extends Activity {

	private ListView mList;
	private ArrayList<String> mLocationIds = new ArrayList<String>();
	private LocationListAdapter mAdapter;
	private ParseUser mUser;
	private Activity mContext = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		mList = (ListView) findViewById(R.id.listView1);

		mUser = ParseUser.getCurrentUser();

		setupListView();
	}

	private void setupListView() {
		beginRetardedPolling();
	}

	// Due to iOS nonsense we're saying 'fuck you' to the 21st century and just
	// rapidly polling rather than using C2DM
	private void beginRetardedPolling() {
		pollStuff();

	}

	private void pollStuff() {
		ParseQuery<ParseObject> groupQuery = ParseQuery.getQuery("Group");
		ArrayList<String> id = new ArrayList<String>();
		id.add(mUser.getObjectId());
		groupQuery.whereContainsAll("users", id);
		groupQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> groups, ParseException arg1) {
				ArrayList<Pair<String, String>> mPlaces = new ArrayList<Pair<String, String>>();
				boolean updated = false;

				if (groups.size() != 0) {

					List<String> locations = groups.get(0).getList("locations");

					for (String locationId : locations) {
						ParseQuery<ParseObject> singleQuery = ParseQuery
								.getQuery("locations");
						ParseObject place = null;
						try {
							place = singleQuery.get(locationId);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Pair<String, String> locationpair = new Pair<String, String>(
								place.getString("name"), place
										.getString("address"));
						mPlaces.add(locationpair);
						mLocationIds.add(place.getObjectId());
						if (mAdapter != null
								&& mAdapter.getCount() != mLocationIds.size()) {
							updated = true;
							mAdapter.clear();
						}
					}
					if (updated || mAdapter == null) {
						mAdapter = new LocationListAdapter(mContext,
								R.layout.list_item, mPlaces, mContext
										.getLayoutInflater());
						mList.setAdapter(mAdapter);
						updated = false;
					}
					
				}
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		ParseUser.getCurrentUser().put("lookingForGroup", false);
		super.onDestroy();
	}

}
