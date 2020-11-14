package higor.mybooks.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "Book")
public class BookDto {
  public Integer     id;
  @Schema(example = "Effective Java")
  public String       title;
  @Schema(example = "Programming Language Guide")
  public String       subtitle;
  @Schema(example = "Joshua Bloch")
  public String       author;
  @Schema(example = "Addison-Wesley")
  public String       publishingCompany;
  public List<String> categories;
  @Schema(example = "252")
  public Integer      pages;
  public boolean      read;

  public BookDto id(Integer id) {
    this.id = id;
    return this;
  }

  public BookDto title(String title) {
    this.title = title;
    return this;
  }

  public BookDto subtitle(String subtitle) {
    this.subtitle = subtitle;
    return this;
  }

  public BookDto author(String author) {
    this.author = author;
    return this;
  }

  public BookDto publishingCompany(String publishingCompany) {
    this.publishingCompany = publishingCompany;
    return this;
  }

  public BookDto categories(List<String> categories) {
    this.categories = categories;
    return this;
  }

  public BookDto pages(Integer pages) {
    this.pages = pages;
    return this;
  }

  public BookDto read(boolean read) {
    this.read = read;
    return this;
  }
}
