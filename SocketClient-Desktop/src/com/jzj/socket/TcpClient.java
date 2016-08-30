package com.jzj.socket;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import bean.PotStatus;
import bean.PotStatusDATA;
import bean.RealTime;

/**
 * TCP Socket客户端
 */
public abstract class TcpClient implements Runnable {

	private int port;
	private String hostIP;
	private boolean connect = false;
	private SocketTransceiver transceiver;

	/**
	 * 建立连接	
	 * 连接的建立将在新线程中进行
	 * 连接建立成功，回调{@code onConnect()}
	 * 连接建立失败，回调{@code onConnectFailed()}	
	 * @param hostIP
	 *            服务器主机IP
	 * @param port
	 *            端口
	 */
	public void connect(String hostIP, int port) {
		this.hostIP = hostIP;
		this.port = port;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			Socket socket = new Socket(hostIP, port);
			transceiver = new SocketTransceiver(socket) {			

				@Override
				public void onDisconnect(InetAddress addr) {
					connect = false;
					TcpClient.this.onDisconnect(this);
				}

				@Override
				public void onReceive(InetAddress addr, RealTime rTime) {
					System.out.println("接受到实时数据"+rTime.toString());
					TcpClient.this.onReceive(this, rTime);  //import
					
				}
				@Override
				public void onReceive(InetAddress addr, PotStatusDATA potStatus) {
					System.out.println("接受到 槽状态数据"+potStatus.toString());
					TcpClient.this.onReceive(this, potStatus);  //import
					
				}
				
				
			};
			transceiver.start();
			connect = true;
			this.onConnect(transceiver);
		} catch (Exception e) {
			e.printStackTrace();
			this.onConnectFailed();
		}
	}

	/**
	 * 断开连接
	 * <p>
	 * 连接断开，回调{@code onDisconnect()}
	 */
	public void disconnect() {
		if (transceiver != null) {
			transceiver.stop();
			transceiver = null;
		}
	}

	/**
	 * 判断是否连接	
	 * @return 当前处于连接状态，则返回true
	 */
	public boolean isConnected() {
		return connect;
	}

	/**
	 * 获取Socket收发器	
	 * @return 未连接则返回null
	 */
	public SocketTransceiver getTransceiver() {
		return isConnected() ? transceiver : null;
	}

	/**
	 * 连接建立
	 * @param transceiver
	 *            SocketTransceiver对象
	 */
	public abstract void onConnect(SocketTransceiver transceiver);

	/**
	 * 连接建立失败
	 */
	public abstract void onConnectFailed();

	/**
	 * 接收到数据
	 * 注意：此回调是在新线程中执行的
	 * @param transceiver
	 *            SocketTransceiver对象	
	 */	
	
	public abstract void onReceive(SocketTransceiver transceiver, RealTime realTime);
	
	public abstract void onReceive(SocketTransceiver transceiver, PotStatusDATA potStatus);
	/**
	 * 连接断开
	 * 注意：此回调是在新线程中执行的
	 * @param transceiver
	 *            SocketTransceiver对象
	 */
	public abstract void onDisconnect(SocketTransceiver transceiver);
}
