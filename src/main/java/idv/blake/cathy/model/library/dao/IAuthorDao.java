package idv.blake.cathy.model.library.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import idv.blake.cathy.model.entity.library.AuthorDbEntity;

@Repository
public interface IAuthorDao extends CrudRepository<AuthorDbEntity, Long> {

	List<AuthorDbEntity> findByAuthorName(String authroName);
}
