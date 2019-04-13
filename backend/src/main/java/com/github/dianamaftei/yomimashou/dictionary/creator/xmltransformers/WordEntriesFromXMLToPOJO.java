package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.DictionaryEntry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.Entry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.Gloss;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.JMdict;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.KEle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.REle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.Sense;
import com.github.dianamaftei.yomimashou.dictionary.word.Word;
import com.github.dianamaftei.yomimashou.dictionary.word.WordMeaning;
import com.github.dianamaftei.yomimashou.dictionary.word.WordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WordEntriesFromXMLToPOJO extends XMLEntryToPOJO {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordEntriesFromXMLToPOJO.class);

    private static final int HIGHEST_PRIORITY = 1;
    private static final int MODERATE_PRIORITY = 2;
    private static final int LOW_PRIORITY = 3;

    private final WordRepository wordRepository;
    private final Map<String, String> partsOfSpeech;

    @Autowired
    public WordEntriesFromXMLToPOJO(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
        this.partsOfSpeech = buildListOfPartsOfSpeech();
        this.dictionarySource = "http://ftp.monash.edu/pub/nihongo/JMdict_e.gz";
        this.fileName = "wordEntries.txt";
    }

    @Override
    List<Entry> getEntries(Object dictionaryFile) {
        return ((JMdict) dictionaryFile).getEntry();
    }

    @Override
    Class getClassForJaxb() {
        return JMdict.class;
    }

    @Override
    void saveToFile(List<? extends DictionaryEntry> entries) {
        try {
            Set<String> wordEntries = ((List<Entry>) entries).parallelStream()
                    .filter(Objects::nonNull)
                    .map(entry -> Arrays.asList(
                            entry.getKEle().stream().map(KEle::getKeb).collect(Collectors.toList()),
                            entry.getREle().stream().map(REle::getReb).collect(Collectors.toList()))
                    ).flatMap(List::stream)
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());

            writeToFile(wordEntries, fileName);
        } catch (Exception e) {
            LOGGER.error("Could not save to file: " + fileName, e);
        }
    }

    @Override
    void saveToDB(List<? extends DictionaryEntry> entries) {
        ((List<Entry>) entries).parallelStream().forEach(entry -> {
            Word word = buildWordEntry(entry);
            wordRepository.save(word);
        });
    }

    private Word buildWordEntry(Entry entry) {
        Word word = new Word();

        word.setKanjiElements(entry.getKEle().stream().map(KEle::getKeb).collect(Collectors.joining("|")));
        word.setReadingElements(entry.getREle().stream().map(REle::getReb).collect(Collectors.joining("|")));
        word.setMeanings(buildMeaningsList(entry));
        word.setPriority(extractHighestPriority(entry));

        return word;
    }

    private Integer extractHighestPriority(Entry entry) {
        Set<Integer> priorities = new HashSet<>();
        entry.getKEle().forEach(kEle -> {
            if (!kEle.getKePri().isEmpty()) {
                kEle.getKePri().forEach(priority -> priorities.add(getPriorityNumber(priority)));
            }
        });
        entry.getREle().forEach(rEle -> {
            if (!rEle.getRePri().isEmpty()) {
                rEle.getRePri().forEach(priority -> priorities.add(getPriorityNumber(priority)));
            }
        });

        if (priorities.isEmpty()) {
            return LOW_PRIORITY;
        }

        // 1 is the highest priority
        return Collections.min(priorities);
    }

    private int getPriorityNumber(String priority) {
        List<String> highestPriorities = Arrays.asList("news1", "ichi1", "spec1", "spec2", "gai1");

        if (highestPriorities.contains(priority)) {
            return HIGHEST_PRIORITY;
        }
        return MODERATE_PRIORITY;
    }

    private List<WordMeaning> buildMeaningsList(Entry entry) {
        List<WordMeaning> meanings = new ArrayList<>();

        for (Sense sense : entry.getSense()) {
            WordMeaning meaning = new WordMeaning();
            meaning.setPartOfSpeech(String.join("|", sense.getPos().stream()
                    .map(partsOfSpeech::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())));
            meaning.setFieldOfApplication(String.join("|", sense.getField()));
            meaning.setAntonym(String.join("|", sense.getAnt()));
            meaning.setGlosses(String.join("|", sense.getGloss().stream()
                    .map(Gloss::getContent)
                    .flatMap(Collection::stream)
                    .map(Object::toString)
                    .collect(Collectors.toList())));

            meanings.add(meaning);
        }

        return meanings;
    }

    private Map<String, String> buildListOfPartsOfSpeech() {
        Map<String, String> partsOfSpeechMap = new HashMap<>();
        partsOfSpeechMap.put("adjective (keiyoushi)", "adj-i");
        partsOfSpeechMap.put("adjectival nouns or quasi-adjectives (keiyodoshi)", "adj-na");
        partsOfSpeechMap.put("nouns which may take the genitive case particle 'no'", "adj-no");
        partsOfSpeechMap.put("pre-noun adjectival (rentaishi)", "adj-pn");
        partsOfSpeechMap.put("'taru' adjective", "adj-t");
        partsOfSpeechMap.put("noun or verb acting prenominally (other than the above)", "adj-f");
        partsOfSpeechMap.put("former adjective classification (being removed)", "adj");
        partsOfSpeechMap.put("adverb (fukushi)", "adv");
        partsOfSpeechMap.put("adverbial noun", "adv-n");
        partsOfSpeechMap.put("adverb taking the 'to' particle", "adv-to");
        partsOfSpeechMap.put("auxiliary", "aux");
        partsOfSpeechMap.put("auxiliary verb", "aux-v");
        partsOfSpeechMap.put("auxiliary adjective", "aux-adj");
        partsOfSpeechMap.put("conjunction", "conj");
        partsOfSpeechMap.put("counter", "ctr");
        partsOfSpeechMap.put("Expressions (phrases, clauses, etc.)", "exp");
        partsOfSpeechMap.put("idiomatic expression", "id");
        partsOfSpeechMap.put("interjection (kandoushi)", "int");
        partsOfSpeechMap.put("irregular verb", "iv");
        partsOfSpeechMap.put("noun (common) (futsuumeishi)", "n");
        partsOfSpeechMap.put("adverbial noun (fukushitekimeishi)", "n-adv");
        partsOfSpeechMap.put("noun, used as a prefix", "n-pref");
        partsOfSpeechMap.put("noun (temporal) (jisoumeishi)", "n-t");
        partsOfSpeechMap.put("numeric", "num");
        partsOfSpeechMap.put("pronoun", "pn");
        partsOfSpeechMap.put("prefix", "pref");
        partsOfSpeechMap.put("particle", "prt");
        partsOfSpeechMap.put("suffix", "suf");
        partsOfSpeechMap.put("Ichidan verb", "v1");
        partsOfSpeechMap.put("Godan verb (not completely classified)", "v5");
        partsOfSpeechMap.put("Godan verb - -aru special class", "v5aru");
        partsOfSpeechMap.put("Godan verb with 'bu' ending", "v5b");
        partsOfSpeechMap.put("Godan verb with 'gu' ending", "v5g");
        partsOfSpeechMap.put("Godan verb with 'ku' ending", "v5k");
        partsOfSpeechMap.put("Godan verb - iku/yuku special class", "v5k-s");
        partsOfSpeechMap.put("Godan verb with 'mu' ending", "v5m");
        partsOfSpeechMap.put("Godan verb with 'nu' ending", "v5n");
        partsOfSpeechMap.put("Godan verb with 'ru' ending", "v5r");
        partsOfSpeechMap.put("Godan verb with 'ru' ending (irregular verb)", "v5r-i");
        partsOfSpeechMap.put("Godan verb with 'su' ending", "v5s");
        partsOfSpeechMap.put("Godan verb with 'tsu' ending", "v5t");
        partsOfSpeechMap.put("Godan verb with 'u' ending", "v5u");
        partsOfSpeechMap.put("Godan verb with 'u' ending (special class)", "v5u-s");
        partsOfSpeechMap.put("Godan verb - uru old class verb (old form of Eru)", "v5uru");
        partsOfSpeechMap.put("Godan verb with 'zu' ending", "v5z");
        partsOfSpeechMap.put("Ichidan verb - zuru verb - (alternative form of -jiru verbs)", "vz");
        partsOfSpeechMap.put("intransitive verb", "vi");
        partsOfSpeechMap.put("kuru verb - special class", "vk");
        partsOfSpeechMap.put("irregular nu verb", "vn");
        partsOfSpeechMap.put("noun or participle which takes the aux. verb suru", "vs");
        partsOfSpeechMap.put("suru verb - irregular", "vs-i");
        partsOfSpeechMap.put("suru verb - special class", "vs-s");
        partsOfSpeechMap.put("transitive verb", "vt");
        return partsOfSpeechMap;
    }

}
