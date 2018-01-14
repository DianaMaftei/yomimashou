// Copyright (C) Beau Carnes @ https://codepen.io/beaucarnes/pen/mmBNBd?editors=0011
// modified by Diana Maftei

let Node = function () {
    this.keys = {};
    this.end = false;
    this.setEnd = function () {
        this.end = true;
    };
    this.isEnd = function () {
        return this.end;
    };
};

class Trie {
    constructor() {
        this.root = new Node();
    }

    add(input, node = this.root) {
        if (input.length === 0) {
            node.setEnd();
            return;
        } else if (!node.keys[input[0]]) {
            node.keys[input[0]] = new Node();
            return this.add(input.substr(1), node.keys[input[0]]);
        } else {
            return this.add(input.substr(1), node.keys[input[0]]);
        }
    };

    isWord(word) {
        let node = this.root;
        while (word.length > 1) {
            if (!node.keys[word[0]]) {
                return false;
            } else {
                node = node.keys[word[0]];
                word = word.substr(1);
            }
        }
        return (node.keys[word] && node.keys[word].isEnd()) ? true : false;
    };

    getAllValidWordsInLine(line) {
        let node = this.root;
        let words = [];

        for(let i = 0; i < line.length; i++) {
            if (node.keys[line[i]] !== null) {
                if(node.end) {
                    words.push(line.substring(0, i+1));
                }
            } else {
                break;
            }
            this.root = node;
        }
        this.root = new Node();
        return words;
    }

    resetNode() {
        this.root = new Node();
    }
}

export default Trie;