package com.mhacks.lunchroulette;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class LunchApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, "RHIntkgdRNEzMEoqFdSc9LBIlxY3JaYN4ckRcuhW",
				"vvlNyTDDR9VVkv4SLpPjM0CcVE8KbdwpWf2T7pi0");

		ParseFacebookUtils.initialize(getString(R.string.app_id));

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).build();
		ImageLoader.getInstance().init(config);

	}

}
