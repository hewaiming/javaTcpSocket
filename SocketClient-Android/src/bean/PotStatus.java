package bean;

import java.io.Serializable;

public class PotStatus implements Serializable {
	private int PotNo;
	private String Status;
	private boolean AutoRun;
	private String Operation;
	private float SetV;
	private float WorkV;
	private int SetNb;
	private int WorkNb;
	private String NbTime;
	public PotStatus(int potNo, String status, boolean autoRun, String operation, float setV, float workV, int setNb,
			int workNb, String nbTime) {
		super();
		PotNo = potNo;
		Status = status;
		AutoRun = autoRun;
		Operation = operation;
		SetV = setV;
		WorkV = workV;
		SetNb = setNb;
		WorkNb = workNb;
		NbTime = nbTime;
	}
	public PotStatus() {
		super();
	}
	public int getPotNo() {
		return PotNo;
	}
	public void setPotNo(int potNo) {
		PotNo = potNo;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public boolean isAutoRun() {
		return AutoRun;
	}
	public void setAutoRun(boolean autoRun) {
		AutoRun = autoRun;
	}
	public String getOperation() {
		return Operation;
	}
	public void setOperation(String operation) {
		Operation = operation;
	}
	public float getSetV() {
		return SetV;
	}
	public void setSetV(float setV) {
		SetV = setV;
	}
	public float getWorkV() {
		return WorkV;
	}
	public void setWorkV(float workV) {
		WorkV = workV;
	}
	public int getSetNb() {
		return SetNb;
	}
	public void setSetNb(int setNb) {
		SetNb = setNb;
	}
	public int getWorkNb() {
		return WorkNb;
	}
	public void setWorkNb(int workNb) {
		WorkNb = workNb;
	}
	public String getNbTime() {
		return NbTime;
	}
	public void setNbTime(String nbTime) {
		NbTime = nbTime;
	}
	@Override
	public String toString() {
		return "PotStatus [PotNo=" + PotNo + ", Status=" + Status + ", AutoRun=" + AutoRun + ", Operation=" + Operation
				+ ", SetV=" + SetV + ", WorkV=" + WorkV + ", SetNb=" + SetNb + ", WorkNb=" + WorkNb + ", NbTime="
				+ NbTime + "]";
	}

}
