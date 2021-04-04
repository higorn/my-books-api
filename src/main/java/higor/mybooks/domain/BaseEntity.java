package higor.mybooks.domain;

public interface BaseEntity<T, ID> {
  ID getId();
  void setId(ID id);
  T id(ID id);
}
