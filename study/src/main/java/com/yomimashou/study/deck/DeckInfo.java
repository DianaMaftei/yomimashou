package com.yomimashou.study.deck;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeckInfo {

    private int totalCards;
    private int activeCards;
    private int cardsDue;

}
