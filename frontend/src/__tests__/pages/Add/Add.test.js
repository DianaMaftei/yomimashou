import React from "react";
import { configure, shallow } from "enzyme";
import Adapter from 'enzyme-adapter-react-16';
import { Add } from "../../../pages/add/Add";
import { Link } from "react-router-dom/umd/react-router-dom";
import ReactQuill from "react-quill/dist/react-quill";

configure({ adapter: new Adapter() });

describe("Add", () => {
    let props;
    let shallowAdd;

    const wrapper = () => {
        if (!shallowAdd) {
            shallowAdd = shallow(
                <Add {...props} />
            );
        }
        return shallowAdd;
    };

    beforeEach(() => {
        props = {
            text: {},
            setText: undefined
        };
        shallowAdd = undefined;
    });

    it("should render Add page component", () => {
        expect(wrapper().find("#add-page")).toHaveLength(1);
    });

    it("should render the title field", () => {
        expect(wrapper().find("#title-required")).toHaveLength(1);
    });

    it("should render the upload file button", () => {
        expect(wrapper().find("#outlined-button-file")).toHaveLength(1);
    });

    it("should contain a button with a Link component leading to /view", () => {
        expect(wrapper().find(Link).props().to).toBe('/view');
    });

    it("should contain an empty ReactQuill if no text is passed", () => {
        expect(wrapper().find(ReactQuill).props().value).toBe('');
    });

    it("should contain a ReactQuill component with text inside if text is provided", () => {
        props.text = {formatted: "Some text goes here"};

        expect(wrapper().find(ReactQuill).props().value).toBe(props.text.formatted);
    });

    it("should call setText when ReactQuill changes value", () => {
        props.text = {formatted: "Some text goes here"};
        let mockSetText = jest.fn(() => {
        });
        props.setText = mockSetText;

        wrapper().find(ReactQuill).simulate('change', { target: { value: 'mock text' } });

        expect(mockSetText.mock.calls.length).toBe(1);
    });
});
