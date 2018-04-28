import RikaiDict from "./RikaiDictionary";

export function isInline(node) {
    let inlineNames = {
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

    return inlineNames.hasOwnProperty(node.nodeName) ||
        // only check styles for elements
        // comments do not have getComputedStyle method
        (document.nodeType === Node.ELEMENT_NODE &&
            (document.defaultView.getComputedStyle(node, null).getPropertyValue('display') === 'inline' ||
            document.defaultView.getComputedStyle(node, null).getPropertyValue('display') === 'inline-block')
        );
}

// Gets text from a node
// returns a string
// node: a node
// selEnd: the selection end object will be changed as a side effect
// maxLength: the maximum length of returned string
// xpathExpr: an XPath expression, which evaluates to text nodes, will be evaluated
// relative to "node" argument
function getInlineText(node, selEndList, maxLength, xpathExpr) {
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

function getFirstTextChild(node) {
    return document.evaluate('descendant::text()[not(parent::rp) and not(ancestor::rt)]',
        node, null, XPathResult.ANY_TYPE, null).iterateNext();
}

export function search(tdata, dictOption, updateSearchResult) {
    let rp = tdata.prevRangeNode;
    let ro = tdata.prevRangeOfs + tdata.uofs;
    let u;

    tdata.uofsNext = 1;

    if (!rp) {
        clearHi();
        return 0;
    }

    if ((ro < 0) || (ro >= rp.data.length)) {
        clearHi();
        return 0;
    }

    // if we have '   XYZ', where whitespace is compressed, X never seems to get selected
    // eslint-disable-next-line
    while (((u = rp.data.charCodeAt(ro)) === 32) || (u === 9) || (u === 10)) {
        ++ro;
        if (ro >= rp.data.length) {
            clearHi();
            return 0;
        }
    }

    if ((isNaN(u)) ||
        ((u !== 0x25CB) &&
        ((u < 0x3001) || (u > 0x30FF)) &&
        ((u < 0x3400) || (u > 0x9FFF)) &&
        ((u < 0xF900) || (u > 0xFAFF)) &&
        ((u < 0xFF10) || (u > 0xFF9D)))) {
        clearHi();
        return -2;
    }

    //selection end data
    let selEndList = [];
    let text = getTextFromRange(rp, ro, selEndList, 13 /*maxlength*/);

    let lastSelEnd = selEndList;
    let lastRo = ro;

    let entries = RikaiDict.search(text, String(dictOption));

    processEntry(entries, lastRo, lastSelEnd, updateSearchResult);

    return 1;
}

function processEntry(e, lastRo, lastSelEnd, updateSearchResult) {
    let tdata = window.rikaiR;
    let ro = lastRo;
    let selEndList = lastSelEnd;

    if (!e) {
        clearHi();
        return -1;
    }

    if (!e.matchLen) e.matchLen = 1;
    tdata.uofsNext = e.matchLen;
    tdata.uofs = (ro - tdata.prevRangeOfs);

    let rp = tdata.prevRangeNode;

    if ((rp) && ((tdata.config.highlight === 'true' && !('form' in tdata.prevTarget)) || (('form' in tdata.prevTarget) && tdata.config.textboxhl === 'true'))) {
        let doc = rp.ownerDocument;
        if (!doc) {
            clearHi();
            return 0;
        }
        highlightMatch(doc, rp, ro, e.result.matchLen, selEndList, tdata);
        window.rikaiR.elemPosition = document.getSelection().getRangeAt(0).getClientRects()[0];
        tdata.prevSelView = doc.defaultView;
    }

    updateSearchResult(e);
    return 1;
};

function highlightMatch(doc, sn, so, matchLen = 1, selEndList, tdata) {
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

// given a node which must not be null,
// returns either the next sibling or next sibling of the father or
// next sibling of the fathers father and so on or null
function getNext(node) {
    let nextNode;

    nextNode = node.nextSibling;

    if (nextNode !== null)
        return nextNode;

    nextNode = node.parentNode;

    if ((nextNode !== null) && isInline(nextNode))
        return getNext(nextNode);

    return null;
};

export function isVisible() {
    let popup = document.getElementById('rikai-window');
    return (popup) && (popup.style.display !== 'none');
}

function getTextFromRange(rangeParent, offset, selEndList, maxLength) {
    let endIndex;
    if (rangeParent.nodeName === 'TEXTAREA' || rangeParent.nodeName === 'INPUT') {
        endIndex = Math.min(rangeParent.data.length, offset + maxLength);
        return rangeParent.value.substring(offset, endIndex);
    }

    let text = '';

    // XPath expression which evaluates to text nodes
    // tells rikaiR which text to translate
    // expression to get all text nodes that are not in (RP or RT) elements
    let textNodeExpr = 'descendant-or-self::text()[not(parent::rp) and not(ancestor::rt)]';

    let xpathExpr = rangeParent.ownerDocument.createExpression(textNodeExpr, null);


    // XPath expression which evaluates to a boolean. If it evaluates to true
    // then rikaiR will not start looking for text in this text node
    // ignore text in RT elements
    let startElementExpr = 'boolean(parent::rp or ancestor::rt)';

    if (rangeParent.ownerDocument.evaluate(startElementExpr, rangeParent, null, XPathResult.BOOLEAN_TYPE, null).booleanValue)
        return '';

    if (rangeParent.nodeType !== Node.TEXT_NODE)
        return '';

    endIndex = Math.min(rangeParent.data.length, offset + maxLength);
    text += rangeParent.data.substring(offset, endIndex);
    selEndList.push({node: rangeParent, offset: endIndex});

    let nextNode = rangeParent;

    // eslint-disable-next-line
    while (((nextNode = getNext(nextNode)) != null) && (isInline(nextNode)) && (text.length < maxLength))
        text += getInlineText(nextNode, selEndList, maxLength - text.length, xpathExpr);

    return text;
};

function clearHi() {
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

export function tryToFindTextAtMouse(ev, defaultDict, updateSearchResult) {
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
            else if (isInline(ev.target)) {
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
            rp = getFirstTextChild(ev.target);
            ro = 0;
        }

        // Otherwise, we're off in nowhere land and we should go home.
        // offset should be 0 or max in this case.
        else if ((!(rp) || ((rp.parentNode !== ev.target)))) {
            rp = null;
            ro = -1;
        }

        if (ev.target === tdata.prevTarget && isVisible()) {
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

    let delay = ev.noDelay ? 1 : window.rikaiR.config.popupDelay;

    if ((rp) && (rp.data) && (ro < rp.data.length)) {
        tdata.popX = ev.clientX;
        tdata.popY = ev.clientY;
        tdata.timer = setTimeout(
            function (rangeNode, rangeOffset) {
                if (!window.rikaiR || rangeNode !== window.rikaiR.prevRangeNode || ro !== window.rikaiR.prevRangeOfs) {
                    return;
                }
                search(tdata, defaultDict, updateSearchResult);
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
        clearHi();
    }
}
