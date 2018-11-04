import React from 'react';
import ReactBricks from "react-bricks-infinite";

export default ({ bricks, reRender }) => (
    <ReactBricks
        reRender={reRender}
        bricks={bricks}
        sizes={[
            {columns: 4, gutter: 35},
            { mq: '375px', columns: 1, gutter: 35 },
            { mq: '768px', columns: 2, gutter: 35 },
            { mq: '1024px', columns: 3, gutter: 35 }
        ]}
    />
)