package com.yomimashou.creator.dictionary.name.namecomponents;

import com.yomimashou.appscommon.model.Name;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.NameType;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.Trans;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.TransDet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NameTranslationComponentTest {

    private final NameTranslationComponent nameTranslationComponent = new NameTranslationComponent();

    @ParameterizedTest
    @EnumSource(NameComponentType.class)
    void applies_should_returnTrue_onlyFor_transComponentType(NameComponentType type) {
        boolean expected = type == NameComponentType.TRANS;
        assertEquals(expected, nameTranslationComponent.applies(type));
    }

    @Test
    void enrich_shouldParse_translations() {
        final TransDet transDet1 = new TransDet();
        transDet1.setvalue("Abe Kouhei");

        final TransDet transDet2 = new TransDet();
        transDet2.setvalue("translation 2");

        final Trans trans = new Trans();
        trans.getTransDet().add(transDet1);
        trans.getTransDet().add(transDet2);
        Name name = new Name();

        nameTranslationComponent.enrich(name, trans);

        assertEquals(transDet1.getvalue().concat("|").concat(transDet2.getvalue()), name.getTranslations());
    }

    @Test
    void enrich_shouldParse_types() {
        final NameType nameType = new NameType();
        nameType.setvalue("female given name or forename");

        final NameType nameType2 = new NameType();
        nameType2.setvalue("full name of a particular person");

        final Trans trans = new Trans();
        trans.getNameType().add(nameType);
        trans.getNameType().add(nameType2);
        Name name = new Name();

        nameTranslationComponent.enrich(name, trans);

        assertEquals(nameType.getvalue().concat("|").concat(nameType2.getvalue()), name.getType());
    }
}