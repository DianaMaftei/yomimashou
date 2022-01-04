import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { withRouter } from 'react-router-dom';
import Header from '../../components/header/Header';
import './read.scss';
import { getTextByIdAction, parseTextWordsAction, resetDictionariesAction, toggleTextActionsMenuAction } from './readActions';
import YomiText from './YomiText/YomiText';


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
    match: any
    history: History
}

export default withRouter(Read);
