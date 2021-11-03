import React from 'react';
import "./practice.scss";
import colors from "../../style/colorConstants";

export const setClientPositions = () => {
    return {
        clientStartX: 0,
        clientStartY: 0,
        clientMoveX: 0,
        clientMoveY: 0
    }
}

export const getBackgroundColors = (totalCardsNo) => {
    const bgColors = [colors.yomiBlue800, colors.yomiBlue600, colors.yomiBlue400];
    const times = Math.ceil(totalCardsNo / bgColors.length)
    const makeRepeated = (arr, repeats) => [].concat(...Array.from({length: repeats}, () => arr));
    return makeRepeated(bgColors, times)
}

export const flip = () => {
    const stackItems = document.getElementById("stack_yomi").children;

    for (let i = 0; i < stackItems.length; i++) {
        if (stackItems[i].classList.contains("stack__item--current")) {
            let classList = stackItems[i].classList;
            if (classList.contains("flip")) {
                classList.remove("flip");
            } else {
                classList.add("flip");
            }
            break;
        }
    }
}

export const handleTouchStart = (client, touchStartEvent) => {
    client.clientStartX = touchStartEvent.touches[0].clientX;
    client.clientStartY = touchStartEvent.touches[0].clientY;
}

export const handleTouchMove = (client, touchMoveEvent) => {
    client.clientMoveX = touchMoveEvent.touches[0].clientX;
    client.clientMoveY = touchMoveEvent.touches[0].clientY;
}

const userHasTapped = (client, touchEndEvent) => {
    const lastXPosition = touchEndEvent.changedTouches[touchEndEvent.changedTouches.length - 1].clientX;
    const lastYPosition = touchEndEvent.changedTouches[touchEndEvent.changedTouches.length - 1].clientY;

    return (client.clientStartX === lastXPosition) && (client.clientStartY === lastYPosition);
}

export const handleTouchEnd = (review, touchEndEvent, star, accept, reject) => {
    let xMovement = review.client.clientMoveX - review.client.clientStartX;
    let yMovement = review.client.clientMoveY - review.client.clientStartY;

    if (userHasTapped(review.client, touchEndEvent)) {
        flip();
        return;
    }

    // don't perform animation for any small gesture
    if (Math.max(Math.abs(xMovement), Math.abs(yMovement)) < 50) {
        return;
    }

    const movedHorizontally = Math.abs(xMovement) > Math.abs(yMovement);
    if ((!movedHorizontally) && yMovement < 0 && review.client.clientMoveY !== 0) {
        star();
    } else if (movedHorizontally && xMovement > 0 && review.client.clientMoveX !== 0) {
        accept();
    } else if (movedHorizontally && xMovement < 0 && review.client.clientMoveX !== 0) {
        reject();
    }

    review.client = setClientPositions();
}

export const performAnimation = (action, review, summary, toggleSummary, noOfVisibleItems) => {
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
            review.currentCardId = stackItem.getAttribute("data-cardid");
            if (i === stackItems.length - 1) {
                setTimeout(function () {
                    summary.show = true;
                    toggleSummary();
                }, 800);
            }
        } else if (foundCurrent) {
            if (backItemsIndex === 0) {
                stackItem.classList = "stack__item stack__item--current";
            }
            setTimeout(function () {
                stackItem.style.zIndex = backItemsIndex === 0 ? parseInt(noOfVisibleItems + 1) : parseInt(noOfVisibleItems - backItemsIndex);
                stackItem.style.transition = 'transform 0.5s ease-out';
                stackItem.style.transform = 'translate3d(0px, 0px, ' + parseInt(-1 * 50 * backItemsIndex) + 'px)';
                backItemsIndex++;
                if (backItemsIndex <= noOfVisibleItems) {
                    stackItem.style.opacity = 1;
                }
            }, 500);

        }
    }
}



