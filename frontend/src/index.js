import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter } from "react-router-dom";
import { Provider } from "react-redux";
import "./style/index.css";
import registerServiceWorker from "./registerServiceWorker";
import state from "./State";
import axios from "axios";
import ButtonAppBar from "./pages/`common/header/index";
import Routes from "./Routes";

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';

const render = () => {
    ReactDOM.render(
        <Provider store={state.store}>
            <BrowserRouter>
                <div id="app-container">
                    <div id="app-header">
                        <ButtonAppBar/>
                    </div>
                    <Routes/>
                </div>
            </BrowserRouter>
        </Provider>,
        document.getElementById('root')
    );
};

registerServiceWorker();
render();

if (module.hot)
    module.hot.accept();