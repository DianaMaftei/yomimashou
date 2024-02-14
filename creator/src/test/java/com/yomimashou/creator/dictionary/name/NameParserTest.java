package com.yomimashou.creator.dictionary.name;

import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.Entry;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.KEle;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.REle;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.Trans;
import com.yomimashou.creator.dictionary.name.namecomponents.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class NameParserTest {

    @InjectMocks
    private NameParser nameParser;

    @Mock
    private NameKanjiComponent nameKanjiComponent;
    @Mock
    private NameReadingComponent nameReadingComponent;
    @Mock
    private NameTranslationComponent nameTranslationComponent;

    @Spy
    private List<NameComponent> nameComponentsEnrichers = new ArrayList<>();

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        nameComponentsEnrichers.add(nameKanjiComponent);
        nameComponentsEnrichers.add(nameReadingComponent);
        nameComponentsEnrichers.add(nameTranslationComponent);
    }

    @Test
    void parseShouldCheckAllKanjiComponentEnrichersForTheCharacter() {
        final Entry entry = new Entry();
        final List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
        final KEle kEle = new KEle();
        entSeqOrKEleOrREleOrTrans.add(kEle);

        nameParser.parse(entry);

        verify(nameKanjiComponent, times(1)).applies(NameComponentType.KELE);
        verify(nameReadingComponent, times(1)).applies(NameComponentType.KELE);
        verify(nameTranslationComponent, times(1)).applies(NameComponentType.KELE);
    }

    @Test
    void parseShouldExtractKEle() {
        final Entry entry = new Entry();
        final List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
        final KEle kEle = new KEle();
        entSeqOrKEleOrREleOrTrans.add(kEle);

        when(nameKanjiComponent.applies(NameComponentType.KELE)).thenReturn(true);

        nameParser.parse(entry);

        verify(nameKanjiComponent, times(1)).enrich(any(), eq(kEle));
    }

    @Test
    void parseShouldExtractREle() {
        final Entry entry = new Entry();
        final List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
        final REle rEle = new REle();
        entSeqOrKEleOrREleOrTrans.add(rEle);

        when(nameReadingComponent.applies(NameComponentType.RELE)).thenReturn(true);

        nameParser.parse(entry);

        verify(nameReadingComponent, times(1)).enrich(any(), eq(rEle));
    }

    @Test
    void parseShouldExtractTranslations() {
        final Entry entry = new Entry();
        final List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
        final Trans trans = new Trans();
        entSeqOrKEleOrREleOrTrans.add(trans);

        when(nameTranslationComponent.applies(NameComponentType.TRANS)).thenReturn(true);

        nameParser.parse(entry);

        verify(nameTranslationComponent, times(1)).enrich(any(), eq(trans));
    }

    @Test
    void parseShouldExtractTypes() {
        final Entry entry = new Entry();
        final List<Object> entSeqOrKEleOrREleOrTrans = entry.getEntSeqOrKEleOrREleOrTrans();
        final Trans trans = new Trans();
        entSeqOrKEleOrREleOrTrans.add(trans);

        when(nameTranslationComponent.applies(NameComponentType.TRANS)).thenReturn(true);

        nameParser.parse(entry);

        verify(nameTranslationComponent, times(1)).enrich(any(), eq(trans));
    }

}