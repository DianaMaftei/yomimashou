import React from "react";
import { shallow } from "enzyme";
import '../../__mocks__/xhr-mock.js';
import '../../__mocks__/LocalStorageMock.js';
import { Read } from "./Read";
import { Link } from "react-router-dom";

describe("Read", () => {
    let props;
    let shallowRead;

    const wrapper = () => {
        if (!shallowRead) {
            shallowRead = shallow(
                <Read {...props} />
            );
        }
        return shallowRead;
    };

    beforeEach(() => {
        props = {
            text: undefined,
            resetText: undefined
        };
        shallowRead = undefined;
    });

    it("should render Read page component", () => {
        expect(wrapper().find("#read-page").length).toBe(1);
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
