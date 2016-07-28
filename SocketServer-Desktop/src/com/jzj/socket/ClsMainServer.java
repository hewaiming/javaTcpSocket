package com.jzj.socket;

import java.awt.List;
import java.util.ArrayList;
import java.util.Random;

import bean.PotStatus;
import bean.RealTime;
import bean.RequestAction;

public class ClsMainServer {

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
			public void onReceive(SocketTransceiver client, String s) {
				printInfo(client, "Send Data: " + s);
				switch (s.toUpperCase()) {
				case "GY":
					client.send("gy is GUANGYUAN");
					break;
				case "AOSTAR":
					client.send("aostar is fuck company");
					break;
				case "POTV":
					client.send(Math.random() + "");
					break;
				default:
					client.send(s);
					break;
				}
			}

			@Override
			public void onDisconnect(SocketTransceiver client) {
				printInfo(client, "Disconnect");
			}

			@Override
			public void onServerStop() {
				System.out.println("--------Server Stopped--------");
			}

			// 处理客户端请求的各种命令
			@Override
			public void onReceive(SocketTransceiver client, RequestAction action) {
				printInfo(client, action.toString());
				switch (action.getActionId()) {
				case 1:
					RealTime realTime = new RealTime();
					realTime.setCur(GetRandom(2010, 1990));
					realTime.setPotv(GetRandom(4100, 3990));
//					client.send(realTime);
					client.send(1, realTime); // 发送实时槽压数据
					break;
				case 2:					
					SendDataToClient(client,action.getPotNo_Area());	 // 发送槽状态数据				
					break;

				}

			}
		};
		System.out.println("--------Server Started--------");
		server.start();
	}

	protected static void SendDataToClient(SocketTransceiver client,String area) {
		int begin_potno = 0, end_potno = 0;
		switch (area) {
		case "11":
			begin_potno = 1101;
			end_potno = 1136;
			break;
		case "12":
			begin_potno = 1201;
			end_potno = 1237;
			break;
		case "13":
			begin_potno = 1301;
			end_potno = 1336;
			break;
		case "21":
			begin_potno = 2101;
			end_potno = 2136;
			break;
		case "22":
			begin_potno = 2201;
			end_potno = 2237;
			break;
		case "23":
			begin_potno = 2301;
			end_potno = 2337;
			break;
		}
		ArrayList<PotStatus> list = new ArrayList<PotStatus>();
		for (int i = begin_potno; i <= end_potno; i++) {
			PotStatus pStatus = new PotStatus();
			pStatus.setPotNo(i);
			pStatus.setStatus("预热");
			pStatus.setAutoRun(false);
			pStatus.setOperation("NB");
			pStatus.setSetV((float) 4.001);
			pStatus.setWorkV((float) 4.002);
			pStatus.setSetNb(182);
			pStatus.setWorkNb(200);
			pStatus.setNbTime("11:20");
			pStatus.setAeSpan(360);
			pStatus.setFaultNo(5);
			pStatus.setYJWJ(210);
			list.add(pStatus);
		}
		client.send(2,list);

	}

	protected static int GetRandom(int max, int min) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	static void printInfo(SocketTransceiver st, String msg) {
		System.out.println("Client " + st.getInetAddress().getHostAddress());
		System.out.println("  " + msg);
	}
}
