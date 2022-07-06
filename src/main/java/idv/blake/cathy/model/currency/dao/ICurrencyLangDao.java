package idv.blake.cathy.model.currency.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import idv.blake.cathy.model.entity.currency.CurrencyLangDbEntity;

@Repository
public interface ICurrencyLangDao extends CrudRepository<CurrencyLangDbEntity, String> {

	List<CurrencyLangDbEntity> findByCode(String code);

	List<CurrencyLangDbEntity> findByLang(String lang);

	CurrencyLangDbEntity findByCodeAndLang(String code, String lang);

	@Transactional
	void deleteByCodeAndLang(String code, String lang);

	@Transactional
	void deleteByCode(String code);

}
