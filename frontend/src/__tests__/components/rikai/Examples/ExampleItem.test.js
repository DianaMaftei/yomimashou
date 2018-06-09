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
        props.example = {};
        expect(wrapper().find(".example").length).toBe(1);
    });

    it("should include the example provided", () => {
        props.example = {id: 0, sentence: "sentence example", meaning: "meaning example"};
        expect(wrapper().find("p").first().text()).toEqual("sentence example");
        expect(wrapper().find("p").last().text()).toEqual("meaning example");
    });

});
