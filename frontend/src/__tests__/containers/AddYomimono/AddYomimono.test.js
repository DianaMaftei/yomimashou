import React from "react";
import { configure, shallow } from "enzyme";
import Adapter from 'enzyme-adapter-react-15';
import { AddYomimono } from "../../../containers/AddYomimono/AddYomimono";
import { Link } from "react-router-dom";

configure({ adapter: new Adapter() });

describe("AddYomimono", () => {
    let props;
    let shallowAddYomimono;

    const wrapper = () => {
        if (!shallowAddYomimono) {
            shallowAddYomimono = shallow(
                <AddYomimono {...props} />
            );
        }
        return shallowAddYomimono;
    };

    beforeEach(() => {
        props = {
            text: undefined,
            setText: undefined
        };
        shallowAddYomimono = undefined;
    });

    it("should render AddYomimono component", () => {
        expect(wrapper().find("#add-yomimono").length).toBe(1);
    });

    it("should render a textarea", () => {
        expect(wrapper().find("textarea").length).toBe(1);
    });

    it("should contain a Link component leading to /add if no text is provided", () => {
        expect(wrapper().find(Link).props().to).toBe('/add');
    });

    it("should contain a Link component leading to /view if text is provided", () => {
        props.text = "Some text goes here";
        expect(wrapper().find(Link).props().to).toBe('/view');
    });

    it("should contain an empty textarea field if no text is passed", () => {
        let textareaValue = wrapper().find("textarea").props().value;

        expect(textareaValue).toBe("");
    });

    it("should contain an empty textarea field if no text is passed", () => {
        props.text = "Some text goes here";

        let textareaValue = wrapper().find("textarea").props().value;

        expect(textareaValue).toBe(props.text);
    });

    it("should call setText when textarea changes value", () => {
        props.text = "Some text goes here";
        let mockSetText = jest.fn(() => {
        });
        props.setText = mockSetText;

        wrapper().find("textarea").simulate('change', { target: { value: 'mock text' } });

        expect(mockSetText.mock.calls.length).toBe(1);
    });
});
