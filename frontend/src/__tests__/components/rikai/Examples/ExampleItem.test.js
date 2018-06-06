import React from "react";
import { configure, shallow } from "enzyme";
import Adapter from 'enzyme-adapter-react-15';
import ExampleItem from "../../../../components/rikai/Examples/ExampleItem/ExampleItem";

configure({ adapter: new Adapter() });

describe("ExampleItem", () => {
    let props;
    let shallowExampleItem;

    const wrapper = () => {
        if (!shallowExampleItem) {
            shallowExampleItem = shallow(
                <ExampleItem {...props} />
            );
        }
        return shallowExampleItem;
    };

    beforeEach(() => {
        props = {
            exampleClassName: undefined,
            example: undefined
        };
        shallowExampleItem = undefined;
    });

    it("should render a div", () => {
        expect(wrapper().find(".example").length).toBe(1);
    });

    it("should include the example provided", () => {
        props.example = "foo bar example";
        expect(wrapper().find("p").text()).toEqual("foo bar example");
    });

});
