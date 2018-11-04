import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import { Provider } from "react-redux";
import "./style/index.css";
import registerServiceWorker from "./registerServiceWorker";
import state from "./State";
import { Register } from "./components/user/Register/Register";
import { Login } from "./components/user/Login/Login";
import ButtonAppBar from "./pages/`common/header/ButtonAppBar";
import Home from "./pages/home/Home";
import Add from "./pages/add/Add";
import axios from "axios";
import Read from "./pages/read/Read";

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';

const render = () => {
    ReactDOM.render(
        <Provider store={state.store}>
            <BrowserRouter>
                <div id="app-container">
                    <div id="app-header">
                        <ButtonAppBar/>
                    </div>
                    <Switch>
                        <Route exact path="/" component={() => <Home/>}/>
                        <Route path="/add" component={() => <Add/>}/>
                        <Route path="/view" component={() => <Read/>}/>
                        <Route path="/register" component={() => <Register/>}/>
                        <Route path="/login" component={() => <Login/>}/>
                    </Switch>
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