import React from 'react';
import { Link } from 'react-router-dom';
import { connect } from "react-redux";

const mapStateToProps = (state) => ({
    text: state.viewYomi.text
});

const mapDispatchToProps = (dispatch) => ({
    setText: text => {
        dispatch({
            type: 'SET_TEXT',
            text
        });
    }
});

export class AddYomimono extends React.Component {

    shouldComponentUpdate(nextProps, nextState) {
        return this.props.text !== nextProps.text;
    };

    render() {
        return (
            <div id="add-yomimono">
                <div id="text-box">
                    <textarea rows="5" cols="50"
                              onChange={event => this.props.setText(event.target.value)}
                              value={this.props.text || ''}
                              placeholder="Paste here the Japanese text that you want to read."/>
                    <br/>
                    <Link to={this.props.text ? "/view" : "/add"}>
                        <button className="btn btn-info"> Start Reading</button>
                    </Link>
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

export default connect(mapStateToProps, mapDispatchToProps)(AddYomimono);