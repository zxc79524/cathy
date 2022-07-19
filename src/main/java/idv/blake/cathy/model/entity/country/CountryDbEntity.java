package idv.blake.cathy.model.entity.country;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COUNTRY")
public class CountryDbEntity {

	@Id
	@Column(name = "LANG", nullable = false, columnDefinition = "char(5)")
	private String lang;

	@Column(name = "COUNRTY_NAME", nullable = false, columnDefinition = "nvarchar(20)")
	private String countryName;

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinTable(name = "CURRENCY_LANG", joinColumns = @JoinColumn(referencedColumnName = "LANG"), inverseJoinColumns = @JoinColumn(referencedColumnName = "LANG"))
//	private List<CurrencyLangDbEntity> currencyLangDbEntities;

	public CountryDbEntity() {
	}

	public CountryDbEntity(String lang, String countryName) {
		this.lang = lang;
		this.countryName = countryName;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

//	public List<CurrencyLangDbEntity> getCurrencyLangDbEntities() {
//		return currencyLangDbEntities;
//	}
//
//	public void setCurrencyLangDbEntities(List<CurrencyLangDbEntity> currencyLangDbEntities) {
//		this.currencyLangDbEntities = currencyLangDbEntities;
//	}

}
