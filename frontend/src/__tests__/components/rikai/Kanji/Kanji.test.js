import React from "react";
import { configure, shallow } from "enzyme";
import Adapter from 'enzyme-adapter-react-15';
import Kanji from "../../../../components/rikai/Kanji/Kanji";

configure({ adapter: new Adapter() });

describe("Kanji", () => {
    let props;
    let shallowKanji;

    const wrapper = () => {
        if (!shallowKanji) {
            shallowKanji = shallow(
                <Kanji {...props} />
            );
        }
        return shallowKanji;
    };

    beforeEach(() => {
        props = {
            result: undefined
        };
        shallowKanji = undefined;
        window.Dmak= function Dmak() {
            this.eraseLastStrokes = jest.fn();
            this.pause = jest.fn();
            this.render = jest.fn();
            this.renderNextStrokes = jest.fn();
            this.erase = jest.fn();
        };
    });

    afterEach(() => {
        window.Dmak= null;
    });

    it("should render Kanji component", () => {
        props.result = { foo: "bar" };
        expect(wrapper().find(".kanji-box").length).toBe(1);
    });

    it("should throw an error if no result is provided", () => {
        expect(() => {
            wrapper()
        }).toThrow();
    });

});
