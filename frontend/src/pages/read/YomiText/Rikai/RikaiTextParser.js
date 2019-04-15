/*
	Originally based on Rikaikun
	Copyright (C) 2010 Erek Speed
	http://code.google.com/p/rikaikun/

	---
	Originally based on Rikaichan 1.07
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
import RikaiDict from "./RikaiDictionary";

// expression to get all text nodes that are not in (RP or RT) elements
let textNodeExpr = 'descendant-or-self::text()[not(parent::rp) and not(ancestor::rt)]';
// ignore text in RT elements
let startElementExpr = 'boolean(parent::rp or ancestor::rt)';

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

export function search(textAtMouseInfo, dictOption, wordList, nameList) {
    if (!textAtMouseInfo) {
        return;
    }

    let prevRangeNode = textAtMouseInfo.prevRangeNode;
    let prevRangeOfs = textAtMouseInfo.prevRangeOfs + textAtMouseInfo.uofs;
    let u;

    textAtMouseInfo.matchLen = 1;

    if (!prevRangeNode || !prevRangeNode.data) {
        return;
    }

    if ((prevRangeOfs < 0)) {
        return;
    }

    // if we have '   XYZ', where whitespace is compressed, X never seems to get selected
    while (((u = prevRangeNode.data.charCodeAt(prevRangeOfs)) === 32) || (u === 9) || (u === 10)) {
        ++prevRangeOfs;
        if (prevRangeOfs >= prevRangeNode.data.length) {
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

    let selEndList = [];

    let text = getTextFromRange(prevRangeNode, prevRangeOfs, selEndList, 13 /*maxlength*/);

    let result = {
        isPointerAtFullStop: text.indexOf("。") === 0,
        entries: RikaiDict.search(text, String(dictOption), wordList, nameList),
        selEndList: selEndList,
        lastRo: prevRangeOfs
    };

    return result;
}

export function isVisible() {
    let popup = document.getElementById("rikai-window");
    return popup && popup.style.display === 'block';
}

export function highlightMatch(info, highlightColor) {
    if (!info.selEndList) {
        return;
    }

    let offset = info.matchLen + info.lastRo;

    let doc = info.prevRangeNode.ownerDocument;

    let sel = doc.defaultView.getSelection();

    let range = doc.createRange();

    range.setStart(info.prevRangeNode, info.lastRo);

    let selEnd;
    for (let i = 0, len = info.selEndList.length; i < len; i++) {
        selEnd = info.selEndList[i];
        if (offset <= selEnd.offset) break;
        offset -= selEnd.offset;
    }

    if(!selEnd) return;

    range.setEnd(selEnd.node, offset);

    // let newNode = document.createElement("span");
    // newNode.style.backgroundColor = highlightColor;
    // range.surroundContents(newNode);
    sel.removeAllRanges();
    sel.addRange(range);
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
}

function getNext(node) {
    let nextNode;

    if ((nextNode = node.nextSibling) != null)
        return nextNode;
    if (((nextNode = node.parentNode) != null) && isInline(nextNode))
        return getNext(nextNode);

    return null;
}

function getTextFromRange(rangeParent, offset, selEndList, maxLength) {
    let text = '';

    let endIndex;

    let xpathExpr = rangeParent.ownerDocument.createExpression(textNodeExpr, null);

    if (rangeParent.ownerDocument.evaluate(startElementExpr, rangeParent, null, XPathResult.BOOLEAN_TYPE, null).booleanValue)
        return '';

    if (rangeParent.nodeType !== Node.TEXT_NODE)
        return '';

    endIndex = Math.min(rangeParent.data.length, offset + maxLength);
    text += rangeParent.data.substring(offset, endIndex);
    selEndList.push({ node: rangeParent, offset: endIndex });

    let nextNode = rangeParent;
    while (((nextNode = getNext(nextNode)) != null) && (isInline(nextNode)) && (text.length < maxLength))
        text += getInlineText(nextNode, selEndList, maxLength - text.length, xpathExpr);

    return text;
}

function getFirstTextChild(node) {
    return document.evaluate('descendant::text()[not(parent::rp) and not(ancestor::rt)]',
        node, null, XPathResult.ANY_TYPE, null).iterateNext();
}

export function tryToFindTextAtMouse(ev) {
    let range, rangePosition, rangeOffset;

    if (document.caretRangeFromPoint) {
        range = document.caretRangeFromPoint(ev.clientX, ev.clientY);
        rangePosition = range.startContainer;
        rangeOffset = range.startOffset;
    } else if (document.caretPositionFromPoint) {
        range = document.caretPositionFromPoint(ev.clientX, ev.clientY);
        rangePosition = range.offsetNode;
        rangeOffset = range.offset;
    } else {
        range = window.getSelection();
        rangePosition = range.anchorNode;
        rangeOffset = range.anchorOffset;
    }

    try {
        // This is to account for bugs in caretRangeFromPoint
        // It includes the fact that it returns text nodes over non text nodes
        // and also the fact that it miss the first character of inline nodes.

        // If the range offset is equal to the node data length
        // Then we have the second case and need to correct.
        if ((rangePosition.data) && rangeOffset === rangePosition.data.length) {
            // A special exception is the WBR tag which is inline but doesn't
            // contain text.
            if ((rangePosition.nextSibling) && (rangePosition.nextSibling.nodeName === 'WBR')) {
                rangePosition = rangePosition.nextSibling.nextSibling;
                rangeOffset = 0;
            }
            // If we're to the right of an inline character we can use the target.
            // However, if we're just in a blank spot don't do anything.
            else if (isInline(ev.target)) {
                if (rangePosition.parentNode !== ev.target) {
                    rangePosition = ev.target.firstChild;
                    rangeOffset = 0;
                }
            }
            // Otherwise we're on the right and can take the next sibling of the
            // inline element.
            else {
                rangePosition = rangePosition.parentNode.nextSibling;
                rangeOffset = 0;
            }
        }
        // The case where the before div is empty so the false spot is in the parent
        // But we should be able to take the target.
        // The 1 seems random but it actually represents the preceding empty tag
        // also we don't want it to mess up with our fake div
        // Also, form elements don't seem to fall into this case either.
        if (!('form' in ev.target) && rangePosition && rangePosition.parentNode !== ev.target && rangeOffset === 1) {
            rangePosition = getFirstTextChild(ev.target);
            rangeOffset = 0;
        }


        // Otherwise, we're off in nowhere land and we should go home.
        // offset should be 0 or max in this case.
        else if ((!(rangePosition) || ((rangePosition.parentNode !== ev.target)))) {
            rangePosition = null;
            rangeOffset = -1;

        }

    }
    catch (err) {
        console.log(err.message);
        return;
    }


    let textSelectInfo = {
        prevTarget: ev.target,
        prevRangeNode: rangePosition,
        prevRangeOfs: rangeOffset,
        // totalOffset: offset,
        uofs: 0,
    };

    return textSelectInfo;
}
