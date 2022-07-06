package idv.blake.cathy.model.entity.currency;

public class CurrencyLangResponse {
	private String code;
	private String lang;
	private String name;

	public CurrencyLangResponse() {
	}

	public CurrencyLangResponse(String code, String lang, String name) {
		this.code = code;
		this.lang = lang;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
