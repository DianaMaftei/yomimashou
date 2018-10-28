import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import { Provider } from "react-redux";
import "./style/index.css";
import registerServiceWorker from "./registerServiceWorker";
import state from "./State";
import AddYomimono from "./containers/AddYomimono/AddYomimono";
import ViewYomimono from "./containers/ViewYomimono/ViewYomimono";
import { Register } from "./components/user/Register/Register";
import { Login } from "./components/user/Login/Login";

const render = () => {
    ReactDOM.render(
        <Provider store={state.store}>
            <BrowserRouter>
                <div>
                    <div id="app-header">Yomimashou!</div>
                    <Switch>
                        <Route exact path="/" component={() => <AddYomimono/>}/>
                        <Route path="/add" component={() => <AddYomimono/>}/>
                        <Route path="/view" component={() => <ViewYomimono/>}/>
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