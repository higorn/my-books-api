package higor.mybooks.domain.user;

import higor.mybooks.domain.BaseEntity;

public class User implements BaseEntity<Integer> {
  private Integer id;
  private String name;
  private String surname;
  private String email;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public User id(Integer id) {
    this.id = id;
    return this;
  }

  public User name(String name) {
    this.name = name;
    return this;
  }

  public User surname(String surname) {
    this.surname = surname;
    return this;
  }

  public User email(String email) {
    this.email = email;
    return this;
  }
}
