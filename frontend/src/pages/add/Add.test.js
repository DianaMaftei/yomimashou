import React from "react";
import { shallow } from "enzyme";
import Add from "./Add";
import Editor from "react-pell";

describe("Add", () => {
    let props;
    let shallowAdd;

    const wrapper = () => {
        if (!shallowAdd) {
            shallowAdd = shallow(
                <Add.WrappedComponent {...props} />
            );
        }
        return shallowAdd;
    };

    beforeEach(() => {
        props = {
            text: {},
            setText: undefined,
            history: {
                push: jest.fn()
            },
            resetText: jest.fn()
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

    it("should contain an Editor field with the text content value", () => {
        props.text.content = "some content";
        expect(wrapper().find(Editor).props().defaultContent).toBe(props.text.content);
    });

});
