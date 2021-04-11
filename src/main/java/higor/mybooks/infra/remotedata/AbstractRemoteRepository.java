package higor.mybooks.infra.remotedata;

import higor.mybooks.domain.BaseEntity;
import higor.mybooks.domain.page.Page;
import higor.mybooks.domain.page.PageRequest;

import java.util.List;
import java.util.Optional;

public abstract class AbstractRemoteRepository<T extends BaseEntity<ID>, ID> {
  private final DataRestClient<T> dataRestClient;

  protected AbstractRemoteRepository(DataRestClient<T> dataRestClient) {
    this.dataRestClient = dataRestClient;
  }

  public List<T> findAll() {
    return null;
  }

  public List<T> findAll(String sort) {
    return null;
  }

  public Page<T> findAll(PageRequest pageRequest) {
    return null;
  }

  public List<T> findAllById(Iterable<ID> ids) {
    return null;
  }

  public long count() {
    return 0;
  }

  public void deleteById(ID id) {

  }

  public void delete(T entity) {

  }

  public void deleteAll(Iterable<? extends T> entities) {

  }

  public void deleteAll() {

  }

  public <S extends T> S save(S entity) {
    return (S) dataRestClient.create(entity);
  }

  public <S extends T> List<S> saveAll(Iterable<S> entities) {
    return null;
  }

  public Optional<T> findById(ID id) {
    return Optional.empty();
  }

  public boolean existsById(ID id) {
    return false;
  }

  public void flush() {

  }

  public <S extends T> S saveAndFlush(S entity) {
    return null;
  }

  public void deleteInBatch(Iterable<T> entities) {

  }

  public void deleteAllInBatch() {

  }

  public T getOne(ID id) {
    return null;
  }
}
