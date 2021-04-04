package higor.mybooks.domain.userbook;

import higor.mybooks.domain.BaseEntity;

public class UserBook implements BaseEntity<UserBook, Integer> {
  private Integer id;
  public String user;
  public String book;
  private boolean read;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getBook() {
    return book;
  }

  public void setBook(String book) {
    this.book = book;
  }

  public boolean isRead() {
    return read;
  }

  public void setRead(boolean read) {
    this.read = read;
  }

  @Override
  public UserBook id(Integer id) {
    this.id = id;
    return this;
  }

  public UserBook user(String user) {
    this.user = user;
    return this;
  }

  public UserBook book(String book) {
    this.book = book;
    return this;
  }

  public UserBook read(boolean read) {
    this.read = read;
    return this;
  }
}
