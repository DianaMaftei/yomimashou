import React from "react";
import { shallow } from "enzyme";
import WordItem from "./WordItem";

describe("WordItem", () => {
    let props;
    let shallowWordItem;

    const wrapper = () => {
        if (!shallowWordItem) {
            shallowWordItem = shallow(
                <WordItem {...props} />
            );
        }
        return shallowWordItem;
    };

    beforeEach(() => {
        props = {
            kanji: undefined,
            kana: undefined,
            grammar: undefined,
            shortDef: undefined,
            showExamples: undefined,
            wordClassName: undefined,
            showLongDef: undefined
        };
        shallowWordItem = undefined;
    });

    it("should render WordItemWithKanjiAndShortDef when props contain kanji and showShortDef is true", () => {
        props.kanji = [{ foo: "bar" }];
        props.kana = [{ bar: "foo" }];
        props.shortDef = "short def";

        expect(wrapper().find("#word-with-kanji-and-short-def").length).toBe(1);
    });

    it("should render WordItemWithKanjiAndLongDef when props contain kanji and showShortDef is false", () => {
        props.kanji = [{ foo: "bar" }];
        props.kana = [{ bar: "foo" }];
        props.shortDef = "short def";
        props.longDef = "long def";

        const event = {
            stopPropagation: () => {
            }
        };

        wrapper().instance().showLongDef(event);
        wrapper().update();


        expect(wrapper().find("#word-with-kanji-and-long-def").length).toBe(1);
    });

    it("should render WordItemWithKanaAndShortDef when props don't contain kanji and showShortDef is true", () => {
        props.kana = [{ bar: "foo" }];
        props.shortDef = "short def";

        expect(wrapper().find("#word-with-kana-and-short-def").length).toBe(1);
    });

    it("should render WordItemWithKanaAndLongDef when props don't contain kanji and showShortDef is false", () => {
        props.kana = [{ bar: "foo" }];
        props.shortDef = "short def";
        props.longDef = "long def";

        const event = {
            stopPropagation: () => {
            }
        };

        wrapper().instance().showLongDef(event);
        wrapper().update();


        expect(wrapper().find("#word-with-kana-and-long-def").length).toBe(1);
    });

});
