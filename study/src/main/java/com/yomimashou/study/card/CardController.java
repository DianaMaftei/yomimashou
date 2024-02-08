package com.yomimashou.study.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/study/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping()
    public List<Card> getAllInDeck(@RequestParam Long deckId) {
        return cardService.findAllInDeck(deckId);
    }

    @GetMapping("/due")
    public List<Card> getDueInDeck(@RequestParam Long deckId) {
        return cardService.findAllDueByDeckId(deckId);
    }

    @PutMapping("/review")
    public Card calculateNextReview(@RequestParam Long id, @RequestParam int easeOfAnswer) {
        return cardService.calculateNextReview(id, easeOfAnswer);
    }

    @PostMapping("/add")
    public Card addToNewDeck(@RequestParam String deckName, @RequestBody Card card) {
        return cardService.addToNewDeck(deckName, card);
    }

    @PostMapping("/add/{deckId}")
    public Card addToExistingDeck(@PathVariable Long deckId, @RequestBody Card card) {
        return cardService.addToExistingDeck(deckId, card);
    }

    @DeleteMapping("/{id}")
    public void removeFromDeck(@PathVariable Long id) {
        cardService.removeFromDeck(id);
    }
}
