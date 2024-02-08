package com.yomimashou.appscommon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KanjiReferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jlptOldLevel;
    private String jlptNewLevel;
    private String nelsonC;
    private String nelsonN;
    private String halpernNjecd;
    private String halpernKkd;
    private String halpernKkld;
    private String halpernKkld2ed;
    private String heisig;
    private String heisig6;
    private String gakken;
    private String oneillNames;
    private String oneillKk;
    private String moro;
    private String henshall;
    private String shKk;
    private String shKk2;
    private String sakade;
    private String jfCards;
    private String henshall3;
    private String tuttCards;
    private String crowley;
    private String kanjiInContext;
    private String busyPeople;
    private String kodanshaCompact;
    private String maniette;
    private String waniKani;
}
