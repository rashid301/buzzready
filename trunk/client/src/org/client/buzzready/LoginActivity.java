package org.client.buzzready;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private EditText username = null;
	private EditText password = null;
	private TextView status = null;
	private SessionManager sessionmanager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		username = (EditText) findViewById(R.id.username);
		username.requestFocus();

		password = (EditText) findViewById(R.id.password);

		status = (TextView) findViewById(R.id.status_login);

		Button btn_login = (Button) findViewById(R.id.btn_login);

		Button btn_register = (Button) findViewById(R.id.btn_register);

		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isNetworkAvailable()) {
					updateView(Constants.NO_NETWORK);
					return;
				}
				if (username.getText().toString().isEmpty()
						|| password.getText().toString().isEmpty()) {
					Log.i("LoginActivity", "Incompleteform");
					updateView(Constants.INCOMPLETE_FORM);
					return;
				}
				LoginAsync process = new LoginAsync();
				process.execute();

			}
		});

		btn_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				intent.putExtra("username", username.toString());
				startActivity(intent);

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		sessionmanager = SessionManager.getInstance(getApplicationContext());

		if (sessionmanager.isLoggedIn()) {
			changeActiviy();
			return;
		}

	}

	private void updateView(int status) {

		switch (status) {
		case Constants.USERNAME_PASSWORD_INVALID:
			this.status.setTextColor(getResources().getColor(R.color.dark_red));
			this.status.setText("username/password invalid");
			break;

		case Constants.SUCCESS:
			this.status
					.setTextColor(getResources().getColor(R.color.dark_blue));
			this.status.setText("Registration Successful");
			sessionmanager.createLoginSession(username.getText().toString());
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

	private class LoginAsync extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			HttpConnection hc = HttpConnection.getInstance();
			return hc.registerOrLogin(Constants.LOGIN_OPER,
					username.toString(), password.toString());
		}

		@Override
		protected void onPostExecute(Integer result) {
			// updateView(result.intValue());
			updateView(Constants.SUCCESS);
		}
	}

	private void changeActiviy() {
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		intent.putExtra("username", username.toString());
		startActivity(intent);
		finish();
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
