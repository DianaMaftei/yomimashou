package com.github.dianamaftei.yomimashou.repository;

import com.github.dianamaftei.yomimashou.model.NameEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameEntryRepository extends JpaRepository<NameEntry, Long> {
}
