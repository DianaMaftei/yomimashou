package com.yomimashou.study.card;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/study/card")
@AllArgsConstructor
public class CardController {

    private final CardService cardService;

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
