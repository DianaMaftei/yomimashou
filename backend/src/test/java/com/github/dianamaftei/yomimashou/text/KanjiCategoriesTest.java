package com.github.dianamaftei.yomimashou.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KanjiCategoriesTest {

  private KanjiCategories kanjiCategories;

  @BeforeEach
  void setUp() {
    kanjiCategories = new KanjiCategories();
  }

  @Test
  void getKanjiCountByCategoryShouldReturnAnEmptyMapIfStringIsNull() {
    Map<String, Integer> kanjiCountByCategory = kanjiCategories.getKanjiCountByCategory(null);
    assertEquals(0, kanjiCountByCategory.keySet().size());
  }

  @Test
  void getKanjiCountByCategoryShouldReturnAMapWithAllValuesZeroIfStringIsEmpty() {
    Map<String, Integer> kanjiCountByCategory = kanjiCategories.getKanjiCountByCategory("");
    kanjiCountByCategory.forEach((key, value) -> assertEquals(0, value.intValue()));
  }

  @Test
  void getKanjiCountByCategoryShouldReturnAMapWithAllValuesZeroIfStringDoesNotContainKanji() {
    Map<String, Integer> kanjiCountByCategory = kanjiCategories
        .getKanjiCountByCategory("こんなゆめをみた。");
    kanjiCountByCategory.forEach((key, value) -> assertEquals(0, value.intValue()));
  }

  @Test
  void getKanjiCountByCategoryShouldReturnAMapWithCorrectJLPTValuesIfStringContainsKanji() {
    Map<String, Integer> kanjiCountByCategory = kanjiCategories.getKanjiCountByCategory(
        "むかしむかし、神さまは十二種類の動物をそれぞれの年の大将にして、" + "この世に誕生したばかりの人間たちの教育係にしようと考えました。");
    assertEquals(1, kanjiCountByCategory.get("N1").intValue());
    assertEquals(1, kanjiCountByCategory.get("N2").intValue());
    assertEquals(5, kanjiCountByCategory.get("N3").intValue());
    assertEquals(5, kanjiCountByCategory.get("N4").intValue());
    assertEquals(7, kanjiCountByCategory.get("N5").intValue());
  }

  @Test
  void getKanjiCountByCategoryShouldReturnAMapWithCorrectJouyouValuesIfStringContainsKanji() {
    Map<String, Integer> kanjiCountByCategory = kanjiCategories.getKanjiCountByCategory(
        "むかしむかし、神さまは十二種類の動物をそれぞれの年の大将にして、" + "この世に誕生したばかりの人間たちの教育係にしようと考えました。");
    assertEquals(6, kanjiCountByCategory.get("jouyou-1").intValue());
    assertEquals(3, kanjiCountByCategory.get("jouyou-2").intValue());
    assertEquals(6, kanjiCountByCategory.get("jouyou-3").intValue());
    assertEquals(2, kanjiCountByCategory.get("jouyou-4").intValue());
    assertEquals(0, kanjiCountByCategory.get("jouyou-5").intValue());
    assertEquals(2, kanjiCountByCategory.get("jouyou-6").intValue());
    assertEquals(0, kanjiCountByCategory.get("jouyou-S").intValue());
  }

  @Test
  void getKanjiCountByCategoryShouldReturnAMapWithCorrectUnknownValuesIfStringContainsUnkownKanji() {
    Map<String, Integer> kanjiCountByCategory = kanjiCategories
        .getKanjiCountByCategory("　龍は天に昇れるので、神の言葉を人間に伝えてくれるだろう。");
    assertEquals(1, kanjiCountByCategory.get("unknown").intValue());
  }
}
