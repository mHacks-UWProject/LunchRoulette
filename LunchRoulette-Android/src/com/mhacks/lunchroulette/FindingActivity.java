package com.mhacks.lunchroulette;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.widget.ListView;

import com.mhacks.lunchroulette.adapters.FoundPeopleListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class FindingActivity extends Activity {

	private ListView mList;
	private ArrayList<String> mParseUsers = new ArrayList<String>();
	private FoundPeopleListAdapter mAdapter;
	private ParseUser mUser;
	private Activity mContext = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finding);
		mList = (ListView) findViewById(R.id.listView1);

		mUser = ParseUser.getCurrentUser();
		if (mUser != null) {
//			Pair<String, String> thisUser = new Pair<String, String>(
//					mUser.getString("name"),
//					imageUrlBuilder(mUser.getString("facebookId")));
//			mPeople.add(thisUser);
//			mParseUsers.add(mUser.getObjectId());
		} else {
			// you fucked up
		}

		setupListView();
	}

	private void setupListView() {
		beginRetardedPolling();
	}

	// Due to iOS nonsense we're saying 'fuck you' to the 21st century and just
	// rapidly polling rather than using C2DM
	private void beginRetardedPolling() {
		Handler handler = new Handler();
		handler.post(rePoll());

//		if (mParseUsers.size() < 4) {
//			handler.postDelayed(rePoll(), 10000);
//		}

	}

	private Runnable rePoll() {
		return new Runnable() {

			@Override
			public void run() {
				pollStuff();
			}
		};
	}

	private void pollStuff() {
		ParseQuery<ParseObject> groupQuery = ParseQuery.getQuery("Group");
		ArrayList<String> id = new ArrayList<String>();
		id.add(mUser.getObjectId());
		groupQuery.whereContainsAll("users", id);
		groupQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> groups, ParseException arg1) {
				 ArrayList<Pair<String, String>> mPeople = new ArrayList<Pair<String, String>>();

				List<String> users = groups.get(0).getList("users");

//				users.removeAll(mParseUsers);

				for (String objectId : users) {
					ParseQuery<ParseUser> singleQuery = ParseQuery.getUserQuery();
					ParseUser user = null;
					try {
						user = singleQuery.get(objectId);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Pair<String, String> userPair = new Pair<String, String>(
							user.getString("name"), imageUrlBuilder(user
									.getString("facebookId")));
					mPeople.add(userPair);
					mParseUsers.add(user.getObjectId());
					mList.invalidate();
				}
				mAdapter = new FoundPeopleListAdapter(mContext, R.layout.list_item,
						mPeople, mContext.getLayoutInflater());
				mList.setAdapter(mAdapter);

			}
		});
	}

	private String imageUrlBuilder(String id) {
		return "http://graph.facebook.com/" + id + "/picture?type=normal";
	}

}
