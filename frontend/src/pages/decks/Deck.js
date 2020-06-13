import React from 'react';
import {connect} from "react-redux";
import {withRouter} from 'react-router-dom'
import axios from "axios";
import {studyApiUrl} from "../../AppUrl";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableBody from "@material-ui/core/TableBody";
import Link from "react-router-dom/Link";
import {withStyles} from "@material-ui/core";
import TableCell from "@material-ui/core/TableCell";

const mapStateToProps = (state) => ({
    deck: state.decks.deck
});

const mapDispatchToProps = (dispatch) => ({
    getDeck: (deckId) => {
        dispatch({
            type: 'GET_DECK',
            payload: axios.get(studyApiUrl + '/api/deck/' + deckId)
        });
    }
});

class Deck extends React.Component {

    constructor(props) {
        super(props);

        const {id} = this.props.match.params;

        this.state = {
            deckId: id
        };
        props.getDeck(id);
    }

    delete(cardId) {
        axios.post(studyApiUrl + '/api/deck/' + this.state.deckId + '/card/delete/' + cardId)
            .then(() => {
                this.props.getDeck(this.state.deckId);
            });
    }

    render() {
        let deck = this.props.deck;

        if (!deck) {
            return <div/>;
        }

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
            <div id="deck-page" className="text-center">
                <h1>{deck.name}</h1>
                <div className="text-center">
                    <Table className="w-75 p-3 mx-auto" aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <StyledTableCell>Kanji</StyledTableCell>
                                <StyledTableCell>Kana</StyledTableCell>
                                <StyledTableCell>Explanation</StyledTableCell>
                                <StyledTableCell>Actions</StyledTableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {deck && deck.cards && deck.cards.map((card) => (
                                <StyledTableRow key={card.id}>
                                    <StyledTableCell component="th" scope="row">
                                        {card.kanji}
                                    </StyledTableCell>
                                    <StyledTableCell component="th" scope="row">
                                        {card.kana}
                                    </StyledTableCell>
                                    <StyledTableCell component="th" scope="row">
                                        {card.explanation}
                                    </StyledTableCell>
                                    <StyledTableCell>
                                        <button className="btn btn-danger" onClick={() => this.delete(card.id)}>Delete
                                        </button>
                                    </StyledTableCell>
                                </StyledTableRow>
                            ))}
                        </TableBody>
                    </Table>
                </div>
                <br/>
                <br/>
                <br/>
                <button>
                    <Link to={"/decks"}>Back</Link>
                </button>

            </div>
        );
    }
}

export default withRouter(
    connect(mapStateToProps, mapDispatchToProps)(Deck));