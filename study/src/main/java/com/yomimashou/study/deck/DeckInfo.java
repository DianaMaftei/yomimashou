package com.yomimashou.study.deck;

public class DeckInfo {

    private int totalCards;
    private int activeCards;
    private int cardsDue;

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
