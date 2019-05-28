import React from "react";

const rtkInfo = ({keyword, components, story1, story2}) => {
    return (
        <div className="kanji-rtk">
            <h6>RTK info</h6>
            <div>{keyword && 'Keyword: ' + keyword}</div>
            <div>{components && 'Components: ' + components}</div>
            <br/>
            <div>{story1 && 'Story 1: ' + story1}</div>
            <br/>
            <div>{story2 && 'Story 2: ' + story2}</div>
            <br/>
        </div>
    );
};
export default rtkInfo;