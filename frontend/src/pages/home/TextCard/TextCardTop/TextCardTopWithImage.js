import React from 'react';
import DifficultyLevel from "./DifficultyLevel";
import CardMedia from "@material-ui/core/es/CardMedia/CardMedia";

export default (text) => (
    <div className="text-card-top">
        <CardMedia
            style={{ height: 160 }}
            component="img"
            image={require("../../pics/ghost-cat.jpg")}
            title="Contemplative Reptile"
        />
        <div className="difficulty-bar">
            <DifficultyLevel level={text.level}/>
        </div>
    </div>
);