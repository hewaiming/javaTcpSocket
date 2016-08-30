package com.jzj.server.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import bean.PotStatusDATA;
import bean.RealTime;
import bean.RequestAction;

/**
 * TCP Socket服务器端
 */
public abstract class TcpServer implements Runnable {

	private int port;
	private boolean runFlag;	
	private List<SocketTransceiver> clients = new ArrayList<SocketTransceiver>();
	/**
	 * 实例化 监听的端口
	 */
	public TcpServer(int port) {
		this.port = port;

	}
	/**
	 * 启动服务器 如果启动失败，会回调{@code onServerStop()}
	 */
	public void start() {		
		runFlag = true;
		new Thread(this).start();
	}

	/**
	 * 停止服务器 服务器停止后，会回调{@code onServerStop()}
	 */
	public void stop() {
		runFlag = false;		
	}
	/**
	 * 监听端口，接受客户端连接(新线程中运行)
	 */
	@Override
	public void run() {
		try {
			final ServerSocket server = new ServerSocket(port);
			while (runFlag) {
				try {
					final Socket socket = server.accept();
					startClient(socket);
				} catch (IOException e) {
					// 接受客户端连接出错
					e.printStackTrace();
					this.onConnectFailed();
				}
			}
			// 停止服务器，断开与每个客户端的连接
			try {
				for (SocketTransceiver client : clients) {
					client.stop();
				}
				clients.clear();
				server.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			// ServerSocket对象创建出错，服务器启动失败
			e.printStackTrace();
		}
		this.onServerStop();
	}

	/**
	 * 启动客户端收发
	 */
	private void startClient(final Socket socket) {
		SocketTransceiver client = new SocketTransceiver(socket) {

			@Override
			public void onDisconnect(InetAddress addr) {
				clients.remove(this);
				TcpServer.this.onDisconnect(this);
			}

			// 处理客户端各种命令
			@Override
			public void onReceive(InetAddress addr, RequestAction action) {
				if(action!=null){
					TcpServer.this.onReceive(this, action); // import
				}	
			}
		};
		client.start();
		clients.add(client);
		this.onConnect(client);
	}
	/**
	 * 客户端：连接建立 注意：此回调是在新线程中执行的 SocketTransceiver对象
	 */
	public abstract void onConnect(SocketTransceiver client);
	/**
	 * 客户端：连接建立失败 注意：此回调是在新线程中执行的
	 */
	public abstract void onConnectFailed();
	/**
	 * * 注意：此回调是在新线程中执行的 客户端：收到字符串
	 */
	public abstract void onReceive(SocketTransceiver client, RequestAction action);

	/**
	 * 客户端：连接断开 注意：此回调是在新线程中执行的
	 */
	public abstract void onDisconnect(SocketTransceiver client);
	/**
	 * 服务器停止 注意：此回调是在新线程中执行的
	 */
	public abstract void onServerStop();

}
