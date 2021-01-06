package com.yomimashou.sentence;

import com.atilika.kuromoji.jumandic.Token;
import com.atilika.kuromoji.jumandic.Tokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class SentenceService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SentenceService.class);
  private static final String KUROMOJI_PATH = "kuromoji";
  private static final String TAB = "\t";

  private ResourceLoader resourceLoader;

  private Tokenizer tokenizer;

  private Map<String, String> posLvl1Map;
  private Map<String, String> posLvl2Map;
  private Map<String, String> posLvl3Map;
  private Map<String, String> posLvl4Map;

  @Autowired
  public SentenceService(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
    posLvl1Map = getPosTranslationsFromFile(getResource("POS-lvl1.csv"));
    posLvl2Map = getPosTranslationsFromFile(getResource("POS-lvl2.csv"));
    posLvl3Map = getPosTranslationsFromFile(getResource("POS-lvl3.csv"));
    posLvl4Map = getPosTranslationsFromFile(getResource("POS-lvl4.csv"));
  }

  public List<SentenceToken> analyze(String sentence) {
    initTokenizer();
    List<SentenceToken> tokens = new ArrayList<>();
    tokenizer.tokenize(sentence).forEach(token -> tokens.add(buildSentenceToken(token)));
    return tokens;
  }

  private Resource getResource(String fileName) {
    return resourceLoader.getResource("classpath:" + KUROMOJI_PATH + File.separator + fileName);
  }

  private void initTokenizer() {
    if (this.tokenizer == null) {
      this.tokenizer = new Tokenizer();
    }
  }

  private SentenceToken buildSentenceToken(Token token) {
    SentenceToken sentenceToken = new SentenceToken();
    sentenceToken.setSurface(token.getSurface());
    sentenceToken.setBaseForm(token.getBaseForm());
    sentenceToken.setReading(token.getReading());
    sentenceToken.setPartOfSpeechLevel1(posLvl1Map.get(token.getPartOfSpeechLevel1()));
    sentenceToken.setPartOfSpeechLevel2(posLvl2Map.get(token.getPartOfSpeechLevel2()));
    sentenceToken.setPartOfSpeechLevel3(posLvl3Map.get(token.getPartOfSpeechLevel3()));
    sentenceToken.setPartOfSpeechLevel4(posLvl4Map.get(token.getPartOfSpeechLevel4()));
    String semanticInfo = token.getSemanticInformation();
    sentenceToken.setSemanticInformation((semanticInfo != null && !semanticInfo.equals("*")) ?
        semanticInfo : null);
    return sentenceToken;
  }

  // pos translations in files taken from https://rekken.g.hatena.ne.jp/murawaki/20100129/p1
  private static Map<String, String> getPosTranslationsFromFile(Resource resource) {
    Map<String, String> map = new HashMap<>();
    try (BufferedReader lineReader = new BufferedReader(
        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
      lineReader.lines().forEach(line -> {
        String[] columns = line.split(TAB);
        map.put(columns[0], columns.length > 1 ? columns[1] : "");
      });

    } catch (IOException e) {
      LOGGER.error("Could not read POS from resource: " + resource.getFilename(), e);
    }

    return map;
  }

  void setTokenizer(Tokenizer tokenizer) {
    this.tokenizer = tokenizer;
  }
}
