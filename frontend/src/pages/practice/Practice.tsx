import React from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import Header from '../../components/header/Header';
import { getDeckAction } from '../decks/decksActions';
import './practice.scss';
import { getDueCardsAction, reviewCardAction, toggleSummaryAction } from './practiceActions';
import { getBackgroundColors, handleTouchEnd, handleTouchMove, handleTouchStart, performAnimation, setClientPositions } from './PracticeAnimation';
import StackCard from './StackCard';
import Summary from './summary/Summary';


const mapStateToProps = (state) => ({
    deck: state.decks.deck,
    showSummary: state.study.showSummary,
    cardsDue: state.study.cardsDue
});

const mapDispatchToProps = (dispatch) => ({
    getDeck: (id) => dispatch(getDeckAction(id)),
    getCardsDue: (deckId) => dispatch(getDueCardsAction(deckId)),
    toggleSummary: () => dispatch(toggleSummaryAction()),
    reviewCard: (cardId, easeOfAnswer) => dispatch(reviewCardAction(cardId, easeOfAnswer))
});

class Practice extends React.Component {

    constructor(props) {
        super(props);
        const {id} = this.props.match.params;

        props.getDeck(id);
        props.getCardsDue(id);

        this.review = {
            client: setClientPositions(),
            currentCardId: null
        }

        this.noOfVisibleItems = 3;
        this.ANSWER_DONT_KNOW = 1
        this.ANSWER_KNOW = 2
        this.ANSWER_KNOW_WELL = 3

        this.initSummary();
    }

    initSummary() {
        this.summary = {
            show: false,
            know: 0,
            knowWell: 0,
            doNotKnow: 0
        }
    }

    getStackItems(cards) {
        const bgColors = getBackgroundColors(cards.length);

        return cards.map((card, index) => {
            const stackItemStyle = {
                opacity: index < this.noOfVisibleItems ? 1 : 0,
                pointerEvents: 'auto',
                zIndex: index === 0 ? parseInt(this.noOfVisibleItems + 1) : parseInt(this.noOfVisibleItems - index),
                transform: 'translate3d(0px, 0px, ' + parseInt(-1 * 50 * index) + 'px)'
            }

            return <StackCard key={card.id} index={index} style={stackItemStyle} card={card} bgColors={bgColors}/>
        })
    }

    accept() {
        performAnimation("accept", this.review, this.summary, this.props.toggleSummary.bind(this), this.noOfVisibleItems);
        this.props.reviewCard(this.review.currentCardId, this.ANSWER_KNOW);
        this.summary.know += 1;
    }

    reject() {
        performAnimation("reject", this.review, this.summary, this.props.toggleSummary.bind(this), this.noOfVisibleItems);
        this.props.reviewCard(this.review.currentCardId, this.ANSWER_DONT_KNOW);
        this.summary.doNotKnow += 1;
    }

    star() {
        performAnimation("star", this.review, this.summary, this.props.toggleSummary.bind(this), this.noOfVisibleItems);
        this.props.reviewCard(this.review.currentCardId, this.ANSWER_KNOW_WELL);
        this.summary.knowWell += 1;
    }

    practiceMore() {
        this.props.toggleSummary();
        this.initSummary();
        this.props.getCardsDue(this.props.match.params.id);
    }

    render() {
        if (!this.props.deck) {
            return <div/>
        }

        return (
            <div id="practice-page" className="text-center">
                <div id="app-header">
                    <Header leftIcon="menu" centerText={"Practice - " + this.props.deck.name} fontSize={32}/>
                </div>
                {!this.summary.show && this.props.cardsDue && this.props.cardsDue.length !== 0 &&
                <div className="content color-3">
                    <ul id="stack_yomi" className="stack stack--yomi"
                        onTouchStart={e => handleTouchStart(this.review.client, e)}
                        onTouchEnd={e => handleTouchEnd(this.review, e, this.star.bind(this), this.accept.bind(this), this.reject.bind(this))}
                        onTouchMove={e => handleTouchMove(this.review.client, e)}>
                        {this.getStackItems(this.props.cardsDue)}
                    </ul>
                </div>}
                {!this.props.cardsDue || this.props.cardsDue.length === 0 && <h3>You have no cards due</h3>}
                <Summary summary={this.summary} practiceMore={this.practiceMore.bind(this)}/>
            </div>
        );
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Practice));
