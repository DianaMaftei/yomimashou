import { History } from 'history';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { withRouter } from 'react-router-dom';
import ActionButton from '../../components/buttons/actionBtn/ActionButton';
import Header from '../../components/header/Header';
import { Text } from '../../model/Text';
import { createText } from '../../service/TextService';
import './add.scss';
import {
    resetTextAction,
    setTagInputAction,
    setTextContentAction,
    setTextTagsAction,
    setTextTitleAction,
    togglePlaceholderAction
} from './addActions';
import Tags from './Tags';
import TextContainer from './text/Text';


const stripRubyAndFormatting = (html: string) => {
    let doc = new DOMParser().parseFromString(html, 'text/html');

    let rubyStart = /(?=<ruby)(.*?)(?:><rb>)/g;
    let rubyEnd = /(<\/rb><rt)(.*?)(<\/ruby>)/g;
    let text = doc.body.innerHTML.replace(rubyStart, '');
    text = text.replace(rubyEnd, '');

    text = text.replace(/<\/p><p/g, '</p>\n<p');

    return text.replace(/\n/g, '<br>');
};

const isHTML = (str: string) => {
    var a = document.createElement('div');
    a.innerHTML = str;

    for(var c = a.childNodes, i = c.length; i--;) {
        if(c[i].nodeType === 1) {
            return true;
        }
    }

    return false;
};

const disableAddBtn = (text: Text) => {
    let textHasNoTitle = !text.title || text.title.length === 0;
    let textHasNoContent = !text.content || text.content.trim().length === 0;
    return textHasNoTitle || textHasNoContent;
};

const addTag = (text: Text, tagInput: string, setTags: (tags: string[]) => void, setTagInput: (tag: string) => void) => {
    if(!text.tags) {
        text.tags = [];
    }
    if(tagInput && tagInput.trim().length > 0) {
        text.tags.push(tagInput);
        setTags(text.tags);
        setTagInput('');
    }
};

const deleteTag = (index: number, setTextTags: (tags: string[]) => void, text: Text) => {
    text.tags.splice(index, 1);
    setTextTags(text.tags);
};

const submitText = (setText: (text: string) => void, text: Text, textImage: File, history: History) => {
    let doc = new DOMParser().parseFromString(text.content, 'text/html');
    doc.body.innerHTML = doc.body.innerHTML.replace(/<\/p><p/g, '</p>\n<p');
    let newtext = doc.body.innerText.replace(/\n/g, '<br>');
    setText(newtext);

    text.content = newtext;

    let data = new FormData();
    data.append('file', textImage);
    data.append('text', new Blob([JSON.stringify(text)], {type: 'application/json'}));

    // TODO use action
    createText(data).then((response) => {
        history.push('/read/' + response.data.id);
    });
};

const removePlaceholder = (showTextPlaceholder: boolean, toggleTextPlaceholder: (showTextPlaceholder: boolean) => void, setTextContent: (content: string) => void) => {
    if(showTextPlaceholder) {
        setTextContent('');
        toggleTextPlaceholder(showTextPlaceholder);
    }
};

const Add = ({history}: AddProps) => {
    const dispatch = useDispatch();
    const text = useSelector(state => state.add.text);
    const tagInput = useSelector(state => state.add.tagInput);
    const textImage = useSelector(state => state.add.textImage);
    const showTextPlaceholder = useSelector(state => state.add.showTextPlaceholder);

    useEffect(() => {
        let username = localStorage.getItem('username');
        if(!username) {
            history.push('/');
        }
    }, []);

    useEffect(() => dispatch(resetTextAction()), [dispatch]);

    const setTextContent = (content: string) => dispatch(setTextContentAction(isHTML(content) ? stripRubyAndFormatting(content) : content));
    const setTextTags = (tags: string[]) => dispatch(setTextTagsAction(tags));
    const setTagInput = (tag: string) => dispatch(setTagInputAction(tag));
    const toggleTextPlaceholder = (showTextPlaceholder: boolean) => dispatch(togglePlaceholderAction(showTextPlaceholder));

    return (
        <div id="add-page">
            <div id="app-header">
                <Header leftIcon="menu" centerText="Add new text" fontSize={32}/>
            </div>
            <div className="add-text-container">
                <TextContainer title={text.title} setTitle={e => dispatch(setTextTitleAction(e.target.value))}
                               removePlaceholder={() => removePlaceholder(showTextPlaceholder, toggleTextPlaceholder, setTextContent)}
                               setText={setTextContent} text={text.content}/>

                <div className="add-action-footer">
                    <Tags tagInput={tagInput} updateTag={e => setTagInput(e.target.value)}
                          addTag={() => addTag(text, tagInput, setTextTags, setTagInput)} tags={text.tags}
                          deleteTag={(index: number) => deleteTag(index, setTextTags, text)}/>

                    <ActionButton disabled={disableAddBtn(text)}
                                  onClick={() => submitText(setTextContent, text, textImage, history)}
                                  label="Add & Read"/>
                </div>
            </div>
        </div>
    );
};

type AddProps = {
    history: History
}

export default withRouter(Add);
