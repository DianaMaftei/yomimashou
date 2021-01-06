package com.yomimashou.creator.xmltransformers.word;

import com.yomimashou.appscommon.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

}
