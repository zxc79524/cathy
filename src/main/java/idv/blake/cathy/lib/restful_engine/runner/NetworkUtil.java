
package idv.blake.cathy.lib.restful_engine.runner;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import idv.blake.cathy.lib.restful_engine.Restful;

public class NetworkUtil {

	public static final String HEADER_VALUE_ACCEPT_ENCODING = "gzip";

	public static final String HEADER_KEY_ACCEPT_ENCODING = "Accept-Encoding";

	public static String parseNetworkResponse(Restful service, InputStream responseInputStream) throws IOException {
		boolean isWebServiceEncodeGzip = isWebServiceEncodeGzip(service.getHeaders());

		if (isWebServiceEncodeGzip) {
			return parseResponseByGzip(responseInputStream);
		} else {
			return parseResponse(responseInputStream);
		}
	}

	public static String parseNetworkResponse(Restful service, InputStream responseInputStream, String inputCharset)
			throws IOException {
		boolean isWebServiceEncodeGzip = isWebServiceEncodeGzip(service.getHeaders());

		if (isWebServiceEncodeGzip) {
			return parseResponseByGzip(responseInputStream);
		} else {
			return parseResponse(responseInputStream, inputCharset);
		}
	}

	private static String parseResponseByGzip(InputStream responseInputStream) throws IOException {
		InputStream is = null;
		InputStreamReader reader = null;
		BufferedReader in = null;

		byte[] response = parseResponseByteArray(responseInputStream);
		try {
			is = new GZIPInputStream(new ByteArrayInputStream(response));

			String output = "";
			reader = new InputStreamReader(is);
			in = new BufferedReader(reader);
			String read;
			while ((read = in.readLine()) != null) {
				output += read;
			}

			return output;
		} catch (IOException e) {
			if (e != null && e.getMessage() != null && e.getMessage().indexOf("unknown format") >= 0) {
				return new String(response);
			} else {
				return "";
			}
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (in != null)
					in.close();
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String parseNetworkResponse(Restful service, byte[] responseData) throws IOException {
		return parseNetworkResponse(service, new ByteArrayInputStream(responseData));
	}

	private static byte[] parseResponseByteArray(InputStream inputStream) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();

		return buffer.toByteArray();
	}

	private static String parseResponse(InputStream inputStream) {
		StringBuffer responseBuffer = new StringBuffer();
		try {
			Charset charset = Charset.forName("UTF8");
			InputStreamReader stream = new InputStreamReader(inputStream, charset);
			BufferedReader reader = new BufferedReader(stream);

			String read = "";
			while ((read = reader.readLine()) != null) {
				responseBuffer.append(read).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseBuffer.toString();
	}

	private static String parseResponse(InputStream inputStream, String inputCharset) {
		StringBuffer responseBuffer = new StringBuffer();
		try {
			Charset charset = Charset.forName(inputCharset);
			InputStreamReader stream = new InputStreamReader(inputStream, charset);
			BufferedReader reader = new BufferedReader(stream);

			String read = "";
			while ((read = reader.readLine()) != null) {
				responseBuffer.append(read).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseBuffer.toString();
	}

	private static boolean isWebServiceEncodeGzip(Map<String, String> headers) {
		return headers.containsKey(HEADER_KEY_ACCEPT_ENCODING) && headers.get(HEADER_KEY_ACCEPT_ENCODING) != null
				&& headers.get(HEADER_KEY_ACCEPT_ENCODING).equalsIgnoreCase(HEADER_VALUE_ACCEPT_ENCODING);
	}

	public static SocketFactory allowAllSSL() throws KeyManagementException, NoSuchAlgorithmException {
		return getSSLContextAllowAll().getSocketFactory();
	}

	public static SSLContext getSSLContextAllowAll() throws KeyManagementException, NoSuchAlgorithmException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Create an SSLContext that uses our TrustManager
		SSLContext context = SSLContext.getInstance("TLSv1.2");
		context.init(null, trustAllCerts, new java.security.SecureRandom());
		return context;
	}

}
