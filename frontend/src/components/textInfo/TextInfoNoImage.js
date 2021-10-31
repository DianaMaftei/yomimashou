import React from 'react';

export default (text) => (
    <div className="text-info-container">
        {/*TODO set filled or outlined : bookmark*/}
        <div className="text-bookmark">
            {/*<MaterialIcon icon="bookmark_border" color="#C33702" size="large" />*/}
        </div>
        <div className="text-info-line"/>
        <div className="text-info-body">
            <div>
                <div className="text-info-length">
                    <span>{text.characterCount}</span>
                    <span>&nbsp;ch.</span>
                </div>
                <span className="text-info-date">
                    {text.creationDate}
                </span>
            </div>
        </div>
    </div>
)