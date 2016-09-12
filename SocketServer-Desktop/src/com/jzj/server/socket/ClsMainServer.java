package com.jzj.server.socket;

import java.awt.List;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.server.ServerCloneException;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.Random;

import bean.PotStatus;
import bean.PotStatusDATA;
import bean.RealTime;
import bean.RequestAction;

public class ClsMainServer {

	private static final int[][] PotMax = { { 36, 37, 37 }, { 36, 37, 37 } };
	private static Socket socket_jkj1;
	private static Socket socket_jkj2;
	private static BufferedInputStream in_jkj1;
	private static BufferedInputStream in_jkj2;
	private static BufferedOutputStream out_jkj1;
	private static BufferedOutputStream out_jkj2;

	public static void main(String[] args) {

		int port = 1234;
		TcpServer server = new TcpServer(port) {
			@Override
			public void onConnect(SocketTransceiver client) {
				printInfo(client, "Connect");
			}

			@Override
			public void onConnectFailed() {
				System.out.println("Client Connect Failed");
			}

			@Override
			public void onDisconnect(SocketTransceiver client) {
				printInfo(client, "Disconnect");
			}

			@Override
			public void onServerStop() {
				System.out.println("--------Server Stopped--------");
			}

			// 处理手机客户端请求的各种命令
			@Override
			public void onReceive(SocketTransceiver client, RequestAction action) {
				if (action == null) {
					return;
				}
				// printInfo(client, action.toString());
				switch (action.getActionId()) {
				case 1:
					client.send(1, GetRealTrendFromJKJ(action.getPotNo_Area()));// 向上位机发送取‘实时曲线数据’命令
					break;
				case 2:
					client.send(2, GetPotStatusFromJKJ(action.getPotNo_Area()));// 向上位机发送取‘槽状态数据’命令
					break;
				}
			}
		};
		System.out.println("--------Server Started--------");
		server.start();

	}

	protected static PotStatusDATA GetPotStatusFromJKJ(String potNo_Area) {
		int id=Integer.valueOf(potNo_Area);		
		if (id>=11 && id<=23) {
			int potno = Integer.valueOf(potNo_Area);
			int room = potno / 10;
			int area = potno % 10;
			byte[] cmdBuf = new byte[30];
			// 向上位机发送取槽状态数据命令ReadPotStatus
			cmdBuf[0] = 'R';
			cmdBuf[1] = 'e';
			cmdBuf[2] = 'a';
			cmdBuf[3] = 'd';
			cmdBuf[4] = 'P';
			cmdBuf[5] = 'o';
			cmdBuf[6] = 't';
			cmdBuf[7] = 'S';
			cmdBuf[8] = 't';
			cmdBuf[9] = 'a';
			cmdBuf[10] = 't';
			cmdBuf[11] = 'u';
			cmdBuf[12] = 's';
			cmdBuf[13] = ' ';
			cmdBuf[14] = (byte) (char) (room + '0'); // 厂房号
			cmdBuf[15] = (byte) ' ';
			cmdBuf[16] = (byte) (char) (area + '0'); // 区号
			cmdBuf[17] = (byte) 0x0d;
			cmdBuf[18] = (byte) 0x0a;
			if (room == 1) {
				jkj1_start();// 启动上位机1
				if (out_jkj1 != null) {
					try {
						out_jkj1.write(cmdBuf);
						out_jkj1.flush();
						byte[] firstBuf = new byte[4];
						byte[] RecvBuf = new byte[2200];
						int firstLen = in_jkj1.read(firstBuf);
						int len = in_jkj1.read(RecvBuf);
						int recvLenght = ((firstBuf[2] & 0x00ff) << 8) + (firstBuf[3] & 0x00ff);
						if ((len <= 4) && (RecvBuf[0] != 0x00) && (recvLenght != len)) {
							len = in_jkj1.read(RecvBuf);// 再从上位机取一次槽状态数据
						}
						jkj1_stop();
						return WriteToPotStatusList(RecvBuf);
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}
			} else if (room == 2) {
				jkj2_start();// 启动上位机2
				if (out_jkj2 != null) {
					try {
						out_jkj2.write(cmdBuf);
						out_jkj2.flush();
						byte[] firstBuf = new byte[4];
						byte[] RecvBuf = new byte[2200];
						int firstLen = in_jkj2.read(firstBuf);
						int len = in_jkj2.read(RecvBuf);
						int recvLenght = ((firstBuf[2] & 0x00ff) << 8) + (firstBuf[3] & 0x00ff);
						if ((len <= 4) && (RecvBuf[0] != 0x00) && (recvLenght != len)) {
							len = in_jkj2.read(RecvBuf);// 再从上位机取一次槽状态数据
						}
						jkj2_stop();
						return WriteToPotStatusList(RecvBuf);
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}
			}
		}
		return null;
	}

	private static PotStatusDATA WriteToPotStatusList(byte[] RecvBuf) {
		String[] OsStatus = { "NB", "AEB", "AU", "AD", "AC", "TAP", "IRF", "FNB", "APB", "TMT", "RRK" };
		String[] AeStatus = { "N1", "W1", "N2", "W2", "NX", "CO", " ", " " };
		String[] WorkStatus = { "NORM", "预热", "启动", "停槽", "CErr" };
		PotStatusDATA mData = new PotStatusDATA();
		int room = RecvBuf[1];
		int area = RecvBuf[2];
		// System.out.println("厂房：" + RecvBuf[1] + " 区号：" + RecvBuf[2]);
		int SysI = ((RecvBuf[5] & 0x00ff) << 8) + (RecvBuf[4] & 0x00ff);
		// System.out.println("系列电流:" + SysI);
		int SysV = ((RecvBuf[7] & 0x00ff) << 8) + (RecvBuf[6] & 0x00ff);
		// System.out.println("系列电压:" + SysV);
		int RoomV = ((RecvBuf[9] & 0x00ff) << 8) + (RecvBuf[8] & 0x00ff);
		// System.out.println("厂房电压:" + RoomV);
		mData.setRoom(room);
		mData.setArea(area);
		mData.setSysI(SysI);
		mData.setSysV(SysV);
		mData.setRoomV(RoomV);
		ArrayList<PotStatus> mList = new ArrayList<PotStatus>();
		int S = 17;
		for (int i = 1; i <= PotMax[room - 1][area - 1]; i++) {
			PotStatus pStatus = new PotStatus();
			int Potno = room * 1000 + area * 100 + i;
			pStatus.setPotNo(Potno);
			// System.out.println("槽号：" + i);
			if ((RecvBuf[S] & 0x40) != 0) {
				// System.out.println("自手动：MAN");
				pStatus.setAutoRun(false);
			} else {
				// System.out.println("自手动: ");
				pStatus.setAutoRun(true);
			}
			int action = RecvBuf[S] & 0x3f;
			if ((action >= 1) && (action <= 11)) {
				// System.out.println("槽作业: " + OsStatus[action - 1]);
				pStatus.setOperation(OsStatus[action - 1]);
			} else {
				// System.out.println("槽作业: ");
				pStatus.setOperation("");
			}

			int worksta = RecvBuf[S + 1] & 0x03;
			/*if (worksta == 3) {
				// System.out.println("槽状态：" + " 停槽 ");
			} else {
				// System.out.println("槽状态：" +WorkStatus[worksta] );
			}*/
			pStatus.setStatus(WorkStatus[worksta]);
			int tmp = RecvBuf[S + 2] & 0x07;
			// System.out.println("加料状态:" + AeStatus[tmp]);//
			// 发生效应标志
			if ((RecvBuf[S + 2] & 0x80)!=0){
				pStatus.setAeFlag(true);
			}else{
				pStatus.setAeFlag(false);
			}
				
			pStatus.setOStatus(AeStatus[tmp]);
			// System.out.println("故障:" + RecvBuf[S + 3]);
			pStatus.setFaultNo(RecvBuf[S + 3]);
			int SetV = ((RecvBuf[S + 5] & 0x00ff) << 8) + (RecvBuf[S + 4] & 0x00ff);
			pStatus.setSetV(String.valueOf(SetV));
			// System.out.println("设定电压:" + String.format("%.3f",
			// SetV/1000.000)); // 设定电压

			int WorkV = ((RecvBuf[S + 7] & 0x00ff) << 8) + (RecvBuf[S + 6] & 0x00ff);
			pStatus.setWorkV(String.valueOf(WorkV));
			// System.out.println("工作电压:" + String.format("%.3f",
			// WorkV/1000.000));// 工作电压

			int comerr = RecvBuf[S + 8] - 35;
			pStatus.setComerr(comerr);
			// System.out.println("通讯故障:" + comerr); //			

			int SetNb = ((RecvBuf[S + 10] & 0x00ff) << 8) + (RecvBuf[S + 9] & 0x00ff);
			pStatus.setSetNb(SetNb);
			// System.out.println("设定NB:" + SetNb);

			int RealNb = ((RecvBuf[S + 12] & 0x00ff) << 8) + (RecvBuf[S + 11] & 0x00ff);
			pStatus.setWorkNb(RealNb);
			// System.out.println("实际NB:" + RealNb);

			int AeTimes = ((RecvBuf[S + 14] & 0x00ff) << 8) + (RecvBuf[S + 13] & 0x00ff);
			pStatus.setAeSpan(AeTimes);
			// System.out.println("效应间隔:" + AeTimes/60);

			int Aetmp = RecvBuf[S + 15] & 0x07;
			pStatus.setAeStatus(AeStatus[Aetmp]);
			// System.out.println("效应状态:" + AeStatus[Aetmp]);

			// System.out.println("效应时刻:" +Integer.toHexString(RecvBuf[S +
			// 16])+"/"+Integer.toHexString(RecvBuf[S +
			// 17])+":"+Integer.toHexString(RecvBuf[S + 18]));
			pStatus.setAeDateTime(Integer.toHexString(RecvBuf[S + 16]) + "/" + Integer.toHexString(RecvBuf[S + 17])
					+ ":" + Integer.toHexString(RecvBuf[S + 18]));

			int AeContinus = ((RecvBuf[S + 20] & 0x00ff) << 8) + (RecvBuf[S + 19] & 0x00ff);
			pStatus.setAeContinue(AeContinus);
			// System.out.println("AE时间:" + AeContinus);

			// System.out.println("NB时刻:" +Integer.toHexString(RecvBuf[S + 24])
			// + ":" +Integer.toHexString(RecvBuf[S + 23])); // ok
			pStatus.setNbTime(Integer.toHexString(RecvBuf[S + 24]) + ":" + Integer.toHexString(RecvBuf[S + 23]));
		
			pStatus.setAbnormal_Flag(RecvBuf[S+42]);	//第42位异常槽压和电压摆
			
			int Nbplus = ((RecvBuf[S + 44] & 0x00ff) << 8) + (RecvBuf[S + 43] & 0x00ff);
			pStatus.setNbPlus(Nbplus);
			// System.out.println("过欠:" + Nbplus);

			int AeCnt = ((RecvBuf[S + 38] & 0x00ff) << 8) + (RecvBuf[S + 37] & 0x00ff);
			pStatus.setAeCnt(AeCnt);
			// System.out.println("效应次数:" + AeCnt);

			int AeV = ((RecvBuf[S + 46] & 0x00ff) << 8) + (RecvBuf[S + 45] & 0x00ff);
			pStatus.setAeV(String.valueOf(AeV));
			// System.out.println("AE电压:" + Math.round(AeV/1000.0 * 100) *
			// 0.01d);

			int yjwz = ((RecvBuf[S + 48] & 0x00ff) << 8) + (RecvBuf[S + 47] & 0x00ff);
			pStatus.setYJWJ(yjwz);
			// System.out.println("阳极位置:" + yjwz);

			int noise = ((RecvBuf[S + 52] & 0x00ff) << 8) + (RecvBuf[S + 51] & 0x00ff);
			pStatus.setNoise(noise);
			mList.add(pStatus);
			// System.out.println("噪音:" + noise);
			S = S + 53;
		}
		mData.setPotData(mList);
		return mData;
	}

	protected static RealTime GetRealTrendFromJKJ(String potNo_Area) {
		if (potNo_Area.length() == 4) {
			int potno = Integer.valueOf(potNo_Area);
			int room = potno / 1000;
			int area = (potno % 1000) / 100;
			int pot = potno % 100;
			// 向上位机发送取实时曲线 命令
			byte[] cmdBuf = new byte[30];
			cmdBuf[0] = (byte) 'R';
			cmdBuf[1] = (byte) 'e';
			cmdBuf[2] = (byte) 'a';
			cmdBuf[3] = (byte) 'd';
			cmdBuf[4] = (byte) 'R';
			cmdBuf[5] = (byte) 'e';
			cmdBuf[6] = (byte) 'a';
			cmdBuf[7] = (byte) 'l';
			cmdBuf[8] = (byte) 'T';
			cmdBuf[9] = (byte) 'r';
			cmdBuf[10] = (byte) 'e';
			cmdBuf[11] = (byte) 'n';
			cmdBuf[12] = (byte) 'd';
			cmdBuf[13] = (byte) 'D';
			cmdBuf[14] = (byte) 'a';
			cmdBuf[15] = (byte) 't';
			cmdBuf[16] = (byte) 'a';
			cmdBuf[17] = (byte) ' ';
			cmdBuf[18] = (byte) (char) (room + '0'); // 厂房号
			cmdBuf[19] = (byte) ' ';
			cmdBuf[20] = (byte) (char) (area + '0'); // 区号
			cmdBuf[21] = (byte) ' ';
			if (pot <= 9) {
				cmdBuf[22] = (byte) (char) (pot + '0'); // 槽号
				cmdBuf[23] = (byte) 0x0d;
				cmdBuf[24] = (byte) 0x0a;
			} else {
				char pot1 = (char) (pot / 10 + '0');
				char pot2 = (char) (pot % 10 + '0');
				cmdBuf[22] = (byte) pot1; // 槽号
				cmdBuf[23] = (byte) pot2; // 槽号
				cmdBuf[24] = (byte) 0x0d;
				cmdBuf[25] = (byte) 0x0a;
			}
			if (room == 1) {
				jkj1_start();// 启动上位机1
				if (out_jkj1 != null) {
					try {
						out_jkj1.write(cmdBuf);
						out_jkj1.flush();
						byte[] RecvBuf = new byte[14];
						int len = in_jkj1.read(RecvBuf);
						if ((len <= 4) && (RecvBuf[0] != 0x42)) {
							len = in_jkj1.read(RecvBuf);// 再取一次数据
						}
						int SysI = ((RecvBuf[9 - 4] & 0x00ff) << 8) + (RecvBuf[8 - 4] & 0x00ff);
						int PotV = ((RecvBuf[11 - 4] & 0x00ff) << 8) + (RecvBuf[10 - 4] & 0x00ff);
						RealTime rTime = new RealTime();
						rTime.setCur(SysI);
						rTime.setPotv(PotV);
						int potNo = RecvBuf[1] * 1000 + RecvBuf[2] * 100 + RecvBuf[3];
						rTime.setPotNo(potNo);
						jkj1_stop();
						return rTime;
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}
			} else if (room == 2) {
				jkj2_start();// 启动上位机2
				if (out_jkj2 != null) {
					try {
						out_jkj2.write(cmdBuf);
						out_jkj2.flush();
						byte[] RecvBuf = new byte[14];
						int len = in_jkj2.read(RecvBuf);
						if ((len <= 4) && (RecvBuf[0] != 0x42)) {
							len = in_jkj2.read(RecvBuf);// 再取一次数据
						}
						int SysI = ((RecvBuf[9 - 4] & 0x00ff) << 8) + (RecvBuf[8 - 4] & 0x00ff);
						int PotV = ((RecvBuf[11 - 4] & 0x00ff) << 8) + (RecvBuf[10 - 4] & 0x00ff);
						RealTime rTime = new RealTime();
						rTime.setCur(SysI);
						rTime.setPotv(PotV);
						int potNo = RecvBuf[1] * 1000 + RecvBuf[2] * 100 + RecvBuf[3];
						rTime.setPotNo(potNo);
						jkj2_stop();
						return rTime;
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}
			}
		}
		return null;
	}

	private static void jkj1_start() {
		socket_jkj1 = new Socket();
		try {
			socket_jkj1.connect(new InetSocketAddress("172.16.0.5", 9099), 3000);
			System.out.println("172.16.0.5连接成功！");
			in_jkj1 = new BufferedInputStream(socket_jkj1.getInputStream());
			out_jkj1 = new BufferedOutputStream(socket_jkj1.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void jkj2_start() {
		socket_jkj2 = new Socket();
		try {
			socket_jkj2.connect(new InetSocketAddress("172.16.0.6", 9099), 3000);
			System.out.println("172.16.0.6连接成功！");
			in_jkj2 = new BufferedInputStream(socket_jkj2.getInputStream());
			out_jkj2 = new BufferedOutputStream(socket_jkj2.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static void jkj1_stop() {
		try {
			if (in_jkj1 != null) {
				in_jkj1.close();
			}
			if (out_jkj1 != null) {
				out_jkj1.close();
			}
			if (socket_jkj1 != null) {
				socket_jkj1.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static void jkj2_stop() {
		try {
			if (in_jkj2 != null) {
				in_jkj2.close();
			}
			if (out_jkj2 != null) {
				out_jkj2.close();
			}
			if (socket_jkj2 != null) {
				socket_jkj2.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void delay() {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	static void printInfo(SocketTransceiver st, String msg) {
		System.out.println("Client " + st.getInetAddress().getHostAddress());
		System.out.println("  " + msg);
	}
}
