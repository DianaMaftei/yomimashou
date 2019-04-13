import React from "react";
import { shallow } from "enzyme";
import Add from "./Add";
import Text from "./Text";
import Tags from "./Tags";
import Series from "./Series";
import ActionButtons from "./ActionButtons";

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
            tags:[],
            setTitle: jest.fn(),
            setText: jest.fn(),
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

    it("should contain a Text component with the text title value", () => {
        props.text.title = "some content";
        expect(wrapper().find(Text).props().title).toBe(props.text.title);
    });

    it("should contain a Tags component with the text tags value", () => {
        props.text.tags = ["tag1", "tag2"];
        expect(wrapper().find(Tags).props().tags).toBe(props.text.tags);
    });

    it("should contain a Series component", () => {
        expect(wrapper().find(Series)).toHaveLength(1);
    });

    it("should contain an ActionButtons component", () => {
        expect(wrapper().find(ActionButtons)).toHaveLength(1);
    });

});
