import React, {useEffect} from 'react';
import {useDispatch, useSelector} from "react-redux";
import YomiText from './YomiText/YomiText';
import "./read.scss";
import Header from "../../components/header/Header";
import {withRouter} from "react-router-dom";
import {
    getTextByIdAction,
    parseTextWordsAction,
    resetDictionariesAction,
    toggleTextActionsMenuAction
} from "./readActions";

const Read = ({match, history}: ReadProps) => {
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
        dispatch(getTextByIdAction(id));
        return () => dispatch(resetDictionariesAction());
    }, [dispatch])

    useEffect(() => {
        if (text.content) {
            dispatch(parseTextWordsAction(text));
            // dispatch(parseTextNamesAction(text));
        }
    }, [text.content]);

    return (
        <div id="read-page">
            <div id="app-header">
                <Header leftIcon="menu" rightIcon="more_vert"
                        onRightIconClick={() => dispatch(toggleTextActionsMenuAction())}/>
            </div>
            <YomiText text={text} id={match.params.id}/>
        </div>
    )
}

type ReadProps = {
    match: object
    history: object
}

export default withRouter(Read);
