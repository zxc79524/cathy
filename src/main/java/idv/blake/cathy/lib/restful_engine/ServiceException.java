package idv.blake.cathy.lib.restful_engine;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 6597650915042705283L;

	private int statusCode = 400;
	private String responseBody;
	private String requestBody;

	public ServiceException(String errorMessage) {
		super(errorMessage);
	}

	public ServiceException(String errorMessage, int statusCode) {
		this(errorMessage);
		this.statusCode = statusCode;
	}

	public ServiceException(String message, Throwable cause, int statusCode) {
		super(message, cause);
		this.statusCode = statusCode;
	}

	/*
	 * public ServiceException(VolleyError error) { super(error); if (error == null)
	 * { Log.d("NeweggBox ServiceError", "VolleyError = null"); return; } if
	 * (error.networkResponse == null) { Log.d("NeweggBox ServiceError",
	 * "VolleyError.networkResponse = null_error = " + error); return; } if
	 * (error.networkResponse != null) { this.statusCode =
	 * error.networkResponse.statusCode; try { this.responseBody = new
	 * String(error.networkResponse.data, "utf-8"); } catch
	 * (UnsupportedEncodingException e) { e.printStackTrace(); } } else {
	 * this.statusCode = 0; this.responseBody = error.getMessage(); } }
	 */

	public String getResponseBody() {
		return responseBody;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public String toString() {
		return statusCode + " : " + getCause() + " : " + getLocalizedMessage();
	}

}
