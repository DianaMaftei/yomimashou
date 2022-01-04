import BookIcon from 'mdi-react/BookIcon';
import BookOpenOutlineIcon from 'mdi-react/BookOpenOutlineIcon';
import BookOpenPageIcon from 'mdi-react/BookOpenPageVariantIcon';
import React from 'react';


const TextStatus = ({status}: TextStatusProps) => {

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
};

type TextStatusProps = {
    status: string
}

export default TextStatus;
