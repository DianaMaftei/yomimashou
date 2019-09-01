package com.github.dianamaftei.creator.xmltransformers.word;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PartsOfSpeech {

  static Map<String, String> buildListOfPartsOfSpeech() {
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
