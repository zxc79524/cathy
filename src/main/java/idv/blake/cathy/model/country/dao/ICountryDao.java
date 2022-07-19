package idv.blake.cathy.model.country.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import idv.blake.cathy.model.entity.country.CountryDbEntity;

@Repository
public interface ICountryDao extends CrudRepository<CountryDbEntity, String> {
	CountryDbEntity findByLang(String lang);
}
