import React from 'react';
import CardMedia from "@material-ui/core/CardMedia/CardMedia";
import {apiUrl} from "../../../../AppUrl";

export default (text) => (
    <div className="text-card-top">
        <CardMedia
            style={{height: 160}}
            component="img"
            image={apiUrl + "/api/file/" + text.imageFileName}
            title={text.imageFileName}
        />
    </div>
);