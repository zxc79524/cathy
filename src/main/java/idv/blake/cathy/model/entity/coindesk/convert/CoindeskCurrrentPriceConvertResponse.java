package idv.blake.cathy.model.entity.coindesk.convert;

import java.util.HashMap;
import java.util.Map;

public class CoindeskCurrrentPriceConvertResponse {
	private String updateTime;

	private Map<String, CoindeskBpiConvetrtResponse> currency = new HashMap<String, CoindeskBpiConvetrtResponse>();

	public CoindeskCurrrentPriceConvertResponse() {
	}

	public CoindeskCurrrentPriceConvertResponse(String updateTime, Map<String, CoindeskBpiConvetrtResponse> currency) {
		this.updateTime = updateTime;
		this.currency = currency;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Map<String, CoindeskBpiConvetrtResponse> getCurrency() {
		return currency;
	}

	public void setCurrency(Map<String, CoindeskBpiConvetrtResponse> currency) {
		this.currency = currency;
	}

}
