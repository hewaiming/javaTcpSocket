package bean;

import java.io.Serializable;

public class PotStatus implements Serializable {
	private int PotNo;
	private String Status;//槽状态
	private boolean AutoRun;
	private boolean AeFlag;
	private byte Abnormal_Flag;
	private String Operation;//槽作业
	private String SetV;
	private String WorkV;
	private int SetNb;
	private int WorkNb;
	private int NbPlus; //过欠NB
	private String NbTime; 	 
	private int AeSpan;  //Ae间隔
	private String AeDateTime; 
	private String AeV;
	private int AeContinue; //Ae时长
	private String AeStatus;
	private int AeCnt;
	private String OStatus;//加料状态
	private int YJWJ;  //阳极位置
	private int FaultNo;
	private int Noise;  //噪音
	private int Comerr; //通讯故障	
	private int PotCtrl;  //槽状态控制字开关
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
	public int getNbPlus() {
		return NbPlus;
	}
	public void setNbPlus(int nbPlus) {
		NbPlus = nbPlus;
	}
	public String getNbTime() {
		return NbTime;
	}
	public void setNbTime(String nbTime) {
		NbTime = nbTime;
	}
	public int getAeSpan() {
		return AeSpan;
	}
	public void setAeSpan(int aeSpan) {
		AeSpan = aeSpan;
	}
	public String getAeDateTime() {
		return AeDateTime;
	}
	public void setAeDateTime(String aeDateTime) {
		AeDateTime = aeDateTime;
	}
	
	public int getAeContinue() {
		return AeContinue;
	}
	public void setAeContinue(int aeContinue) {
		AeContinue = aeContinue;
	}
	public String getAeStatus() {
		return AeStatus;
	}
	public void setAeStatus(String aeStatus) {
		AeStatus = aeStatus;
	}
	public int getAeCnt() {
		return AeCnt;
	}
	public void setAeCnt(int aeCnt) {
		AeCnt = aeCnt;
	}
	public String getOStatus() {
		return OStatus;
	}
	public void setOStatus(String oStatus) {
		OStatus = oStatus;
	}
	public int getYJWJ() {
		return YJWJ;
	}
	public void setYJWJ(int yJWJ) {
		YJWJ = yJWJ;
	}
	public int getFaultNo() {
		return FaultNo;
	}
	public void setFaultNo(int faultNo) {
		FaultNo = faultNo;
	}
	public int getNoise() {
		return Noise;
	}
	public void setNoise(int noise) {
		Noise = noise;
	}
	public int getComerr() {
		return Comerr;
	}
	public void setComerr(int comerr) {
		Comerr = comerr;
	}


	public String getSetV() {
		return SetV;
	}


	public void setSetV(String setV) {
		SetV = setV;
	}


	public String getWorkV() {
		return WorkV;
	}


	public void setWorkV(String workV) {
		WorkV = workV;
	}


	public String getAeV() {
		return AeV;
	}


	public void setAeV(String aeV) {
		AeV = aeV;
	}
	
	public boolean isAeFlag() {
		return AeFlag;
	}


	public void setAeFlag(boolean aeFlag) {
		AeFlag = aeFlag;
	}


	public byte getAbnormal_Flag() {
		return Abnormal_Flag;
	}


	public void setAbnormal_Flag(byte abnormal_Flag) {
		Abnormal_Flag = abnormal_Flag;
	}


	
	public int getPotCtrl() {
		return PotCtrl;
	}


	public void setPotCtrl(int potCtrl) {
		PotCtrl = potCtrl;
	}


	public PotStatus(int potNo, String status, boolean autoRun, boolean aeFlag, byte abnormal_Flag, String operation,
			String setV, String workV, int setNb, int workNb, int nbPlus, String nbTime, int aeSpan, String aeDateTime,
			String aeV, int aeContinue, String aeStatus, int aeCnt, String oStatus, int yJWJ, int faultNo, int noise,
			int comerr) {
		super();
		PotNo = potNo;
		Status = status;
		AutoRun = autoRun;
		AeFlag = aeFlag;
		Abnormal_Flag = abnormal_Flag;
		Operation = operation;
		SetV = setV;
		WorkV = workV;
		SetNb = setNb;
		WorkNb = workNb;
		NbPlus = nbPlus;
		NbTime = nbTime;
		AeSpan = aeSpan;
		AeDateTime = aeDateTime;
		AeV = aeV;
		AeContinue = aeContinue;
		AeStatus = aeStatus;
		AeCnt = aeCnt;
		OStatus = oStatus;
		YJWJ = yJWJ;
		FaultNo = faultNo;
		Noise = noise;
		Comerr = comerr;
	}


	public PotStatus(int potNo, String status, boolean autoRun, boolean aeFlag, byte abnormal_Flag, String operation,
			String setV, String workV, int setNb, int workNb, int nbPlus, String nbTime, int aeSpan, String aeDateTime,
			String aeV, int aeContinue, String aeStatus, int aeCnt, String oStatus, int yJWJ, int faultNo, int noise,
			int comerr, int potCtrl) {
		super();
		PotNo = potNo;
		Status = status;
		AutoRun = autoRun;
		AeFlag = aeFlag;
		Abnormal_Flag = abnormal_Flag;
		Operation = operation;
		SetV = setV;
		WorkV = workV;
		SetNb = setNb;
		WorkNb = workNb;
		NbPlus = nbPlus;
		NbTime = nbTime;
		AeSpan = aeSpan;
		AeDateTime = aeDateTime;
		AeV = aeV;
		AeContinue = aeContinue;
		AeStatus = aeStatus;
		AeCnt = aeCnt;
		OStatus = oStatus;
		YJWJ = yJWJ;
		FaultNo = faultNo;
		Noise = noise;
		Comerr = comerr;
		PotCtrl = potCtrl;
	}


	@Override
	public String toString() {
		return "PotStatus [PotNo=" + PotNo + ", Status=" + Status + ", AutoRun=" + AutoRun + ", AeFlag=" + AeFlag
				+ ", Abnormal_Flag=" + Abnormal_Flag + ", Operation=" + Operation + ", SetV=" + SetV + ", WorkV="
				+ WorkV + ", SetNb=" + SetNb + ", WorkNb=" + WorkNb + ", NbPlus=" + NbPlus + ", NbTime=" + NbTime
				+ ", AeSpan=" + AeSpan + ", AeDateTime=" + AeDateTime + ", AeV=" + AeV + ", AeContinue=" + AeContinue
				+ ", AeStatus=" + AeStatus + ", AeCnt=" + AeCnt + ", OStatus=" + OStatus + ", YJWJ=" + YJWJ
				+ ", FaultNo=" + FaultNo + ", Noise=" + Noise + ", Comerr=" + Comerr + ", PotCtrl=" + PotCtrl + "]";
	}


}
