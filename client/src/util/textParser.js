function isInline(node) {
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

export default getTextFromRange;
