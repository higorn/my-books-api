package higor.mybooks.domain.userbook;

import higor.mybooks.domain.user.User;
import higor.mybooks.domain.book.Book;

import javax.persistence.*;

@Entity
@Table(name = "account_book")
public class UserBook {
  @Id
  @SequenceGenerator(name = "UserBook_SEQ", sequenceName = "account_book_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserBook_SEQ")
  private Integer id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id")
  private User    user;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id")
  private Book    book;
  @Column(name = "is_read")
  private boolean read;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public boolean isRead() {
    return read;
  }

  public void setRead(boolean read) {
    this.read = read;
  }
}
