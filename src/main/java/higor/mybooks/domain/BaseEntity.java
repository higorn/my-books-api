package higor.mybooks.domain;

public interface BaseEntity<ID> {
  ID getId();
  void setId(ID id);
}
