package com.yomimashou.study.studydeck;

public class DeckInfo {

  private String id;
  private String name;
  private int totalCards;
  private int activeCards;
  private int cardsDue;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getTotalCards() {
    return totalCards;
  }

  public void setTotalCards(int totalCards) {
    this.totalCards = totalCards;
  }

  public int getActiveCards() {
    return activeCards;
  }

  public void setActiveCards(int activeCards) {
    this.activeCards = activeCards;
  }

  public int getCardsDue() {
    return cardsDue;
  }

  public void setCardsDue(int cardsDue) {
    this.cardsDue = cardsDue;
  }
}
