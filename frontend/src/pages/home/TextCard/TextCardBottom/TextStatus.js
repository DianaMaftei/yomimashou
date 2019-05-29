import React from 'react';
import BookIcon from 'mdi-react/BookIcon';
import BookOpenPageIcon from 'mdi-react/BookOpenPageVariantIcon';
import BookOpenOutlineIcon from 'mdi-react/BookOpenOutlineIcon';

export default ({status}) => {

    const statuses = {
        OPEN: <BookOpenOutlineIcon/>,
        READING: <BookOpenPageIcon/>,
        READ: <BookIcon/>
    };

    return (
        <div>
            {statuses[status]}
        </div>
    );
}