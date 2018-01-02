import React from 'react';
import {connect} from 'react-redux';
import YomiText from './YomiText';
import {resetText} from '../actions/index';
import '../style/rikai.css';

const mapDispatchToProps = (dispatch) => ({
    resetText: () => {
        dispatch(resetText());
    }
});

class ViewYomimono extends React.Component {
    render() {
        return (
            <div>
                <div id="user-options">
                    <h4>Things you can do:</h4>
                    <ul>
                        <li>Hover over the text to detect words in the dictionary and click to see the
                            definition.
                        </li>
                        <li>Press the 'S' key to switch between available dictionaries (words / kanji).</li>
                        <li>Click 'Ex' on word definitions to see example sentences.</li>
                    </ul>
                </div>
                <YomiText/>
                <div id="reset-btn">
                    <button onClick={this.props.resetText}> Try a different text</button>
                </div>
            </div>
        )
    }
}

export default connect(null, mapDispatchToProps)(ViewYomimono);