import RikaiDictionary from "../../../util/rikai/RikaiDictionary";
jest.mock('../../../util/rikai/RikaiDictionary', () => jest.fn());
import {isInline, highlightMatch, search, tryToFindTextAtMouse} from "../../../util/rikai/RikaiTextParser";

describe("RikaiTextParser", () => {

    it("should return true when given node is a span element", () => {
        let spanElement = document.createElement("span");

        expect(isInline(spanElement)).toBe(true);
    });

    it("should return false when given node is a div element", () => {
        let divElement = document.createElement("div");

        expect(isInline(divElement)).toBe(false);
    });

});
