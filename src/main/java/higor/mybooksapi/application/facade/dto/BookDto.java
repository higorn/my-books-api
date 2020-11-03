package higor.mybooksapi.application.facade.dto;

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
}
