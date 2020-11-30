package higor.mybooks.infra;

import higor.mybooks.domain.book.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "books", path = "/v1/books")
public interface BooksClient {

  @GetMapping(path = "/")
  PagedModel<Book> getBooks(Pageable page);
  @GetMapping(path = "/{id}")
  Book getBook(@PathVariable("id") int id);
}
