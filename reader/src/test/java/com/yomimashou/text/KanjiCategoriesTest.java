package com.yomimashou.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.yomimashou.appscommon.service.KanjiCategoriesService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KanjiCategoriesTest {

  private KanjiCategoriesService kanjiCategories;

  @BeforeEach
  void setUp() {
    kanjiCategories = new KanjiCategoriesService();
  }

  @Test
  void getKanjiCountByCategoryShouldReturnAnEmptyMapIfStringIsNull() {
    final Map<String, Integer> kanjiCountByCategory = kanjiCategories.getKanjiCountByCategory(null);
    assertEquals(0, kanjiCountByCategory.keySet().size());
  }

  @Test
  void getKanjiCountByCategoryShouldReturnAMapWithAllValuesZeroIfStringIsEmpty() {
    final Map<String, Integer> kanjiCountByCategory = kanjiCategories.getKanjiCountByCategory("");
    kanjiCountByCategory.forEach((key, value) -> assertEquals(0, value.intValue()));
  }

  @Test
  void getKanjiCountByCategoryShouldReturnAMapWithAllValuesZeroIfStringDoesNotContainKanji() {
    final Map<String, Integer> kanjiCountByCategory = kanjiCategories
        .getKanjiCountByCategory("こんなゆめをみた。");
    kanjiCountByCategory.forEach((key, value) -> assertEquals(0, value.intValue()));
  }

  @Test
  void getKanjiCountByCategoryShouldReturnAMapWithCorrectJLPTValuesIfStringContainsKanji() {
    final Map<String, Integer> kanjiCountByCategory = kanjiCategories.getKanjiCountByCategory(
        "むかしむかし、神さまは十二種類の動物をそれぞれの年の大将にして、" + "この世に誕生したばかりの人間たちの教育係にしようと考えました。");
    assertEquals(1, kanjiCountByCategory.get("N1").intValue());
    assertEquals(1, kanjiCountByCategory.get("N2").intValue());
    assertEquals(5, kanjiCountByCategory.get("N3").intValue());
    assertEquals(5, kanjiCountByCategory.get("N4").intValue());
    assertEquals(7, kanjiCountByCategory.get("N5").intValue());
  }

  @Test
  void getKanjiCountByCategoryShouldReturnAMapWithCorrectJouyouValuesIfStringContainsKanji() {
    final Map<String, Integer> kanjiCountByCategory = kanjiCategories.getKanjiCountByCategory(
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
    final Map<String, Integer> kanjiCountByCategory = kanjiCategories
        .getKanjiCountByCategory("　龍は天に昇れるので、神の言葉を人間に伝えてくれるだろう。");
    assertEquals(1, kanjiCountByCategory.get("unknown").intValue());
  }
}
