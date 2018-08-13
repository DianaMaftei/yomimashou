package com.github.dianamaftei.yomimashou.dictionary.example;

import javax.persistence.*;

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

    public void setId(Long id) {
        this.id = id;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getTextBreakdown() {
        return textBreakdown;
    }

    public void setTextBreakdown(String textBreakdown) {
        this.textBreakdown = textBreakdown;
    }
}
