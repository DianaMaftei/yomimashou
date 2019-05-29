import React from 'react';
import LocalOffer from 'mdi-react/LocalOfferIcon';
import FavoriteBorder from 'mdi-react/FavoriteBorderIcon';
import TextStatus from "./TextStatus";

export default ({ text, status }) => (
    <div className="text-card-bottom">
        <span className="favorite-icon">
            <FavoriteBorder fontSize="small"/>
        </span>
        {status && <TextStatus status={status}/>}
        {text.tags.length > 0 &&
            <span>
                <LocalOffer fontSize="small" className="tag-icon"/>
                <span className="tag-list">
                    {text.tags.join(", ")}
                </span>
            </span>
        }
    </div>
)