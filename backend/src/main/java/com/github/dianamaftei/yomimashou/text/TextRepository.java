package com.github.dianamaftei.yomimashou.text;

import com.github.dianamaftei.appscommon.model.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {
}
