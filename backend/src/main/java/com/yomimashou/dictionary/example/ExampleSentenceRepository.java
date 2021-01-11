package com.yomimashou.dictionary.example;

import com.yomimashou.appscommon.model.ExampleSentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleSentenceRepository extends JpaRepository<ExampleSentence, Long> {

}