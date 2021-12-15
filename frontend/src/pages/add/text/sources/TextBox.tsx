// @ts-ignore
import Editor from 'react-pell';
import React from 'react';


const TextBox = ({content, onClick, onChange}: TextBoxProps) => {
    let defaultContent = content != null ? content : '<p id="default-content">Paste your text here</p>';

    return <div id="tab-content-0" aria-labelledby="tab-0">
        <h6>Copy and paste here the text that you want to read</h6>
        <div onClick={onClick}>
            <Editor defaultContent={defaultContent} actions={[]}
                    actionBarClass="my-custom-class"
                    onChange={onChange}
                    onPaste={onChange}/>
        </div>
    </div>;
};

type TextBoxProps = {
    content?: string
    onClick: React.MouseEventHandler<HTMLDivElement>
    onChange: React.MouseEventHandler<HTMLButtonElement>
}

export default TextBox;
