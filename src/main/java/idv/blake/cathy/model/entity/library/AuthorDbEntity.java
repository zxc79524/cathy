package idv.blake.cathy.model.entity.library;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Author")
public class AuthorDbEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long authorId;

	@Column(name = "AUTHOR_NAME", columnDefinition = "nvarchar(50)")
	private String authorName;

	@OneToMany(targetEntity = BookDbEntity.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "AuthorId", referencedColumnName = "Id") // 指到關聯資料表Book的外鍵欄位名稱
	private List<BookDbEntity> bookList = new ArrayList<>();

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public List<BookDbEntity> getBookList() {
		return bookList;
	}

	public void setBookList(List<BookDbEntity> bookList) {
		this.bookList = bookList;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

}
