import Trie from "../../../util/rikai/Trie";

describe("Trie", () => {
    let trie;

    beforeEach(() => {
        trie = new Trie();
    });

    it("should add a word to the trie", () => {
        trie.add("foo");

        expect(trie.isWord("foo")).toBe(true);
    });

    it("should return false if the word is not found", () => {
        expect(trie.isWord("foo")).toBe(false);
    });

    it("should return all words contained within the passed argument", () => {
        trie.add("foo");
        trie.add("food");
        trie.add("cat");
        trie.add("blue");

        expect(trie.getAllValidWordsInLine("foodie")).toEqual(["food", "foo"]);
    });
});
