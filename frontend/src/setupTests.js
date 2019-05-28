import Enzyme from 'enzyme'
import Adapter from 'enzyme-adapter-react-16'

window.HTMLCanvasElement.prototype.getContext = jest.fn();

Object.defineProperty(document, 'currentScript', {
    value: document.createElement('script'),
});

Enzyme.configure({ adapter: new Adapter() });