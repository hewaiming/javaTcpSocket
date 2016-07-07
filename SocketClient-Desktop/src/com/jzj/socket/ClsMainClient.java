package com.jzj.socket;

import java.util.ArrayList;

import bean.PotStatus;
import bean.RealTime;
import bean.RequestAction;

public class ClsMainClient {

	public static void main(String[] args) {
		TcpClient c1 = new TcpClient() {

			@Override
			public void onReceive(SocketTransceiver st, String s) {
				System.out.println("Client1 Receive: " + s);
			}

			@Override
			public void onDisconnect(SocketTransceiver st) {
				System.out.println("Client1 Disconnect");
			}

			@Override
			public void onConnect(SocketTransceiver transceiver) {
				System.out.println("Client1 Connect");
			}

			@Override
			public void onConnectFailed() {
				System.out.println("Client1 Connect Failed");
			}

			@Override
			public void onReceive(SocketTransceiver transceiver, RealTime realTime) {
				System.out.println("Client1 Receive RealTime:"+realTime.toString());
				
			}

			@Override
			public void onReceive(SocketTransceiver transceiver, ArrayList<PotStatus> potStatus) {
				System.out.println("Client1 Receive PotStats"+potStatus.toString());
				
			}
		};
		TcpClient c2 = new TcpClient() {

			@Override
			public void onReceive(SocketTransceiver st, String s) {
				System.out.println("Client2 Receive: " + s);
			}

			@Override
			public void onDisconnect(SocketTransceiver st) {
				System.out.println("Client2 Disconnect");
			}

			@Override
			public void onConnect(SocketTransceiver transceiver) {
				System.out.println("Client2 Connect");
			}

			@Override
			public void onConnectFailed() {
				System.out.println("Client2 Connect Failed");
			}

			@Override
			public void onReceive(SocketTransceiver transceiver, RealTime realTime) {
				System.out.println("Clients RealTime Receive data: "+realTime);
				
			}

			@Override
			public void onReceive(SocketTransceiver transceiver, ArrayList<PotStatus> potStatus) {
				System.out.println("CLient2  POTSTATUS data: "+potStatus.toString());
				
			}
		};
		TcpClient c3 = new TcpClient(){

			@Override
			public void onConnect(SocketTransceiver transceiver) {
				System.out.println("Client3 Connect");
				
			}

			@Override
			public void onConnectFailed() {
				System.out.println("Client3 Connect Failed");
				
			}

			@Override
			public void onReceive(SocketTransceiver transceiver, String s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onReceive(SocketTransceiver transceiver, RealTime realTime) {
				System.out.println("Client3 RealTime Receive data: "+realTime);
				
			}

			@Override
			public void onReceive(SocketTransceiver transceiver, ArrayList<PotStatus> potStatus) {
				System.out.println("CLient3  POTSTATUS data: "+potStatus.toString());
				
			}

			@Override
			public void onDisconnect(SocketTransceiver transceiver) {
				System.out.println("Client3 DISConnect ");
				
			}
			
		};
		c1.connect("127.0.0.1", 1234);
		c2.connect("127.0.0.1", 1234);
		c3.connect("127.0.0.1", 1234);
		delay();
		while (true) {
			if (c1.isConnected()) {
				RequestAction action=new RequestAction();
				action.setActionId(1);
				action.setPotNo_Area("2209");
				c1.getTransceiver().send(action);
			
			} else {
				break;
			}
			delay();
			if (c2.isConnected()) {
				RequestAction action=new RequestAction();
				action.setActionId(2);
				action.setPotNo_Area("11");
				c2.getTransceiver().send(action);
//				c2.getTransceiver().send("aostar");
			} else {
				break;
			}
			delay();
			if (c3.isConnected()) {
				RequestAction action=new RequestAction();
				action.setActionId(2);
				action.setPotNo_Area("23");
				c3.getTransceiver().send(action);
			} else {
				break;
			}
			delay();
		}
	}

	static void delay() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
