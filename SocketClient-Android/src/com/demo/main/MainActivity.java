package com.demo.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.jzj.socket.SocketTransceiver;
import com.jzj.socket.TcpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import bean.PotStatus;
import bean.RealTime;
import bean.RequestAction;

public class MainActivity extends Activity implements OnClickListener {

	private Button bnConnect, btnSet;
	private TextView txReceive;
	private EditText edIP, edPort, edData;
	private ListView lv_PotStatus;
	private List<PotStatus> pList = null;
	private Context ctx;
	private SharedPreferences sp;
	private String ip;
	private int port;
	private Spinner spinner;
	private ArrayList<String> PotStatusList;
	private ArrayAdapter<String> PotStatus_adapter;
	private String area;
	private ArrayAdapter<PotStatus> adapter = null;
	private Spinner spinner_potno;
	private ArrayList<String> PotNoList;
	private ArrayAdapter<String> PotNo_adapter = null;
	private String PotNo;
	private TextView txRealTime;
	private Timer timer = null;
	private TimerTask task = null;

	private Handler handler = new Handler(Looper.getMainLooper());

	private TcpClient client = new TcpClient() {

		@Override
		public void onConnect(SocketTransceiver transceiver) {
			refreshUI(true);
		}

		@Override
		public void onDisconnect(SocketTransceiver transceiver) {
			refreshUI(false);
		}

		@Override
		public void onConnectFailed() {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
				}
			});
		}

		@Override
		public void onReceive(SocketTransceiver transceiver, final String s) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					txReceive.append(s);
					txReceive.append("\n");
				}
			});
		}

		@Override
		public void onReceive(SocketTransceiver transceiver, final RealTime realTime) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					txRealTime.setText(realTime.toString());

				}
			});

		}

		@Override
		public void onReceive(SocketTransceiver transceiver, final ArrayList<PotStatus> potStatus) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					if (lv_PotStatus != null) {
						if (adapter == null) {
							adapter = new ArrayAdapter<PotStatus>(getApplicationContext(),
									android.R.layout.simple_expandable_list_item_1, potStatus);
							lv_PotStatus.setAdapter(adapter);
						} else if (!(adapter.isEmpty())) {
							adapter.clear();
							adapter.notifyDataSetChanged();
						} else {
							adapter = new ArrayAdapter<PotStatus>(getApplicationContext(),
									android.R.layout.simple_list_item_1, potStatus);
							lv_PotStatus.setAdapter(adapter);
						}

					}

				}
			});

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ctx = this;
		this.findViewById(R.id.bn_send).setOnClickListener(this);
		bnConnect = (Button) this.findViewById(R.id.bn_connect);
		bnConnect.setOnClickListener(this); // 连接按钮

		edIP = (EditText) this.findViewById(R.id.ed_ip);
		edPort = (EditText) this.findViewById(R.id.ed_port);
		edData = (EditText) this.findViewById(R.id.ed_dat);
		txReceive = (TextView) this.findViewById(R.id.tx_receive);
		txRealTime = (TextView) this.findViewById(R.id.tv_realtime);
		txReceive.setOnClickListener(this);
		lv_PotStatus = (ListView) findViewById(R.id.lv_potStatus);
		btnSet = (Button) findViewById(R.id.bn_set); // 设置
		btnSet.setOnClickListener(this);
		initdate();
		edIP.setText(ip);
		edPort.setText(port + "");
		init_potStatus();
		ini_potno();
		timer = new Timer();
		refreshUI(false);
	}

	private void ini_potno() {
		spinner_potno = (Spinner) findViewById(R.id.sp_potno);
		PotNoList = new ArrayList<String>();
		for (int i = 1101; i <= 1136; i++) {
			PotNoList.add(i + "");
		}
		PotNo_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PotNoList);
		PotNo_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_potno.setAdapter(PotNo_adapter);
		spinner_potno.setVisibility(View.VISIBLE);
		PotNo = spinner_potno.getItemAtPosition(0).toString();
		spinner_potno.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				PotNo = PotNoList.get(position).toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	private void init_potStatus() {
		spinner = (Spinner) findViewById(R.id.sp_potstatus);
		PotStatusList = new ArrayList<String>();
		PotStatusList.add("一厂一区");
		PotStatusList.add("一厂二区");
		PotStatusList.add("一厂三区");
		PotStatusList.add("二厂一区");
		PotStatusList.add("二厂二区");
		PotStatusList.add("二厂三区");
		PotStatus_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PotStatusList);
		PotStatus_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(PotStatus_adapter);
		spinner.setVisibility(View.VISIBLE);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					area = "11";
					break;
				case 1:
					area = "12";
					break;
				case 2:
					area = "13";
					break;
				case 3:
					area = "21";
					break;
				case 4:
					area = "22";
					break;
				case 5:
					area = "23";
					break;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

	}

	protected void FreshPotStatus() {
		if (task != null) {
			task.cancel();
		}
		task = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						sendPotStatusAction();
						sendRealTimeAction();
					}
				});

			}
		};
		timer.schedule(task, 0, 1000);

	}

	@Override
	public void onStop() {
		client.disconnect();
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bn_connect:
			connect();
			break;
		case R.id.bn_send:
			FreshPotStatus();
			break;
		case R.id.tx_receive:
			clear();
			break;
		case R.id.bn_set:
			Setting();
			break;
		}
	}

	private void Setting() {
		Intent intent = new Intent(MainActivity.this, Setting.class);
		startActivity(intent);// 打开设置界面

	}

	public void initdate() {
		sp = ctx.getSharedPreferences("SP", ctx.MODE_PRIVATE);
		ip = sp.getString("ipstr", ip);
		port = Integer.parseInt(sp.getString("port", String.valueOf(port)));
		// MyLog.i(TAG, "获取到ip端口:" + ip + ";" + port);
	}

	/**
	 * 刷新界面显示
	 * 
	 * @param isConnected
	 */
	private void refreshUI(final boolean isConnected) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				edPort.setEnabled(!isConnected);
				edIP.setEnabled(!isConnected);
				bnConnect.setText(isConnected ? "断开" : "连接");
			}
		});
	}

	/**
	 * 设置IP和端口地址,连接或断开
	 */
	private void connect() {
		if (client.isConnected()) {
			// 断开连接
			client.disconnect();
		} else {
			try {
				String hostIP = edIP.getText().toString();
				int port = Integer.parseInt(edPort.getText().toString());
				client.connect(hostIP, port);
			} catch (NumberFormatException e) {
				Toast.makeText(this, "端口错误", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送数据
	 */
	private void sendPotStatusAction() {
		try {
			RequestAction action = new RequestAction();
			action.setActionId(2);
			action.setPotNo_Area(area);
			client.getTransceiver().send(action);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void sendRealTimeAction() {
		try {
			RequestAction action = new RequestAction();
			action.setActionId(1);
			action.setPotNo_Area(PotNo);
			client.getTransceiver().send(action);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 清空接收框
	 */
	private void clear() {
		new AlertDialog.Builder(this).setTitle("确认清除?").setNegativeButton("取消", null)
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						txReceive.setText("");
					}
				}).show();
	}
}
