package idv.blake.cathy.model.entity.coindesk;

import java.util.Map;

public class CoindeskCurrentPriceResponse {
	CoindeskTimeResponse time;
	private String disclaimer;
	private String chartName;
	private Map<String, CoindeskBpiResponse> bpi;

	public CoindeskTimeResponse getTime() {
		return time;
	}

	public void setTime(CoindeskTimeResponse time) {
		this.time = time;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getChartName() {
		return chartName;
	}

	public void setChartName(String chartName) {
		this.chartName = chartName;
	}

	public Map<String, CoindeskBpiResponse> getBpi() {
		return bpi;
	}

	public void setBpi(Map<String, CoindeskBpiResponse> bpi) {
		this.bpi = bpi;
	}

}
