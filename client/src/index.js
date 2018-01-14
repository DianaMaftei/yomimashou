import React from "react";
import ReactDOM from "react-dom";
import {Provider} from "react-redux";
import {applyMiddleware, createStore} from "redux";
import {logger} from "redux-logger";
import {composeWithDevTools} from "redux-devtools-extension/developmentOnly";
import reducer from "./reducers";
import "./style/index.css";
import "./style/App.css";

import App from "./App";
import registerServiceWorker from "./registerServiceWorker";

const store = createStore(reducer);
// const store = createStore(reducer, composeWithDevTools(applyMiddleware(logger)));

ReactDOM.render(
    <Provider store={store}>
        <App />
    </Provider>,
    document.getElementById('root'));
registerServiceWorker();