package com.yomimashou.study.card.spacedrepetition;

import com.yomimashou.study.BLValidationException;
import com.yomimashou.study.card.Card;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Super Memo 2 Algorithm http://www.supermemo.com/english/ol/sm2.htm
 *
 * Tweaked: - only 3 options for answers - don't know, know and know well - user must get 3 right answers in a row (or 1
 * strong right answer) before card becomes active and starts the regular flow of the algorithm
 */
@Component
public class SM2Tweaked extends PracticeAlgorithm {

  protected static final int REPS_TO_ACTIVATE_CARD = 3;

  protected static final int ANSWER_DONT_KNOW = 1;
  protected static final int ANSWER_KNOW = 2;
  protected static final int ANSWER_KNOW_WELL = 3;

  protected static final float MIN_INTERVAL = 0.33f; // 8h, studying cards too soon creates a feeling of false learning
  protected static final float MAX_INTERVAL = 6f; // 6 days, studying cards after a too long period leads to forgetfulness
  protected static final int ONE_DAY = 1;
  protected static final int THREE_DAYS = 3;

  public Card calculateNextReview(Card card, int easeOfAnswer) {
    if (easeOfAnswer < 1 || easeOfAnswer > 3) {
      throw new BLValidationException("easeOfAnswer range is from 1 to 3");
    }

    int repetitions = calculateRepetitions(card.getRepetitions(), easeOfAnswer);
    float nextInterval = calculateNextInterval(card.getInterval(), easeOfAnswer);
    LocalDateTime nextPracticeTime = calculateNextPracticeTime(LocalDateTime.now(), nextInterval);

    if (!card.isActive()) {
      if (repetitions == REPS_TO_ACTIVATE_CARD || easeOfAnswer == ANSWER_KNOW_WELL) {
        card.setActive(true);
        repetitions = 0;

        nextInterval = easeOfAnswer == ANSWER_KNOW ? ONE_DAY : THREE_DAYS;

      } else {
        nextInterval = MIN_INTERVAL;
        nextPracticeTime = LocalDateTime.now();
      }
    }

    if (easeOfAnswer == ANSWER_DONT_KNOW) {
      repetitions = 0;

      if (nextInterval < MIN_INTERVAL) {
        nextInterval = MIN_INTERVAL;
      } else if (nextInterval > MAX_INTERVAL) {
        nextInterval = MAX_INTERVAL;
      }

      nextPracticeTime = LocalDateTime.now();
    }

    card.setRepetitions(repetitions);
    card.setInterval(nextInterval);
    card.setNextPractice(nextPracticeTime);

    return card;
  }

  private static int calculateRepetitions(int repetitions, int easeOfAnswer) {
    if (easeOfAnswer == ANSWER_DONT_KNOW) {
      return 0;
    } else {
      return repetitions + 1;
    }
  }

  private static float calculateNextInterval(float interval, int easeOfAnswer) {
    switch (easeOfAnswer) {
      case ANSWER_DONT_KNOW:
        return interval * 0.5f;
      case ANSWER_KNOW:
        return interval * 1.4f;
      case ANSWER_KNOW_WELL:
        return interval * 2.2f;
      default:
        return interval;
    }
  }

  private static LocalDateTime calculateNextPracticeTime(LocalDateTime lastPracticeTime, float interval) {
    long lastPractice = lastPracticeTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    long nextPracticeTime = lastPractice + getIntervalInMilis(interval);

    return LocalDateTime.ofInstant(Instant.ofEpochMilli(nextPracticeTime), ZoneId.systemDefault());
  }

  private static long getIntervalInMilis(final float interval) {
    double millisecondsInDay = 60 * 60 * 24 * 1000d;

    return Math.round(millisecondsInDay * interval);
  }
}
