package com.jzj.socket;

import java.awt.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import bean.PotStatus;
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
	protected ObjectOutputStream objectOutputStream;	
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

	/**
	 * 发送字符串
	 * 
	 * @param s
	 *            字符串
	 * @return 发送成功返回true
	 */
	public boolean send(String s) {
		if (out != null) {
			try {
				out.writeUTF(s);
				out.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// 发送DATA类型数据
	public boolean send(RealTime realTime) {
		if (out != null) {
			try {
				out.writeInt(realTime.getCur());
				out.writeInt(realTime.getPotv());
				out.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	// 发送复合实时曲线数据到客户吨段
		public boolean send(int actionId, RealTime realTime) {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.writeInt(actionId);
					objectOutputStream.writeObject(realTime);					
					objectOutputStream.flush();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}

	
	// 发送槽状态数据
		public boolean send(ArrayList<PotStatus> potStatus) {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.writeObject(potStatus);					
					objectOutputStream.flush();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		
		//发送复合槽状态数据到客户段
		public boolean send(int actionId,ArrayList<PotStatus> potStatus) {			
			 //发送槽状态和操作命令			
			if (objectOutputStream != null) {
				try {
					objectOutputStream.writeInt(actionId);  
					objectOutputStream.writeObject(potStatus);					
					objectOutputStream.flush();
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
			objectOutputStream=new ObjectOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			runFlag = false;
		}
		while (runFlag) {
			final RequestAction requestAction = ReadFromClient(in);
			this.onReceive(addr, requestAction);
			// final String s = in.readUTF();
			// this.onReceive(addr, s);
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

	private RequestAction ReadFromClient(DataInputStream in) {
		RequestAction action = new RequestAction();
		try {
			action.setActionId(in.readInt());
			action.setPotNo_Area(in.readUTF());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return action;
	}

	/**
	 * 接收到数据
	 * <p>
	 * 注意：此回调是在新线程中执行的
	 * 
	 * @param addr
	 *            连接到的Socket地址
	 * @param s
	 *            收到的字符串
	 */
	public abstract void onReceive(InetAddress addr, String s);

	// 接受客户端命令
	public abstract void onReceive(InetAddress addr, RequestAction action);

	/**
	 * 连接断开
	 * <p>
	 * 注意：此回调是在新线程中执行的
	 * 
	 * @param addr
	 *            连接到的Socket地址
	 */
	public abstract void onDisconnect(InetAddress addr);
}
