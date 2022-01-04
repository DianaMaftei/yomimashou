import deepFreeze from 'deepfreeze';
import yomiText from './readReducers';


describe('YomiTextReducers', function () {

    const initialState = deepFreeze({
        textSelectInfo: {}
    });

    it('should return the default state when no valid action type is passed', () => {

        const action = deepFreeze({
            type: 'FOO_BAR'
        });

        const finalState = yomiText(initialState, action);

        expect(finalState).toEqual(initialState);
    });

    it('should update the textSelectInfo', () => {
        const textSelectInfo = deepFreeze({
            foo: "bar"
        });

        const action = deepFreeze({
            type: 'UPDATE_TEXT_SELECT_INFO',
            textSelectInfo: textSelectInfo
        });

        const finalState = yomiText(initialState, action);

        expect(finalState.textSelectInfo).toEqual(textSelectInfo);
    });


});
