package bean;

import java.awt.List;
import java.io.Serializable;
import java.util.ArrayList;

public class PotStatusDATA implements Serializable {
	
	private int room;
	private int area;
	private int sysI;
	private int sysV;
	private int roomV;
	private ArrayList<PotStatus> potData;
	public int getRoom() {
		return room;
	}
	public void setRoom(int room) {
		this.room = room;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public int getSysI() {
		return sysI;
	}
	public void setSysI(int sysI) {
		this.sysI = sysI;
	}
	public int getSysV() {
		return sysV;
	}
	public void setSysV(int sysV) {
		this.sysV = sysV;
	}
	public int getRoomV() {
		return roomV;
	}
	public void setRoomV(int roomV) {
		this.roomV = roomV;
	}
	public ArrayList<PotStatus> getPotData() {
		return potData;
	}
	public void setPotData(ArrayList<PotStatus> potData) {
		this.potData = potData;
	}
	public PotStatusDATA(int room, int area, int sysI, int sysV, int roomV, ArrayList<PotStatus> potData) {
		super();
		this.room = room;
		this.area = area;
		this.sysI = sysI;
		this.sysV = sysV;
		this.roomV = roomV;
		this.potData = potData;
	}
	public PotStatusDATA() {
		super();
	}
	@Override
	public String toString() {
		return "PotStatusDATA [room=" + room + ", area=" + area + ", sysI=" + sysI + ", sysV=" + sysV + ", roomV="
				+ roomV + ", potData=" + potData + "]";
	}	

}
