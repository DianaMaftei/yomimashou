import React from "react";
import { shallow } from "enzyme";
import WordList from "./WordList";

describe("WordList", () => {
    let props;
    let shallowWordList;

    const wrapper = () => {
        if (!shallowWordList) {
            shallowWordList = shallow(
                <WordList {...props} />
            );
        }
        return shallowWordList;
    };

    beforeEach(() => {
        props = {
            resultList: undefined,
            limit: undefined,
            showExamples: undefined
        };
        shallowWordList = undefined;
    });

    it("always renders an empty div if no resultList is provided", () => {
        expect(wrapper().find("div").children().length).toBe(0);
    });

    it("always renders full list when limit is larger than resultList length", () => {
        props.resultList = [{ word: "one" }, { word: "two" }];
        props.limit = 5;

        expect(wrapper().find("#word-list-full").length).toBe(1);
    });

    it("always renders full list when there is no limit", () => {
        props.resultList = [{ word: "one" }, { word: "two" }, { word: "three" }, { word: "four" }, { word: "five" }, { word: "six" }];
        props.limit = 5;

        wrapper().instance().showMoreResults();
        wrapper().update();

        expect(wrapper().find("#word-list-full").length).toBe(1);


    });

    it("always renders limited list when there is a limit and there are more elements in the list than the limit", () => {
        props.resultList = [{ word: "one" }, { word: "two" }, { word: "three" }, { word: "four" }, { word: "five" }, { word: "six" }];

        expect(wrapper().find("#word-list-limited").length).toBe(1);
    });
});
