package com.github.dianamaftei.study.studydeck.card;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/calculate")
    public Card calculate(@RequestBody Card card, @RequestParam("easeOfAnswer") int easeOfAnswer) {
      return cardService.calculate(card, easeOfAnswer);
    }
}
