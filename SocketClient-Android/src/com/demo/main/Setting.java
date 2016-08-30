package com.demo.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Setting extends Activity {
	private Button btnsave,btnFinish;
	private EditText edtip;
	private EditText edtport;
	SharedPreferences sp;
	private Context ctx;
	private String ip;
	private int port;
	private String TAG="=Setting=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		btnsave = (Button) findViewById(R.id.button1);
		edtip = (EditText) findViewById(R.id.editText1);
		edtport = (EditText) findViewById(R.id.editText2);
		sp = this.getSharedPreferences("SP", MODE_PRIVATE);
		edtip.setText(sp.getString("ipstr", ""));
		edtport.setText(sp.getString("port", ""));
		btnFinish=(Button) findViewById(R.id.btnReturn);
		btnFinish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});

		btnsave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG,"��ʼ�޸�");
				String ip = edtip.getText().toString();//
				String port = edtport.getText().toString();//
				Editor editor = sp.edit();
				editor.putString("ipstr", ip);
				editor.putString("port", port);
				editor.commit();//����������
				Log.i(TAG, "����ɹ�"+sp.getString("ipstr", "")+";"+sp.getString("port", ""));

			}
		});
	}
}
