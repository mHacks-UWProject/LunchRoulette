package com.mhacks.lunchroulette;

import java.util.ArrayList;
import java.util.List;

import com.mhacks.lunchroulette.adapters.FoundPeopleListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.widget.ListView;

public class FindingActivity extends Activity {

	private ListView mList;
	private ArrayList<Pair<String, String>> mPeople = new ArrayList<Pair<String, String>>();
	private FoundPeopleListAdapter mAdapter;
	private ParseUser mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finding);
		mList = (ListView) findViewById(R.id.listView1);

		mUser = ParseUser.getCurrentUser();
		if (mUser != null) {
			Pair<String, String> thisUser = new Pair<String, String>(
					mUser.getString("name"),
					imageUrlBuilder(mUser.getString("facebookId")));
			mPeople.add(thisUser);
		} else {
			// you fucked up
		}

		setupListView();
	}

	private void setupListView() {
		mAdapter = new FoundPeopleListAdapter(this, R.layout.list_item,
				mPeople, this.getLayoutInflater());
		mList.setAdapter(mAdapter);
		mList.invalidate();

		beginRetardedPolling();

	}

	// Due to iOS nonsense we're saying 'fuck you' to the 21st century and just
	// rapidly polling rather than using C2DM
	private void beginRetardedPolling() {
		Handler handler = new Handler();

		handler.post(new Runnable() {

			@Override
			public void run() {
				while (mPeople.size() < 4) {
					pollStuff();
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {

					}
				}
			}
		});
	}

	private void pollStuff() {
		ParseQuery<ParseObject> groupQuery = ParseQuery.getQuery("Group");
		ArrayList<String> id = new ArrayList<String>();
		id.add(mUser.getString("id"));
		groupQuery.whereContainsAll("users", id);
		groupQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> groups, ParseException arg1) {

				List<ParseUser> users = groups.get(0).getList("users");

				users.remove(mUser);

				for (ParseUser user : users) {
					Pair<String, String> userPair = new Pair<String, String>(
							user.getString("name"),
							imageUrlBuilder(user.getString("facebookId")));
					mPeople.add(userPair);
					mAdapter.add(userPair);
					mList.invalidate();
				}

			}
		});
	}

	private String imageUrlBuilder(String id) {
		return "http://graph.facebook.com/" + id + "/picture?type=normal";
	}

}
