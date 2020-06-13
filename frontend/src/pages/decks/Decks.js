import React from 'react';
import {connect} from "react-redux";
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import {withStyles} from '@material-ui/core/styles';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import {withRouter} from 'react-router-dom'
import axios from "axios";
import {studyApiUrl} from "../../AppUrl";
import Link from "react-router-dom/Link";

const mapStateToProps = (state) => ({
    decks: state.decks.decks
});

const mapDispatchToProps = (dispatch) => ({
    getDecks: () => {
        dispatch({
            type: 'GET_DECKS',
            payload: axios.get(studyApiUrl + '/api/deck')
        });
    }
});

class Decks extends React.Component {

    constructor(props) {
        super(props);
        props.getDecks();
    }

    edit(deckId) {
        this.props.history.push('/deck/' + deckId);
    }

    delete(deckId) {
        axios.delete(studyApiUrl + '/api/deck/' + deckId)
            .then(() => {
                this.props.getDecks();
            });
    }

    render() {
        if (!this.props.decks) {
            return <div/>;
        }

        let decks = this.props.decks.content;

        const StyledTableRow = withStyles((theme) => ({
            root: {
                '&:nth-of-type(odd)': {
                    backgroundColor: theme.palette.action.hover,
                },
            },
        }))(TableRow);

        const StyledTableCell = withStyles((theme) => ({
            head: {
                backgroundColor: theme.palette.common.black,
                color: theme.palette.common.white,
            },
            body: {
                fontSize: 14,
            },
        }))(TableCell);

        return (
            <div id="decks-page" className="text-center">
                <h1>My decks</h1>
                <div className="text-center">
                    <Table className="w-75 p-3 mx-auto" aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <StyledTableCell>Deck name</StyledTableCell>
                                <StyledTableCell align="right">Actions</StyledTableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {decks.map((deck) => (
                                <StyledTableRow key={deck.id}>
                                    <StyledTableCell component="th" scope="row">
                                        {deck.name}
                                    </StyledTableCell>
                                    <StyledTableCell align="right">
                                        <button className="btn btn-secondary" onClick={() => this.edit(deck.id)}>View
                                        </button>
                                        <button className="btn btn-danger" onClick={() => this.delete(deck.id)}>Delete
                                        </button>
                                    </StyledTableCell>
                                </StyledTableRow>
                            ))}
                        </TableBody>
                    </Table>
                </div>
            </div>
        );
    }
}

export default withRouter(
    connect(mapStateToProps, mapDispatchToProps)(Decks));