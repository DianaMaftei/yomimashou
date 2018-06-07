import Trie from "./Trie";
import LZString from "lz-string";

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

    loadDictionaries();
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

    function fileRead(url, callback) {
        let req = new XMLHttpRequest();
        req.open("GET", url, true);
        req.onload = function (e) {
            if (req.readyState === 4) {
                if (req.status === 200) {
                    callback(req.responseText);
                } else {
                    console.error(req.statusText, e);
                }
            }
        };
        req.onerror = function (e) {
            console.error(req.statusText, e);
        };
        req.send(null);
    }

    function fileReadArray(name, callback) {
        fileRead(name, (result => {
            let a = result.split('\n');
            while ((a.length > 0) && (a[a.length - 1].length === 0)) a.pop();
            callback(a);
        }));
    }

    // function loadNames() {
    //     if ((config.nameDict) && (config.nameIndex)) return;
    //     config.nameDict = fileRead(config.nameDictURL);
    //     config.nameIndex = fileRead(config.nameIndexURL);
    // };

    //	Note: These are mostly flat text files; loaded as one continuous string to reduce memory use
    function loadDictionaries() {
        // let config.nameDictURL = require("../../data/names.dat");
        // let config.nameIndexURL = require("../../data/names.idx");

        if ((config.wordEntries) && (config.kanjiEntries) && (config.radicals)) return;
        loadOrSaveDictionary("kanjiEntries");
        loadOrSaveDictionary("wordEntries");
        loadOrSaveDictionary("radicals");
    }

    function loadOrSaveDictionary(dictionaryName) {
        let localDictionary = localStorage.getItem(dictionaryName);
        if (!localDictionary) {
            if (dictionaryName === "radicals") {
                fileReadArray(require("../../data/radicals.dat"), result => {
                    doAfterDictionaryIsFetched("radicals", result)
                });
                return;
            }
            fileRead(require("../../data/" + dictionaryName + ".txt"), result => doAfterDictionaryIsFetched(dictionaryName, result));
        } else {
            localDictionary = JSON.parse(LZString.decompressFromUTF16(localDictionary));
            loadDictionary(dictionaryName, localDictionary);
        }
    }

    function loadDictionary(dictionaryName, dictionary) {
        if (dictionaryName === "kanjiEntries") {
            dictionary = dictionary.split("|");
        } else if (dictionaryName === "wordEntries") {
            dictionary = loadTrie(dictionary);
        }
        config[dictionaryName] = dictionary;
    }

    function doAfterDictionaryIsFetched(dictionaryName, dictionary) {
        saveDictionaryLocally(dictionaryName, dictionary);
        loadDictionary(dictionaryName, dictionary);
    }

    function saveDictionaryLocally(dictionaryName, dictionary) {
        try {
            localStorage.setItem(dictionaryName, LZString.compressToUTF16(JSON.stringify(dictionary)));
        } catch (e) {
            console.error("Local Storage is full, could not save " + dictionaryName);
        }
    }

    function loadDIF() {
        config.difReasons = [];
        config.difRules = [];
        config.difExact = [];

        let callback = (result => {
            let buffer = result;
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
        });

        fileReadArray(require("../../data/deinflect.dat"), callback);
    }

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
    }

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

                let isWord = config.wordEntries.isWord(deinflectedWord.word);
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