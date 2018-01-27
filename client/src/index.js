import React from "react";
import ReactDOM from "react-dom";
import {Provider} from "react-redux";
import {applyMiddleware, createStore} from "redux";
import {logger} from "redux-logger";
import {composeWithDevTools} from "redux-devtools-extension/developmentOnly";
import promiseMiddleware from 'redux-promise-middleware';
import reducer from "./reducers";
import "./style/index.css";
import "./style/App.css";

import App from "./App";
import registerServiceWorker from "./registerServiceWorker";

const store = createStore(reducer, applyMiddleware(promiseMiddleware()));
// const store = createStore(reducer, composeWithDevTools(applyMiddleware(logger, promiseMiddleware)));

ReactDOM.render(
    <Provider store={store}>
        <App />
    </Provider>,
    document.getElementById('root'));
registerServiceWorker();