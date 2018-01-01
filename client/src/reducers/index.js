import {combineReducers} from 'redux';
import yomi from './yomi';
import popUp from './popUp';
import config from './config'

const reducer = combineReducers({
    yomi,
    popUp,
    config
});

export default reducer;