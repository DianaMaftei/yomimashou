import React, {useEffect} from 'react';
import {useDispatch, useSelector} from "react-redux";
import YomiText from './YomiText/YomiText';
import "./read.scss";
import axios from "axios/index";
import {apiUrl} from "../../AppUrl";
import Header from "../../components/header/Header";
import {withRouter} from "react-router-dom";

const Read = ({match, history}) => {
    const {id} = match.params;
    
    if (!id) {
        history.push('/')
    }
    
    const dispatch = useDispatch();
    const text = useSelector(state => state.yomiText.text);

    // if(isAuthenticated()) {
    //     axios.post(apiUrl + '/api/users/textStatus?progressStatus=OPEN&textId=' +id, {}, withHeaders());
    // }

    useEffect(() => {
        dispatch({
            type: 'GET_TEXT_BY_ID',
            payload: axios.get(apiUrl + '/api/text/' + id)
        });

        return () => dispatch({type: 'RESET_DICTIONARIES'});
    }, [dispatch])

    useEffect(() => {
        if (text.content) {
            dispatch({
                type: 'PARSE_TEXT_WORDS',
                payload: axios.post(apiUrl + '/api/text/parse/words', text.title + " " + text.content.replace(/<br>/g, ""))
            })
            // dispatch({
            //     type: 'PARSE_TEXT_NAMES',
            //     payload: axios.post(apiUrl + '/api/text/parse/names', text.title + " " + text.content.replace(/<br>/g, ""))
            // })
        }
    }, [text.content]);

    return (
        <div id="read-page">
            <div id="app-header">
                <Header leftIcon="menu" rightIcon="more_vert"
                        onRightIconClick={() => dispatch({type: 'TOGGLE_TEXT_ACTIONS_MENU'})}/>
            </div>
            <YomiText text={text} id={match.params.id}/>
        </div>
    )
}

export default withRouter(Read);
