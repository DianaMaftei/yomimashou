package com.yomimashou.study.card;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yomimashou.study.deck.Deck;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "card")
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "deck_id")
  private Deck deck;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Card() {
    this.nextPractice = LocalDateTime.now();
    this.interval = 1;
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

  public Deck getDeck() {
    return deck;
  }

  public void setDeck(Deck deck) {
    this.deck = deck;
  }
}
