package com.yomimashou.creator.dictionary.name.namecomponents;

import com.yomimashou.appscommon.model.Name;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.REle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NameReadingComponentTest {

    private final NameReadingComponent nameReadingComponent = new NameReadingComponent();

    @ParameterizedTest
    @EnumSource(NameComponentType.class)
    void applies_should_returnTrue_onlyFor_releComponentType(NameComponentType type) {
        boolean expected = type == NameComponentType.RELE;
        assertEquals(expected, nameReadingComponent.applies(type));
    }

    @Test
    void enrich_shouldParse_reading() {
        final REle rEle = new REle();
        rEle.setReb("あべこうへい");
        Name name = new Name();

        nameReadingComponent.enrich(name, rEle);

        assertEquals(rEle.getReb(), name.getReading());
    }
}