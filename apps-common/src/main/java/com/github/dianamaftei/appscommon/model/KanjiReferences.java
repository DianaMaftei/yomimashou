package com.github.dianamaftei.appscommon.model;

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

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getJlptOldLevel() {
    return jlptOldLevel;
  }

  public void setJlptOldLevel(final String jlptOldLevel) {
    this.jlptOldLevel = jlptOldLevel;
  }

  public String getJlptNewLevel() {
    return jlptNewLevel;
  }

  public void setJlptNewLevel(final String jlptNewLevel) {
    this.jlptNewLevel = jlptNewLevel;
  }

  public String getNelsonC() {
    return nelsonC;
  }

  public void setNelsonC(final String nelsonC) {
    this.nelsonC = nelsonC;
  }

  public String getNelsonN() {
    return nelsonN;
  }

  public void setNelsonN(final String nelsonN) {
    this.nelsonN = nelsonN;
  }

  public String getHalpernNjecd() {
    return halpernNjecd;
  }

  public void setHalpernNjecd(final String halpernNjecd) {
    this.halpernNjecd = halpernNjecd;
  }

  public String getHalpernKkd() {
    return halpernKkd;
  }

  public void setHalpernKkd(final String halpernKkd) {
    this.halpernKkd = halpernKkd;
  }

  public String getHalpernKkld() {
    return halpernKkld;
  }

  public void setHalpernKkld(final String halpernKkld) {
    this.halpernKkld = halpernKkld;
  }

  public String getHalpernKkld2ed() {
    return halpernKkld2ed;
  }

  public void setHalpernKkld2ed(final String halpernKkld2ed) {
    this.halpernKkld2ed = halpernKkld2ed;
  }

  public String getHeisig() {
    return heisig;
  }

  public void setHeisig(final String heisig) {
    this.heisig = heisig;
  }

  public String getHeisig6() {
    return heisig6;
  }

  public void setHeisig6(final String heisig6) {
    this.heisig6 = heisig6;
  }

  public String getGakken() {
    return gakken;
  }

  public void setGakken(final String gakken) {
    this.gakken = gakken;
  }

  public String getOneillNames() {
    return oneillNames;
  }

  public void setOneillNames(final String oneillNames) {
    this.oneillNames = oneillNames;
  }

  public String getOneillKk() {
    return oneillKk;
  }

  public void setOneillKk(final String oneillKk) {
    this.oneillKk = oneillKk;
  }

  public String getMoro() {
    return moro;
  }

  public void setMoro(final String moro) {
    this.moro = moro;
  }

  public String getHenshall() {
    return henshall;
  }

  public void setHenshall(final String henshall) {
    this.henshall = henshall;
  }

  public String getShKk() {
    return shKk;
  }

  public void setShKk(final String shKk) {
    this.shKk = shKk;
  }

  public String getShKk2() {
    return shKk2;
  }

  public void setShKk2(final String shKk2) {
    this.shKk2 = shKk2;
  }

  public String getSakade() {
    return sakade;
  }

  public void setSakade(final String sakade) {
    this.sakade = sakade;
  }

  public String getJfCards() {
    return jfCards;
  }

  public void setJfCards(final String jfCards) {
    this.jfCards = jfCards;
  }

  public String getHenshall3() {
    return henshall3;
  }

  public void setHenshall3(final String henshall3) {
    this.henshall3 = henshall3;
  }

  public String getTuttCards() {
    return tuttCards;
  }

  public void setTuttCards(final String tuttCards) {
    this.tuttCards = tuttCards;
  }

  public String getCrowley() {
    return crowley;
  }

  public void setCrowley(final String crowley) {
    this.crowley = crowley;
  }

  public String getKanjiInContext() {
    return kanjiInContext;
  }

  public void setKanjiInContext(final String kanjiInContext) {
    this.kanjiInContext = kanjiInContext;
  }

  public String getBusyPeople() {
    return busyPeople;
  }

  public void setBusyPeople(final String busyPeople) {
    this.busyPeople = busyPeople;
  }

  public String getKodanshaCompact() {
    return kodanshaCompact;
  }

  public void setKodanshaCompact(final String kodanshaCompact) {
    this.kodanshaCompact = kodanshaCompact;
  }

  public String getManiette() {
    return maniette;
  }

  public void setManiette(final String maniette) {
    this.maniette = maniette;
  }

  public String getWaniKani() {
    return waniKani;
  }

  public void setWaniKani(final String waniKani) {
    this.waniKani = waniKani;
  }
}
