package com.jzj.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import bean.PotStatus;
import bean.PotStatusDATA;
import bean.RealTime;
import bean.RequestAction;

/**
 * Socket收发器 通过Socket发送数据，并使用新线程监听Socket接收到的数据
 * 
 * @author jzj1993
 * @since 2015-2-22
 */
public abstract class SocketTransceiver implements Runnable {

	protected Socket socket;
	protected InetAddress addr;
	protected DataInputStream in;
	protected DataOutputStream out;
	protected ObjectInputStream objectInputStream;
	private boolean runFlag;

	/**
	 * 实例化
	 * 
	 * @param socket
	 *            已经建立连接的socket
	 */
	public SocketTransceiver(Socket socket) {
		this.socket = socket;
		this.addr = socket.getInetAddress();
	}

	/**
	 * 获取连接到的Socket地址
	 * 
	 * @return InetAddress对象
	 */
	public InetAddress getInetAddress() {
		return addr;
	}

	/**
	 * 开启Socket收发
	 * <p>
	 * 如果开启失败，会断开连接并回调{@code onDisconnect()}
	 */
	public void start() {
		runFlag = true;
		new Thread(this).start();
	}

	/**
	 * 断开连接(主动)
	 * <p>
	 * 连接断开后，会回调{@code onDisconnect()}
	 */
	public void stop() {
		runFlag = false;
		try {
			socket.shutdownInput();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 向服务端发送操作命令
	public boolean send(RequestAction action) {
		if (out != null) {
			try {
				out.writeInt(action.getActionId());
				out.writeUTF(action.getPotNo_Area());
				out.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 监听Socket接收的数据(新线程中运行)
	 */
	@Override
	public void run() {
		try {
			in = new DataInputStream(this.socket.getInputStream());
			out = new DataOutputStream(this.socket.getOutputStream());
			objectInputStream = new ObjectInputStream(this.socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			runFlag = false;
		}
		while (runFlag) {
			try {
				if (objectInputStream != null) {
					int actionId = objectInputStream.readInt();
					if (actionId == 1) {
						final RealTime rTime = (RealTime) objectInputStream.readObject();
						if (rTime!=null) { this.onReceive(addr, rTime); }
					} else if (actionId == 2) {
						final PotStatusDATA pList = (PotStatusDATA) objectInputStream.readObject();
						if (pList!=null) {this.onReceive(addr, pList);}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
		// 断开连接
		try {
			in.close();
			out.close();
			socket.close();
			in = null;
			out = null;
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.onDisconnect(addr);
	}

	// 从服务器读取实时曲线数据
//	private RealTime GetRealTimeFromServer(DataInputStream in) {
//		RealTime rTime = new RealTime();
//		try {
//			rTime.setCur(in.readInt());
//			rTime.setPotv(in.readInt());
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//		return rTime;
//	}
	

	/**
	 * 接收到数据
	 * 注意：此回调是在新线程中执行的
	 *            连接到的Socket地址	
	 */
	// 接受服务端发送过来的实时曲线数据
	public abstract void onReceive(InetAddress addr, RealTime rTime);

	// 接受服务端发送过来的槽状态数据
	public abstract void onReceive(InetAddress addr, PotStatusDATA potStatus);

	/**
	 * 连接断开
	 * 注意：此回调是在新线程中执行的
	 * 
	 * @param addr
	 *            连接到的Socket地址
	 */
	public abstract void onDisconnect(InetAddress addr);
}
