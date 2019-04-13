package com.github.dianamaftei.yomimashou.text.dictionary;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import org.junit.Before;
import org.junit.Test;

public class WordDictionaryTest {

  private WordDictionary wordDictionary;

  @Before
  public void setUp() {
    WordDictionary.setReader(new StringReader(
        "じゆうみんしゅとう|いたバイク|ぬかずきむし|ちきゅうがいせいめい|せんたいしょう|じゅんまいしゅ|"
            + "ガリアじん|おにおこぜ|スタンフォード|主眼をおく|"));

    wordDictionary = WordDictionary.getInstance("");
  }

  @Test
  public void isWordInDictionaryShouldReturnTrueIfWordIsInFile() {
    assertTrue(wordDictionary.isWordInDictionary("じゅんまいしゅ"));
  }

  @Test
  public void isWordInDictionaryShouldReturnFalseIfWordIsNotInFile() {
    assertFalse(wordDictionary.isWordInDictionary("竜飛人"));
  }
}