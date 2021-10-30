import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter } from "react-router-dom";
import { Provider } from "react-redux";
import 'normalize.css';
import "./style/index.css";
import registerServiceWorker from "./registerServiceWorker";
import state from "./State";
import axios from "axios";
import Routes from "./Routes";
import Drawer, { closeDrawer } from "./components/drawer/Drawer";

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';

const render = () => {
    ReactDOM.render(
        <Provider store={state.store}>
            <BrowserRouter>
                <div id="router-container">
                    <Drawer/>
                    <div id="app-container" onClick={closeDrawer}>
                        <div id="container-overlay"/>
                        <Routes/>
                    </div>
                </div>
            </BrowserRouter>
        </Provider>,
        document.getElementById('root')
    );
};

registerServiceWorker();
render();

if (module.hot) {
    module.hot.accept();
}
