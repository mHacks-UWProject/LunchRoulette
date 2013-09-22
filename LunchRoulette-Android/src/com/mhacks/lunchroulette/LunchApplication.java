package com.mhacks.lunchroulette;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LunchApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, "RHIntkgdRNEzMEoqFdSc9LBIlxY3JaYN4ckRcuhW",
				"vvlNyTDDR9VVkv4SLpPjM0CcVE8KbdwpWf2T7pi0");
		ParseFacebookUtils.initialize("705483976146781");
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		// If you would like all objects to be private by default, remove this
		// line.
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
	}
	
}
