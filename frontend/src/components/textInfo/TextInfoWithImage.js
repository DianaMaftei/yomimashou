import React from 'react';
import CardMedia from "@material-ui/core/CardMedia/CardMedia";
import {apiUrl} from "../../AppUrl";
import BookmarkBorderIcon from 'mdi-react/BookmarkBorderIcon';

export default (text) => (
    <div className="text-info-image-container">
        <CardMedia
            component="img"
            image={apiUrl + "/api/file/" + text.imageFileName}
            title={text.imageFileName}
        />
        {/*TODO set filled or outlined : bookmark*/}
        <div className="text-bookmark">
            {/*<BookmarkBorderIcon color="#C33702" size="50" />*/}
        </div>
        <div className="text-info-line"/>
        <div className="text-info-body">
            <div>
                <div className="text-info-length">
                    <span>{text.characterCount}</span>
                    <span>&nbsp;ch.</span>
                </div>
                <div className="text-info-date">
                    <span>{text.creationDate}</span>
                </div>
            </div>
        </div>
    </div>
);
