package com.github.dianamaftei.yomimashou.dictionary.name;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dianamaftei.appscommon.model.Name;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class NameServiceTest {

  @InjectMocks
  private NameService nameService;

  @Mock
  private NameRepository nameRepository;

  @Test
  void getShouldReturnAListOfNames() {
    final Name name = new Name();
    name.setKanji("のりみや");
    name.setId(5L);
    final String[] searchItems = {"searchItem"};
    final List<Name> names = Collections.singletonList(name);

    when(nameRepository.findDistinctByKanjiInOrReadingIn(searchItems, searchItems,
        Pageable.unpaged()))
        .thenReturn(new PageImpl<>(names));

    final Page<Name> pageableNames = nameService
        .getByReadingElemOrKanjiElem(searchItems, Pageable.unpaged());

    verify(nameRepository, times(1))
        .findDistinctByKanjiInOrReadingIn(searchItems, searchItems,
            Pageable.unpaged());

    assertAll("Should return a list with only the name element declared above",
        () -> assertEquals(1, pageableNames.getContent().size()),
        () -> assertEquals(name.getId(), pageableNames.getContent().get(0).getId()));
  }
}
