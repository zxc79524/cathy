package idv.blake.cathy.lib.restful_engine;

import java.util.Map;

import javax.net.SocketFactory;

public interface ServiceRunner {
	public void run(Restful service);

	public void run(Restful service, Object user);

	public void cancelRequest(Object user);

	public Map<String, SocketFactory> getSocketFactoryMap();

	public void setSocketFactoryMap(Map<String, SocketFactory> socketFactory);
}
