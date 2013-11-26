package org.client.buzzready;

import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Activity mActivity = null;
	TextView scanResultView = null;
	TextView btn_qrscan = null;
	private SessionManager sessionmanager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);

		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.custom_title_bar);
		mActivity = this;

		scanResultView = (TextView) findViewById(R.id.qr_result);

		btn_qrscan = (TextView) findViewById(R.id.btn_qrscan);

		btn_qrscan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IntentIntegrator.initiateScan(mActivity);

			}
		});

		TextView btn_notify = (TextView) findViewById(R.id.btn_notify);
		btn_notify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// notify the server. Send the registration Id

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sessionmanager = SessionManager.getInstance(getApplicationContext());
		sessionmanager.checkLogin();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE:
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, data);
			if (scanResult == null) {
				return;
			}
			final String result = scanResult.getContents();
			if (result != null) {
				scanResultView.setText(result);
				btn_qrscan.setVisibility(View.INVISIBLE);
				TextView insert_order_text = (TextView) findViewById(R.id.insert_order_text);
				insert_order_text.setVisibility(View.INVISIBLE);
				TextView choice = (TextView) findViewById(R.id.choice);
				choice.setVisibility(View.INVISIBLE);
				TextView order_id = (TextView) findViewById(R.id.order_id);
				order_id.setVisibility(View.INVISIBLE);

				scanResultView.setVisibility(View.VISIBLE);

			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.logout:
			sessionmanager.logoutUser();

		}
		return super.onOptionsItemSelected(item);
	}

}
