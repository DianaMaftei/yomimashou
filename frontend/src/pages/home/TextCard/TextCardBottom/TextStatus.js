import React from 'react';
import BookIcon from 'mdi-react/BookIcon';
import BookOpenPageIcon from 'mdi-react/BookOpenPageVariantIcon';
import BookOpenOutlineIcon from 'mdi-react/BookOpenOutlineIcon';
import * as PropTypes from "prop-types";

const TextStatus = ({status}) => {

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

TextStatus.propTypes = {
    status: PropTypes.string.isRequired
};

export default TextStatus;
