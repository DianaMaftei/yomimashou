package com.github.dianamaftei.yomimashou.sentence;

public class SentenceToken {

  private String surface;
  private String baseForm;
  private String reading;
  private String partOfSpeechLevel1;
  private String partOfSpeechLevel2;
  private String partOfSpeechLevel3;
  private String partOfSpeechLevel4;
  private String semanticInformation;

  public String getSurface() {
    return surface;
  }

  public void setSurface(String surface) {
    this.surface = surface;
  }

  public String getBaseForm() {
    return baseForm;
  }

  public void setBaseForm(String baseForm) {
    this.baseForm = baseForm;
  }

  public String getReading() {
    return reading;
  }

  public void setReading(String reading) {
    this.reading = reading;
  }

  public String getPartOfSpeechLevel1() {
    return partOfSpeechLevel1;
  }

  public void setPartOfSpeechLevel1(String partOfSpeechLevel1) {
    this.partOfSpeechLevel1 = partOfSpeechLevel1;
  }

  public String getPartOfSpeechLevel2() {
    return partOfSpeechLevel2;
  }

  public void setPartOfSpeechLevel2(String partOfSpeechLevel2) {
    this.partOfSpeechLevel2 = partOfSpeechLevel2;
  }

  public String getPartOfSpeechLevel3() {
    return partOfSpeechLevel3;
  }

  public void setPartOfSpeechLevel3(String partOfSpeechLevel3) {
    this.partOfSpeechLevel3 = partOfSpeechLevel3;
  }

  public String getPartOfSpeechLevel4() {
    return partOfSpeechLevel4;
  }

  public void setPartOfSpeechLevel4(String partOfSpeechLevel4) {
    this.partOfSpeechLevel4 = partOfSpeechLevel4;
  }

  public String getSemanticInformation() {
    return semanticInformation;
  }

  public void setSemanticInformation(String semanticInformation) {
    this.semanticInformation = semanticInformation;
  }
}