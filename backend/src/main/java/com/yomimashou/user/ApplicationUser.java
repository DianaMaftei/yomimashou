package com.yomimashou.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yomimashou.text.ProgressStatus;

import java.util.Map;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String username;
    @Email
    private String email;
    @NotNull
    private String password;
  @ElementCollection
  private Map<Long, ProgressStatus> textsStatuses;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setPassword(String password) {
        this.password = password;
    }

  public Map<Long, ProgressStatus> getTextsStatuses() {
    return textsStatuses;
  }

  public void setTextsStatuses(
      Map<Long, ProgressStatus> textsStatuses) {
    this.textsStatuses = textsStatuses;
  }
}
