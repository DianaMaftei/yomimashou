package com.yomimashou.creator.dictionary.kanji.kanjicomponents;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Meaning;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Reading;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.ReadingMeaning;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Rmgroup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KanjiReadingMeaningComponent implements KanjiComponent {

    private static final String ON_READING = "ja_on";
    private static final String KUN_READING = "ja_kun";

    @Override
    public boolean applies(final KanjiComponentType kanjiComponentType) {
        return KanjiComponentType.READINGMEANING.equals(kanjiComponentType);
    }

    @Override
    public Kanji enrich(final Kanji kanji, final Object component) {
        enrichKanjiEntryWithReadingsAndMeanings(kanji, (ReadingMeaning) component);
        return kanji;
    }

    private void enrichKanjiEntryWithReadingsAndMeanings(final Kanji kanji, final ReadingMeaning readingMeaning) {
        final List<String> onReadings = new ArrayList<>();
        final List<String> kunReadings = new ArrayList<>();
        final List<String> meanings = new ArrayList<>();

        for (final Rmgroup rmgroup : readingMeaning.getRmgroup()) {

            for (final Reading reading : rmgroup.getReading()) {
                if (ON_READING.equals(reading.getRType())) {
                    onReadings.add(reading.getContent());
                    continue;
                }

                if (KUN_READING.equals(reading.getRType())) {
                    kunReadings.add(reading.getContent());
                }
            }

            for (final Meaning meaning : rmgroup.getMeaning()) {
                if (meaning.getMLang() == null) {
                    meanings.add(meaning.getContent());
                }
            }
        }

        kanji.setOnReading(String.join("|", onReadings));
        kanji.setKunReading(String.join("|", kunReadings));
        kanji.setMeaning(String.join("|", meanings));
    }
}
