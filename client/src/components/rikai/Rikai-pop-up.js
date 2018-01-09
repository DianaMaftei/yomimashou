import React, {Component} from "react";
import WordList from "./WordList";
import NameList from "./NameList";
import Kanji from "./Kanji";
import ExampleList from "./ExampleList";

import "../../style/rikai.css";

class RikaiPopUp extends Component {

    render() {
        if (!this.props.result) return <div></div>;

        let popUp = null;
        let title = '';

        if (this.props.result.type === 'words') {
            title = 'Word Dictionary';
            popUp = <WordList resultList={this.props.result.resultList} limit={this.props.limit}
                              showExamples={this.props.showExamples}/>;
        } else if (this.props.result.type === 'kanji') {
            title = 'Kanji Dictionary';
            popUp = <Kanji result={this.props.result.resultList[0]}/>;
        } else if (this.props.result.type === 'names') {
            title = 'Names Dictionary';
            popUp = <NameList resultList={this.props.result.resultList} limit={this.props.limit}/>;
        } else if (this.props.result.type === 'examples') {
            title = 'Examples';
            popUp = <ExampleList resultList={this.props.result.resultList}/>;
        }

        return (
            <div id="rikai-window">
                <div className="rikai-top">
                    <span className="rikai-title">{title}</span>
                    <span className="closeBtn" onClick={this.props.hidePopup}>&#x2716;</span>
                </div>
                <div className="rikai-display">
                    {popUp}
                </div>
            </div>
        );
    }
}

export default RikaiPopUp;