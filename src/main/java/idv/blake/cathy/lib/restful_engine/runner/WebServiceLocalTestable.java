
package idv.blake.cathy.lib.restful_engine.runner;

import java.io.File;

import idv.blake.cathy.lib.restful_engine.Restful;

public abstract class WebServiceLocalTestable implements Restful {

	private String mFilePath;

	public String getTestResourceFilePath() {
		return mFilePath;
	}

	public void setTestResourceFilePath(String path) {
		File file = new File(path);
		if (file.exists()) {
			mFilePath = path;
		} else {
			throw new RuntimeException("No Test Resource File.");
		}
	}
}
