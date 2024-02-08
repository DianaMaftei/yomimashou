package com.yomimashou.dictionary.word;

import com.yomimashou.appscommon.model.Word;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    public List<Word> findByReadingElementsOrKanjiElements(Set<String> words) {
        return wordRepository.findDistinctByReadingElementsInOrKanjiElementsIn(words, words);
    }

    public Page<Word> getByStartingKanji(final String character, final Pageable pageable) {
        return wordRepository.findDistinctByKanjiElementsStartsWithOrderByPriority(character, pageable);
    }

    public Page<Word> getByEndingKanji(final String character, final Pageable pageable) {
        return wordRepository.findDistinctByKanjiElementsEndsWithOrderByPriority(character, pageable);
    }

    public Page<Word> getByContainingKanji(final String character, final Pageable pageable) {
        return wordRepository.findDistinctByKanjiElementsContainsOrderByPriority(character, pageable);
    }
}
