import React from 'react';
import { connect } from "react-redux";
import { withRouter } from 'react-router-dom'
import axios from "axios";
import { studyApiUrl } from "../../AppUrl";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableBody from "@material-ui/core/TableBody";
import { withStyles } from "@material-ui/core";
import TableCell from "@material-ui/core/TableCell";
import Header from "../../components/header/Header";
import "./decks.css";
import colors from "../../style/colorConstants";

const mapStateToProps = (state) => ({
    deck: state.decks.deck,
    cards: state.decks.cardsInDeck
});

const mapDispatchToProps = (dispatch) => ({
    getDeck: (deckId) => {
        dispatch({
            type: 'GET_DECK',
            payload: axios.get(studyApiUrl + '/api/study/deck/' + deckId)
        });
    },
    getCardsInDeck: (deckId) => {
        dispatch({
            type: 'GET_CARDS_IN_DECK',
            payload: axios.get(studyApiUrl + '/api/study/card?deckId=' + deckId)
        });
    },
});

class EditDeck extends React.Component {

    constructor(props) {
        super(props);

        const {id} = this.props.match.params;

        this.state = {
            deckId: id,
            isDesktop: false
        };
        props.getDeck(id);
        props.getCardsInDeck(id);
        this.updatePredicate = this.updatePredicate.bind(this);
    }

    componentDidMount() {
        this.updatePredicate();
        window.addEventListener("resize", this.updatePredicate);
    }

    componentWillUnmount() {
        window.removeEventListener("resize", this.updatePredicate);
    }

    updatePredicate() {
        this.setState({ isDesktop: window.innerWidth > 1450 });
    }

    delete(cardId) {
        axios.delete(studyApiUrl + '/api/study/card/' + cardId)
            .then(() => {
                this.props.getCardsInDeck(this.state.deckId);
            });
    }

    handleBackClick() {
        this.props.history.push('/decks');
    }

    render() {
        const deck = this.props.deck;
        const cards = this.props.cards;
        const isDesktop = this.state.isDesktop;

        if (!deck) {
            return <div/>;
        }

        const StyledTableRow = withStyles((theme) => ({
            root: {
                '&:nth-of-type(odd)': {
                    backgroundColor: colors.yomiGray200,
                },
            },
        }))(TableRow);

        const StyledTableCell = withStyles((theme) => ({
            head: {
                backgroundColor: colors.yomiDarkBlue,
                color: theme.palette.common.white,
                fontWeight: "bold"
            },
            body: {
                fontSize: 14,
                padding: "10px 5px"
            },
        }))(TableCell);

        return (
            <div id="deck-page">
                <div id="app-header">
                    <Header leftIcon="back" centerText={deck.name} onLeftIconClick={this.handleBackClick.bind(this)}/>
                </div>
                <div className="decks-center p-4 mx-auto">
                    <Table className="mx-auto" aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <StyledTableCell padding="checkbox" align="center">Kanji</StyledTableCell>
                                <StyledTableCell padding="checkbox" align="center">Kana</StyledTableCell>
                                <StyledTableCell padding="checkbox" align="center">Explanation</StyledTableCell>
                                {/*{isDesktop && */}
                                <StyledTableCell align="center">Actions</StyledTableCell>
                                {/*}*/}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {cards && cards.map((card) => (
                                <StyledTableRow key={card.id}>
                                    <StyledTableCell component="th" scope="row" padding="checkbox" align="center">
                                        {card.kanji}
                                    </StyledTableCell>
                                    <StyledTableCell component="th" scope="row" padding="checkbox" align="center">
                                        {card.kana}
                                    </StyledTableCell>
                                    <StyledTableCell component="th" scope="row" padding="checkbox" align="center">
                                        {card.explanation}
                                    </StyledTableCell>
                                    {/*{isDesktop && (*/}
                                        <StyledTableCell padding="checkbox" align="center">
                                            <button className="btn btn-danger"
                                                    onClick={() => this.delete(card.id)}>Delete
                                            </button>
                                        </StyledTableCell>
                                         {/*)}*/}
                                </StyledTableRow>
                            ))}
                        </TableBody>
                    </Table>
                </div>
                <br/>
                <br/>
                <br/>
            </div>
        );
    }
}

export default withRouter(
    connect(mapStateToProps, mapDispatchToProps)(EditDeck));
