package idv.blake.cathy.model.entity.currency;

import java.io.Serializable;

public class CurrencyLangPKDbEntity implements Serializable {
	private String code;

	private String lang;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
