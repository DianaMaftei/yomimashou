import '../../../__mocks__/LocalStorageMock.js';
import '../../../__mocks__/xhr-mock.js';
import LZString from "lz-string";
import Trie from "../../../util/rikai/Trie";

jest.mock('lz-string', () => ({
    decompressFromUTF16: jest.fn()
}));

jest.mock('../../../util/rikai/Trie', () => jest.fn());
Trie.mockImplementation(() => ({
    add: jest.fn(),
    isWord: () => {
        return false;
    }
}));

describe("RikaiDictionary", () => {

    beforeEach(() => {
        localStorage.clear();
    });

    it("should return a result with an empty array and type words when default dict is 2 and no valid words exist in the dictionary", () => {
        localStorage.setItem("wordEntries", "{}");
        LZString.decompressFromUTF16.mockReturnValue('"foo | bar"');

        let RikaiDictionary = require("../../../util/rikai/RikaiDictionary");

        let searchResult = RikaiDictionary.default.search("むかしむかし、神さまは", 2);
        expect(searchResult).toEqual({ "result": { "data": [], "matchLen": 0 }, "type": "words" });
    });

});
