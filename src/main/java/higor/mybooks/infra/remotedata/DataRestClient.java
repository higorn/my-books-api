package higor.mybooks.infra.remotedata;

import higor.mybooks.domain.page.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DataRestClient<T> {
  @GetMapping(path = "/")
  PagedModel<T> findAll(PageRequest pageRequest);
  @GetMapping(path = "/{id}")
  EntityModel<T> findById(@PathVariable("id") int id);
  @PostMapping(path = "/")
  EntityModel<T> create(@RequestBody T entity);
}
