package higor.mybooks.infra.remotedata;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DataRestClient<T> {
  @GetMapping(path = "/")
  PagedModel<T> findAll(Pageable page);
  @GetMapping(path = "/{id}")
  EntityModel<T> findById(@PathVariable("id") int id);
  @PostMapping(path = "/")
  EntityModel<T> create(@RequestBody T entity);
}
