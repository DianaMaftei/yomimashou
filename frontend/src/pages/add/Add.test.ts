import { shallow } from 'enzyme';
import ActionButtons from './ActionButtons';
import Add from './Add';
import Tags from './Tags';
import Text from './text/Text';


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

    it("should contain an ActionButtons component", () => {
        expect(wrapper().find(ActionButtons)).toHaveLength(1);
    });

});
