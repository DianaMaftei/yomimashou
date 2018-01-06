import React from 'react';
import {connect} from 'react-redux';
import {setText} from '../actions/index';

const mapDispatchToProps = (dispatch) => ({
    setText: text => {
        dispatch(setText(text));
    }
});

class AddYomimono extends React.Component {
    render() {
        return (
            <div>
                <div id="text-box">
                <textarea rows="5" cols="50" onChange={event => this.props.setText(event.target.value)}
                          placeholder="Paste here the Japanese text that you want to read."/>
                </div>
                <h4 id="example-header">Example</h4>
                <p id="example-text">
                    むかしむかし、神さまは十二種類の動物をそれぞれの年の大将にして、この世に誕生したばかりの人間たちの教育係にしようと考えました。
                    <br/>
                    <br/>
                    牛は真面目に働くから、人間たちの仕事を手伝ってくれるだろう。
                    <br/>
                    <br/>
                    よし、十二支にしよう
                    <br/>
                    <br/>
                    トラは力が強くて狩りが上手だから、人間たちに狩りを教えてくれるだろう。
                </p>
            </div>
        )
    }
}

export default connect(null, mapDispatchToProps)(AddYomimono);
