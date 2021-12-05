import React, {useEffect} from 'react';
import {useDispatch, useSelector} from "react-redux";
import {withRouter} from 'react-router-dom'
import axios from "axios";
import {studyApiUrl} from "../../AppUrl";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableBody from "@material-ui/core/TableBody";
import {withStyles} from "@material-ui/core";
import TableCell from "@material-ui/core/TableCell";
import Header from "../../components/header/Header";
import "./decks.scss";
import colors from "../../style/colorConstants";
import ActionButton from "../../components/buttons/actionBtn/ActionButton";
import {getCardsInDeckAction, getDeckAction} from "./decksActions";

const StyledTableRow = withStyles(() => ({
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

const EditDeck = ({match, history}) => {
    const dispatch = useDispatch();
    const deck = useSelector(state => state.decks.deck);
    const cards = useSelector(state => state.decks.cardsInDeck);
    const id = match.params.id;

    const fetchDeck = () => dispatch(getDeckAction(id));

    const fetchCardsInDeck = () => dispatch(getCardsInDeckAction(id));

    useEffect(() => fetchDeck(), [dispatch]);
    useEffect(() => fetchCardsInDeck(), [dispatch]);

    const deleteCard = (cardId) => {
        axios.delete(studyApiUrl + '/api/study/card/' + cardId)
            .then(() => {
                fetchCardsInDeck();
            });
    }

    if (!deck) {
        return <div/>;
    }

    return (
        <div id="deck-page">
            <div id="app-header">
                <Header leftIcon="back" centerText={deck.name} onLeftIconClick={() => history.push('/decks')} fontSize={32}/>
            </div>
            <div className="decks-center p-4 mx-auto">
                <Table className="mx-auto" aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <StyledTableCell padding="checkbox" align="center">Kanji</StyledTableCell>
                            <StyledTableCell padding="checkbox" align="center">Kana</StyledTableCell>
                            <StyledTableCell padding="checkbox" align="center">Explanation</StyledTableCell>
                            <StyledTableCell align="center">Actions</StyledTableCell>
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
                                <StyledTableCell padding="checkbox" align="center">
                                    <ActionButton onClick={() => deleteCard(card.id)} label="Delete" />
                                </StyledTableCell>
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

export default withRouter(EditDeck);
