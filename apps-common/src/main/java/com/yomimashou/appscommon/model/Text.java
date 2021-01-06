package com.yomimashou.appscommon.model;

import com.yomimashou.appscommon.audit.Auditable;

import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Text extends Auditable<String> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String title;

  @NotNull
  @Column(columnDefinition = "TEXT")
  private String content;

  @Column(columnDefinition = "TEXT")
  private String excerpt;

  private String imageFileName;

  @ElementCollection
  private List<String> tags;

  @ElementCollection
  private Map<String, Integer> kanjiCountByLevel;

  private int characterCount;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public String getExcerpt() {
    return excerpt;
  }

  public void setExcerpt(String excerpt) {
    this.excerpt = excerpt;
  }

  public Map<String, Integer> getKanjiCountByLevel() {
    return kanjiCountByLevel;
  }

  public void setKanjiCountByLevel(Map<String, Integer> kanjiCountByLevel) {
    this.kanjiCountByLevel = kanjiCountByLevel;
  }

  public String getImageFileName() {
    return imageFileName;
  }

  public void setImageFileName(String imageFileName) {
    this.imageFileName = imageFileName;
  }

  public int getCharacterCount() {
    return characterCount;
  }

  public void setCharacterCount(int characterCount) {
    this.characterCount = characterCount;
  }
}
