package idv.blake.cathy.model.entity.currency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CURRENCY_LANG")
public class CurrencyLangDbEntity {

	@Id
	@Column(nullable = false, length = 3, columnDefinition = "char(3)")
	private String code;

	@Column(nullable = false, length = 15, columnDefinition = "nvarchar(15)")
	private String name;

	public CurrencyLangDbEntity() {
	}

	public CurrencyLangDbEntity(String code, String name) {
		this.code = code;
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

}
