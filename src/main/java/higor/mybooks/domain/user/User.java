package higor.mybooks.domain.user;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class User {
  @Id
  @SequenceGenerator(name = "User_SEQ", sequenceName = "account_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_SEQ")
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
}
