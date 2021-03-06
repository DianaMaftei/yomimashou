package com.github.dianamaftei.appscommon.model;

import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Word {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ElementCollection
  private Set<String> kanjiElements;

  @ElementCollection
  private Set<String> readingElements;

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn(name = "word_id")
  private List<WordMeaning> meanings;

  private int priority;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Set<String> getKanjiElements() {
    return kanjiElements;
  }

  public void setKanjiElements(final Set<String> kanjiElements) {
    this.kanjiElements = kanjiElements;
  }

  public Set<String> getReadingElements() {
    return readingElements;
  }

  public void setReadingElements(final Set<String> readingElements) {
    this.readingElements = readingElements;
  }

  public List<WordMeaning> getMeanings() {
    return meanings;
  }

  public void setMeanings(final List<WordMeaning> meanings) {
    this.meanings = meanings;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(final int priority) {
    this.priority = priority;
  }

}
