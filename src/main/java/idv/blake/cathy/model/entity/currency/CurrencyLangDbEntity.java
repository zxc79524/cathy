package idv.blake.cathy.model.entity.currency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(CurrencyLangPKDbEntity.class)
@Table(name = "CURRENCY_LANG")
public class CurrencyLangDbEntity {

	@Id
	@Column(name = "CODE", nullable = false, columnDefinition = "char(3)")
	private String code;

	@Id
	@Column(name = "LANG", nullable = false, columnDefinition = "char(5)")
	private String lang;

	@Column(name = "NAME", nullable = false, columnDefinition = "nvarchar(15)")
	private String name;

	public CurrencyLangDbEntity() {
	}

	public CurrencyLangDbEntity(String code, String lang, String name) {
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
