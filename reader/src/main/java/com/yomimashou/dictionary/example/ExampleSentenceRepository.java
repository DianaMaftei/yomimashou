package com.yomimashou.dictionary.example;

import com.yomimashou.appscommon.model.ExampleSentence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleSentenceRepository extends JpaRepository<ExampleSentence, Long> {
    @Query("SELECT es FROM ExampleSentence es WHERE es.sentence IN :searchItems OR es.textBreakdown IN :searchItems")
    Page<ExampleSentence> findBySentenceOrTextBreakdownIn(String[] searchItems, Pageable pageable);

}
