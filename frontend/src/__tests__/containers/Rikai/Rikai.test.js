import React from "react";
import { configure, shallow } from "enzyme";
import Adapter from 'enzyme-adapter-react-16';
import deepFreeze from "deepfreeze";
import { getResult, Rikai } from "../../../containers/Rikai/Rikai";

configure({ adapter: new Adapter() });

describe("Rikai", () => {
    let props;
    let shallowRikai;

    const wrapper = () => {
        if (!shallowRikai) {
            shallowRikai = shallow(
                <Rikai {...props} />
            );
        }
        return shallowRikai;
    };

    beforeEach(() => {
        props = {
            searchResult: undefined,
            showResult: undefined,
            popupInfo: {
                position: undefined,
                visible: false
            },
            limit: undefined,
            setPopupInfo: undefined,
            updateShowResult: undefined
        };
        shallowRikai = undefined;
    });

    it("should render Rikai component", () => {
        expect(wrapper().find("#rikai-window").length).toBe(1);
    });

    it("should render RikaiLoading when no result is passed as prop", () => {
        expect(wrapper().find("#spinner").length).toBe(1);
    });

    it("should render RikaiExamples when a result of type 'examples' is passed as prop", () => {
        props.searchResult = { type: "examples", result: {} };
        props.showResult = { type: "examples", result: [] };

        expect(wrapper().find("#title-examples").length).toBe(1);
    });

    it("should render RikaiKanji when a result of type 'kanji' is passed as prop", () => {
        props.searchResult = { type: "kanji", result: {} };
        props.showResult = { type: "kanji", result: {} };

        expect(wrapper().find("#title-kanji").length).toBe(1);
    });

    it("should render RikaiWords when a result of type 'words' is passed as prop", () => {
        props.searchResult = { type: "words", result: {} };
        props.showResult = { type: "words", result: [] };

        expect(wrapper().find("#title-words").length).toBe(1);
    });


    it("should convert showResult", () => {
        const searchResult = deepFreeze({
            type: "words", result: {
                data: [
                    {
                        word: "神さま",
                        grammarPoint: ""
                    },
                    {
                        word: "神",
                        grammarPoint: ""
                    }],
                matchLen: 3
            }
        });
        const showResult = deepFreeze({
            type: "words",
            result: [
                {
                    id: 34345,
                    kanjiElements: "神",
                    readingElements: "かみ | かむ | かん",
                    meanings: [{
                        id: 41384,
                        partOfSpeech: "n",
                        fieldOfApplication: "",
                        glosses: "god | deity | divinity | spirit | kami",
                        antonym: ""
                    }, {
                        id: 41385,
                        partOfSpeech: "n | n - pref",
                        fieldOfApplication: "",
                        glosses: "incredible | fantastic",
                        antonym: ""
                    }, {
                        id: 41386,
                        partOfSpeech: "n",
                        fieldOfApplication: "",
                        glosses: "emperor of Japan",
                        antonym: ""
                    }, {
                        id: 41387,
                        partOfSpeech: "",
                        fieldOfApplication: "",
                        glosses: "thunder",
                        antonym: ""
                    }]
                },
                {
                    id: 34391,
                    kanjiElements: "神様 | 神さま | 神さん",
                    readingElements: "かみさま | かみさん",
                    meanings: [{
                        id: 41442,
                        partOfSpeech: "n",
                        fieldOfApplication: "",
                        glosses: "God",
                        antonym: ""
                    }]
                }, {
                    id: 133643,
                    kanjiElements: "神",
                    readingElements: "しん | じん",
                    meanings: [{
                        id: 152590,
                        partOfSpeech: "n",
                        fieldOfApplication: "",
                        glosses: "spirit | psyche",
                        antonym: ""
                    }, {
                        id: 152591,
                        partOfSpeech: "",
                        fieldOfApplication: "",
                        glosses: "god | deity | divinity | kami",
                        antonym: ""
                    }]
                }, {
                    id: 162296,
                    kanjiElements: "神 | 霊",
                    readingElements: "み",
                    meanings: [{

                        id: 183422,
                        partOfSpeech: "n",
                        fieldOfApplication: "",
                        glosses: "soul | spirit | divine spirit",
                        antonym: ""
                    }]
                }]
        });

        const result = {
            type: "words",
            result: [
                {
                    grammar: null,
                    kana: ["かみさま ", " かみさん"],
                    kanji: ["神様 ", " 神さま ", " 神さん"],
                    longDef: "(n) God",
                    showShortDef: null
                }, {
                    grammar: null,
                    kana: ["かみ ", " かむ ", " かん"],
                    kanji: ["神"],
                    longDef: "(n) god ,  deity ,  divinity ,  spirit ,  kami; (n | n - pref) incredible ,  fantastic; (n) emperor of Japan; thunder",
                    showShortDef: null
                }, {
                    grammar: null,
                    kana: ["しん ", " じん"],
                    kanji: ["神"],
                    longDef: "(n) spirit ,  psyche; god ,  deity ,  divinity ,  kami",
                    showShortDef: null
                }, {
                    grammar: null,
                    kana: ["み"],
                    kanji: ["神 ", " 霊"],
                    longDef: "(n) soul ,  spirit ,  divine spirit",
                    showShortDef: null
                }]
        };

        expect(getResult(searchResult, showResult)).toEqual(result);
    });

    it("should render RikaiLoading when a result of type 'names' is passed as prop", () => {
        props.searchResult = { type: "names", result: null };
        props.showResult = { type: "names", result: "" };

        expect(wrapper().find("#spinner").length).toBe(1);
    });

});
