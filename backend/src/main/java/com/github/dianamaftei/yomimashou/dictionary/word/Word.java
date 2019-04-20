package com.github.dianamaftei.yomimashou.dictionary.word;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Word implements Comparable<Word>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kanjiElements;

    private String readingElements;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "word_id")
    private List<WordMeaning> meanings;

    private int priority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKanjiElements() {
        return kanjiElements;
    }

    public void setKanjiElements(String kanjiElements) {
        this.kanjiElements = kanjiElements;
    }

    public String getReadingElements() {
        return readingElements;
    }

    public void setReadingElements(String readingElements) {
        this.readingElements = readingElements;
    }

    public List<WordMeaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<WordMeaning> meanings) {
        this.meanings = meanings;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Word o) {
        return this.priority - o.priority;
    }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Word word = (Word) o;

    return new EqualsBuilder()
        .append(priority, word.priority)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(priority)
        .toHashCode();
  }
}
