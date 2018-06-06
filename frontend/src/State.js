import { applyMiddleware, combineReducers, compose, createStore } from 'redux';
import popUp from "./reducers/popUp";
import config from "./reducers/config";
import viewYomi from "./containers/ViewYomimono";
import yomiText from "./containers/YomiText";
import promiseMiddleware from 'redux-promise-middleware';

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const store = createStore(combineReducers({
    viewYomi,
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