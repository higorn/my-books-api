package higor.mybooks.infra.remotedata;

import higor.mybooks.domain.BaseEntity;
import higor.mybooks.domain.page.Page;
import higor.mybooks.domain.page.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractRemoteRepository<T extends BaseEntity<T, ID>, ID> {
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
    return (S) toEntity(dataRestClient.create(entity));
  }

  protected Page<T> toEntityPage(PagedModel<EntityModel<T>> pagedEntityModel) {
    PagedModel.PageMetadata metadata = pagedEntityModel.getMetadata();
    List<T> entityList = pagedEntityModel.getContent().stream().map(this::toEntity).collect(Collectors.toList());
    return Page.of(entityList, Page.Metadata.of((int)metadata.getNumber(), (int)metadata.getSize(),
        metadata.getTotalElements()));
  }

  protected T toEntity(EntityModel<T> entityModel) {
    String href = entityModel.getLink("self").get().getHref();
    Integer id = Integer.valueOf(href.substring(href.lastIndexOf("/") + 1));
    return entityModel.getContent().id((ID) id);
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
