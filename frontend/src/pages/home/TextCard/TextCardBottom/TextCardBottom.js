import React from 'react';
import TextStatus from "./TextStatus";
import LabelOutlineIcon from 'mdi-react/LabelOutlineIcon';
import FavoriteBorderIcon from 'mdi-react/FavoriteBorderIcon';

export default ({text, status}) => (
    <div className="text-card-bottom">
        {/*<span className="favorite-icon">*/}
            {/*TODO replace icon with full one if text was favorited*/}
            {/*<FavoriteBorderIcon color="#DA3603" size="24"/>*/}
            {/*<span>{text.favoriteCounter}</span>*/}
        {/*</span>*/}

        {/*TODO only show if text read*/}
        {/*{status && <TextStatus status={status}/>}*/}

        {text.tags.length > 0 &&
        <span className="text-tags">
                <span className="tag-icon">
                    <LabelOutlineIcon color="#202020" size="24"/>
                </span>

                <span className="tag-list">
                    {text.tags.join(", ")}
                </span>
            </span>
        }
    </div>
)
