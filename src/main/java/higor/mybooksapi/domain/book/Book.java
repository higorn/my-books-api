package higor.mybooksapi.domain.book;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Book {
  @Id
  private Integer      id;
  private String       title;
  private String       subtitle;
  private String       author;
  private String       publishingCompany;
  private Integer      pages;
  private boolean      read;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getPublishingCompany() {
    return publishingCompany;
  }

  public void setPublishingCompany(String publishingCompany) {
    this.publishingCompany = publishingCompany;
  }

  public Integer getPages() {
    return pages;
  }

  public void setPages(Integer pages) {
    this.pages = pages;
  }

  public boolean isRead() {
    return read;
  }

  public void setRead(boolean read) {
    this.read = read;
  }
}
