import React from 'react';


export default ({resultList}) => {
    if(!resultList) {
        return <div / >;
    } else if(resultList.length === 0) {
        return <ExampleListWithoutResults/>;
    } else {
        return <ExampleListWithResults resultList={resultList}/>;
    }
};
