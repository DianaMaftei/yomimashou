import React from 'react';
import Example from '../ExampleItem/ExampleItem';


const getExamplesFromResultList = (resultList) => {
    return resultList.map((result, i) => <Example
        exampleClassName={(i % 2 === 0) ? 'definition-light' : 'definition-dark'}
        key={result.id + result.sentence} example={result}/>);
};

const ExampleListWithResults = ({resultList}: ExampleListWithResultsProps) => (
    <div id="example-list-with-results">
        {getExamplesFromResultList(resultList)}
    </div>
);

type ExampleListWithResultsProps = {
    resultList: any
}

export default ExampleListWithResults;
