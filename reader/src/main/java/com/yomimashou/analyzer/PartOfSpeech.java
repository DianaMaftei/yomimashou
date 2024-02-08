package com.yomimashou.analyzer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PartOfSpeech {

    VERB("動詞"),
    NOUN("名詞"),
    PRONOUN("代名詞"),
    NOUN_MODIFIER("連体詞"),
    ADJECTIVE("形容詞"),
    ADJECTIVE_VERB("形容動詞"),
    ADVERB("副詞"),
    PREPOSITION("前置詞"),
    CONJUNCTION("接続詞"),
    INTERJECTION("感動詞"),
    PREFIX("接頭"),
    SUFFIX("接尾"),
    PARTICLE("助詞"),
    CONJUNCTION_PARTICLE("接続助詞"),
    BINDING_PARTICLE("係助詞"),
    AUXILIARY_VERB("助動詞"),
    NUMERAL("数詞"),
    ARTICLE("冠詞"),
    UNKNOWN("未知語"),
    SYMBOL("記号"),
    OTHER("その他"),
    CHAINED("連結未知語");

    private String value;
}
