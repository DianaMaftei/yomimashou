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
let RikaiDict = (function () {

    let config = {
        dictCount: 2,
        // dictCount: 3,
        kanji: '1',
        words: '2',
        // names: '3',
        wordEntries: [],
        nameEntries: []
    };

    loadDIF();

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

        fileReadArray(require("./data/deinflect.dat"), callback);
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

    function findValidWordsInString(string, wordList) {
        if (!wordList) return;

        let validEntries = [];
        let alreadyFound = [];
        let deinflectionResult = [];

        let maxLengthOfWord = 0;
        let deinflectedWord = "";

        while (string.length > 0) {
            deinflectionResult = deinflect(string);
            deinflectedWord = "";
            let originalWord = deinflectionResult[0].word;

            for (let i = 0; i < deinflectionResult.length; i++) {
                deinflectedWord = deinflectionResult[i];
                let isWord = wordList.includes(deinflectedWord.word);
                if (isWord) {
                    if (alreadyFound.indexOf(deinflectedWord.word) === -1) {
                        alreadyFound.push(deinflectedWord.word);
                        validEntries.push({ word: deinflectedWord.word, grammarPoint: deinflectedWord.reason, originalWord: originalWord });
                        maxLengthOfWord = originalWord.length > maxLengthOfWord ? originalWord.length : maxLengthOfWord;
                    }
                }
            }
            string = string.substr(0, string.length - 1);
        }

        return { data: validEntries, matchLen: maxLengthOfWord };
    }

    function findValidNamesInString(string, nameList) {
        if (!nameList) return;

        let validEntries = [];
        let maxLengthOfName = 0;

        while (string.length > 0) {

            for (let i = 0; i < string.length; i++) {

                let isName = nameList.includes(string);
                if (isName) {
                    if (validEntries.indexOf(string) === -1) {
                        validEntries.push(string);
                        maxLengthOfName = string.length > maxLengthOfName ? string.length : maxLengthOfName;
                    }
                }
            }
            string = string.substr(0, string.length - 1);
        }

        return { data: validEntries, matchLen: maxLengthOfName };
    }

    function isKanji(character) {
        //common and uncommon kanji
        return (character >= "\u4e00" && character <= "\u9faf") ||
            (character >= "\u3400" && character <= "\u4dbf");
    }

    function search(text, dictOption, wordList, nameList) {
        let e = null;

        switch (dictOption) {
            case config.kanji:
                if (!isKanji(text.charAt(0))) {
                    return e = null;
                }
                e = { result: [text.charAt(0)], type: "kanji", matchLen: 1 };
                return e;
            case config.words:
                e = { result: findValidWordsInString(text, wordList), type: "words" };
                break;
            case config.names:
                e = { result: findValidNamesInString(text, nameList), type: "names" };
                break;
            default:
                break;
        }
        return e;
    }

    return {
        search: function (text, dictOption, wordList, nameList) {
            return search(text, dictOption, wordList, nameList);
        }
    }
})();

export default RikaiDict;