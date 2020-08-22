package com.github.dianamaftei.study.spacedrepetition;

import com.github.dianamaftei.study.BLValidationException;
import com.github.dianamaftei.study.studydeck.card.Card;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Super Memo 2 Algorithm http://www.supermemo.com/english/ol/sm2.htm
 *
 * repetitions - the number of times a user sees a flashcard. 0 means they haven't studied it yet, 1
 * means it is their first time, and so on
 *
 * quality - how easily the information was remembered. The scale is from 0 (complete blackout) to 5
 * (perfect response).
 *
 * easiness - this is also referred to as the easiness factor or EFactor or EF. It is multiplier
 * used to increase the "space" in spaced repetition. The range is from 1.3 (hardest) to 2.5. (easiest)
 *
 * interval - this is the length of time (in days) between repetitions. It is the "space" of spaced
 * repetition.
 *
 * nextPractice - This is the date/time of when the flashcard comes due to review again.
 */

public class SM2Original implements SRSAlgorithm {

  private static final float E_FACTOR_FLOOR = 1.3f;
  private static final int QUALITY_SUBTRACTOR = 5;

  public Card processAnswer(Card card, int quality) {
    if (quality < 0 || quality > 5) {
      throw new BLValidationException("quality range is from 0 to 5");
    }

    int repetitions = calculateRepetitions(card.getRepetitions(), quality);
//    float newEasinessFactor = calculateNewEasinessFactor(card.getEasinessFactor(), quality);
    float newEasinessFactor = calculateNewEasinessFactor(2.5f, quality);
    float nextInterval = calculateNextInterval(card.getInterval(), repetitions, newEasinessFactor);
    LocalDateTime nextPracticeTime = calculateNextPracticeTime(nextInterval);

//    card.setEasinessFactor(newEasinessFactor);
    card.setRepetitions(repetitions);
    card.setInterval(nextInterval);
    card.setNextPractice(nextPracticeTime);

    return card;
  }

  private static float calculateNewEasinessFactor(float previousEasinessFactor, int quality) {
    float qFactor = (QUALITY_SUBTRACTOR - quality);
    float newEasinessFactor = previousEasinessFactor + 0.1f - qFactor * (0.08f + qFactor * 0.02f);

    return Math.max(E_FACTOR_FLOOR, newEasinessFactor);
  }

  private static int calculateRepetitions(int repetitions, int quality) {
    if (quality < 3) {
      return 0;
    } else {
      return repetitions + 1;
    }
  }

  private static float calculateNextInterval(float interval, int repetitions, float easinessFactor) {
    if (repetitions <= 1) {
      interval = 1;
    } else if (repetitions == 2) {
      interval = 6;
    } else {
      interval = Math.round(interval * easinessFactor);
    }

    return interval;
  }

  private static LocalDateTime calculateNextPracticeTime(float interval) {
    int millisecondsInDay = 60 * 60 * 24 * 1000;
    long now = System.currentTimeMillis();
    long nextPracticeDate = now + millisecondsInDay * Math.round(interval);

    return LocalDateTime.ofInstant(Instant.ofEpochMilli(nextPracticeDate), ZoneId.systemDefault());
  }
}
