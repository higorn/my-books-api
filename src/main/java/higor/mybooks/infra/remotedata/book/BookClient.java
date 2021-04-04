package higor.mybooks.infra.remotedata.book;

import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.page.MyPageRequest;
import higor.mybooks.domain.user.User;
import higor.mybooks.infra.remotedata.DataRestClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "books", path = "/v1/books")
public interface BookClient extends DataRestClient<Book> {

  @GetMapping(path = "/search/find-by-term")
  PagedModel<EntityModel<Book>> findByTerm(@RequestParam("term") String term, Pageable page);
  @GetMapping(path = "/search/find-by-term")
  PagedModel<EntityModel<Book>> findByTerm2(@RequestParam("term") String term, MyPageRequest page);

  @GetMapping(path = "/{id}/users")
  PagedModel<EntityModel<User>> findBookUsers(@PathVariable("id") int id);

  // This is been kept just for reference, but shouldn't be used
  @Deprecated
  @GetMapping(path = "/{id}/users", consumes = "text/uri-list")
  ResponseEntity<Void> updateBookUsers(@PathVariable("id") int id, @RequestBody String userUris);
}
