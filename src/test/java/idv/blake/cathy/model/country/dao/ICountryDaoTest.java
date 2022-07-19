package idv.blake.cathy.model.country.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import idv.blake.cathy.BaseUnitTest;
import idv.blake.cathy.model.currency.dao.ICurrencyLangDao;
import idv.blake.cathy.model.entity.country.CountryDbEntity;
import idv.blake.cathy.model.entity.currency.CurrencyLangDbEntity;

class ICountryDaoTest extends BaseUnitTest {

	@Autowired
	ICurrencyLangDao currencyLangDao;

	@Autowired
	ICountryDao countryDao;

	@Test
	void test() {
		currencyLangDao.save(new CurrencyLangDbEntity("TWD", "zh_TW", "新台幣"));
		currencyLangDao.save(new CurrencyLangDbEntity("TWD", "zh_CN", "新台币"));
		currencyLangDao.save(new CurrencyLangDbEntity("CNY", "zh_TW", "人民幣"));
		currencyLangDao.save(new CurrencyLangDbEntity("CNY", "zh_CN", "人民币"));

		countryDao.save(new CountryDbEntity("zh_TW", "台灣"));
		countryDao.save(new CountryDbEntity("zh_CN", "中共"));

		CountryDbEntity countryDbEntity = countryDao.findByLang("zh_TW");

		System.out.println(toJson(countryDbEntity));
	}

}
