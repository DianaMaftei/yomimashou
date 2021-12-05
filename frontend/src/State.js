import {applyMiddleware, combineReducers, compose, createStore} from 'redux';
import popUp from "./reducers/popUp";
import config from "./reducers/config";
import yomiText from "./pages/read/YomiText/readReducers";
import promiseMiddleware from 'redux-promise-middleware';
import add from "./pages/add/addReducers";
import imageUpload from "./pages/add/image-upload/imageUploadReducers";
import home from "./pages/home/homeReducers";
import decks from "./pages/decks";
import authenticate from "./pages/authentication/authenticateReducers";
import study from "./pages/practice/practiceReducers";
import {actionSanitizer, stateSanitizer} from "./reduxDevtoolsConfig";

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
