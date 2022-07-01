/******************************************************************
 * Copyright (C) Digiwin Corporation. All rights reserved.
 *
 * Author: Samuel Lin 
 * Create Date: 2015/10/12 
 * Description:
 *
 * Revision History:
 * Date         Author           Description
 *****************************************************************/
package idv.blake.cathy.lib.restful_engine;

import java.net.Proxy;
import java.util.Map;

import javax.net.SocketFactory;

import idv.blake.cathy.lib.restful_engine.runner.JavaSyncHttpServiceRunner;
import idv.blake.cathy.lib.restful_engine.runner.LocalFileServiceRunner;


public class RestfulEngine {

	private static RunnerType mServiceRunnerType = RunnerType.JavaSync;
	private static ServiceRunner mServiceRunner;
	private static Map<String, SocketFactory> socketFactoryMap;
	private static String charset = "UTF-8";
	
	public static void init(RunnerType type, Map<String, SocketFactory> socketFactoryMap) {
		mServiceRunnerType = type;
		init(socketFactoryMap);
	}

	public static void init(RunnerType type, Map<String, SocketFactory> socketFactoryMap, String inputCharset) {
		init(type, socketFactoryMap);
		charset = inputCharset;
	}

	public static void init(Map<String, SocketFactory> socketFactoryMapInput) {
		socketFactoryMap = socketFactoryMapInput;
		initServiceRunner();
	}

	public static void init(Map<String, SocketFactory> socketFactoryMapInput, String inputCharset) {
		init(socketFactoryMapInput);
		charset = inputCharset;
	}

	private static void initServiceRunner() {
		switch (mServiceRunnerType) {
		case JavaSync:
			mServiceRunner = new JavaSyncHttpServiceRunner();
			break;
		case LocalFile:
			mServiceRunner = new LocalFileServiceRunner();
			break;
		default:
			throw new IllegalStateException("Known Service Runner Type");
		}
	}

	public static ServiceRunner getServiceRunner() {
		return mServiceRunner;
	}

	public static void request(Restful service) {
		mServiceRunner.run(service);
	}

	public static void request(Restful service, Object user) {
		mServiceRunner.run(service, user);
	}

	public static void cancelRequest(Object user) {
		mServiceRunner.cancelRequest(user);
	}

	public static ServiceRunner requestSync(Restful service) throws Exception {
		JavaSyncHttpServiceRunner runner = new JavaSyncHttpServiceRunner();
		runner.setCharset(charset);
		runner.setSocketFactoryMap(socketFactoryMap);
		runner.run(service);
		return runner;
	}

	public static ServiceRunner requestSync(Restful service, Proxy proxy) throws Exception {
		JavaSyncHttpServiceRunner runner = new JavaSyncHttpServiceRunner();
		runner.setCharset(charset);
		runner.setSocketFactoryMap(socketFactoryMap);
		runner.setProxy(proxy);
		runner.run(service);
		return runner;
	}

	public enum RunnerType {
		JavaSync, LocalFile
	}

}
