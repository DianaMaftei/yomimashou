package com.github.dianamaftei.yomimashou.repository;

import com.github.dianamaftei.yomimashou.model.WordEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordEntryRepository extends JpaRepository<WordEntry, Long> {
}
