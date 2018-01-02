import React from "react";
import {connect} from 'react-redux';
import {updateSearchResult} from '../actions/index';
import {updateShowResult} from '../actions/index';
import {popUpSetVisibility} from '../actions/index';
import RikaiDict from "../components/rikai/RikaiDictionary";
import Rikai from "../components/rikai/Rikai";
import RikaiPopUp from '../components/rikai/Rikai-pop-up';
import getTextFromRange from "../util/textParser";

const mapStateToProps = (state) => ({
    text: state.yomi.text,
    searchResult: state.popUp.searchResult,
    showResult: state.popUp.showResult,
    limit: state.config.popUp.limit
});

const mapDispatchToProps = (dispatch) => ({
    updateSearchResult: result => {
        dispatch(updateSearchResult(result));
    },
    updateShowResult: result => {
        dispatch(updateShowResult(result));
    },
    togglePopupVisibility: visibility => {
        dispatch(popUpSetVisibility(visibility));
    }
});

class YomiText extends React.Component {
    constructor(props) {
        super(props);

        this.lastPos = {x: null, y: null};
        this.lastTarget = null;
        this.defaultDict = 2;
        this.keysDown = [];
        this.nextDict = 3;
        this.showMode = 0;

        this.config = {};
        this.config.highlight = 'true';
        this.config.popupDelay = 0;
        this.config.disablekeys = 'false';
        this.config.limit = 5;

        // Hack because SelEnd can't be sent in messages
        this.lastSelEnd = [];

        this.searchResult = {
            type: "words"
        };

        // Hack because ro was coming out always 0 for some reason.
        this.lastRo = 0;

        this.onMouseMove = this.onMouseMove.bind(this);
        this.onMouseClick = this.onMouseClick.bind(this);
        this.onKeyDown = this.onKeyDown.bind(this);
        this.onKeyUp = this.onKeyUp.bind(this);

        this.tryUpdatePopup = this.tryUpdatePopup.bind(this);
        this.isVisible = this.isVisible.bind(this);
        this.hidePopup = this.hidePopup.bind(this);
        this.showPopup = this.showPopup.bind(this);

        this.enable();
    }

    onKeyDown(ev) {
        if ((ev.shiftKey) && (ev.keyCode !== 16)) return;
        if (this.keysDown[ev.keyCode]) return;
        if (!this.isVisible()) return;

        if (window.rikaiR.config.disablekeys === 'true' && (ev.keyCode !== 16)) return;

        let i;

        switch (ev.keyCode) {
            case 83:	// s - switch dictionaries (kanji, names, words)
                this.show(ev.currentTarget.rikaiR, this.nextDict);
                this.props.updateShowResult(this.props.searchResult);
                this.showPopup(window.rikaiR.prevTarget);
                break;
            case 81:	// q - hide popup
                this.hidePopup();
                break;
            case 66:	// b - previous character
                let ofs = ev.currentTarget.rikaiR.uofs;
                for (i = 50; i > 0; --i) {
                    ev.currentTarget.rikaiR.uofs = --ofs;
                    if (this.show(ev.currentTarget.rikaiR, this.defaultDict) >= 0) {
                        if (ofs >= ev.currentTarget.rikaiR.uofs) break;	// ! change later
                    }
                }
                this.props.updateShowResult(this.props.searchResult);
                this.showPopup(window.rikaiR.prevTarget);
                break;
            case 77:	// m - next character
                ev.currentTarget.rikaiR.uofsNext = 1;

            // eslint-disable-next-line
            case 78:	// n - next word
                for (i = 50; i > 0; --i) {
                    ev.currentTarget.rikaiR.uofs += ev.currentTarget.rikaiR.uofsNext;
                    if (this.show(ev.currentTarget.rikaiR, this.defaultDict) >= 0) break;
                }
                this.props.updateShowResult(this.props.searchResult);
                this.showPopup(window.rikaiR.prevTarget);
                break;
            default:
                return;
        }

        this.keysDown[ev.keyCode] = 1;

        // don't eat shift
        ev.preventDefault();
    }

    onKeyUp(ev) {
        if (this.keysDown[ev.keyCode]) this.keysDown[ev.keyCode] = 0;
    }

    //Add the listeners and configure app
    enable() {
        if (window.rikaiR === null || window.rikaiR === undefined) {
            window.rikaiR = {};

            window.rikaiR.config = this.config;

            window.addEventListener('mousemove', this.onMouseMove, false);
            window.addEventListener('click', this.onMouseClick, false);
            window.addEventListener('keydown', this.onKeyDown, true);
            window.addEventListener('keyup', this.onKeyUp, true);

            this.dict = new RikaiDict(this.config, this.props.updateSearchResult);

            this.Rikai = new Rikai(this.dict);
        }
    }

    //Removes the listeners and deletes the element
    disable() {
        if (window.rikaiR !== null) {
            let e;
            window.removeEventListener('mousemove', this.onMouseMove, false);
            window.removeEventListener('click', this.onMouseClick, false);
            window.removeEventListener('keydown', this.onKeyDown, true);
            window.removeEventListener('keyup', this.onKeyUp, true);

            e = document.getElementById('rikai-window');
            if (e) e.parentNode.removeChild(e);

            this.clearHi();
            delete window.rikaiR;
        }
    }

    clearHi() {
        let tdata = window.rikaiR;
        if ((!tdata) || (!tdata.prevSelView)) return;
        if (tdata.prevSelView.closed) {
            tdata.prevSelView = null;
            return;
        }

        let sel = tdata.prevSelView.getSelection();
        // If there is an empty selection or the selection was done by
        // rikaikun then we'll clear it
        if ((!sel.toString()) || (tdata.selText === sel.toString())) {
            // In the case of no selection we clear the oldTA
            // The reason for this is becasue if there's no selection
            // we probably clicked somewhere else and we don't want to
            // bounce back.
            if (!sel.toString())
                tdata.oldTA = null;

            // clear all selections
            sel.removeAllRanges();
            //Text area stuff
            // If oldTA is still around that means we had a highlighted region
            // which we just cleared and now we're going to jump back to where we were
            // the cursor was before our lookup
            // if oldCaret is less than 0 it means we clicked outside the box and shouldn't
            // come back
            if (tdata.oldTA && tdata.oldCaret >= 0) {
                tdata.oldTA.selectionStart = tdata.oldTA.selectionEnd = tdata.oldCaret;

            }

        }
        tdata.prevSelView = null;
        tdata.kanjiChar = null;
        tdata.selText = null;
    }

    processEntry(e) {
        let tdata = window.rikaiR;
        let ro = this.lastRo;
        let selEndList = this.lastSelEnd;

        if (!e) {
            // this.hidePopup();
            this.clearHi();
            return -1;
        }
        this.lastFound = [e];

        if (!e.matchLen) e.matchLen = 1;
        tdata.uofsNext = e.matchLen;
        tdata.uofs = (ro - tdata.prevRangeOfs);

        let rp = tdata.prevRangeNode;

        if ((rp) && ((tdata.config.highlight === 'true' && !this.mDown && !('form' in tdata.prevTarget)) || (('form' in tdata.prevTarget) && tdata.config.textboxhl === 'true'))) {
            let doc = rp.ownerDocument;
            if (!doc) {
                this.clearHi();
                // this.hidePopup();
                return 0;
            }
            this.highlightMatch(doc, rp, ro, e.matchLen, selEndList, tdata);
            this.elemPosition = document.getSelection().getRangeAt(0).getClientRects()[0];
            tdata.prevSelView = doc.defaultView;
        }

        this.result = this.dict.getResultFromEntry(e);
        this.props.updateSearchResult(this.result);

        return 1;
    };

    highlightMatch(doc, sn, so, matchLen, selEndList, tdata) {
        let sel = doc.defaultView.getSelection();

        // If selEndList is empty then we're dealing with a textarea/input situation
        if (selEndList.length === 0) {
            try {
                if (sn.nodeName === 'TEXTAREA' || sn.nodeName === 'INPUT') {

                    // If there is already a selected region not caused by
                    // rikaikun, leave it alone
                    if ((sel.toString()) && (tdata.selText !== sel.toString()))
                        return;

                    // If there is no selected region and the saved
                    // textbox is the same as the current one
                    // then save the current cursor position
                    // The second half of the condition let's us place the
                    // cursor in another text box without having it jump back
                    if (!sel.toString() && tdata.oldTA === sn) {
                        tdata.oldCaret = sn.selectionStart;
                        tdata.oldTA = sn;
                    }
                    sn.selectionStart = so;
                    sn.selectionEnd = matchLen + so;

                    tdata.selText = sn.value.substring(so, matchLen + so);
                }
            }
            catch (err) {
                // If there is an error it is probably caused by the input type
                // being not text.  This is the most general way to deal with
                // arbitrary types.

                // we set oldTA to null because we don't want to do weird stuf
                // with buttons
                tdata.oldTA = null;
                //console.log("invalid input type for selection:" + sn.type);
                console.log(err.message);
            }
            return;
        }

        // Special case for leaving a text box to an outside japanese
        // Even if we're not currently in a text area we should save
        // the last one we were in.
        if (tdata.oldTA && !sel.toString() && tdata.oldCaret >= 0)
            tdata.oldCaret = tdata.oldTA.selectionStart;

        let selEnd;
        let offset = matchLen + so;

        for (let i = 0, len = selEndList.length; i < len; i++) {
            selEnd = selEndList[i];
            if (offset <= selEnd.offset) break;
            offset -= selEnd.offset;
        }

        let range = doc.createRange();
        range.setStart(sn, so);
        range.setEnd(selEnd.node, offset);

        if ((sel.toString()) && (tdata.selText !== sel.toString()))
            return;
        sel.removeAllRanges();
        sel.addRange(range);
        tdata.selText = sel.toString();
    };

    show(tdata, dictOption) {
        let rp = tdata.prevRangeNode;
        let ro = tdata.prevRangeOfs + tdata.uofs;
        let u;

        tdata.uofsNext = 1;

        if (!rp) {
            this.clearHi();
            // this.hidePopup();
            return 0;
        }

        if ((ro < 0) || (ro >= rp.data.length)) {
            this.clearHi();
            // this.hidePopup();
            return 0;
        }

        // if we have '   XYZ', where whitespace is compressed, X never seems to get selected
        // eslint-disable-next-line
        while (((u = rp.data.charCodeAt(ro)) === 32) || (u === 9) || (u === 10)) {
            ++ro;
            if (ro >= rp.data.length) {
                this.clearHi();
                // this.hidePopup();
                return 0;
            }
        }


        if ((isNaN(u)) ||
            ((u !== 0x25CB) &&
            ((u < 0x3001) || (u > 0x30FF)) &&
            ((u < 0x3400) || (u > 0x9FFF)) &&
            ((u < 0xF900) || (u > 0xFAFF)) &&
            ((u < 0xFF10) || (u > 0xFF9D)))) {
            this.clearHi();
            // this.hidePopup();
            return -2;
        }

        //selection end data
        let selEndList = [];
        let text = getTextFromRange(rp, ro, selEndList, 13 /*maxlength*/);

        this.lastSelEnd = selEndList;
        this.lastRo = ro;

        let answer = this.Rikai.search(text, String(dictOption));

        this.processEntry(answer);

        return 1;

    }

    getFirstTextChild(node) {
        return document.evaluate('descendant::text()[not(parent::rp) and not(ancestor::rt)]',
            node, null, XPathResult.ANY_TYPE, null).iterateNext();
    }

    tryUpdatePopup(ev) {
        let self = this;
        let tdata = window.rikaiR; // per-tab data
        let range, rp, ro;

        if (document.caretRangeFromPoint) {
            range = document.caretRangeFromPoint(ev.clientX, ev.clientY);
            rp = range.startContainer;
            ro = range.startOffset;

        } else if (document.caretPositionFromPoint) {
            range = document.caretPositionFromPoint(ev.clientX, ev.clientY);
            rp = range.offsetNode;
            ro = range.offset;
        } else {
            range = window.getSelection();
            rp = range.anchorNode;
            ro = range.anchorOffset;
        }

        if (!range) {
            return;
        }

        // Put this in a try catch so that an exception here doesn't prevent editing due to div.
        try {
            // This is to account for bugs in caretRangeFromPoint
            // It includes the fact that it returns text nodes over non text nodes
            // and also the fact that it miss the first character of inline nodes.

            // If the range offset is equal to the node data length
            // Then we have the second case and need to correct.
            if ((rp && rp.data) && ro === rp.data.length) {
                // A special exception is the WBR tag which is inline but doesn't
                // contain text.
                if ((rp.nextSibling) && (rp.nextSibling.nodeName === 'WBR')) {
                    rp = rp.nextSibling.nextSibling;
                    ro = 0;
                }
                // If we're to the right of an inline character we can use the target.
                // However, if we're just in a blank spot don't do anything.
                else if (self.isInline(ev.target)) {
                    if (rp.parentNode === ev.target)
                        ;
                    else if (rp.parentNode.innerText === ev.target.value)
                        ;
                    else {
                        rp = ev.target.firstChild;
                        ro = 0;
                    }
                }
                // Otherwise we're on the right and can take the next sibling of the
                // inline element.
                else {
                    rp = rp.parentNode.nextSibling;
                    ro = 0;
                }
            }
            // The case where the before div is empty so the false spot is in the parent
            // But we should be able to take the target.
            // The 1 seems random but it actually represents the preceding empty tag
            // also we don't want it to mess up with our fake div
            // Also, form elements don't seem to fall into this case either.
            if (!('form' in ev.target) && rp && rp.parentNode !== ev.target && ro === 1) {
                rp = self.getFirstTextChild(ev.target);
                ro = 0;
            }

            // Otherwise, we're off in nowhere land and we should go home.
            // offset should be 0 or max in this case.
            else if ((!(rp) || ((rp.parentNode !== ev.target)))) {
                rp = null;
                ro = -1;
            }

            if (ev.target === tdata.prevTarget && self.isVisible()) {
                //console.log("exit due to same target");
                if (tdata.title) {
                    return;
                }
                if ((rp === tdata.prevRangeNode) && (ro === tdata.prevRangeOfs)) {
                    return;
                }
            }

        }
        catch (err) {
            console.log(err.message);
            return;
        }

        tdata.prevTarget = ev.target;
        tdata.prevRangeNode = rp;
        tdata.prevRangeOfs = ro;
        tdata.title = null;
        tdata.uofs = 0;
        self.uofsNext = 1;

        let delay = ev.noDelay ? 1 : window.rikaiR.config.popupDelay;

        if ((rp) && (rp.data) && (ro < rp.data.length)) {
            tdata.popX = ev.clientX;
            tdata.popY = ev.clientY;
            tdata.timer = setTimeout(
                function (rangeNode, rangeOffset) {
                    if (!window.rikaiR || rangeNode !== window.rikaiR.prevRangeNode || ro !== window.rikaiR.prevRangeOfs) {
                        return;
                    }
                    self.show(tdata, self.defaultDict);
                }, delay, rp, ro);
            return;
        }

        if ((typeof(ev.target.title) === 'string') && (ev.target.title.length)) {
            tdata.title = ev.target.title;
        }
        else if ((typeof(ev.target.alt) === 'string') && (ev.target.alt.length)) {
            tdata.title = ev.target.alt;
        }

        // FF3
        if (ev.target.nodeName === 'OPTION') {
            tdata.title = ev.target.text;
        }
        else if (ev.target.nodeName === 'SELECT') {
            tdata.title = ev.target.options[ev.target.selectedIndex].text;
        }

        // dont close just because we moved from a valid popup slightly over to a place with nothing
        let dx = tdata.popX - ev.clientX;
        let dy = tdata.popY - ev.clientY;
        let distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 4) {
            self.clearHi();
            // that.hidePopup();
        }
    }

    onMouseMove(ev) {
        this.lastPos.x = ev.clientX;
        this.lastPos.y = ev.clientY;
        this.lastTarget = ev.target;

        if (ev.target.id === 'text-show') {
            this.tryUpdatePopup(ev);
        }
    }

    onMouseClick(ev) {
        if (ev.target.id === 'text-show') {
            this.props.updateShowResult(this.props.searchResult);
            this.showPopup(window.rikaiR.prevTarget);
        } else if (ev.target.id !== 'more-btn') {
            let popupFound = false;
            let element = ev.target;
            while (element.parentElement !== null) {
                if (ev.target.id === 'rikai-window' || element.parentElement.id === 'rikai-window') {
                    popupFound = true;
                    break;
                }
                element = element.parentElement;
            }
            if (!popupFound) {
                this.hidePopup();
            }
        }
    }

    hidePopup() {
        let popup = document.getElementById('rikai-window');
        if (popup) {
            popup.style.display = 'none';
        }
    }

    showPopup(elem) {

        let topdoc = window.document;

        let x, y;

        let popup = topdoc.getElementById('rikai-window');

        // calculate position
        if (elem) {

            popup.style.top = '-1000px';
            popup.style.left = '0px';

            // make the popup visible and gets its height and width
            popup.style.display = 'block';
            let pW = popup.offsetWidth;

            // below the selection
            x = this.elemPosition.left;
            y = this.elemPosition.bottom;

            let scroll = document.documentElement.scrollTop || document.body.scrollTop;
            y += scroll;
            
            // go left if necessary
            if ((x + pW) > (window.innerWidth - 20)) {
                x -= window.innerWidth - 20 - pW;
            }
        } else {
            x += window.scrollX;
            y += window.scrollY;
        }

        popup.style.left = x + 'px';
        popup.style.top = y + 'px';
        popup.style.display = 'block';
    }

    isVisible() {
        let popup = document.getElementById('rikai-window');
        return (popup) && (popup.style.display !== 'none');
    }

    render() {
        return (
            <div>
                <div id="text-show">{this.props.text}</div>
                <RikaiPopUp hidePopup={this.hidePopup} result={this.props.showResult} limit={this.props.limit}/>
            </div>
        );
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(YomiText);
