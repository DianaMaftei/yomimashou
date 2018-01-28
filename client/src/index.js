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
import { AppContainer } from 'react-hot-loader';

// const store = createStore(reducer, applyMiddleware(logger, promiseMiddleware()));
const store = createStore(reducer, composeWithDevTools(applyMiddleware(promiseMiddleware())));

const render = Component => {
    ReactDOM.render(
        <AppContainer>
            <Provider store={store}>
                <Component/>
            </Provider>
        </AppContainer>,
        document.getElementById('root'));
};

registerServiceWorker();

render(App);

if (module.hot) {
    module.hot.accept('./App', () => {
        render(App);
    });
}



