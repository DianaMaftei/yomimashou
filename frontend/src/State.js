import { applyMiddleware, combineReducers, compose, createStore } from 'redux';
import popUp from "./reducers/popUp";
import config from "./reducers/config";
import yomiText from "./pages/read/YomiText";
import promiseMiddleware from 'redux-promise-middleware';
import add from "./pages/add/index";
import home from "./pages/home/reducers";
import decks from "./pages/decks";
import login from "./pages/login/loginReducers";
import study from "./pages/practice";

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const store = createStore(combineReducers({
    add,
    home,
    yomiText,
    popUp,
    config,
    login,
    decks,
    study
}), composeEnhancers(
    applyMiddleware(promiseMiddleware())
));


const subscribe = store.subscribe;

export default {
    store,
    subscribe
};