import Trie from "./Trie";

let RikaiDict = (function () {

    let config = {
        dictCount: 3,
        kanjiN: 1,
        namesN: 2,
        showMode: 0,
        forceKanji: '1',
        defaultDict: '2',
        nextDictionary: '3'
    };

    // let config.nameDictURL = require("../../data/names.dat");
    // let config.nameIndexURL = require("../../data/names.idx");

    config.wordIndexTxtURL = require("../../data/wordEntries.txt");

    config.wordDictURL = require("../../data/dict.dat");
    config.wordIndexURL = require("../../data/dict.idx");

    config.kanjiDataURL = require("../../data/kanji.dat");
    config.radDataURL = require("../../data/radicals.dat");

    config.deinflectURL = require("../../data/deinflect.dat");

    loadDictionary();
    // loadNames();
    loadDIF();

    function loadTrie(text) {
        let myTrie = new Trie();

        let items = text.split('|');
        for (let itemIdx = 0; itemIdx < items.length; itemIdx++) {
            myTrie.add(items[itemIdx]);
        }

        return myTrie;
    }

    function fileRead(url, charset) {
        let req = new XMLHttpRequest();
        req.open("GET", url, false);
        req.send(null);
        return req.responseText;
    };

    function fileReadArray(name, charset) {
        let a = fileRead(name, charset).split('\n');
        // Is this just in case there is blank shit in the file.  It was writtin by Jon though.
        // I suppose this is more robust
        while ((a.length > 0) && (a[a.length - 1].length === 0)) a.pop();
        return a;
    };

    // function loadNames() {
    //     if ((config.nameDict) && (config.nameIndex)) return;
    //     config.nameDict = fileRead(config.nameDictURL);
    //     config.nameIndex = fileRead(config.nameIndexURL);
    // };

    //	Note: These are mostly flat text files; loaded as one continuous string to reduce memory use
    function loadDictionary() {
        if ((config.wordDict) && (config.wordIndex) && (config.kanjiData) && (config.radData)) return;
        config.wordDict = fileRead(config.wordDictURL);
        config.wordIndex = fileRead(config.wordIndexURL);
        config.kanjiData = fileRead(config.kanjiDataURL, 'UTF-8');
        config.radData = fileReadArray(config.radDataURL, 'UTF-8');

        config.wordTrie = loadTrie(fileRead(config.wordIndexTxtURL));

    };

    function loadDIF() {
        config.difReasons = [];
        config.difRules = [];
        config.difExact = [];

        let buffer = fileReadArray(config.deinflectURL, 'UTF-8');
        let prevLen = -1;
        let g, o;

        // i = 1: skip header
        for (let i = 1; i < buffer.length; ++i) {
            let f = buffer[i].split('\t');

            if (f.length === 1) {
                config.difReasons.push(f[0]);
            }
            else if (f.length === 4) {
                o = {};
                o.from = f[0];
                o.to = f[1];
                o.type = f[2];
                o.reason = f[3];

                if (prevLen !== o.from.length) {
                    prevLen = o.from.length;
                    g = [];
                    g.flen = prevLen;
                    config.difRules.push(g);
                }
                g.push(o);
            }
        }
    };

    function deinflect(word) {
        let r = [];
        let have = [];
        let o;

        o = {};
        o.word = word;
        o.type = 0xFF;
        o.reason = '';
        //o.debug = 'root';
        r.push(o);
        have[word] = 0;

        let i, j, k;

        i = 0;
        do {
            word = r[i].word;
            let wordLen = word.length;
            let type = r[i].type;

            for (j = 0; j < config.difRules.length; ++j) {
                let g = config.difRules[j];
                if (g.flen <= wordLen) {
                    let end = word.substr(-g.flen);
                    for (k = 0; k < g.length; ++k) {
                        let rule = g[k];
                        if ((type & rule.type) && (end === rule.from)) {
                            let newWord = word.substr(0, word.length - rule.from.length) + rule.to;
                            if (newWord.length <= 1) continue;
                            o = {};
                            if (have[newWord] !== undefined) {
                                o = r[have[newWord]];
                                o.type |= (rule.type >> 8);

                                //o.reason += ' / ' + r[i].reason + ' ' + config.difReasons[rule.reason];
                                //o.debug += ' @ ' + rule.debug;
                                continue;
                            }
                            have[newWord] = r.length;
                            if (r[i].reason && r[i].reason.length) o.reason = config.difReasons[rule.reason] + ' &lt; ' + r[i].reason;
                            else o.reason = config.difReasons[rule.reason];
                            o.type = rule.type >> 8;
                            o.word = newWord;
                            //o.debug = r[i].debug + ' $ ' + rule.debug;
                            r.push(o);
                        }
                    }
                }
            }

        } while (++i < r.length);

        return r;
    };

    function findValidWordsInString(string) {
        let validEntries = [];
        let alreadyFound = [];
        let deinflectionResult = [];

        let maxLengthOfWord = 0;
        let deinflectedWord = "";

        while (string.length > 0) {
            deinflectionResult = deinflect(string);
            deinflectedWord = "";

            for (let i = 0; i < deinflectionResult.length; i++) {
                deinflectedWord = deinflectionResult[i];

                let isWord = config.wordTrie.isWord(deinflectedWord.word);
                if (isWord) {
                    if (alreadyFound.indexOf(deinflectedWord.word) === -1) {
                        alreadyFound.push(deinflectedWord.word);
                        validEntries.push({ word: deinflectedWord.word, grammarPoint: deinflectedWord.reason });
                        maxLengthOfWord = deinflectedWord.word.length > maxLengthOfWord ? deinflectedWord.word.length : maxLengthOfWord;
                    }
                }
            }
            string = string.substr(0, string.length - 1);
        }

        return { data: validEntries, matchLen: maxLengthOfWord };
    }

    function isKanji(character) {
        //common and uncommon kanji
        return (character >= "\u4e00" && character <= "\u9faf") ||
            (character >= "\u3400" && character <= "\u4dbf");
    }

    function search(text, dictOption) {
        let e = null;

        switch (dictOption) {
            case config.forceKanji:
                if (!isKanji(text.charAt(0))) {
                    return e = null;
                }
                e = { result: [text.charAt(0)], type: "kanji" };
                return e;
            case config.defaultDict:
                config.showMode = 0;
                break;
            case config.nextDictionary:
                config.showMode = (config.showMode + 1) % config.dictCount;
                break;
            default:
                break;
        }

        const m = config.showMode;

        do {
            switch (config.showMode) {
                case 0:
                    e = { result: findValidWordsInString(text), type: "words" };
                    break;
                case config.kanjiN:
                    if (!isKanji(text.charAt(0))) {
                        return e = null;
                    }
                    e = { result: [text.charAt(0)], type: "kanji" };
                    break;
                // case namesN:
                //     e = wordSearch(text, true);
                //     break;
                default:
                    break;
            }
            if (e) break;
            config.showMode = (config.showMode + 1) % config.dictCount;
        } while (config.showMode !== m);

        return e;
    }

    return {
        search: function (text, dictOption) {
            return search(text, dictOption);
        }
    }
})();

export default RikaiDict;