import React from "react";
import {shallow} from "enzyme";
import Text from "./Text";
import Editor from "react-pell";

describe("Text", () => {
    let props;
    let shallowText;

    const wrapper = () => {
        if (!shallowText) {
            shallowText = shallow(
                <Text {...props} />
            );
        }
        return shallowText;
    };

    beforeEach(() => {
        props = {
            editorContent: "",
            title: "",
            setTitle: jest.fn(),
            setText: jest.fn(),
            removePlaceholder: false
        };
        shallowText = undefined;
    });


    it("should render the title field", () => {
        expect(wrapper().find("#title-required")).toHaveLength(1);
    });

    it("should render the upload file button", () => {
        expect(wrapper().find("#outlined-button-file")).toHaveLength(1);
    });

    it("should contain an Editor field with the text content value", () => {
        props.editorContent = "some content";
        expect(wrapper().find(Editor).props().defaultContent).toBe(props.editorContent);
    });
});
