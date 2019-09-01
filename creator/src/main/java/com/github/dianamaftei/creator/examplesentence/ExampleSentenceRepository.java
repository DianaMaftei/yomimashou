package com.github.dianamaftei.creator.examplesentence;

import com.github.dianamaftei.appscommon.model.ExampleSentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleSentenceRepository extends JpaRepository<ExampleSentence, Long> {

}
