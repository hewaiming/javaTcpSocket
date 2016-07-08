package bean;

import java.io.Serializable;

public class RealTime implements Serializable {
	private int Cur;
	private int Potv;

	public RealTime() {

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

	public RealTime(int cur, int potv) {
		super();
		Cur = cur;
		Potv = potv;
	}

	@Override
	public String toString() {
		return "RealTime [Cur=" + Cur + ", Potv=" + Potv + "]";
	}

}
