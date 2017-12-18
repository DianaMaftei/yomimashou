import React from 'react';

import AddYomimono from './components/AddYomimono';
import ViewYomimono from './components/ViewYomimono';

class App extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            rikaiResult: {},
            text: null
        };

        this.handleTextChange = this.handleTextChange.bind(this);
        this.updateRikaiResult = this.updateRikaiResult.bind(this);
        this.resetText = this.resetText.bind(this);
    }

    handleTextChange(event) {
        this.setState({text: event.target.value});
    }

    resetText() {
        this.setState({text: null});
    }

    updateRikaiResult(rikaiResult) {
        this.setState(
            {rikaiResult: rikaiResult}
        );
    }

    render() {

        let currentPage = '';
        if (!this.state.text) {
            currentPage = <AddYomimono handleTextChange={this.handleTextChange}/>;
        } else {
            currentPage = <ViewYomimono text={this.state.text} reset={this.resetText} rikaiResult={this.state.rikaiResult} updateRikaiResult={this.updateRikaiResult}/>;
        }

        return (
            <div>
                <div id="app-header">Yomimashou!</div>
                {currentPage}
            </div>
        );
    }
}

export default App;