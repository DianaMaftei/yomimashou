import React from 'react';
import RikaiPopUp from './rikai/Rikai-pop-up';
import '../style/rikai.css';

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
                        <li>Press 'S' to switch between available dictionaries (words / kanji).</li>
                    </ul>
                </div>
                <div id="text-show">{this.props.text}</div>
                <div id="reset-btn">
                    <button onClick={this.props.reset}> Try a different text</button>
                </div>
                <RikaiPopUp result={this.props.rikaiResult} update={this.props.updateRikaiResult}/>
            </div>
        )
    }
}

export default ViewYomimono;
