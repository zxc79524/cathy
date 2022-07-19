package idv.blake.cathy.model.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import idv.blake.cathy.model.entity.library.BookDbEntity;

@Repository
public interface IBookDao extends CrudRepository<BookDbEntity, Long> {

}
