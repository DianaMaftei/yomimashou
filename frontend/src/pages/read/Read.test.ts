import { shallow } from 'enzyme';
import '../../__mocks__/LocalStorageMock.js';
import '../../__mocks__/xhr-mock.js';
import { Read } from './Read';


describe("Read", () => {
    let props;
    let shallowRead;

    let historyPush = jest.fn();

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
            resetText: undefined,
            match: {params: {}},
            history: {
                push: historyPush
            }
        };
        shallowRead = undefined;
    });

    it("should render Read page component", () => {
        props.text = {
            content: "some text content",
            tile: "some text title"
        };
        props.getWordsForText = jest.fn();

        expect(wrapper().find("#read-page").length).toBe(1);
    });

    it("should contain a YomiText component", () => {
        props.text = {
            content: "some text content",
            tile: "some text title"
        };
        props.getWordsForText = jest.fn();

        expect(wrapper().find("Connect(YomiText)")).toHaveLength(1);
    });
});
