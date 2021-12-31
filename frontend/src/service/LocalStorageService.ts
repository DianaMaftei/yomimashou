export const getItem = (itemName: string) => {
    const item = localStorage.getItem(itemName);
    const numPatt = new RegExp(/^\d+$/);
    const jsonPatt = new RegExp(/[\[\{].*[\}\]]/);

    if (!item) {
        return null;
    } else if (jsonPatt.test(item)) {
        return JSON.parse(item);
    } else if (numPatt.test(item)) {
        return parseFloat(item);
    } else {
        return item;
    }
};

export const setItem = (itemName: string, item: object) => {
    if (typeof item === 'object') {
        localStorage.setItem(itemName, JSON.stringify(item));
    } else {
        localStorage.setItem(itemName, item);
    }
};

export const removeItem = (itemName: string) => {
    localStorage.removeItem(itemName);
};
