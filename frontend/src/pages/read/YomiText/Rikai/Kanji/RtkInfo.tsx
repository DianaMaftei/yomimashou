import React from 'react';
import BackButton from '../../../../../components/buttons/backBtn/BackButton';
import colors from '../../../../../style/colorConstants';
import PopupType from '../PopupType';


const getHighlightedStory = (story: string, keyword: string) => {
    let indexOfKeyword = story.toLowerCase().indexOf(keyword.toLowerCase());
    let keywordCaseInsensitive = story.substr(indexOfKeyword, keyword.length);
    let highlightedStory = story;
    highlightedStory = highlightedStory.replace(keywordCaseInsensitive, '<span style="color:initial; font-weight: 500">placeholder</span>');
    highlightedStory = highlightedStory.replace('initial', colors.yomiLightRed);
    highlightedStory = highlightedStory.replace('placeholder', keywordCaseInsensitive);
    return highlightedStory;
}

const RtkInfo = ({character, keyword, components, story1, story2, style, changePopup}: RtkInfoProps) => {

    return (
        <div id="rikai-window" style={style} className="elevation-lg">
            <div className="rikai-display">
                <div className="kanji-rtk">
                    <div className="kanji-rtk-top">
                        <BackButton onClick={() => changePopup(PopupType.KANJI)}/>
                        <span className="kanji-rtk-title">
                            <span className="kanji-rtk-character">{character}</span>
                            <span className="kanji-rtk-keyword">- {keyword}</span>
                        </span>
                    </div>
                    <div className="kanji-rtk-components">
                        {components.split("<br>").map(component => <div key={component}>{component}</div>)}
                    </div>
                    <br/>
                    <ol>
                        <li dangerouslySetInnerHTML={{__html: getHighlightedStory(story1, keyword)}}/>
                        <li dangerouslySetInnerHTML={{__html: getHighlightedStory(story2, keyword)}}/>
                    </ol>
                </div>
            </div>
        </div>
    );
};

type RtkInfoProps = {
    character: string
    keyword: string
    components: string
    story1: string
    story2: string
    style: object
    changePopup: any
}

export default RtkInfo;
