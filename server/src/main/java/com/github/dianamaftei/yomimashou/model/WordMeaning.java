package com.github.dianamaftei.yomimashou.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class WordMeaning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String partOfSpeech;
    private String fieldOfApplication;
    private String glosses;
    private String antonym;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getFieldOfApplication() {
        return fieldOfApplication;
    }

    public void setFieldOfApplication(String fieldOfApplication) {
        this.fieldOfApplication = fieldOfApplication;
    }

    public String getGlosses() {
        return glosses;
    }

    public void setGlosses(String glosses) {
        this.glosses = glosses;
    }

    public String getAntonym() {
        return antonym;
    }

    public void setAntonym(String antonym) {
        this.antonym = antonym;
    }
}
