package com.github.dianamaftei.yomimashou.dictionary.kanji;

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

    public void setId(Long id) {
        this.id = id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getRadical() {
        return radical;
    }

    public void setRadical(String radical) {
        this.radical = radical;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getStrokeCount() {
        return strokeCount;
    }

    public void setStrokeCount(Integer strokeCount) {
        this.strokeCount = strokeCount;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getSkipCode() {
        return skipCode;
    }

    public void setSkipCode(String skipCode) {
        this.skipCode = skipCode;
    }

    public String getOnReading() {
        return onReading;
    }

    public void setOnReading(String onReading) {
        this.onReading = onReading;
    }

    public String getKunReading() {
        return kunReading;
    }

    public void setKunReading(String kunReading) {
        this.kunReading = kunReading;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getCodepoint() {
        return codepoint;
    }

    public void setCodepoint(String codepoint) {
        this.codepoint = codepoint;
    }

    public KanjiReferences getReferences() {
        return references;
    }

    public void setReferences(KanjiReferences references) {
        this.references = references;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getComponents() {
    return components;
  }

  public void setComponents(String components) {
    this.components = components;
  }

  public String getStory1() {
    return story1;
  }

  public void setStory1(String story1) {
    this.story1 = story1;
  }

  public String getStory2() {
    return story2;
  }

  public void setStory2(String story2) {
    this.story2 = story2;
  }
}
