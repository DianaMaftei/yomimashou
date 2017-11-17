package com.github.dianamaftei.yomimashou.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJlptOldLevel() {
        return jlptOldLevel;
    }

    public void setJlptOldLevel(String jlptOldLevel) {
        this.jlptOldLevel = jlptOldLevel;
    }

    public String getJlptNewLevel() {
        return jlptNewLevel;
    }

    public void setJlptNewLevel(String jlptNewLevel) {
        this.jlptNewLevel = jlptNewLevel;
    }

    public String getNelsonC() {
        return nelsonC;
    }

    public void setNelsonC(String nelsonC) {
        this.nelsonC = nelsonC;
    }

    public String getNelsonN() {
        return nelsonN;
    }

    public void setNelsonN(String nelsonN) {
        this.nelsonN = nelsonN;
    }

    public String getHalpernNjecd() {
        return halpernNjecd;
    }

    public void setHalpernNjecd(String halpernNjecd) {
        this.halpernNjecd = halpernNjecd;
    }

    public String getHalpernKkd() {
        return halpernKkd;
    }

    public void setHalpernKkd(String halpernKkd) {
        this.halpernKkd = halpernKkd;
    }

    public String getHalpernKkld() {
        return halpernKkld;
    }

    public void setHalpernKkld(String halpernKkld) {
        this.halpernKkld = halpernKkld;
    }

    public String getHalpernKkld2ed() {
        return halpernKkld2ed;
    }

    public void setHalpernKkld2ed(String halpernKkld2ed) {
        this.halpernKkld2ed = halpernKkld2ed;
    }

    public String getHeisig() {
        return heisig;
    }

    public void setHeisig(String heisig) {
        this.heisig = heisig;
    }

    public String getOneillNames() {
        return oneillNames;
    }

    public void setOneillNames(String oneillNames) {
        this.oneillNames = oneillNames;
    }

    public String getOneillKk() {
        return oneillKk;
    }

    public void setOneillKk(String oneillKk) {
        this.oneillKk = oneillKk;
    }

    public String getMoro() {
        return moro;
    }

    public void setMoro(String moro) {
        this.moro = moro;
    }

    public String getHenshall() {
        return henshall;
    }

    public void setHenshall(String henshall) {
        this.henshall = henshall;
    }

    public String getShKk() {
        return shKk;
    }

    public void setShKk(String shKk) {
        this.shKk = shKk;
    }

    public String getShKk2() {
        return shKk2;
    }

    public void setShKk2(String shKk2) {
        this.shKk2 = shKk2;
    }

    public String getSakade() {
        return sakade;
    }

    public void setSakade(String sakade) {
        this.sakade = sakade;
    }

    public String getJfCards() {
        return jfCards;
    }

    public void setJfCards(String jfCards) {
        this.jfCards = jfCards;
    }

    public String getHenshall3() {
        return henshall3;
    }

    public void setHenshall3(String henshall3) {
        this.henshall3 = henshall3;
    }

    public String getTuttCards() {
        return tuttCards;
    }

    public void setTuttCards(String tuttCards) {
        this.tuttCards = tuttCards;
    }

    public String getCrowley() {
        return crowley;
    }

    public void setCrowley(String crowley) {
        this.crowley = crowley;
    }

    public String getKanjiInContext() {
        return kanjiInContext;
    }

    public void setKanjiInContext(String kanjiInContext) {
        this.kanjiInContext = kanjiInContext;
    }

    public String getBusyPeople() {
        return busyPeople;
    }

    public void setBusyPeople(String busyPeople) {
        this.busyPeople = busyPeople;
    }

    public String getKodanshaCompact() {
        return kodanshaCompact;
    }

    public void setKodanshaCompact(String kodanshaCompact) {
        this.kodanshaCompact = kodanshaCompact;
    }

    public String getManiette() {
        return maniette;
    }

    public void setManiette(String maniette) {
        this.maniette = maniette;
    }

    public String getWaniKani() {
        return waniKani;
    }

    public void setWaniKani(String waniKani) {
        this.waniKani = waniKani;
    }
}
