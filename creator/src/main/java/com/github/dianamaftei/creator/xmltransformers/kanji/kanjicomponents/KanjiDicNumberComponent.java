package com.github.dianamaftei.creator.xmltransformers.kanji.kanjicomponents;

import com.github.dianamaftei.appscommon.model.Kanji;
import com.github.dianamaftei.appscommon.model.KanjiReferences;
import com.github.dianamaftei.creator.jaxbgeneratedmodels.kanjidic.DicNumber;
import com.github.dianamaftei.creator.jaxbgeneratedmodels.kanjidic.DicRef;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KanjiDicNumberComponent implements KanjiComponent {

  private static final char UNDERSCORE = '_';
  private static final String SETTER_METHOD_PREFIX = "set";
  private static final Logger LOGGER = LoggerFactory.getLogger(KanjiDicNumberComponent.class);

  @Override
  public boolean applies(final KanjiComponentType kanjiComponentType) {
    return KanjiComponentType.DICNUMBER.equals(kanjiComponentType);
  }

  @Override
  public Kanji enrich(final Kanji kanji, final Object component) {
    setAllKanjiDictionaryReferences(kanji.getReferences(), (DicNumber) component);
    return kanji;
  }

  private void setAllKanjiDictionaryReferences(final KanjiReferences references,
      final DicNumber component) {
    final List<DicRef> dicRefs = component.getDicRef();

    for (final DicRef dicRef : dicRefs) {
      try {
        KanjiReferences.class
            .getMethod(SETTER_METHOD_PREFIX + convertSnakeCaseToPascalCase(dicRef.getDrType()),
                String.class)
            .invoke(references, dicRef.getContent());
      } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        LOGGER.error("Could not set field: " + dicRef.getDrType(), e);
      }
    }
  }

  private String convertSnakeCaseToPascalCase(final String snakeCaseText) {
    return StringUtils.remove(WordUtils.capitalizeFully(snakeCaseText, UNDERSCORE), UNDERSCORE);
  }
}
