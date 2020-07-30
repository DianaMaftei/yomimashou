import React from 'react';
import TextStatus from "./TextStatus";
import MaterialIcon from "material-icons-react";

export default ({text, status}) => (
    <div className="text-card-bottom">
        <span className="favorite-icon">
            {/*TODO replace icon with full one if text was favorited*/}
            {/*<MaterialIcon icon="favorite_border" color="#DA3603" size="24"/>*/}
            {/*<span>{text.favoriteCounter}</span>*/}
        </span>
        {status && <TextStatus status={status}/>}
        {text.tags.length > 0 &&
        <span className="text-tags">
                <span className="tag-icon">
                    <MaterialIcon icon="label_outline" color="#202020" size="24"/>
                </span>

                <span className="tag-list">
                    {text.tags.join(", ")}
                </span>
            </span>
        }
    </div>
)