import React from "react";
import { configure, shallow } from "enzyme";
import Adapter from 'enzyme-adapter-react-15';
import '../../../__mocks__/xhr-mock.js';
import { ViewYomimono } from "../../../containers/ViewYomimono/ViewYomimono";
import { Link } from "react-router-dom";

configure({ adapter: new Adapter() });

describe("ViewYomimono", () => {
    let props;
    let shallowViewYomimono;

    const wrapper = () => {
        if (!shallowViewYomimono) {
            shallowViewYomimono = shallow(
                <ViewYomimono {...props} />
            );
        }
        return shallowViewYomimono;
    };

    beforeEach(() => {
        props = {
            text: undefined,
            resetText: undefined
        };
        shallowViewYomimono = undefined;
    });

    it("should render ViewYomimono component", () => {
        expect(wrapper().find("#view-yomimono").length).toBe(1);
    });

    it("should contain a YomiText component", () => {
        expect(wrapper().find("Connect(YomiText)")).toHaveLength(1);
    });

    it("should contain a Link component leading to /add", () => {
        expect(wrapper().find(Link).props().to).toBe('/add');
    });

    it("should call resetText when reset-btn is clicked", () => {
        props.text = "Some text goes here";
        let mockResetText = jest.fn(() => {
        });
        props.resetText = mockResetText;

        wrapper().find("#reset-btn button").simulate('click');

        expect(mockResetText.mock.calls.length).toBe(1);
    });
});
