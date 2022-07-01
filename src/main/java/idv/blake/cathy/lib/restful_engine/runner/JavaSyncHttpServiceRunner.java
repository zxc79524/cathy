package idv.blake.cathy.lib.restful_engine.runner;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import idv.blake.cathy.lib.restful_engine.Restful;
import idv.blake.cathy.lib.restful_engine.ServiceException;
import idv.blake.cathy.lib.restful_engine.ServiceRunner;


public class JavaSyncHttpServiceRunner implements ServiceRunner {

	private String responseBody = "";
	private int responseCode = 0;
	private Map<String, SocketFactory> socketFactoryMap;
	private Proxy proxy = null;
	private String charset = "UTF-8";

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public void setSocketFactoryMap(Map<String, SocketFactory> socketFactoryMap) {
		this.socketFactoryMap = socketFactoryMap;
	}

	@Override
	public Map<String, SocketFactory> getSocketFactoryMap() {
		return this.socketFactoryMap;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	@Override
	public void run(Restful service) {
		run(service, null);
	}

	private boolean isHttps(URL url) {
		return "https".equals(url.getProtocol());
	}

	@Override
	public void run(Restful service, Object user) {
		HttpURLConnection conn = null;
		try {
			String requestMethod = service.getMethod().toString();
			URL url = new URL(service.getUrl());

			System.setProperty("http.keepAlive", "true");
			System.setProperty("http.maxConnections", "64");

			if (proxy != null) {
				conn = (HttpURLConnection) ((url.openConnection(proxy)));
			} else {
				conn = (HttpURLConnection) ((url.openConnection()));
			}

			if (isHttps(url)) {
				// if (((HttpsURLConnection) conn).getSSLSocketFactory() == null) {
				setupSocketFactory(conn, url);
				// }
			}

			conn.setRequestProperty("Content-Type", service.getContentType());
			conn.setRequestProperty("Charset", charset);

			Map<String, String> httpHeaders = service.getHeaders();
			Set<String> headerKeySet = httpHeaders.keySet();
			for (String headerKey : headerKeySet) {
				conn.setRequestProperty(headerKey, httpHeaders.get(headerKey));
			}

			conn.setRequestMethod(requestMethod);
			conn.setConnectTimeout(service.getTimeoutMs());
			conn.setReadTimeout(service.getTimeoutMs());

			String requestBody = service.getBody();
			if ((requestMethod.equals("POST") || requestMethod.equals("PUT"))) {
				conn.setDoOutput(true);
				if (requestBody != null && !requestBody.equals("")) {
					OutputStream out = conn.getOutputStream();
					out.write(requestBody.getBytes(charset));
					out.close();
				}
			}
			responseCode = conn.getResponseCode();
			responseBody = NetworkUtil.parseNetworkResponse(service, conn.getInputStream(), charset);

			if (responseCode >= 200 && responseCode <= 204) {
				service.onRequestResult(responseBody);
			} else {
				String errorMessage = "Call Service Error, Http Code: " + conn.getResponseCode() + " Response Body:"
						+ responseBody;
				service.onRequestFail(new ServiceException(errorMessage, responseCode));

			}
		} catch (Exception requestException) {
			// Log.e("responseCode", String.valueOf(responseCode));
			// requestException.printStackTrace();
			ServiceException ex = new ServiceException("Http - Exception " + requestException.getMessage(),
					requestException, responseCode);
			ex.setRequestBody(service.getBody());
			service.onRequestFail(ex);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	private void setupSocketFactory(HttpURLConnection urlConnection, URL url) {
//		String[] pathAry = url.getHost().split("\\.");
//		String domainName = pathAry[pathAry.length - 2] + "." + pathAry[pathAry.length - 1];
//		
//		
//		((HttpsURLConnection) urlConnection).setSSLSocketFactory((SSLSocketFactory) socketFactoryMap.get(domainName));
//		((HttpsURLConnection) urlConnection).setHostnameVerifier(new HostnameVerifier() {
//
//			@Override
//			public boolean verify(String hostname, SSLSession session) {
//				return true;
//			}
//
//		});
	}

	@Override
	public void cancelRequest(Object user) {

	}
}
