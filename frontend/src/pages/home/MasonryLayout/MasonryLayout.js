import React from 'react';
import MasonryInfiniteScroller from 'react-masonry-infinite';
import TextCard from "../TextCard/TextCard";
import * as PropTypes from "prop-types";

const MasonryLayout = ({ texts, hasMore, loadMore, textsStatuses}) => {

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

MasonryLayout.propTypes = {
    texts: PropTypes.arrayOf(PropTypes.object).isRequired,
    hasMore: PropTypes.bool,
    loadMore: PropTypes.func,
    textsStatuses: PropTypes.arrayOf(PropTypes.string).isRequired
};

export default MasonryLayout;
