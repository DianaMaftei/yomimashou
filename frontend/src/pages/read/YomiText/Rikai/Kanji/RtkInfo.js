import React from "react";
import BackButton from "../../../../../components/buttons/backBtn/BackButton";
import colors from "../../../../../style/colorConstants";
import PopupType from "../PopupType";

const rtkInfo = ({character, keyword, components, story1, story2, style, changePopup}) => {
    const getHighlightedStory = (story, keyword) => {
        let indexOfKeyword = story.toLowerCase().indexOf(keyword.toLowerCase());
        let keywordCaseInsensitive = story.substr(indexOfKeyword, keyword.length);
        let highlightedStory = story;
        highlightedStory = highlightedStory.replace(keywordCaseInsensitive, '<span style="color:placeholderColor; font-weight: 500">placeholder</span>');
        highlightedStory = highlightedStory.replace('placeholderColor', colors.yomiLightRed);
        highlightedStory = highlightedStory.replace('placeholder', keywordCaseInsensitive);
        return highlightedStory;
    }

    return (
        <div id="rikai-window" style={style} className="elevation-lg">
            <div className="rikai-display">
                <div className="kanji-rtk">
                    <div className="kanji-rtk-top">
                        <span onClick={() => changePopup(PopupType.KANJI)}>
                            <BackButton/>
                        </span>
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
export default rtkInfo;
