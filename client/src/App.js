import React from 'react';
import {connect} from 'react-redux';

import AddYomimono from './containers/AddYomimono';
import ViewYomimono from './containers/ViewYomimono';

const mapStateToProps = (state) => ({
    text: state.yomi.text
});

class App extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            rikaiResult: {}
        };

        this.updateRikaiResult = this.updateRikaiResult.bind(this);
    }

    updateRikaiResult(rikaiResult) {
        this.setState(
            {rikaiResult: rikaiResult}
        );
    }

    render() {
        let currentPage = '';
        if (!this.props.text) {
            currentPage = <AddYomimono/>;
        } else {
            currentPage = <ViewYomimono rikaiResult={this.state.rikaiResult} updateRikaiResult={this.updateRikaiResult}/>;
        }

        return (
            <div>
                <div id="app-header">Yomimashou!</div>
                {currentPage}
            </div>
        );
    }
}

export default connect(mapStateToProps, null)(App);
