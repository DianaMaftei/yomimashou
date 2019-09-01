package com.github.dianamaftei.creator.xmltransformers.name;

import com.github.dianamaftei.appscommon.model.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameRepository extends JpaRepository<Name, Long> {

}