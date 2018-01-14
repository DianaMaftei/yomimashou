import Trie from "./Trie";
import axios from "axios";

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

    let lastSearchedEntry = null;

    // katakana -> hiragana conversion tables
    config.ch = [0x3092, 0x3041, 0x3043, 0x3045, 0x3047, 0x3049, 0x3083, 0x3085, 0x3087, 0x3063, 0x30FC, 0x3042, 0x3044, 0x3046, 0x3048, 0x304A, 0x304B, 0x304D, 0x304F, 0x3051, 0x3053, 0x3055, 0x3057, 0x3059, 0x305B, 0x305D, 0x305F, 0x3061, 0x3064, 0x3066, 0x3068, 0x306A, 0x306B, 0x306C, 0x306D, 0x306E, 0x306F, 0x3072, 0x3075, 0x3078, 0x307B, 0x307E, 0x307F, 0x3080, 0x3081, 0x3082, 0x3084, 0x3086, 0x3088, 0x3089, 0x308A, 0x308B, 0x308C, 0x308D, 0x308F, 0x3093];
    config.cv = [0x30F4, 0xFF74, 0xFF75, 0x304C, 0x304E, 0x3050, 0x3052, 0x3054, 0x3056, 0x3058, 0x305A, 0x305C, 0x305E, 0x3060, 0x3062, 0x3065, 0x3067, 0x3069, 0xFF85, 0xFF86, 0xFF87, 0xFF88, 0xFF89, 0x3070, 0x3073, 0x3076, 0x3079, 0x307C];
    config.cs = [0x3071, 0x3074, 0x3077, 0x307A, 0x307D];

    // let config.nameDictURL = require("../../data/names.dat");
    // let config.nameIndexURL = require("../../data/names.idx");

    config.wordIndexTxtURL = require("../../data/dict.txt");

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

        let items = text.split(',');
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

    function find(data, text) {
        const tlen = text.length;
        let beg = 0;
        let end = data.length - 1;
        let i;
        let mi;
        let mis;

        while (beg < end) {
            mi = (beg + end) >> 1;
            i = data.lastIndexOf('\n', mi) + 1;

            mis = data.substr(i, tlen);
            if (text < mis) end = i - 1;
            else if (text > mis) beg = data.indexOf('\n', mi + 1) + 1;
            else return data.substring(i, data.indexOf('\n', mi + 1));
        }
        return null;
    };

    function loadNames() {
        if ((config.nameDict) && (config.nameIndex)) return;
        config.nameDict = fileRead(config.nameDictURL);
        config.nameIndex = fileRead(config.nameIndexURL);
    };

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

    function wordSearch(word, doNames, max) {
        let i, u, v, r, p;
        let trueLen = [0];
        let entry = {};

        // half & full-width katakana to hiragana conversion
        // note: katakana vu is never converted to hiragana

        p = 0;
        r = '';
        for (i = 0; i < word.length; ++i) {
            u = v = word.charCodeAt(i);

            if (u <= 0x3000) break; //space

            // full-width katakana to hiragana
            if ((u >= 0x30A1) && (u <= 0x30F3)) {
                u -= 0x60;
            }
            // half-width katakana to hiragana
            else if ((u >= 0xFF66) && (u <= 0xFF9D)) {
                u = config.ch[u - 0xFF66];
            }
            // voiced (used in half-width katakana) to hiragana
            else if (u === 0xFF9E) {
                if ((p >= 0xFF73) && (p <= 0xFF8E)) {
                    r = r.substr(0, r.length - 1);
                    u = config.cv[p - 0xFF73];
                }
            }
            // semi-voiced (used in half-width katakana) to hiragana
            else if (u === 0xFF9F) {
                if ((p >= 0xFF8A) && (p <= 0xFF8E)) {
                    r = r.substr(0, r.length - 1);
                    u = config.cs[p - 0xFF8A];
                }
            }
            // ignore J~
            else if (u === 0xFF5E) {
                p = 0;
                continue;
            }

            r += String.fromCharCode(u);
            trueLen[r.length] = i + 1;	// need to keep real length because of the half-width semi/voiced conversion
            p = v;
        }
        word = r;

        let dict;
        let index;
        let maxTrim;
        let cache = [];
        let have = [];
        let count = 0;
        let maxLen = 0;

        let trie;

        if (doNames) {
            loadNames();
            dict = config.nameDict;
            index = config.nameIndex;
            maxTrim = 20;//config.namax;
            entry.names = 1;
        }
        else {
            dict = config.wordDict;
            index = config.wordIndex;
            trie = config.wordTrie;
            maxTrim = 7;//config.wmax;
        }

        if (max !== null) maxTrim = max;

        entry.data = [];

        let entries = [];

        while (word.length > 0) {
            let showInf = (count !== 0);
            let trys;

            if (doNames) trys = [{ 'word': word, 'type': 0xFF, 'reason': null }];
            else trys = deinflect(word);

            for (i = 0; i < trys.length; i++) {
                u = trys[i];

                let ix = cache[u.word];
                if (!ix) {

                    ix = find(index, u.word + ',');

                    let isWord = trie.isWord(u.word);

                    if (isWord) {
                        entries.push(u.word);
                    }

                    if (!ix) {
                        cache[u.word] = [];
                        continue;
                    }
                    ix = ix.split(',');
                    cache[u.word] = ix;
                }

                for (let j = 1; j < ix.length; ++j) {
                    let ofs = ix[j];
                    if (have[ofs]) continue;

                    let dentry = dict.substring(ofs, dict.indexOf('\n', ofs));

                    let ok = true;
                    if (i > 0) {
                        // > 0 a de-inflected word

                        // ex:
                        // /(io) (v5r) to finish/to close/
                        // /(v5r) to finish/to close/(P)/
                        // /(aux-v,v1) to begin to/(P)/
                        // /(adj-na,exp,int) thank you/many thanks/
                        // /(adj-i) shrill/

                        let w;
                        let x = dentry.split(/[,()]/);
                        let y = u.type;
                        let z = x.length - 1;
                        if (z > 10) z = 10;
                        for (; z >= 0; --z) {
                            w = x[z];
                            if ((y & 1) && (w === 'v1')) break;
                            if ((y & 4) && (w === 'adj-i')) break;
                            if ((y & 2) && (w.substr(0, 2) === 'v5')) break;
                            if ((y & 16) && (w.substr(0, 3) === 'vs-')) break;
                            if ((y & 8) && (w === 'vk')) break;
                        }
                        ok = (z !== -1);
                    }
                    if (ok) {
                        if (count >= maxTrim) {
                            entry.more = 1;
                            break;
                        }

                        have[ofs] = 1;
                        ++count;
                        if (maxLen === 0) maxLen = trueLen[word.length];

                        if (trys[i].reason) {
                            if (showInf) r = '&lt; ' + trys[i].reason + ' &lt; ' + word;
                            else r = '&lt; ' + trys[i].reason;
                        }
                        else {
                            r = null;
                        }

                        entry.data.push([dentry, r]);
                    }
                }	// for j < ix.length
                if (count >= maxTrim) break;
            }	// for i < trys.length

            if (count >= maxTrim) break;
            word = word.substr(0, word.length - 1);
        }	// while word.length > 0

        if (entries.toString().length !== 0 && lastSearchedEntry !== entries) {
            console.log("entries: ", entries);
            console.log("lastSearchedEntry: ", lastSearchedEntry);
            axios.get('http://localhost:8080/api/words?word=' + entries.toString())
                .then(function (response) {
                    lastSearchedEntry = entries;
                    console.log("response.data: ", response.data);
                    console.log("response.data formatted: ", getWordResultFromEntry(response.data));
                    entries = [];
                })
                .catch(function (error) {
                    console.log(error);
                });
        }

        if (entry.data.length === 0) return null;

        entry.matchLen = maxLen;
        console.log("entry", entry);
        return entry;
    }

    function kanjiSearch(kanji) {
        const hex = '0123456789ABCDEF';
        let kde;
        let entry;
        let a, b;
        let i;

        i = kanji.charCodeAt(0);
        if (i < 0x3000) return null;

        kde = find(config.kanjiData, kanji);

        if (!kde) return null;

        a = kde.split('|');
        if (a.length !== 6) return null;

        entry = {};
        entry.kanji = a[0];

        entry.misc = {};
        entry.misc['U'] = hex[(i >>> 12) & 15] + hex[(i >>> 8) & 15] + hex[(i >>> 4) & 15] + hex[i & 15];

        b = a[1].split(' ');
        for (i = 0; i < b.length; ++i) {
            if (b[i].match(/^([A-Z]+)(.*)/)) {
                if (!entry.misc[RegExp.$1]) entry.misc[RegExp.$1] = RegExp.$2;
                else entry.misc[RegExp.$1] += ' ' + RegExp.$2;
            }
        }

        entry.onkun = a[2].replace(/\s+/g, '\u3001 ');
        entry.nanori = a[3].replace(/\s+/g, '\u3001 ');
        entry.bushumei = a[4].replace(/\s+/g, '\u3001 ');
        entry.eigo = a[5];

        return entry;
    }

    function getWordResultFromEntry(entry) {
        let result = [];
        for (let i = 0; i < entry.length; i++) {
            let word = {};
            word.kanji = entry[i].kanjiElements.replace(/\//g, ", ");
            word.kana = entry[i].readingElements.replace(/\//g, ";");
            word.def = entry[i].meanings.map(meaning => ((meaning.partOfSpeech ? "(" + meaning.partOfSpeech + ") " : "") + meaning.glosses.replace(/\//g, ", "))).join("; ");
            result.push(word);
        }
        return result;
    }

    function getResultFromEntry(entry) {
        let result = {};

        let resultList = [];

        let i, e;

        if (entry === null) return;

        // kanji dictionary
        if (entry.kanji) {

            result.type = 'kanji';

            let kanji = {};
            kanji.kanji = entry.kanji;

            // get on and kun readings
            kanji.onkun = entry.onkun;
            getSeparateOnKun(kanji);

            // get english definition
            kanji.eigo = entry.eigo;

            // get reading for names
            if (entry.nanori.length) {
                kanji.nanori = entry.nanori;
            }

            // get name of radical
            if (entry.bushumei.length) {
                kanji.bushumei = entry.bushumei;
            }

            // get kanji components and radical
            kanji.components = [];

            let componentsData = config.radData;

            kanji.radical = componentsData[entry.misc['B'] - 1].charAt(0) + ' ' + entry.misc['B'];
            delete entry.misc['B'];

            kanji.grade = entry.misc['G'];
            delete entry.misc['G'];

            kanji.frequency = entry.misc['F'];
            delete entry.misc['F'];

            kanji.strokes = entry.misc['S'];
            delete entry.misc['S'];

            for (i = 0; i < componentsData.length; ++i) {
                let componentInfo = componentsData[i];
                if ((componentInfo.indexOf(entry.kanji) !== -1)) {
                    componentInfo = componentInfo.split('\t');

                    let component = {};
                    component.radical = componentInfo[0];
                    component.reading = componentInfo[2];
                    component.meaning = componentInfo[3];

                    kanji.components.push(component);
                }
            }

            kanji.misc = getKanjiMiscDetails(entry);

            resultList.push(kanji);
        }

        // names dictionary
        else if (entry.names) {
            result.type = 'names';

            let names = [];

            for (i = 0; i < entry.data.length; ++i) {

                let name = {};

                e = entry.data[i][0].match(/^(.+?)\s+(?:\[(.*?)\])?\s*\/(.+)\//);

                if (!e) continue;

                // the next two lines re-process the entries that contain separate search key and spelling due to mixed hiragana/katakana spelling
                let e3 = e[3].match(/^(.+?)\s+(?:\[(.*?)\])?\s*\/(.+)\//);
                if (e3) e = e3;

                name.kanji = e[1];
                name.kana = e[2];
                name.def = e[3].replace(/\//g, '; ');

                names.push(name);
            }
            resultList = names;
        }

        // word dictionary
        else {
            if (!entry.data) return;

            result.type = 'words';

            for (i = 0; i < entry.data.length; ++i) {
                e = entry.data[i][0].match(/^(.+?)\s+(?:\[(.*?)\])?\s*\/(.+)\//);
                if (!e) continue;

                let word = {};

                if (e[2]) {
                    word.kanji = e[1];
                    word.kana = e[2];
                } else {
                    word.kana = e[1];
                }

                if (entry.data[i][1]) {
                    word.conj = '  (' + entry.data[i][1].replace(new RegExp('&lt;', 'g'), '') + ' )';
                }

                word.def = e[3].replace(/\//g, '; ');

                resultList.push(word);
            }
        }

        result.resultList = resultList;

        return result;
    }

    function getSeparateOnKun(kanji) {
        let onkun = kanji.onkun.split('、 ');

        if (onkun) {

            let onReading = [];
            let kunReading = [];

            let hiragana = ['ぁ', 'あ', 'ぃ', 'い', 'ぅ', 'う', 'ぇ', 'え', 'ぉ', 'お', 'か', 'が', 'き', 'ぎ', 'く', 'ぐ', 'け', 'げ', 'こ', 'ご', 'さ', 'ざ', 'し', 'じ', 'す', 'ず', 'せ', 'ぜ', 'そ', 'ぞ', 'た', 'だ', 'ち', 'ぢ', 'っ', 'つ', 'づ', 'て', 'で', 'と', 'ど', 'な', 'に', 'ぬ', 'ね', 'の', 'は', 'ば', 'ぱ', 'ひ', 'び', 'ぴ', 'ふ', 'ぶ', 'ぷ', 'へ', 'べ', 'ぺ', 'ほ', 'ぼ', 'ぽ', 'ま', 'み', 'む', 'め', 'も', 'ゃ', 'や', 'ゅ', 'ゆ', 'ょ', 'よ', 'ら', 'り', 'る', 'れ', 'ろ', 'ゎ', 'わ', 'ゐ', 'ゑ', 'を', 'ん', 'ゔ', 'ゕ', 'ゖ'];
            let katakana = ['ァ', 'ア', 'ィ', 'イ', 'ゥ', 'ウ', 'ェ', 'エ', 'ォ', 'オ', 'カ', 'ガ', 'キ', 'ギ', 'ク', 'グ', 'ケ', 'ゲ', 'コ', 'ゴ', 'サ', 'ザ', 'シ', 'ジ', 'ス', 'ズ', 'セ', 'ゼ', 'ソ', 'ゾ', 'タ', 'ダ', 'チ', 'ヂ', 'ッ', 'ツ', 'ヅ', 'テ', 'デ', 'ト', 'ド', 'ナ', 'ニ', 'ヌ', 'ネ', 'ノ', 'ハ', 'バ', 'パ', 'ヒ', 'ビ', 'ピ', 'フ', 'ブ', 'プ', 'ヘ', 'ベ', 'ペ', 'ホ', 'ボ', 'ポ', 'マ', 'ミ', 'ム', 'メ', 'モ', 'ャ', 'ヤ', 'ュ', 'ユ', 'ョ', 'ヨ', 'ラ', 'リ', 'ル', 'レ', 'ロ', 'ヮ', 'ワ', 'ヰ', 'ヱ', 'ヲ', 'ン', 'ヴ', 'ヵ', 'ヶ', 'ヷ', 'ヸ', 'ヹ', 'ヺ'];

            for (let i = 0; i < onkun.length; i++) {
                for (let j = 0; j < onkun[i].length; j++) {
                    let char = onkun[i].charAt(j);

                    if (hiragana.indexOf(char) !== -1) {
                        kunReading.push(onkun[i]);
                        break;
                    } else if (katakana.indexOf(char) !== -1) {
                        onReading.push(onkun[i]);
                        break;
                    }
                }
            }

            kanji.onReading = onReading;
            kanji.kunReading = kunReading;
            delete kanji.onkun;
            return kanji;
        }
    }

    function getKanjiMiscDetails(entry) {
        let kanjiMisc = entry.misc;

        let miscList = [];

        if (kanjiMisc) {
            let miscData = {
                H: 'Halpern',
                L: 'Heisig',
                E: 'Henshall',
                DK: 'Kanji Learners Dictionary',
                N: 'Nelson',
                V: 'New Nelson',
                Y: 'PinYin',
                P: 'Skip Pattern',
                IN: 'Tuttle Kanji &amp; Kana',
                I: 'Tuttle Kanji Dictionary',
                U: 'Unicode'
            };

            let kanjiMiscKeys = Object.keys(kanjiMisc);

            for (let i = 0; i < kanjiMiscKeys.length; i++) {
                let misc = {};
                let miscFullName = miscData[kanjiMiscKeys[i]];
                misc[miscFullName] = kanjiMisc[kanjiMiscKeys[i]];
                miscList.push(misc);
            }
            return miscList;
        }
    }

    function search(text, dictOption) {
        let e = null;

        switch (dictOption) {
            case config.forceKanji:
                e = kanjiSearch(text.charAt(0));
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
                    e = wordSearch(text, false);
                    break;
                case config.kanjiN:
                    e = kanjiSearch(text.charAt(0));
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
        },
        getResultFromEntry: function (entry) {
            return getResultFromEntry(entry);
        }
    }
})();

export default RikaiDict;