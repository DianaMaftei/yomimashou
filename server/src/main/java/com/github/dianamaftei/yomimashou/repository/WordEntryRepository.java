package com.github.dianamaftei.yomimashou.repository;

import com.github.dianamaftei.yomimashou.model.WordEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface WordEntryRepository extends CrudRepository<WordEntry, Long> {
}
