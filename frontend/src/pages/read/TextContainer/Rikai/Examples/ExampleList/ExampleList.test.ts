import { shallow } from 'enzyme';
import ExampleList from './index';


describe("ExampleList", () => {
    let props;
    let shallowExampleList;

    const wrapper = () => {
        if (!shallowExampleList) {
            shallowExampleList = shallow(
                <ExampleList {...props} />
            );
        }
        return shallowExampleList;
    };

    beforeEach(() => {
        props = {
            resultList: undefined
        };
        shallowExampleList = undefined;
    });

    it("should render an empty div when no example list is provided", () => {
        expect(wrapper().find("div").children().length).toBe(0);
    });

    it("should render ExampleListWithoutResults when example list is empty", () => {
        props.resultList = [];
        expect(wrapper().find("#example-list-without-results").length).toBe(1);
    });

    it("should render ExampleListWithResults when example list includes at least one item", () => {
        props.resultList = [{ foo: "bar" }];
        expect(wrapper().find("#example-list-with-results").length).toBe(1);
    });

});
