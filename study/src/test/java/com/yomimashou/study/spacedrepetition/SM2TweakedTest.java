package com.yomimashou.study.spacedrepetition;

import static com.yomimashou.study.spacedrepetition.SM2Tweaked.*;
import static org.junit.jupiter.api.Assertions.*;

import com.yomimashou.study.BLValidationException;
import com.yomimashou.study.studydeck.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SM2TweakedTest {

  private Card card;

  private SM2Tweaked sm2Tweaked = new SM2Tweaked();

  @BeforeEach
  void setUp() {
    card = buildCard();
  }

  @Test
  void calculateThrowsExceptionIfAnswerEasinessIsLessThanRange() {
    assertThrows(BLValidationException.class, () -> {
      sm2Tweaked.calculateNextReview(card, ANSWER_DONT_KNOW - 1);
    });
  }

  @Test
  void calculateThrowsExceptionIfAnswerEasinessIsHigherThanRange() {
    assertThrows(BLValidationException.class, () -> {
      sm2Tweaked.calculateNextReview(card, ANSWER_KNOW_WELL + 1);
    });
  }

  @Test
  void calculateMakesACardActiveAfterThreeConsecutiveWeakCorrectAnswers() {
    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW);
    assertFalse(card.isActive());

    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW);
    assertFalse(card.isActive());

    sm2Tweaked.calculateNextReview(card, ANSWER_DONT_KNOW); // correct streak is broken
    assertFalse(card.isActive());

    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW); // correct streak starts again
    assertFalse(card.isActive());

    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW);
    assertFalse(card.isActive());

    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW);
    assertTrue(card.isActive());
  }

  @Test
  void calculateMakesACardActiveAfterOneStrongCorrectAnswer() {
    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW_WELL);
    assertTrue(card.isActive());
  }

  @Test
  void calculateResetsTheRepetitionsCountAfterCardIsMadeActive() {
    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW);
    assertFalse(card.isActive());
    assertEquals(1, card.getRepetitions());

    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW);
    assertFalse(card.isActive());
    assertEquals(2, card.getRepetitions());

    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW);
    assertTrue(card.isActive());
    assertEquals(0, card.getRepetitions());
  }

  @Test
  void calculateAdjustsTheIntervalToNeverFallBelowMinimumWhenAnsweringIncorrectly() {
    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW);
    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW);
    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW);
    assertTrue(card.getInterval() == ONE_DAY);

    sm2Tweaked.calculateNextReview(card, ANSWER_DONT_KNOW);
    sm2Tweaked.calculateNextReview(card, ANSWER_DONT_KNOW);

    assertTrue(card.getInterval() == MIN_INTERVAL);

    sm2Tweaked.calculateNextReview(card, ANSWER_DONT_KNOW);
    sm2Tweaked.calculateNextReview(card, ANSWER_DONT_KNOW);

    assertTrue(card.getInterval() == MIN_INTERVAL);

  }

  @Test
  void calculateAdjustsTheIntervalToNeverBeAboveMaximumWhenAnsweringIncorrectly() {
    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW_WELL);
    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW_WELL);
    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW_WELL);
    sm2Tweaked.calculateNextReview(card, ANSWER_KNOW_WELL);
    assertTrue(card.getInterval() > 31);

    sm2Tweaked.calculateNextReview(card, ANSWER_DONT_KNOW);
    assertTrue(card.getInterval() == MAX_INTERVAL);
  }

  private Card buildCard() {
    Card card = new Card();
    card.setKanji("猫");
    card.setKana("ねこ ビョウ");
    card.setExplanation("cat");

    return card;
  }
}