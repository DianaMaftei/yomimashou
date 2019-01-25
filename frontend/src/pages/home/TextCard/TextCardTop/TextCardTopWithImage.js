import React from 'react';
import CardMedia from "@material-ui/core/CardMedia/CardMedia";

export default (text) => (
    <div className="text-card-top">
        <CardMedia
            style={{ height: 160 }}
            component="img"
            image={require("../../pics/ghost-cat.jpg")}
            title="Contemplative Reptile"
        />
    </div>
);