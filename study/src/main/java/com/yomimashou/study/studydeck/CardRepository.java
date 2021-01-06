package com.yomimashou.study.studydeck;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CardRepository extends PagingAndSortingRepository<Card, String> {

}
