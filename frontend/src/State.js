import { applyMiddleware, combineReducers, compose, createStore } from 'redux';
import popUp from "./reducers/popUp";
import config from "./reducers/config";
import yomiText from "./containers/YomiText";
import promiseMiddleware from 'redux-promise-middleware';
import add from "./pages/add/index";

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const store = createStore(combineReducers({
    add,
    yomiText,
    popUp,
    config
}), composeEnhancers(
    applyMiddleware(promiseMiddleware())
));



const subscribe = store.subscribe;
const getCurrentState = () => store.getState();

export default {
    store,
    subscribe,
    getCurrentState};