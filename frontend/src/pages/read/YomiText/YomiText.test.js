import React from "react";
import { shallow } from "enzyme";
import '../../../__mocks__/xhr-mock.js';
import { YomiText } from "./YomiText";
import { highlightMatch, isVisible, search, tryToFindTextAtMouse } from "./Rikai/RikaiTextParser";
import deepFreeze from "deepfreeze";

jest.mock('./Rikai/RikaiTextParser', () => ({
    highlightMatch: jest.fn(),
    isVisible: jest.fn(),
    search: jest.fn().mockReturnValue({ entries: { result: { matchLen: 3 } } }),
    tryToFindTextAtMouse: jest.fn().mockReturnValue({ prevRangeNode: { ownerDocument: "mock value" } })
}));

describe("YomiText", () => {
    let props;
    let shallowYomiText;

    const wrapper = () => {
        if (!shallowYomiText) {
            shallowYomiText = shallow(
                <YomiText {...props} />
            );
        }
        return shallowYomiText;
    };

    beforeAll(() => {
        window.getSelection = () => {
            return {
                removeAllRanges: () => {
                }
            };
        };

        document.getElementById = jest.fn().mockReturnValue({});
    });

    beforeEach(() => {
        props = {
            text: {title: "some title"},
            textSelectInfo: undefined,
            searchResult: undefined,
            showResult: undefined,
            limit: undefined,
            currentDictionary: undefined,
            popupInfo: undefined,
            updateTextSelectInfo: undefined,
            fetchData: undefined,
            updateSearchResult: undefined,
            setPopupInfo: undefined,
            setAnalyzer: jest.fn(() => {
            })
        };
        shallowYomiText = undefined;
    });

    it("should render YomiText component", () => {
        expect(wrapper().find("#yomi-text").length).toBe(1);
    });

    it("should contain a Rikai component", () => {
        expect(wrapper().find("Connect(Rikai)")).toHaveLength(1);
    });

    it("should call onMouseMove when user moves the mouse inside the container", () => {
        let mockOnMouseMove = jest.fn(() => {
        });

        wrapper().instance().onMouseMove = mockOnMouseMove;
        wrapper().update();

        wrapper().find("#yomi-text-box").simulate('mousemove', { target: { value: 'mock text' } });

        expect(mockOnMouseMove.mock.calls.length).toBe(1);
    });

    it("should call onMouseClick when user clicks inside the container", () => {
        let mockOnMouseClick = jest.fn(() => {
        });

        wrapper().instance().onMouseClick = mockOnMouseClick;
        wrapper().update();

        wrapper().find("#yomi-text-box").simulate('click', { target: { value: 'mock text' } });

        expect(mockOnMouseClick.mock.calls.length).toBe(1);
    });

    it("should call methods from Rikai Parser when triggering mouse move", () => {
        props.updateSearchResult = jest.fn();
        props.updateTextSelectInfo = jest.fn();

        wrapper().find("#yomi-text-box").simulate('mousemove', { target: { value: 'mock text' } });

        expect(isVisible).toHaveBeenCalledTimes(1);
        expect(tryToFindTextAtMouse).toHaveBeenCalledTimes(1);
        expect(search).toHaveBeenCalledTimes(1);
        expect(highlightMatch).toHaveBeenCalledTimes(1);
    });

    it("should try to fetch data when searchResult's type is 'words' and user has clicked inside container", () => {
        props.setPopupInfo = jest.fn();
        props.fetchData = jest.fn();
        const result = deepFreeze({ type: "words", result: { data: [] } });
        props.searchResult = result;

        const getSelection = () => {
            return {
                getRangeAt: () => {
                    return {
                        getClientRects: () => [{ x: 20, y: 20 }]
                    }
                }
            }
        };

        document.getSelection = jest.fn().mockImplementation(getSelection);

        wrapper().find("#yomi-text-box").simulate('click', { target: { value: 'mock text' } });

        expect(props.fetchData).toHaveBeenCalledWith(result.result.data, result.type);
        expect(props.setPopupInfo).toHaveBeenCalledWith({ "position": { "x": 10, "y": 45 }, "visibility": true });
    });

    it("should try to fetch data when searchResult's type is 'kanji' and user has clicked inside container", () => {
        props.setPopupInfo = jest.fn();
        props.fetchData = jest.fn();
        const result = deepFreeze({ type: "kanji", result: { kanji: "k" } });
        props.searchResult = result;

        const getSelection = () => {
            return {
                getRangeAt: () => {
                    return {
                        getClientRects: () => [{ x: 20, y: 20 }]
                    }
                }
            }
        };

        document.getSelection = jest.fn().mockImplementation(getSelection);

        wrapper().find("#yomi-text-box").simulate('click', { target: { value: 'mock text' } });

        expect(props.fetchData).toHaveBeenCalledWith(result.result, result.type);
        expect(props.setPopupInfo).toHaveBeenCalledWith({ "position": { "x": 10, "y": 45 }, "visibility": true });
    });
});
