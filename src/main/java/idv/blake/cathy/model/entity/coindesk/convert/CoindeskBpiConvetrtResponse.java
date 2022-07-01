package idv.blake.cathy.model.entity.coindesk.convert;

public class CoindeskBpiConvetrtResponse {
	private String code;
	private String name;
	private String rate;
	private float rateFloat;

	public CoindeskBpiConvetrtResponse() {
	}

	public CoindeskBpiConvetrtResponse(String code, String name, String rate, float rateFloat) {
		this.code = code;
		this.name = name;
		this.rate = rate;
		this.rateFloat = rateFloat;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public float getRateFloat() {
		return rateFloat;
	}

	public void setRateFloat(float rateFloat) {
		this.rateFloat = rateFloat;
	}

}
