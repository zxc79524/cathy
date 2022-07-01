package idv.blake.cathy.model.webservice;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import idv.blake.cathy.lib.restful_engine.Restful;
import idv.blake.cathy.lib.restful_engine.ServiceException;
import idv.blake.cathy.model.entity.coindesk.CoindeskCurrentPriceResponse;

public class CoinDeskCurrentPriceWebService implements Restful {

	private WebServiceListener<CoindeskCurrentPriceResponse> listener;

	public CoinDeskCurrentPriceWebService(WebServiceListener<CoindeskCurrentPriceResponse> listener) {
		this.listener = listener;
	}

	@Override
	public Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json");
		// TODO Auto-generated method stub
		return headers;
	}

	@Override
	public String getUrl() {
		return "https://api.coindesk.com/v1/bpi/currentprice.json";
	}

	@Override
	public void onRequestResult(String response) {
		// TODO Auto-generated method stub


		listener.onRequestSuccess(new Gson().fromJson(response, CoindeskCurrentPriceResponse.class));

	}

	@Override
	public void onRequestFail(ServiceException error) {
		// TODO Auto-generated method stub
		listener.onRequestFail(error);
	}

	@Override
	public int getTimeoutMs() {
		// TODO Auto-generated method stub
		return 10000;
	}

	@Override
	public Method getMethod() {
		// TODO Auto-generated method stub
		return Method.GET;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return "application/json; charset=utf-8";
	}

	@Override
	public String getBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void request(Object tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestSync() throws Exception {
		// TODO Auto-generated method stub
	}

}
