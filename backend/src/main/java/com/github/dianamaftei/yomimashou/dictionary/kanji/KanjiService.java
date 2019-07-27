package com.github.dianamaftei.yomimashou.dictionary.kanji;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KanjiService {

  private static final Logger LOGGER = LoggerFactory.getLogger(KanjiService.class);

  private final KanjiRepository kanjiRepository;

  @Value("${kanji.path}")
  private String kanjiPath;

  @Autowired
  public KanjiService(KanjiRepository kanjiRepository) {
    this.kanjiRepository = kanjiRepository;
  }

  public Kanji get(String searchItem) {
    return kanjiRepository.findByCharacter(searchItem);
  }

  public byte[] getStrokesSVG(String kanji) {
    try (InputStream in = new FileInputStream(kanjiPath + File.separator + kanji);
        ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

      byte[] buffer = new byte[1024];
      int read = 0;
      while ((read = in.read(buffer, 0, buffer.length)) != -1) {
        baos.write(buffer, 0, read);
      }
      baos.flush();
      return baos.toByteArray();
    } catch (IOException e) {
      LOGGER.error("could not read kanji svg", e);
    }
    return new byte[0];
  }
}
