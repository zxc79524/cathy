
package idv.blake.cathy.lib.restful_engine;

import java.util.Map;

public interface Restful {
	public Map<String, String> getHeaders();

	public String getUrl();

	public void onRequestResult(String response);

	public void onRequestFail(ServiceException error);

	public int getTimeoutMs();

	public Method getMethod();

	public String getContentType();

	public String getBody();

	public void request(Object tag);

	public void requestSync() throws Exception;

	public static enum Method {
		GET, POST, PUT, DELETE
	}

	public static interface WebServiceListener<E> {
		public void onRequestSuccess(E entity);

		public void onRequestFail(ServiceException error);
	}
}
