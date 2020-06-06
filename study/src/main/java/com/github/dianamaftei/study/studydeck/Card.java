package com.github.dianamaftei.study.studydeck;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Card {

  @Id
  private String id;

  private String deckId;

  private String front;
  private String back;

  private boolean active;
  private int repetitions;
  private float easinessFactor;
  private float interval;
  private LocalDateTime nextPractice;

  public Card() {
    this.easinessFactor = 2.5f;
    this.nextPractice = LocalDateTime.now();
    this.interval = 1;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getDeckId() {
    return deckId;
  }

  public void setDeckId(final String deckId) {
    this.deckId = deckId;
  }

  public String getFront() {
    return front;
  }

  public void setFront(final String front) {
    this.front = front;
  }

  public String getBack() {
    return back;
  }

  public void setBack(final String back) {
    this.back = back;
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

  public float getEasinessFactor() {
    return easinessFactor;
  }

  public void setEasinessFactor(final float easinessFactor) {
    this.easinessFactor = easinessFactor;
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

  @Override
  public String toString() {
    return "Card{" +
        "active=" + active +
        ", repetitions=" + repetitions +
        ", easinessFactor=" + easinessFactor +
        ", interval=" + interval +
        ", nextPractice=" + nextPractice +
        '}';
  }
}
