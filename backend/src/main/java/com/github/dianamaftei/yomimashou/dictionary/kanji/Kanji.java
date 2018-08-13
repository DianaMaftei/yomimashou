package com.github.dianamaftei.yomimashou.dictionary.kanji;

import javax.persistence.*;

@Entity
public class Kanji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kanji;
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

    @OneToOne(cascade = {CascadeType.ALL})
    private KanjiReferences references;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
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
}
