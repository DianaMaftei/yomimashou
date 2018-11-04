import React from 'react';
import LocalOffer from '@material-ui/icons/LocalOffer';
import FavoriteBorder from '@material-ui/icons/FavoriteBorder';

export default ({text}) => (
    <div className="text-card-bottom">
        <span>
            <LocalOffer fontSize="small" className="tag-icon"/>
            <span className="tag-list">
                {text.tags.join(", ")}
            </span>
        </span>
        <span className="favorite-icon">
            <FavoriteBorder fontSize="small"/>
        </span>
    </div>
)