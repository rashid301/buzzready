package org.client.buzzready;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends Activity {

	private TextView username = null;
	private TextView password = null;
	private TextView re_password = null;
	private TextView status = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		 requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.register);

	//     getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_bar);
		username = (TextView) findViewById(R.id.reg_username);
		username.requestFocus();

		password = (TextView) findViewById(R.id.reg_password);

		re_password = (TextView) findViewById(R.id.re_enter_password);

		status = (TextView) findViewById(R.id.status);

		Button btn_register = (Button) findViewById(R.id.btn_send_register);

		btn_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// register the user
				startRegistration();

			}

		});

	}

	private void startRegistration() {

		if (!isNetworkAvailable()) {
			updateView(Constants.NO_NETWORK);
			return;
		}
		if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
			updateView(Constants.INCOMPLETE_FORM);
			return;
		}
		
		if (!password.getText().toString().equals(re_password.getText().toString())) {
			updateView(Constants.PASSWORD_MISMATCH);
			return;
		}

		RegisterAsync process = new RegisterAsync();
		process.execute();

	}

	private void updateView(int status) {

		switch (status) {
		case Constants.PASSWORD_MISMATCH:
			this.status.setTextColor(getResources().getColor(R.color.dark_red));
			this.status.setText("Passwords do not match");
			break;

		case Constants.USERNAME_EXITS:
			this.status.setTextColor(getResources().getColor(R.color.dark_red));
			this.status.setText("username already taken");
			break;

		case Constants.SUCCESS:
			this.status
					.setTextColor(getResources().getColor(R.color.dark_blue));
			this.status.setText("Registration Successful");
			changeActiviy();
			break;

		case Constants.NO_NETWORK:
			this.status.setTextColor(getResources().getColor(R.color.dark_red));
			this.status.setText("No Internet Connectivity");
			break;
			
		case Constants.INCOMPLETE_FORM:
			this.status.setTextColor(getResources().getColor(R.color.dark_red));
			this.status.setText("Please fill all fields");
			break;
		}

		this.status.setVisibility(View.VISIBLE);
	}

	private void changeActiviy() {
		Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
		intent.putExtra("username", username.toString());
		startActivity(intent);
		finish();

	}

	private class RegisterAsync extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			HttpConnection hc = HttpConnection.getInstance();
			return hc.registerOrLogin(Constants.REGISTER_OPER,
					username.getText().toString(), password.getText().toString());

		}

		@Override
		protected void onPostExecute(Integer result) {
			updateView(result.intValue());
		}
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

}
