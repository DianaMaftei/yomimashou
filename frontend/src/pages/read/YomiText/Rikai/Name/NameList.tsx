import React from "react";
import NameListFull from "./NameListFull";
import NameListLimited from "./NameListLimited";

class NameList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            limit: true
        };
    }

    componentWillUpdate(nextProps, nextState) {
        if (this.props.resultList !== nextProps.resultList) {
            this.setState({ limit: true });
        }
    }

    showMoreResults() {
        this.setState({ limit: false });
    }

    render() {
        if (!this.props.resultList) {
            return <div/>;
        }
        else if (!this.state.limit || this.props.resultList.length < this.props.limit) {
            return NameListFull(this.props.resultList)
        }
        else if (this.state.limit) {
            return NameListLimited(this.props.resultList, this.props.limit, this.showMoreResults.bind(this))
        }
    }
}

export default NameList;
