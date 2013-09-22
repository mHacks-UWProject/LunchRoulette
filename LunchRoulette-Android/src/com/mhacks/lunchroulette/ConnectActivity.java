package com.mhacks.lunchroulette;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.LoginActivity;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class ConnectActivity extends Activity {

	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		ParseAnalytics.trackAppOpened(getIntent());
		ParseFacebookUtils.initialize("705483976146781");

		((Button) findViewById(R.id.loginButton))
				.setOnClickListener(connectPressListener());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);

	}

	private OnClickListener connectPressListener() {
		return new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				buttonClick();
			}
		};
	}

	private void buttonClick() {

		ConnectActivity.this.progressDialog = ProgressDialog.show(
				ConnectActivity.this, "", "Logging in...", true);
		List<String> permissions = Arrays.asList("basic_info", "user_about_me",
				"user_relationships", "user_birthday", "user_location");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				ConnectActivity.this.progressDialog.dismiss();
				if (user == null) {
					// You fucked up, you fucked up, you fucked up
				} else if (user.isNew()) {
					proceed();
				} else {
					proceed();
				}
			}

		});
	}

	public void proceed() {
		startActivity(new Intent(this, IntroductionActivity.class));
	}
}
