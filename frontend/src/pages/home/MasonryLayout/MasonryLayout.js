import React from 'react';
import MasonryInfiniteScroller from 'react-masonry-infinite';
import TextCard from "../TextCard/TextCard";

export default ({ texts, hasMore, loadMore }) => {

    let sizes = [
        { mq: '375px', columns: 1, gutter: 25 },
        { mq: '768px', columns: 2, gutter: 25 },
        { mq: '1024px', columns: 3, gutter: 25 }
    ];

    return (
        <div id="bricks-layout">
            <MasonryInfiniteScroller hasMore={false} loadMore={() => {}} sizes={sizes}>
                {
                    texts && texts.map(text => <TextCard key={text.id} text={text}/>)
                }
            </MasonryInfiniteScroller>
        </div>
    )
};