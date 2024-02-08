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
import SearchType from './SearchType';


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

    function findValidWordsInString(string, wordList) {
        if (!wordList) {
            return;
        }

        let validEntries = [];
        let maxLengthOfWord = 0;

        while(string.length > 0) {
            if (wordList[string]) {
                validEntries.push({surfaceForm: string, baseForm: wordList[string]});
                if (maxLengthOfWord < string.length) {
                    maxLengthOfWord = string.length;
                }
            }

            string = string.substr(0, string.length - 1);
        }

        return {data: validEntries.find(word => word.surfaceForm.length == maxLengthOfWord), matchLen: maxLengthOfWord};
    }

    function findValidNamesInString(string, nameList) {
        if (!nameList) {
            return;
        }

        let validEntries = [];
        let maxLengthOfName = 0;

        while(string.length > 0) {

            for(let i = 0; i < string.length; i++) {

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

        return {data: validEntries, matchLen: maxLengthOfName};
    }

    function isKanji(character) {
        //common and uncommon kanji
        return (character >= '\u4e00' && character <= '\u9faf') ||
            (character >= '\u3400' && character <= '\u4dbf');
    }

    function search(text, dictOption, wordList, nameList) {
        let e = null;

        switch(dictOption) {
            case config.kanji:
                if (!isKanji(text.charAt(0))) {
                    return e = null;
                }
                e = {result: [text.charAt(0)], type: SearchType.KANJI, matchLen: 1};
                return e;
            case config.words:
                e = {result: findValidWordsInString(text, wordList), type: SearchType.WORD};
                break;
            case config.names:
                e = {result: findValidNamesInString(text, nameList), type: SearchType.NAME};
                break;
            default:
                break;
        }
        return e;
    }

    return {
        search, isKanji
    }
})();

export default RikaiDict;
