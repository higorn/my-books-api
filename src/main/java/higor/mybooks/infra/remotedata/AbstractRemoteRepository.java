package higor.mybooks.infra.remotedata;

import higor.mybooks.domain.BaseEntity;
import org.springframework.data.domain.*;
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

  public List<T> findAll(Sort sort) {
    return null;
  }

  public Page<T> findAll(Pageable pageable) {
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

  protected Page<T> toEntityPage(PagedModel<EntityModel<T>> pagedEntityModel, Sort sort) {
    PagedModel.PageMetadata metadata = pagedEntityModel.getMetadata();
    PageRequest pageRequest = PageRequest.of((int) metadata.getNumber(), (int) metadata.getSize(), sort);
    List<T> entityList = pagedEntityModel.getContent().stream().map(this::toEntity).collect(Collectors.toList());
    return new PageImpl<>(entityList, pageRequest, metadata.getTotalElements());
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

  public <S extends T> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  public <S extends T> List<S> findAll(Example<S> example) {
    return null;
  }

  public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  public <S extends T> long count(Example<S> example) {
    return 0;
  }

  public <S extends T> boolean exists(Example<S> example) {
    return false;
  }
}
