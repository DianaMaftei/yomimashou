package com.github.dianamaftei.yomimashou.text;

import com.github.dianamaftei.appscommon.model.KanjiCategories;
import java.util.List;
import java.util.Optional;

import com.github.dianamaftei.appscommon.model.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TextService {

  private static final String CREATION_DATE = "creationDate";
  private static final String ELLIPSIS = "...";
  private final TextRepository textRepository;
  private final KanjiCategories kanjiCategories;
  private static final String[] SENTENCE_ENDING_CHARACTERS = {"。", ".", "…", "‥", "！", "？"};

  @Autowired
  public TextService(final TextRepository textRepository, final KanjiCategories kanjiCategories) {
    this.textRepository = textRepository;
    this.kanjiCategories = kanjiCategories;
  }

  public Text add(final Text text) {
    text.setExcerpt(getExcerpt(text));
    text.setKanjiCountByLevel(kanjiCategories.getKanjiCountByCategory(text.getContent()));
    text.setCharacterCount(text.getContent().length());
    return textRepository.save(text);
  }

  public List<Text> getAll() {
    return this.textRepository.findAll(new Sort(Sort.Direction.DESC, CREATION_DATE));
  }

  public Optional<Text> getById(final Long id) {
    return this.textRepository.findById(id);
  }

  private String getExcerpt(final Text text) {
    final String snippet = text.getContent().substring(0, getIndexOfSentenceEnd(text.getContent()));
    boolean snippetEndsWithEndingCharacter = false;

    for (final String sentenceEndingCharacter : SENTENCE_ENDING_CHARACTERS) {
      if (snippet.endsWith(sentenceEndingCharacter)) {
        snippetEndsWithEndingCharacter = true;
        break;
      }
    }

    if (!snippetEndsWithEndingCharacter) {
      return snippet + ELLIPSIS;
    }

    return snippet;
  }

  private int getIndexOfSentenceEnd(final String text) {
    final int startOfEndingCharacterSearch = 250;
    final int endOfEndingCharacterSearch = 350;
    int indexOfEndingCharacter = 100;

    if (text.length() < endOfEndingCharacterSearch) {
      return text.length();
    }

    final String substring = text
        .substring(startOfEndingCharacterSearch, endOfEndingCharacterSearch + 1);

    for (final String character : SENTENCE_ENDING_CHARACTERS) {
      if (substring.contains(character) && substring.indexOf(character) < indexOfEndingCharacter) {
        indexOfEndingCharacter = substring.indexOf(character);
      }
    }

    return startOfEndingCharacterSearch + indexOfEndingCharacter + 1;
  }
}
