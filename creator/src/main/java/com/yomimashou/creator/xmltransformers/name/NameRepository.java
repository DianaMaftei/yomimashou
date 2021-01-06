package com.yomimashou.creator.xmltransformers.name;

import com.yomimashou.appscommon.model.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameRepository extends JpaRepository<Name, Long> {

}
