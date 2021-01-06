import React from 'react';
import "./practice.css";
import Header from "../`common/header/Header";
import {withRouter} from "react-router-dom";
import colors from "../../style/colorConstants";
import axios from "axios";
import {studyApiUrl} from "../../AppUrl";
import {connect} from "react-redux";


const mapStateToProps = (state) => ({
    deck: state.decks.deck
});

const mapDispatchToProps = (dispatch) => ({
    getDeck: (id) => {
        dispatch({
            type: 'GET_DECK',
            payload: axios.get(studyApiUrl + '/api/deck/' + id)
        });
    }
});

class Practice extends React.Component {

    constructor(props) {
        super(props);
        props.getDeck('5f5dd1d8ee0d5515e49d9abc')

        this.setClientPositions();

        this.noOfVisibleItems = 3;
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

            return <div className={"card stack__item " + (index == 0 ? "stack__item--current" : "")}
                        style={stackItemStyle} key={bgColors[index] + index}>
                    <div className="card-inner">
                        <li className="card-front" style={{backgroundColor: bgColors[index]}}>
                            <h1>{card.kanji}</h1>
                            <h3>{card.kana}</h3>
                            <h6>{card.repetitions + " repetitions"}</h6>
                        </li>
                        <li className="card-back" style={{backgroundColor: bgColors[index]}}>
                            <h2>{card.explanation}</h2>
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
    }

    reject() {
        this.performAnimation("reject");
    }

    star() {
        this.performAnimation("star");
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

    render() {
        if (!this.props.deck) {
            return <div/>
        }

        return (
            <div id="practice-page" className="text-center">
                <div id="app-header">
                    <Header leftIcon="menu" centerText={"Practice - " + this.props.deck.name}/>
                </div>
                <div className="content color-3">
                    <ul id="stack_yomi" className="stack stack--yomi"
                        onTouchStart={this.handleTouchStart.bind(this)}
                        onTouchEnd={this.handleTouchEnd.bind(this)}
                        onTouchMove={this.handleTouchMove.bind(this)}>
                        {this.getStackItems(this.props.deck.cards)}
                    </ul>
                </div>
            </div>
        );
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Practice));
