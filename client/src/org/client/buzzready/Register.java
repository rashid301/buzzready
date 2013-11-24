package org.client.buzzready;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Register extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register);

		TextView username = (TextView) findViewById(R.id.reg_username);

		TextView password = (TextView) findViewById(R.id.reg_password);

		TextView re_password = (TextView) findViewById(R.id.re_enter_password);

		Button btn_register = (Button) findViewById(R.id.btn_register);

		btn_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// register the user

			}
		});

	}

}
