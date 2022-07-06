package idv.blake.cathy.model.entity.currency;

public class CurrencyLangRequest {
	private String code;
	private String name;
	private String lang;

	public CurrencyLangRequest() {
	}

	public CurrencyLangRequest(String code, String lang, String name) {
		this.code = code;
		this.name = name;
		this.lang = lang;
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
