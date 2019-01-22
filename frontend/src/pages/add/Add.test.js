import React from "react";
import { shallow } from "enzyme";
import { Add } from "./Add";
import { Link } from "react-router-dom/umd/react-router-dom";

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
        expect(wrapper().find(Link).props().to).toBe('/read');
    });

    it("should contain a text adding field component", () => {
        expect(wrapper().find("#add-text-field")).toHaveLength(1);
    });
    
});
