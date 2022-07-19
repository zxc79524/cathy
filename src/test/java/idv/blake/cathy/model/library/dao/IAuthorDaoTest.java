package idv.blake.cathy.model.library.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import idv.blake.cathy.BaseUnitTest;
import idv.blake.cathy.model.entity.library.AuthorDbEntity;
import idv.blake.cathy.model.entity.library.BookDbEntity;

class IAuthorDaoTest extends BaseUnitTest {

	@Autowired
	IAuthorDao authorDao;

	@Autowired
	IBookDao bookDao;

	@Transactional
	@Test
	void test() {
		authorDao.save(generateAuthor("藤子不二雄", null));
		authorDao.save(generateAuthor("藤子不二雄", null));
		authorDao.save(generateAuthor("藤子不二雄", null));

		List<AuthorDbEntity> authors = authorDao.findByAuthorName("藤子不二雄");
		Iterable<BookDbEntity> books = bookDao.findAll();
//		System.out.println("================================");
//
//		System.out.println(toJson(authors));
//		System.out.println(toJson(books));

		authors.get(0).setBookList(generateBooks(authors.get(0).getAuthorId(), "哆啦A夢第1集", "哆啦A夢第2集"));
		authors.get(1).setBookList(generateBooks(authors.get(1).getAuthorId(), "哆啦A夢第3集", "哆啦A夢第4集"));
		authors.get(2).setBookList(generateBooks(authors.get(2).getAuthorId(), "哆啦A夢超棒球第1集"));

		authorDao.save(copyAuthorDbEntity(authors.get(0)));
		authorDao.save(copyAuthorDbEntity(authors.get(1)));
		authorDao.save(copyAuthorDbEntity(authors.get(2)));
		authors = authorDao.findByAuthorName("藤子不二雄");
		books = bookDao.findAll();
//		System.out.println("================================");
//		System.out.println(toJson(authors));
//		System.out.println(toJson(books));

		bookDao.delete(authors.get(1).getBookList().get(0));
		bookDao.delete(authors.get(1).getBookList().get(1));
		authors.get(0).getBookList().get(0).setBookName("Hello World");
		authorDao.save(authors.get(0));
		authorDao.delete(authors.get(2));

		authors = authorDao.findByAuthorName("藤子不二雄");
		books = bookDao.findAll();
//		System.out.println("================================");
//		System.out.println(toJson(authors));
//		System.out.println(toJson(books));
	}

	private AuthorDbEntity copyAuthorDbEntity(AuthorDbEntity sourcEntity) {
		AuthorDbEntity result = new AuthorDbEntity();
		result.setAuthorId(sourcEntity.getAuthorId());
		result.setAuthorName(sourcEntity.getAuthorName());
		result.setBookList(new ArrayList<>());

		for (BookDbEntity book : sourcEntity.getBookList()) {
			result.getBookList().add(copyBookDbEntity(book, result));
		}

		return result;
	}

	private BookDbEntity copyBookDbEntity(BookDbEntity source, AuthorDbEntity authorDbEntity) {
		BookDbEntity bookDbEntity = new BookDbEntity();
		bookDbEntity.setBookId(source.getBookId());
		bookDbEntity.setBookName(source.getBookName());
		bookDbEntity.setAuthor(authorDbEntity);

		return bookDbEntity;

	}

	private List<BookDbEntity> generateBooks(Long authroId, String... bookNames) {
		List<BookDbEntity> books = new ArrayList<>();

		for (String bookName : bookNames) {
			BookDbEntity book = new BookDbEntity();
			book.setBookName(bookName);

			AuthorDbEntity authorDbEntity = new AuthorDbEntity();
			authorDbEntity.setAuthorId(authroId);
			book.setAuthor(authorDbEntity);

//			book.setAuthorId(authroId);
			books.add(book);
		}

		return books;
	}

	private AuthorDbEntity generateAuthor(String authorName, List<BookDbEntity> books) {

		AuthorDbEntity author = new AuthorDbEntity();
		author.setAuthorName(authorName);
		author.setBookList(books);

		return author;

	}

}
