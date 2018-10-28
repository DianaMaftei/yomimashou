import React from 'react';
import { Link } from 'react-router-dom';
import { connect } from "react-redux";
import YomiText from '../../containers/YomiText/YomiText';
import "./read.css";

const mapStateToProps = (state) => ({
    text: state.add.text
});

const mapDispatchToProps = (dispatch) => ({
    resetText: result => {
        dispatch({
            type: 'RESET_TEXT'
        });
    }
});

export class Read extends React.Component {

    shouldComponentUpdate(nextProps, nextState) {
        return this.props.text !== nextProps.text;
    };

    render() {
        return (
            <div id="read-page">
                <div id="user-options">
                    <h4>Things you can do:</h4>
                    <ul>
                        <li>Hover over the text to detect words in the dictionary and click to see the
                            definition.
                        </li>
                        <li>Press 'S' to switch between available dictionaries (words / kanji).</li>
                        <li>Click 'Ex' on word definitions to see example sentences.</li>
                    </ul>
                </div>
                <YomiText/>
                <div id="reset-btn">
                    <Link to="/add">
                        <button onClick={this.props.resetText}> Try a different text</button>
                    </Link>
                </div>
            </div>
        )
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Read);