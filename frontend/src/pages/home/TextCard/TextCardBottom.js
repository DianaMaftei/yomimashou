import React from 'react';
import LocalOffer from '@material-ui/icons/LocalOffer';
import FavoriteBorder from '@material-ui/icons/FavoriteBorder';

export default ({ text }) => (
    <div className="text-card-bottom">
        <span className="favorite-icon">
            <FavoriteBorder fontSize="small"/>
        </span>
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