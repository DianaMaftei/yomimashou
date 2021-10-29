package com.yomimashou.study.card;

public class CardTestHelper {
    public static Card buildCard(String kanji, String kana, String explanation, CardItemOrigin cardItemOrigin) {
        Card card = new Card();
        card.setKanji(kanji);
        card.setKana(kana);
        card.setExplanation(explanation);
        card.setCardItemOrigin(cardItemOrigin);
        return card;
    }
}
