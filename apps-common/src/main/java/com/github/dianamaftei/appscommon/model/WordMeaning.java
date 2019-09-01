package com.github.dianamaftei.appscommon.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Type;

@Entity
public class WordMeaning {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String partOfSpeech;
  private String fieldOfApplication;

  @Type(type = "text")
  private String glosses;
  private String antonym;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getPartOfSpeech() {
    return partOfSpeech;
  }

  public void setPartOfSpeech(final String partOfSpeech) {
    this.partOfSpeech = partOfSpeech;
  }

  public String getFieldOfApplication() {
    return fieldOfApplication;
  }

  public void setFieldOfApplication(final String fieldOfApplication) {
    this.fieldOfApplication = fieldOfApplication;
  }

  public String getGlosses() {
    return glosses;
  }

  public void setGlosses(final String glosses) {
    this.glosses = glosses;
  }

  public String getAntonym() {
    return antonym;
  }

  public void setAntonym(final String antonym) {
    this.antonym = antonym;
  }
}
