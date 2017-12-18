class Rikai {
    constructor(dict) {

        this.haveNames = true;
        this.dictCount = 3;
        this.enabled = 0;
        this.kanjiN = 1;
        // this.namesN = 2;
        this.showMode = 0;
        this.forceKanji = '1';
        this.defaultDict = '2';
        this.nextDict = '3';

        this.dict = dict;
    }

    nextDict() {
        this.showMode = (this.showMode + 1) % this.dictCount;
    }

    search(text, dictOption) {
        let e = null;

        switch (dictOption) {
            case this.forceKanji:
                e = this.dict.kanjiSearch(text.charAt(0));
                return e;
            case this.defaultDict:
                this.showMode = 0;
                break;
            case this.nextDict:
                this.showMode = (this.showMode + 1) % this.dictCount;
                break;
            default:
                break;
        }

        const m = this.showMode;

        do {
            switch (this.showMode) {
                case 0:
                    e = this.dict.wordSearch(text, false);
                    break;
                case this.kanjiN:
                    e = this.dict.kanjiSearch(text.charAt(0));
                    break;
                // case this.namesN:
                //     e = this.dict.wordSearch(text, true);
                //     break;
                default:
                    break;
            }
            if (e) break;
            this.showMode = (this.showMode + 1) % this.dictCount;
        } while (this.showMode !== m);

        return e;
    }

}

export default Rikai;
