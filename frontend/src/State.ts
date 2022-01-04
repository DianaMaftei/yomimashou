import { applyMiddleware, combineReducers, compose, createStore } from 'redux';
import promiseMiddleware from 'redux-promise-middleware';
import add from './pages/add/addReducers';
import imageUpload from './pages/add/image-upload/imageUploadReducers';
import authenticate from './pages/authentication/authenticateReducers';
import decks from './pages/decks/decksReducers';
import home from './pages/home/homeReducers';
import study from './pages/practice/practiceReducers';
import yomiText from './pages/read/readReducers';
import popUp from './pages/read/YomiText/Rikai/popUpReducers';
import config from './reducers/config';
import { actionSanitizer, stateSanitizer } from './reduxDevtoolsConfig';


const composeEnhancers = typeof window === 'object' &&
window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({
    actionSanitizer,
    stateSanitizer
}) : compose;

const store = createStore(combineReducers({
    add,
    imageUpload,
    home,
    yomiText,
    popUp,
    config,
    authenticate,
    decks,
    study
}), composeEnhancers(
    applyMiddleware(promiseMiddleware())
));

const subscribe = store.subscribe;

const State = {
    store,
    subscribe
};

export default State;
