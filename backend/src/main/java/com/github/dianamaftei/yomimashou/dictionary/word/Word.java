package com.github.dianamaftei.yomimashou.dictionary.word;

import javax.persistence.*;
import java.util.List;

@Entity
public class Word {

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
}
