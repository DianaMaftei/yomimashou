package com.yomimashou.appscommon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
