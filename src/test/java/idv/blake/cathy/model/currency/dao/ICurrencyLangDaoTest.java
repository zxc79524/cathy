package idv.blake.cathy.model.currency.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import idv.blake.cathy.BaseUnitTest;
import idv.blake.cathy.model.entity.currency.CurrencyLangDbEntity;

class ICurrencyLangDaoTest extends BaseUnitTest {

	@Autowired
	ICurrencyLangDao dao;

	@Test
	void test() {
		dao.save(new CurrencyLangDbEntity("TWD", "zh_TW", "新台幣"));
		dao.save(new CurrencyLangDbEntity("TWD", "zh_CN", "新台币"));
		dao.save(new CurrencyLangDbEntity("CNY", "zh_TW", "人民幣"));
		dao.save(new CurrencyLangDbEntity("CNY", "zh_CN", "人民币"));

		List<CurrencyLangDbEntity> resultList = dao.findByCode("TWD");
		assertEquals(2, resultList.size());
		System.out.println(toJson(resultList));

		dao.deleteByCode("TWD");
		resultList = dao.findByCode("TWD");
		assertEquals(0, resultList.size());
		System.out.println(toJson(resultList));

		resultList = dao.findByCode("CNY");
		assertEquals(2, resultList.size());
		System.out.println(toJson(resultList));

		dao.save(new CurrencyLangDbEntity("TWD", "zh_TW", "新台幣"));
		resultList = dao.findByLang("zh_TW");
		assertEquals(2, resultList.size());
		System.out.println(toJson(resultList));

		resultList = dao.findByLang("zh_CN");
		assertEquals(1, resultList.size());
		System.out.println(toJson(resultList));

	}

}
