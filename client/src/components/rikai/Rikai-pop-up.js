/*
 Rikai React component
 Copyright (C) 2017 Diana Maftei
 URL to come

 ---

 Originally based on Rikaikun
 Copyright (C) 2010 Erek Speed
 http://code.google.com/p/rikaikun/

 ---

 Originally based on rikaichan 1.07
 by Jonathan Zarate
 http://www.polarcloud.com/

 ---

 Originally based on RikaiXUL 0.4 by Todd Rudick
 http://www.rikai.com/
 http://rikaixul.mozdev.org/

 ---

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 ---

 Please do not change or remove any of the copyrights or links to web pages
 when modifying any of the files. - Jon

 */
import React, {Component} from "react";
import RikaiDict from "./RikaiDictionary";
import Rikai from "./Rikai";
import WordList from "./WordList";
import NameList from "./NameList";
import ExampleList from "./ExampleList";
import Kanji from "./Kanji";

import '../../style/rikai.css';

class RikaiPopUp extends Component {

    constructor(props) {
        super(props);

        // XPath expression which evaluates to text nodes
        // tells rikaiR which text to translate
        // expression to get all text nodes that are not in (RP or RT) elements
        this.textNodeExpr = 'descendant-or-self::text()[not(parent::rp) and not(ancestor::rt)]';

        // XPath expression which evaluates to a boolean. If it evaluates to true
        // then rikaiR will not start looking for text in this text node
        // ignore text in RT elements
        this.startElementExpr = 'boolean(parent::rp or ancestor::rt)';

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

        // Hack because ro was coming out always 0 for some reason.
        this.lastRo = 0;

        this.inlineNames = {
            // text node
            '#text': true,

            // font style
            'FONT': true,
            'TT': true,
            'I': true,
            'B': true,
            'BIG': true,
            'SMALL': true,
            //deprecated
            'STRIKE': true,
            'S': true,
            'U': true,

            // phrase
            'EM': true,
            'STRONG': true,
            'DFN': true,
            'CODE': true,
            'SAMP': true,
            'KBD': true,
            'VAR': true,
            'CITE': true,
            'ABBR': true,
            'ACRONYM': true,

            // special, not included IMG, OBJECT, BR, SCRIPT, MAP, BDO
            'A': true,
            'Q': true,
            'SUB': true,
            'SUP': true,
            'SPAN': true,
            'WBR': true,

            // ruby
            'RUBY': true,
            'RBC': true,
            'RTC': true,
            'RB': true,
            'RT': true,
            'RP': true
        };

        this.result = null;

        this.onMouseMove = this.onMouseMove.bind(this);
        this.onMouseClick = this.onMouseClick.bind(this);
        this.onKeyDown = this.onKeyDown.bind(this);
        this.onKeyUp = this.onKeyUp.bind(this);

        this.hidePopup = this.hidePopup.bind(this);
        this.showPopup = this.showPopup.bind(this);
        this.getNext = this.getNext.bind(this);
        this.processEntry = this.processEntry.bind(this);
        this.isVisible = this.isVisible.bind(this);
        this.show = this.show.bind(this);
        this.tryUpdatePopup = this.tryUpdatePopup.bind(this);
        this.updateResult = this.updateResult.bind(this);
        this.showExamples = this.showExamples.bind(this);

        this.enable();
    }

    updateResult(result) {
        this.result = result;
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
                this.props.update(this.result);
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
                this.props.update(this.result);
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
                this.props.update(this.result);
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

            this.dict = new RikaiDict(this.config, this.updateResult);

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

    hidePopup() {
        let popup = document.getElementById('rikai-window');
        if (popup) {
            popup.style.display = 'none';
        }
    }

    isInline(node) {
        return this.inlineNames.hasOwnProperty(node.nodeName) ||
            // only check styles for elements
            // comments do not have getComputedStyle method
            (document.nodeType === Node.ELEMENT_NODE &&
                (document.defaultView.getComputedStyle(node, null).getPropertyValue('display') === 'inline' ||
                    document.defaultView.getComputedStyle(node, null).getPropertyValue('display') === 'inline-block')
            );
    }

    isVisible() {
        let popup = document.getElementById('rikai-window');
        return (popup) && (popup.style.display !== 'none');
    }

    getFirstTextChild(node) {
        return document.evaluate('descendant::text()[not(parent::rp) and not(ancestor::rt)]',
            node, null, XPathResult.ANY_TYPE, null).iterateNext();
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

    // Gets text from a node
    // returns a string
    // node: a node
    // selEnd: the selection end object will be changed as a side effect
    // maxLength: the maximum length of returned string
    // xpathExpr: an XPath expression, which evaluates to text nodes, will be evaluated
    // relative to "node" argument
    getInlineText(node, selEndList, maxLength, xpathExpr) {
        let text = '';
        let endIndex;

        if (node.nodeName === '#text') {
            endIndex = Math.min(maxLength, node.data.length);
            selEndList.push({node: node, offset: endIndex});
            return node.data.substring(0, endIndex);
        }

        let result = xpathExpr.evaluate(node, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);

        // eslint-disable-next-line
        while ((text.length < maxLength) && (node = result.iterateNext())) {
            endIndex = Math.min(node.data.length, maxLength - text.length);
            text += node.data.substring(0, endIndex);
            selEndList.push({node: node, offset: endIndex});
        }

        return text;
    };

    // given a node which must not be null,
    // returns either the next sibling or next sibling of the father or
    // next sibling of the fathers father and so on or null
    getNext(node) {
        let nextNode;

        nextNode = node.nextSibling;

        if (nextNode !== null)
            return nextNode;

        nextNode = node.parentNode;

        if ((nextNode !== null) && this.isInline(nextNode))
            return this.getNext(nextNode);

        return null;
    };

    getTextFromRange(rangeParent, offset, selEndList, maxLength) {
        let endIndex;
        if (rangeParent.nodeName === 'TEXTAREA' || rangeParent.nodeName === 'INPUT') {
            endIndex = Math.min(rangeParent.data.length, offset + maxLength);
            return rangeParent.value.substring(offset, endIndex);
        }

        let text = '';

        let xpathExpr = rangeParent.ownerDocument.createExpression(this.textNodeExpr, null);

        if (rangeParent.ownerDocument.evaluate(this.startElementExpr, rangeParent, null, XPathResult.BOOLEAN_TYPE, null).booleanValue)
            return '';

        if (rangeParent.nodeType !== Node.TEXT_NODE)
            return '';

        endIndex = Math.min(rangeParent.data.length, offset + maxLength);
        text += rangeParent.data.substring(offset, endIndex);
        selEndList.push({node: rangeParent, offset: endIndex});

        let nextNode = rangeParent;

        // eslint-disable-next-line
        while (((nextNode = this.getNext(nextNode)) != null) && (this.isInline(nextNode)) && (text.length < maxLength))
            text += this.getInlineText(nextNode, selEndList, maxLength - text.length, xpathExpr);

        return text;
    };

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

        this.dict.getResultFromEntry(e);

        return 1;
    };

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

            // go left if necessary
            if ((x + pW) > (window.innerWidth - 20)) {
                x = this.elemPosition.right - pW;
            }
        }
        else {
            x += window.scrollX;
            y += window.scrollY;
        }

        popup.style.left = x + 'px';
        popup.style.top = y + 'px';
        popup.style.display = 'block';

    }

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
        let text = this.getTextFromRange(rp, ro, selEndList, 13 /*maxlength*/);

        this.lastSelEnd = selEndList;
        this.lastRo = ro;

        let answer = this.Rikai.search(text, String(dictOption));

        this.processEntry(answer);

        return 1;

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
            this.props.update(this.result);
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

    shouldComponentUpdate(nextProps) {
        const differentResult = this.props.result !== nextProps.result;
        return differentResult;
    }

    showExamples(word) {
        let term1 = word.kanji !== undefined ? word.kanji : word.kana;
        let term2 = word.kanji !== undefined ? word.kana : '';
        let url = `http://nihongo.monash.edu/cgi-bin/wwwjdic?1ZEU${term1}=1=${term2}`;

        let axios = require('axios');
        let updateResult = this.props.update.bind(this);

        axios.get(url)
            .then(function (response) {
                let data = response.data.substring(response.data.indexOf('<pre>') + 5, response.data.indexOf('</pre>'));
                let resultList = data.split('\n').filter(entry => entry.substring(0, 2) === 'A:').map(entry => entry.substring(2, entry.indexOf('#')).trim());
                updateResult({type: 'examples', resultList: resultList});
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    render() {
        if (!this.props.result) return <div></div>;

        let popUp = null;
        let title = '';

        if (this.props.result.type === 'words') {
            title = 'Word Dictionary';
            popUp = <WordList resultList={this.props.result.resultList} limit={this.config.limit} showExamples={this.showExamples}/>;
        } else if (this.props.result.type === 'kanji') {
            title = 'Kanji Dictionary';
            popUp = <Kanji result={this.props.result.resultList[0]}/>;
        } else if (this.props.result.type === 'names') {
            title = 'Names Dictionary';
            popUp = <NameList resultList={this.props.result.resultList} limit={this.config.limit}/>;
        } else if (this.props.result.type === 'examples') {
            title = 'Examples';
            popUp = <ExampleList resultList={this.props.result.resultList}/>;
        }

        return (
            <div id="rikai-window">
                <div className="rikai-top">
                    <span className="rikai-title">{title}</span>
                    <span className="closeBtn" onClick={this.hidePopup}>&#x2716;</span>
                </div>
                <div className="rikai-display">
                    {popUp}
                </div>
            </div>
        );
    }
}

export default RikaiPopUp;