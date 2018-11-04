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

export function search(tdata, dictOption, wordList, nameList) {
    if (!tdata) {
        return;
    }

    let rp = tdata.prevRangeNode;
    let ro = tdata.prevRangeOfs + tdata.uofs;
    let u;

    tdata.uofsNext = 1;

    if (!rp || !rp.data) {
        return;
    }

    if ((ro < 0)) {
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
    let maxLength = dictOption !== 3 ? 13 : 25;
    let end = ro + maxLength;
    selEndList.push({ node: rp, offset: Math.min(rp.data.length, end) });

    let text = rp.textContent.substring(ro, end);

    let result = {
        entries: RikaiDict.search(text, String(dictOption), wordList, nameList),
        lastSelEnd: selEndList,
        lastRo: ro
    };

    return result;
}

export function isVisible() {
    let popup = document.getElementById("rikai-window");
    return popup && popup.style.display === 'block';
}

export function highlightMatch(so, matchLen = 1, selEndList, color) {
    if (!selEndList) {
        return;
    }

    let offset = matchLen + so;

    let parentNode = document.getElementById("yomi-text-container");

    let text = parentNode.textContent;

    let highlight = text.substring(so, offset);
    let newText = text.substring(0, so) + "<span id='highlight-selection' style='background-color: " + color + "'>" + highlight + "</span>" + text.substring(offset, text.length);

    parentNode.innerHTML = newText;
}

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

    let node = rp.parentElement;
    if (node.id === "yomi-text") {
        node = rp;
    }
    let offset = 0;

    if(node.id === "highlight-selection") {
        rp = document.createTextNode(rp.textContent + node.nextSibling.textContent);
        offset = node.previousSibling ? node.previousSibling.length + ro : ro ;
    } else {
        for (let index = 0; index < node.childNodes.length; index++) {
            let currentNode = node.childNodes[index];
            if (currentNode === rp) {
                if (index === 0) {
                    offset = ro;
                    break;
                } else {
                    offset += ro;
                }

            } else if (currentNode.length) {
                offset += currentNode.length;
            } else if (currentNode.textContent) {
                offset += currentNode.textContent.length;
            }
        }
    }

    let textSelectInfo = {
        prevTarget: ev.target,
        prevRangeNode: rp,
        prevRangeOfs: ro,
        totalOffset: offset,
        uofs: 0,
    };

    return textSelectInfo;
}
