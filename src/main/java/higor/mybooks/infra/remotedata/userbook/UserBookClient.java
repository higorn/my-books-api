package higor.mybooks.infra.remotedata.userbook;

import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.page.PageRequest;
import higor.mybooks.domain.userbook.UserBook;
import higor.mybooks.infra.remotedata.DataRestClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-books-service", path = "/v1/userBooks")
public interface UserBookClient extends DataRestClient<UserBook> {
  @GetMapping(path = "/search/findByUserId")
  PagedModel<EntityModel<UserBook>> findByUserId(@RequestParam("userId") Integer userId, PageRequest pageRequest);
  @GetMapping(path = "/{id}/book")
  EntityModel<Book> getBookByUserId(@PathVariable("id") int id);
}
