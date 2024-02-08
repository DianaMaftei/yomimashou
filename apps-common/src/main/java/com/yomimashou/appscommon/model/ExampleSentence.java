package com.yomimashou.appscommon.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ExampleSentence {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT")
  private String sentence;
  @Column(columnDefinition = "TEXT")
  private String meaning;
  @Column(columnDefinition = "TEXT")
  private String textBreakdown;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getSentence() {
    return sentence;
  }

  public void setSentence(final String sentence) {
    this.sentence = sentence;
  }

  public String getMeaning() {
    return meaning;
  }

  public void setMeaning(final String meaning) {
    this.meaning = meaning;
  }

  public String getTextBreakdown() {
    return textBreakdown;
  }

  public void setTextBreakdown(final String textBreakdown) {
    this.textBreakdown = textBreakdown;
  }
}
