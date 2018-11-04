import React from "react";
import { configure, shallow } from "enzyme";
import Adapter from 'enzyme-adapter-react-16';
import '../../../__mocks__/xhr-mock.js';
import { YomiText } from "../../../containers/YomiText/YomiText";
import { highlightMatch, isVisible, search, tryToFindTextAtMouse } from "../../../util/rikai/RikaiTextParser";
import deepFreeze from "deepfreeze";

configure({ adapter: new Adapter() });
jest.mock('../../../util/rikai/RikaiTextParser', () => ({
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
    });

    beforeEach(() => {
        props = {
            text: undefined,
            textSelectInfo: undefined,
            searchResult: undefined,
            showResult: undefined,
            limit: undefined,
            currentDictionary: undefined,
            popupInfo: undefined,
            updateTextSelectInfo: undefined,
            fetchData: undefined,
            updateSearchResult: undefined,
            setPopupInfo: undefined
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

        wrapper().find("#yomi-text-container").simulate('mousemove', { target: { value: 'mock text' } });

        expect(mockOnMouseMove.mock.calls.length).toBe(1);
    });

    it("should call onMouseClick when user clicks inside the container", () => {
        let mockOnMouseClick = jest.fn(() => {
        });

        wrapper().instance().onMouseClick = mockOnMouseClick;
        wrapper().update();

        wrapper().find("#yomi-text-container").simulate('click', { target: { value: 'mock text' } });

        expect(mockOnMouseClick.mock.calls.length).toBe(1);
    });

    it("should call methods from Rikai Parser when triggering mouse move", () => {
        props.updateSearchResult = jest.fn();
        props.updateTextSelectInfo = jest.fn();

        wrapper().find("#yomi-text-container").simulate('mousemove', { target: { value: 'mock text' } });

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
                        getClientRects: () => [{ left: 20, bottom: 20 }]
                    }
                }
            }
        };

        document.getSelection = jest.fn().mockImplementation(getSelection);

        wrapper().find("#yomi-text-container").simulate('click', { target: { value: 'mock text' } });

        expect(props.fetchData).toHaveBeenCalledWith(result.result.data, result.type);
        expect(props.setPopupInfo).toHaveBeenCalledWith({ "position": { "x": 20, "y": 20 }, "visibility": true });
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
                        getClientRects: () => [{ left: 20, bottom: 20 }]
                    }
                }
            }
        };

        document.getSelection = jest.fn().mockImplementation(getSelection);

        wrapper().find("#yomi-text-container").simulate('click', { target: { value: 'mock text' } });

        expect(props.fetchData).toHaveBeenCalledWith(result.result, result.type);
        expect(props.setPopupInfo).toHaveBeenCalledWith({ "position": { "x": 20, "y": 20 }, "visibility": true });
    });
});
