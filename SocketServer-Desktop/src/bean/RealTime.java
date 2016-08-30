package bean;

import java.io.Serializable;

public class RealTime implements Serializable {
	private int Cur;
	private int Potv;
    private int PotNo;
	public RealTime() {

	}

	public int getPotNo() {
		return PotNo;
	}

	public void setPotNo(int potNo) {
		PotNo = potNo;
	}

	public int getCur() {
		return Cur;
	}

	public void setCur(int cur) {
		Cur = cur;
	}

	public int getPotv() {
		return Potv;
	}

	public void setPotv(int potv) {
		Potv = potv;
	}

	public RealTime(int cur, int potv, int potNo) {
		super();
		Cur = cur;
		Potv = potv;
		PotNo = potNo;
	}

	@Override
	public String toString() {
		return "RealTime [Cur=" + Cur + ", Potv=" + Potv + ", PotNo=" + PotNo + "]";
	}
	

}
