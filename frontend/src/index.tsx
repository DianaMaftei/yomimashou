import 'normalize.css';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import Drawer, { closeDrawer } from './components/drawer/Drawer';
import registerServiceWorker from './registerServiceWorker';
import Routes from './Routes';
import state from './State';
import './style/index.scss';


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

if(module.hot) {
    module.hot.accept();
}
