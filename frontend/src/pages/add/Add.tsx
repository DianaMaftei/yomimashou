import React, {useEffect} from 'react';
import axios from "axios/index";
import {apiUrl} from "../../AppUrl";
import {useDispatch, useSelector} from "react-redux";
import {withRouter} from 'react-router-dom'
import Tags from "./Tags";
import Text from "./text/Text";
import Header from "../../components/header/Header";
import ActionButton from "../../components/buttons/actionBtn/ActionButton";
import "./add.scss";
import {
    resetTextAction,
    setTagInputAction,
    setTextContentAction,
    setTextTagsAction,
    setTextTitleAction,
    togglePlaceholderAction
} from "./addActions";

const stripRubyAndFormatting = html => {
    let doc = new DOMParser().parseFromString(html, "text/html");

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

    for (var c = a.childNodes, i = c.length; i--;) {
        if (c[i].nodeType === 1) return true;
    }

    return false;
}

const disableAddBtn = (text) => {
    let textHasNoTitle = !text.title || text.title.length === 0;
    let textHasNoContent = !text.content || text.content.trim().length === 0;
    return textHasNoTitle || textHasNoContent;
}

const addTag = (text, tagInput, setTags, setTagInput) => {
    if (!text.tags) {
        text.tags = [];
    }
    if (tagInput && tagInput.trim().length > 0) {
        text.tags.push(tagInput);
        setTags(text.tags);
        setTagInput("");
    }
}

const deleteTag = (index, setTextTags, text) => {
    text.tags.splice(index, 1);
    setTextTags(text.tags);
}

const submitText = (setText, text, textImage, history) => {
    const token = localStorage.getItem('token');

    let doc = new DOMParser().parseFromString(text.content, "text/html");
    doc.body.innerHTML = doc.body.innerHTML.replace(/<\/p><p/g, '</p>\n<p');
    let newtext = doc.body.innerText.replace(/\n/g, '<br>');
    setText(newtext);

    text.content = newtext;

    let data = new FormData();
    data.append('file', textImage);
    data.append('text', new Blob([JSON.stringify(text)], {type: "application/json"}));

    axios.post(apiUrl + '/api/text', data, {headers: {Authorization: token}}).then((response) => {
        history.push('/read/' + response.data.id);
    });
}

const removePlaceholder = (showTextPlaceholder, toggleTextPlaceholder, setTextContent) => {
    if (showTextPlaceholder) {
        setTextContent('');
        toggleTextPlaceholder();
    }
}

const Add = ({history}) => {
    const dispatch = useDispatch();
    const text = useSelector(state => state.add.text);
    const tagInput = useSelector(state => state.add.tagInput);
    const textImage = useSelector(state => state.add.textImage);
    const showTextPlaceholder = useSelector(state => state.add.showTextPlaceholder);

    useEffect(() => {
        let username = localStorage.getItem('username');
        if (!username) {
            history.push('/');
        }
    }, []);

    useEffect(() => dispatch(resetTextAction()), [dispatch])

    const setTextContent = (content) => dispatch(setTextContentAction(isHTML(content) ? stripRubyAndFormatting(content) : content));
    const setTextTags = (tags) => dispatch(setTextTagsAction(tags));
    const setTagInput = (tag) => dispatch(setTagInputAction(tag));
    const toggleTextPlaceholder = (showTextPlaceholder) => dispatch(togglePlaceholderAction(showTextPlaceholder));

    return (
        <div id="add-page">
            <div id="app-header">
                <Header leftIcon="menu" centerText="Add new text" fontSize={32}/>
            </div>
            <div className="add-text-container">
                <Text title={text.title} setTitle={e => dispatch(setTextTitleAction(e.target.value))}
                      removePlaceholder={() => removePlaceholder(showTextPlaceholder, toggleTextPlaceholder, setTextContent)}
                      setText={setTextContent} text={text.content}/>

                <div className="add-action-footer">
                    <Tags tagInput={tagInput} updateTag={e => setTagInput(e.target.value)}
                          addTag={() => addTag(text, tagInput, setTextTags, setTagInput)} tags={text.tags}
                          deleteTag={(index) => deleteTag(index, setTextTags, text)}/>

                    <ActionButton disabled={disableAddBtn(text)}
                                  onClick={() => submitText(setTextContent, text, textImage, history)}
                                  label="Add & Read"/>
                </div>
            </div>
        </div>
    );
}

export default withRouter(Add);
