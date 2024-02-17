package com.yomimashou.study.deck;


import com.yomimashou.study.BLValidationException;
import com.yomimashou.study.card.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class DeckService {

    private DeckRepository deckRepository;
    private CardRepository cardRepository;

    public List<Deck> getAll() {
        return deckRepository.findAll();
    }

    public Deck save(final Deck deck) {
        return deckRepository.save(deck);
    }

    private void validateSave(final Deck deck) {
        // TODO check mandatory fields
        // TODO check that deck name is unique for user
    }

    public Deck findById(final Long id) {
        return deckRepository.findById(id).orElseThrow(() -> new BLValidationException("invalid deck id"));
    }

    public void delete(final Long id) {
        cardRepository.deleteAllByDeckId(id);
        deckRepository.deleteById(id);
    }
}
