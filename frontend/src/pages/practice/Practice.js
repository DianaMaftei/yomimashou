import React from 'react';
import "./practice.css";
import Header from "../../components/header/Header";
import {withRouter} from "react-router-dom";
import colors from "../../style/colorConstants";
import axios from "axios";
import {studyApiUrl} from "../../AppUrl";
import {connect} from "react-redux";
import Summary from "./summary/Summary";


const mapStateToProps = (state) => ({
    deck: state.decks.deck,
    showSummary: state.study.showSummary,
    cardsDue: state.study.cardsDue
});

const mapDispatchToProps = (dispatch) => ({
    getDeck: (id) => {
        dispatch({
            type: 'GET_DECK',
            payload: axios.get(studyApiUrl + '/api/study/deck/' + id)
        });
    },
    getCardsDue: (deckId) => {
        dispatch({
            type: 'GET_DUE_CARDS',
            payload: axios.get(studyApiUrl + '/api/study/card/due/?deckId=' + deckId)
        });
    },
    toggleSummary: () => {
        dispatch({
            type: 'TOGGLE_SUMMARY'
        });
    },
    reviewCard: (cardId, easeOfAnswer) => {
        dispatch({
            type: 'REVIEW_CARD',
            payload: axios.post(studyApiUrl + '/api/study/card/review', null, { params: {
                    cardId,
                    easeOfAnswer
        }})
        });
    }
});

class Practice extends React.Component {

    constructor(props) {
        super(props);
        const {id} = this.props.match.params;

        props.getDeck(id);
        props.getCardsDue(id);

        this.setClientPositions();

        this.noOfVisibleItems = 3;
        this.currentCardId = null;

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

    setClientPositions() {
        this.clientStartX = 0;
        this.clientStartY = 0;
        this.clientMoveX = 0;
        this.clientMoveY = 0;
    }

    getBackgroundColors(totalCardsNo) {
        const bgColors = [colors.yomiBlue800, colors.yomiBlue600, colors.yomiBlue400];
        const times = Math.ceil(totalCardsNo / bgColors.length)

        const makeRepeated = (arr, repeats) =>
            [].concat(...Array.from({length: repeats}, () => arr));

        return makeRepeated(bgColors, times)
    }

    getStackItems(cards) {
        const bgColors = this.getBackgroundColors(cards.length);

        return cards.map((card, index) => {
            const stackItemStyle = {
                opacity: index < this.noOfVisibleItems ? 1 : 0,
                pointerEvents: 'auto',
                zIndex: index === 0 ? parseInt(this.noOfVisibleItems + 1) : parseInt(this.noOfVisibleItems - index),
                transform: 'translate3d(0px, 0px, ' + parseInt(-1 * 50 * index) + 'px)'
            }

            return <div className={"card stack__item " + (index === 0 ? "stack__item--current" : "")}
                        style={stackItemStyle} key={card.id} data-cardid={card.id}>
                    <div className="card-inner">
                        <li className="card-front" style={{backgroundColor: bgColors[index]}}>
                            <h1>{card.kanji}</h1>
                            <h3>{card.kana}</h3>
                            <h6>{card.repetitions + " repetitions"}</h6>
                        </li>
                        <li className="card-back" style={{backgroundColor: bgColors[index]}}>
                            <h3>{card.explanation}</h3>
                        </li>
                        </div>
                    </div>
        })
    }

    performAnimation(action) {
        let foundCurrent = false;
        let backItemsIndex = 0;
        const stackItems = document.getElementById("stack_yomi").children;
        for (let i = 0; i < stackItems.length; i++) {
            let stackItem = stackItems[i];
            if (!foundCurrent && stackItem.classList.contains("stack__item--current")) {
                let classList = "stack__item stack__item--" + action;
                if (stackItem.classList.contains("flip")) {
                    classList += " flip";
                }
                stackItem.classList = classList;
                setTimeout(function () {
                    stackItem.classList = "stack__item";
                    stackItem.style.opacity = 0;
                }, 500);

                foundCurrent = true;
                this.currentCardId = stackItem.getAttribute("data-cardid");
                let that = this;
                if(i === stackItems.length - 1) {
                    setTimeout(function(){
                        that.summary.show = true;
                        that.props.toggleSummary();
                    }, 800);
                }
            } else if (foundCurrent) {

                if (backItemsIndex === 0) {
                    stackItem.classList = "stack__item stack__item--current";
                }
                const that = this;
                setTimeout(function () {
                    stackItem.style.zIndex = backItemsIndex === 0 ? parseInt(that.noOfVisibleItems + 1) : parseInt(that.noOfVisibleItems - backItemsIndex);
                    stackItem.style.transition = 'transform 0.5s ease-out';
                    stackItem.style.transform = 'translate3d(0px, 0px, ' + parseInt(-1 * 50 * backItemsIndex) + 'px)';
                    backItemsIndex++;
                    if (backItemsIndex <= that.noOfVisibleItems) {
                        stackItem.style.opacity = 1;
                    }
                }, 500);

            }
        }
    }

    accept() {
        this.performAnimation("accept");
        this.props.reviewCard(this.currentCardId, this.ANSWER_KNOW);
        this.summary.know += 1;
    }

    reject() {
        this.performAnimation("reject");
        this.props.reviewCard(this.currentCardId, this.ANSWER_DONT_KNOW);
        this.summary.doNotKnow += 1;
    }

    star() {
        this.performAnimation("star");
        this.props.reviewCard(this.currentCardId, this.ANSWER_KNOW_WELL);
        this.summary.knowWell += 1;
    }

    flip() {
        const stackItems = document.getElementById("stack_yomi").children;

        for (let i = 0; i < stackItems.length; i++) {
            if (stackItems[i].classList.contains("stack__item--current")) {
                let classList = stackItems[i].classList;
                if(classList.contains("flip")) {
                    classList.remove("flip");
                } else {
                    classList.add("flip");
                }
                break;
            }
        }
    }

    handleTouchStart(touchStartEvent) {
        this.clientStartX = touchStartEvent.touches[0].clientX;
        this.clientStartY = touchStartEvent.touches[0].clientY;
    }

    handleTouchMove(touchMoveEvent) {
        this.clientMoveX = touchMoveEvent.touches[0].clientX;
        this.clientMoveY = touchMoveEvent.touches[0].clientY;
    }

    handleTouchEnd(touchEndEvent) {
        let xMovement = this.clientMoveX - this.clientStartX;
        let yMovement = this.clientMoveY - this.clientStartY;

        if (this.userHasTapped(touchEndEvent)) {
            this.flip();
        }

        // don't perform animation for any small gesture
        if (Math.max(Math.abs(xMovement), Math.abs(yMovement)) < 50) {
            return;
        }

        const movedHorizontally = Math.abs(xMovement) > Math.abs(yMovement);
        if ((!movedHorizontally) && yMovement < 0 && this.clientMoveY !== 0) {
            this.star();
        } else if (movedHorizontally && xMovement > 0 && this.clientMoveX !== 0) {
            this.accept();
        } else if (movedHorizontally && xMovement < 0 && this.clientMoveX !== 0) {
            this.reject();
        }

        this.setClientPositions();
    }

    userHasTapped(touchEndEvent) {
        const lastXPosition = touchEndEvent.changedTouches[touchEndEvent.changedTouches.length - 1].clientX;
        const lastYPosition = touchEndEvent.changedTouches[touchEndEvent.changedTouches.length - 1].clientY;

        return (this.clientStartX === lastXPosition) && (this.clientStartY === lastYPosition);
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
                        onTouchStart={this.handleTouchStart.bind(this)}
                        onTouchEnd={this.handleTouchEnd.bind(this)}
                        onTouchMove={this.handleTouchMove.bind(this)}>
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
