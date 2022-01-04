import Adapter from '@wojtekmaj/enzyme-adapter-react-17';
import Enzyme from 'enzyme';


window.HTMLCanvasElement.prototype.getContext = jest.fn();

Object.defineProperty(document, 'currentScript', {
    value: document.createElement('script'),
});

Enzyme.configure({ adapter: new Adapter() });
