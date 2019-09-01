package com.github.dianamaftei.appscommon.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Name {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String kanji;
  private String reading;
  private String type;
  private String translations;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getKanji() {
    return kanji;
  }

  public void setKanji(final String kanji) {
    this.kanji = kanji;
  }

  public String getReading() {
    return reading;
  }

  public void setReading(final String reading) {
    this.reading = reading;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getTranslations() {
    return translations;
  }

  public void setTranslations(final String translations) {
    this.translations = translations;
  }
}
