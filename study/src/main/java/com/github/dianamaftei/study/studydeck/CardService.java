package com.github.dianamaftei.study.studydeck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

  @Autowired
  private CardRepository cardRepository;

  public Card save(Card card) {
    return cardRepository.save(card);
  }

  public void delete(Card card) {
    cardRepository.delete(card);
  }
}
