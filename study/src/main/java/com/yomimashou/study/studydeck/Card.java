package com.yomimashou.study.studydeck;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Card {

  @Id
  private String id;

  private String kanji;
  private String kana;
  private String explanation;
  private CardItemOrigin cardItemOrigin;

  private boolean active;
  private int repetitions;
  private float interval;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime nextPractice;

  public Card() {
    this.nextPractice = LocalDateTime.now();
    this.interval = 1;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getKanji() {
    return kanji;
  }

  public void setKanji(final String kanji) {
    this.kanji = kanji;
  }

  public String getKana() {
    return kana;
  }

  public void setKana(String kana) {
    this.kana = kana;
  }

  public String getExplanation() {
    return explanation;
  }

  public void setExplanation(final String explanation) {
    this.explanation = explanation;
  }

  public CardItemOrigin getCardItemOrigin() {
    return cardItemOrigin;
  }

  public void setCardItemOrigin(CardItemOrigin cardItemOrigin) {
    this.cardItemOrigin = cardItemOrigin;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(final boolean active) {
    this.active = active;
  }

  public int getRepetitions() {
    return repetitions;
  }

  public void setRepetitions(final int repetitions) {
    this.repetitions = repetitions;
  }

  public float getInterval() {
    return interval;
  }

  public void setInterval(final float interval) {
    this.interval = interval;
  }

  public LocalDateTime getNextPractice() {
    return nextPractice;
  }

  public void setNextPractice(final LocalDateTime nextPractice) {
    this.nextPractice = nextPractice;
  }

}
