import React from "react";
import ReactDOM from "react-dom";
import { Provider } from "react-redux";
import { applyMiddleware, createStore } from "redux";
import { composeWithDevTools } from "redux-devtools-extension/developmentOnly";
import promiseMiddleware from 'redux-promise-middleware';
import reducer from "./reducers";
import "./style/index.css";
import "./style/App.css";

import App from "./App";
import registerServiceWorker from "./registerServiceWorker";

const store = createStore(reducer, composeWithDevTools(applyMiddleware(promiseMiddleware())));

const render = () => {
    ReactDOM.render(
        <Provider store={store}>
            <App/>
        </Provider>,
        document.getElementById('root')
    );
};

registerServiceWorker();
render();

if (module.hot)
    module.hot.accept();