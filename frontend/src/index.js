import React from 'react';
import ReactDOM from 'react-dom';
import { createBrowserHistory } from 'history';
import { ConnectedRouter, connectRouter, routerMiddleware } from 'connected-react-router';
import { Provider } from 'react-redux';
import { applyMiddleware, createStore } from 'redux';
import { composeWithDevTools } from 'redux-devtools-extension/developmentOnly';
import promiseMiddleware from 'redux-promise-middleware';
import reducer from './reducers';
import './style/index.css';
import { Route, Switch } from 'react-router-dom';
import registerServiceWorker from './registerServiceWorker';
import AddYomimono from './containers/AddYomimono';
import ViewYomimono from './containers/ViewYomimono';

const history = createBrowserHistory();
const store = createStore(connectRouter(history)(reducer), composeWithDevTools(applyMiddleware(routerMiddleware(history), promiseMiddleware())));

const render = () => {
    ReactDOM.render(
        <Provider store={store}>
            <ConnectedRouter history={history}>
                <div>
                    <div id="app-header">Yomimashou!</div>
                    <Switch>
                        <Route exact path="/" component={AddYomimono}/>
                        <Route path="/add" component={AddYomimono}/>
                        <Route path="/view" component={ViewYomimono}/>
                    </Switch>
                </div>
            </ConnectedRouter>
        </Provider>,
        document.getElementById('root')
    );
};

registerServiceWorker();
render();

if (module.hot)
    module.hot.accept();