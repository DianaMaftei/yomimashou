import React from 'react';
import {connect} from 'react-redux';

import AddYomimono from './containers/AddYomimono';
import ViewYomimono from './containers/ViewYomimono';

const mapStateToProps = (state) => ({
    text: state.yomi.text
});

class App extends React.Component {

    render() {
        let currentPage = '';
        if (!this.props.text) {
            currentPage = <AddYomimono/>;
        } else {
            currentPage = <ViewYomimono/>;
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
