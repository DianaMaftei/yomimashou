package com.yomimashou.study.studydeck;

import com.yomimashou.study.BLValidationException;
import com.yomimashou.study.spacedrepetition.PracticeAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private PracticeAlgorithm practiceAlgorithm;

    @PostMapping()
    public Card calculateNextReview(@RequestParam String cardId, @RequestParam int easeOfAnswer) {
        if (StringUtils.isBlank(cardId)) {
            throw new BLValidationException("Invalid card");
        }
        Optional<Card> optionalCard = cardService.findById(cardId);
        if (!optionalCard.isPresent()) {
            throw new BLValidationException("Invalid card");
        }
        Card card = practiceAlgorithm.calculateNextReview(optionalCard.get(), easeOfAnswer);
        return cardService.save(card);
    }

    @GetMapping("/due")
    public List<Card> getDueCardsForDeck(@RequestParam String deckId) {
        if (StringUtils.isBlank(deckId)) {
            throw new BLValidationException("Invalid deck");
        }
        Deck deck = deckService.findById(deckId).orElseThrow(() -> new BLValidationException("Invalid deck"));
        return cardService.findAllDueById(deck.getCards());
    }
}