import deepFreeze from "deepfreeze";
import config from "../../reducers/config";

describe('config', function () {

    const initialState = deepFreeze({
        popUp: {
            defaultDict: 2,
            limit: 5
        }
    });

    it('should return the default state when no valid action type is passed', () => {

        const action = deepFreeze({
            type: 'FOO_BAR'
        });

        const finalState = config(initialState, action);

        expect(finalState).toEqual(initialState);
    });
});