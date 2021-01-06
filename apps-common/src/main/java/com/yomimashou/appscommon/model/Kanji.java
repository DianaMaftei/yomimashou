package com.yomimashou.appscommon.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Kanji {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String character;
  private String radical;
  private Integer grade;
  private Integer strokeCount;
  private Integer frequency;
  private String skipCode;
  private String onReading;
  private String kunReading;
  private String meaning;
  private String codepoint;
  private String variant;

  //RTK info
  private String keyword;
  @Column(columnDefinition = "TEXT")
  private String components;
  @Column(columnDefinition = "TEXT")
  private String story1;
  @Column(columnDefinition = "TEXT")
  private String story2;

  @OneToOne(cascade = {CascadeType.ALL})
  private KanjiReferences references;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getCharacter() {
    return character;
  }

  public void setCharacter(final String character) {
    this.character = character;
  }

  public String getRadical() {
    return radical;
  }

  public void setRadical(final String radical) {
    this.radical = radical;
  }

  public Integer getGrade() {
    return grade;
  }

  public void setGrade(final Integer grade) {
    this.grade = grade;
  }

  public Integer getStrokeCount() {
    return strokeCount;
  }

  public void setStrokeCount(final Integer strokeCount) {
    this.strokeCount = strokeCount;
  }

  public Integer getFrequency() {
    return frequency;
  }

  public void setFrequency(final Integer frequency) {
    this.frequency = frequency;
  }

  public String getSkipCode() {
    return skipCode;
  }

  public void setSkipCode(final String skipCode) {
    this.skipCode = skipCode;
  }

  public String getOnReading() {
    return onReading;
  }

  public void setOnReading(final String onReading) {
    this.onReading = onReading;
  }

  public String getKunReading() {
    return kunReading;
  }

  public void setKunReading(final String kunReading) {
    this.kunReading = kunReading;
  }

  public String getMeaning() {
    return meaning;
  }

  public void setMeaning(final String meaning) {
    this.meaning = meaning;
  }

  public String getCodepoint() {
    return codepoint;
  }

  public void setCodepoint(final String codepoint) {
    this.codepoint = codepoint;
  }

  public KanjiReferences getReferences() {
    return references;
  }

  public void setReferences(final KanjiReferences references) {
    this.references = references;
  }

  public String getVariant() {
    return variant;
  }

  public void setVariant(final String variant) {
    this.variant = variant;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(final String keyword) {
    this.keyword = keyword;
  }

  public String getComponents() {
    return components;
  }

  public void setComponents(final String components) {
    this.components = components;
  }

  public String getStory1() {
    return story1;
  }

  public void setStory1(final String story1) {
    this.story1 = story1;
  }

  public String getStory2() {
    return story2;
  }

  public void setStory2(final String story2) {
    this.story2 = story2;
  }
}
