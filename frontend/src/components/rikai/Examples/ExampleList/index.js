import React from "react";
import ExampleListWithoutResults from "./ExampleListWithoutResults";
import ExampleListWithResults from "./ExampleListWithResults";

export default ({ resultList }) => {
    if (!resultList) return <div/>;
    else if (resultList.length === 0) return ExampleListWithoutResults();
    else return ExampleListWithResults(resultList);
};