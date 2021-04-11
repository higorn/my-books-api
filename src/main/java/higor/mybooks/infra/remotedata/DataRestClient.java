package higor.mybooks.infra.remotedata;

import higor.mybooks.domain.page.Page;
import higor.mybooks.domain.page.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DataRestClient<T> {
  @GetMapping(path = "/")
  Page<T> findAll(PageRequest pageRequest);
  @GetMapping(path = "/{id}")
  T findById(@PathVariable("id") int id);
  @PostMapping(path = "/")
  T create(@RequestBody T entity);
}
