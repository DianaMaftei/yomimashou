package com.yomimashou.dictionary.example;

import com.yomimashou.appscommon.model.ExampleSentence;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class ExampleSentenceService {

    private ExampleSentenceRepository exampleSentenceRepository;

    @Transactional
    public Page<ExampleSentence> get(final String[] searchItems, Pageable pageable) {
        return exampleSentenceRepository.findBySentenceOrTextBreakdownIn(searchItems, pageable);
    }
}
