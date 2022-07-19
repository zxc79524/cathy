package idv.blake.cathy.model.entity.library;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Book")
public class BookDbEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BookId")
	private long bookId;

	@Column(name = "BOOK_NAME", columnDefinition = "nvarchar(50)")
	private String bookName;

//	@Column(name = "AuthorId") // 外鍵欄位
//	private long authorId;

	@OneToOne(targetEntity = AuthorDbEntity.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "AuthorId", referencedColumnName = "Id") // 指到關聯資料表Book的外鍵欄位名稱
	private AuthorDbEntity author;

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public AuthorDbEntity getAuthor() {
		return author;
	}

	public void setAuthor(AuthorDbEntity author) {
		this.author = author;
	}

//	public long getAuthorId() {
//		return authorId;
//	}
//
//	public void setAuthorId(long authorId) {
//		this.authorId = authorId;
//	}

	// getters and setters ommitted

}
