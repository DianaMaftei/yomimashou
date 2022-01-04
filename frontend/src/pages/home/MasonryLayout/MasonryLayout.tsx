import React from 'react';
import MasonryInfiniteScroller from 'react-masonry-infinite';
import TextCard from '../TextCard/TextCard';


const MasonryLayout = ({ texts, hasMore, loadMore, textsStatuses}: MasonryLayoutProps) => {

    let sizes = [
        { mq: '375px', columns: 1, gutter: 25 },
        { mq: '768px', columns: 2, gutter: 25 },
        { mq: '1024px', columns: 3, gutter: 25 }
    ];

    return (
        <div id="bricks-layout">
            <MasonryInfiniteScroller hasMore={hasMore} loadMore={() => loadMore} sizes={sizes}>
                {
                    texts && texts.map(text => <TextCard key={text.id} text={text} status={textsStatuses[text.id]}/>)
                }
            </MasonryInfiniteScroller>
        </div>
    )
};

type MasonryLayoutProps = {
    texts: Array<object>
    hasMore?: boolean
    loadMore?: any
    textsStatuses: object
}

export default MasonryLayout;
