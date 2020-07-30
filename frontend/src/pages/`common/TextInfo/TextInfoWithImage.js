import React from 'react';
import CardMedia from "@material-ui/core/CardMedia/CardMedia";
import {apiUrl} from "../../../AppUrl";
import MaterialIcon from 'material-icons-react';
import "./TextInfo.css";

export default (text) => (
    <div className="text-info-container">
        <CardMedia
            component="img"
            image={apiUrl + "/api/file/" + text.imageFileName}
            title={text.imageFileName}
        />
        <div></div>
        {/*TODO set filled or outlined : bookmark*/}
        <div className="text-bookmark">
            {/*<MaterialIcon icon="bookmark_border" color="#C33702" size="large" />*/}
        </div>
        <div className="text-info-img-gradient"/>
        <div className="text-info-body">
            <div>
                <div className="text-info-length">
                    <span>{text.excerpt.length}</span>
                    <span>&nbsp;characters</span>
                </div>
                <div className="text-info-date">
                    <span>{text.creationDate}</span>
                </div>
            </div>
        </div>
    </div>
);