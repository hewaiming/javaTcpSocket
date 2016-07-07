package bean;

public class RequestAction {
	private int ActionId;
	private String PotNo_Area;

	public int getActionId() {
		return ActionId;
	}

	public void setActionId(int i) {
		ActionId = i;
	}

	public String getPotNo_Area() {
		return PotNo_Area;
	}

	public void setPotNo_Area(String potNo_Area) {
		PotNo_Area = potNo_Area;
	}

	public RequestAction(int actionId, String potNo_Area) {
		super();
		ActionId = actionId;
		PotNo_Area = potNo_Area;
	}

	public RequestAction() {
		super();
	}

	@Override
	public String toString() {
		return "RequestAction [ActionId=" + ActionId + ", PotNo_Area=" + PotNo_Area + "]";
	}

}
