import React from 'react';
import DifficultyLevel from "./DifficultyLevel";

export default (text) => (
    <div className="text-card-top">
        {text.level &&
            <div className="difficulty-bar difficulty-bar-no-image">
                <DifficultyLevel level={text.level}/>
            </div>
        }
    </div>
)