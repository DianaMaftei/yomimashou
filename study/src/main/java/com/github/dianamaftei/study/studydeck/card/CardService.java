package com.github.dianamaftei.study.studydeck.card;

import com.github.dianamaftei.study.spacedrepetition.SRSAlgorithm;
import org.springframework.stereotype.Service;

@Service
public class CardService {

  private final CardRepository cardRepository;
  private final SRSAlgorithm spacedRepetitionAlgorithm;

  public CardService(CardRepository cardRepository, SRSAlgorithm spacedRepetitionAlgorithm) {
    this.cardRepository = cardRepository;
    this.spacedRepetitionAlgorithm = spacedRepetitionAlgorithm;
  }

  public Card save(Card card) {
    return cardRepository.save(card);
  }

  public void delete(Card card) {
    cardRepository.delete(card);
  }

  public Card calculate(Card card, int easeOfAnswer) {
    return spacedRepetitionAlgorithm.processAnswer(card, easeOfAnswer);
  }
}
