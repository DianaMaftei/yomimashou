package com.yomimashou.study.studydeck;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CardRepository extends PagingAndSortingRepository<Card, String> {

    List<Card> findAllByIdInAndNextPracticeBefore(List<String> ids, LocalDateTime nextPractice);
}
