
package idv.blake.cathy.lib.restful_engine.runner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import javax.net.SocketFactory;

import idv.blake.cathy.lib.restful_engine.Restful;
import idv.blake.cathy.lib.restful_engine.ServiceException;
import idv.blake.cathy.lib.restful_engine.ServiceRunner;

public class LocalFileServiceRunner implements ServiceRunner {

	@Override
	public void run(Restful service) {
		run(service, null);
	}

	@Override
	public void run(Restful service, Object user) {
		if (service instanceof WebServiceLocalTestable) {
			WebServiceLocalTestable tesableService = (WebServiceLocalTestable) service;
			try {
				service.onRequestResult(loadFackWebServiceResult(tesableService.getTestResourceFilePath()));
			} catch (IOException e) {
				service.onRequestFail(new ServiceException("Test file not found"));
			}
		} else {
			throw new RuntimeException("Not Local File Testable Service.(Having to extent WebServiceLocalTestable)");
		}
	}

	private String loadFackWebServiceResult(String filePath) throws IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	@Override
	public void cancelRequest(Object user) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, SocketFactory> getSocketFactoryMap() {
		return null;
	}

	@Override
	public void setSocketFactoryMap(Map<String, SocketFactory> socketFactory) {

	}

}
