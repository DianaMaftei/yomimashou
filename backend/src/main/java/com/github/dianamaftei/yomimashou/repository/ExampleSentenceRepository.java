package com.github.dianamaftei.yomimashou.repository;

import com.github.dianamaftei.yomimashou.model.ExampleSentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleSentenceRepository extends JpaRepository<ExampleSentence, Long> {
}
