import deepFreeze from 'deepfreeze';
import add from "./addReducers";

describe('ReadReducers', function () {

    const initialState = deepFreeze({
        text: {}});

    it('should return the default state when no valid action type is passed', () => {

        const action = deepFreeze({
            type: 'FOO_BAR'
        });

        const finalState = add(initialState, action);

        expect(finalState).toEqual(initialState);
    });

    it('should update the text', () => {
        let newText = deepFreeze({"content": "foo"});
        const action = deepFreeze({
            type: 'SET_TEXT',
            text: newText
        });

        const finalState = add(initialState, action);

        expect(finalState.text).toEqual(newText);
    });

    it('should reset the text', () => {
        const action = deepFreeze({
            type: 'RESET_TEXT'
        });

        const finalState = add(initialState, action);

        expect(finalState.text).toEqual({});
    });

});
