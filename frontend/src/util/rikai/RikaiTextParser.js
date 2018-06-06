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
        selEndList.push({ node: node, offset: endIndex });
        return node.data.substring(0, endIndex);
    }

    let result = xpathExpr.evaluate(node, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);

    while ((text.length < maxLength) && (node = result.iterateNext())) {
        endIndex = Math.min(node.data.length, maxLength - text.length);
        text += node.data.substring(0, endIndex);
        selEndList.push({ node: node, offset: endIndex });
    }

    return text;
};

function getFirstTextChild(node) {
    return document.evaluate('descendant::text()[not(parent::rp) and not(ancestor::rt)]',
        node, null, XPathResult.ANY_TYPE, null).iterateNext();
}

export function search(tdata, dictOption, updateSearchResult) {
    if (!tdata) {
        return;
    }

    let rp = tdata.prevRangeNode;
    let ro = tdata.prevRangeOfs + tdata.uofs;
    let u;

    tdata.uofsNext = 1;

    if (!rp) {
        return;
    }

    if ((ro < 0) || (ro >= rp.data.length)) {
        return;
    }

    // if we have '   XYZ', where whitespace is compressed, X never seems to get selected
    while (((u = rp.data.charCodeAt(ro)) === 32) || (u === 9) || (u === 10)) {
        ++ro;
        if (ro >= rp.data.length) {
            return;
        }
    }

    if ((isNaN(u)) ||
        ((u !== 0x25CB) &&
        ((u < 0x3001) || (u > 0x30FF)) &&
        ((u < 0x3400) || (u > 0x9FFF)) &&
        ((u < 0xF900) || (u > 0xFAFF)) &&
        ((u < 0xFF10) || (u > 0xFF9D)))) {
        return;
    }

    //selection end data
    let selEndList = [];
    let text = getTextFromRange(rp, ro, selEndList, 13 /*maxlength*/);

    let result = {
        entries: RikaiDict.search(text, String(dictOption)),
        lastSelEnd: selEndList,
        lastRo: ro
    };

    return result;
}

export function highlightMatch(so, matchLen = 1, selEndList, tdata) {
    if (!selEndList) {
        return;
    }

    let selection = document.defaultView.getSelection();

    // Special case for leaving a text box to an outside japanese
    // Even if we're not currently in a text area we should save
    // the last one we were in.
    if (tdata.oldTA && !selection.toString() && tdata.oldCaret >= 0)
        tdata.oldCaret = tdata.oldTA.selectionStart;

    let selEnd;
    let offset = matchLen + so;

    for (let i = 0, len = selEndList.length; i < len; i++) {
        selEnd = selEndList[i];
        if (offset <= selEnd.offset) break;
        offset -= selEnd.offset;
    }

    let node = document.getElementById("yomi-text-container").firstChild;
    let range = document.createRange();
    range.setStart(node, so);
    range.setEnd(node, offset);

    selection.removeAllRanges();
    selection.addRange(range);
};

// given a node which must not be null,
// returns either the next sibling or next sibling of the father or
// next sibling of the fathers father and so on or null
function getNextNode(node) {
    let nextNode;

    nextNode = node.nextSibling;

    if (nextNode !== null)
        return nextNode;

    nextNode = node.parentNode;

    if ((nextNode !== null) && isInline(nextNode))
        return getNextNode(nextNode);

    return null;
};

function getTextFromRange(rangeParent, offset, selEndList, maxLength) {
    let endIndex;
    if (rangeParent.nodeName === 'TEXTAREA' || rangeParent.nodeName === 'INPUT') {
        endIndex = Math.min(rangeParent.data.length, offset + maxLength);
        return rangeParent.value.substring(offset, endIndex);
    }

    let text = '';

    // XPath expression which evaluates to text nodes
    // tells rikai which text to translate
    // expression to get all text nodes that are not in (RP or RT) elements
    let textNodeExpr = 'descendant-or-self::text()[not(parent::rp) and not(ancestor::rt)]';

    let xpathExpr = rangeParent.ownerDocument.createExpression(textNodeExpr, null);


    // XPath expression which evaluates to a boolean. If it evaluates to true
    // then rikai will not start looking for text in this text node
    // ignore text in RT elements
    let startElementExpr = 'boolean(parent::rp or ancestor::rt)';

    if (rangeParent.ownerDocument.evaluate(startElementExpr, rangeParent, null, XPathResult.BOOLEAN_TYPE, null).booleanValue)
        return '';

    if (rangeParent.nodeType !== Node.TEXT_NODE)
        return '';

    endIndex = Math.min(rangeParent.data.length, offset + maxLength);
    text += rangeParent.data.substring(offset, endIndex);
    selEndList.push({ node: rangeParent, offset: endIndex });

    let nextNode = rangeParent;

    while (((nextNode = getNextNode(nextNode)) != null) && (isInline(nextNode)) && (text.length < maxLength))
        text += getInlineText(nextNode, selEndList, maxLength - text.length, xpathExpr);

    return text;
};

export function tryToFindTextAtMouse(ev) {
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

    let textSelectInfo = {
        prevTarget: ev.target,
        prevRangeNode: rp,
        prevRangeOfs: ro,
        uofs: 0,
    };

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

        // if (ev.target === textSelectInfo.prevTarget && isVisible()) {
        //     //console.log("exit due to same target");
        //     if (textSelectInfo.title) {
        //         return;
        //     }
        //     if ((rp === textSelectInfo.prevRangeNode) && (ro === textSelectInfo.prevRangeOfs)) {
        //         return;
        //     }
        // }

    }
    catch (err) {
        console.log(err.message);
        return;
    }

    return textSelectInfo;
}
