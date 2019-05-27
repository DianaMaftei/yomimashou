import React from "react";
import Editor from 'react-pell';
import { Button, TextField } from "@material-ui/core/umd/material-ui.development";
import ReactCrop from 'react-image-crop';
import { withRouter } from "react-router-dom";
import { connect } from "react-redux";
import Check from 'mdi-react/CheckIcon';

const mapDispatchToProps = (dispatch) => ({
    setTextImage: (image) => {
        dispatch({
            type: 'SET_TEXT_IMAGE',
            image
        });
    }
});

class Text extends React.Component {

    constructor(props) {
        super(props);
    }

    state = {
        src: null,
        crop: {
            aspect: 16 / 9,
            width: 200,
            x: 0,
            y: 0
        },
        croppedImageUrl: null,
        showCrop: false
    };

    resetState() {
        this.setState({
            src: null,
            crop: {
                aspect: 16 / 9,
                width: 200,
                x: 0,
                y: 0
            },
            croppedImageUrl: null,
            showCrop: false
        })
    }

    onSelectFile = e => {
        this.resetState();

        if (e.target.files && e.target.files.length > 0) {
            const reader = new FileReader();
            reader.addEventListener("load", () =>
                this.setState({src: reader.result})
            );
            reader.readAsDataURL(e.target.files[0]);
        }
    };

    onImageLoaded = (image, crop) => {
        this.imageRef = image;
    };

    onCropComplete = crop => {
        this.makeClientCrop(crop);
    };

    onCropChange = crop => {
        this.setState({crop});
    };

    async makeClientCrop(crop) {
        let self = this;
        if (this.imageRef && crop.width && crop.height) {
            this.getCroppedImg(
                this.imageRef,
                crop,
                "newFile.jpeg"
            ).then(croppedImageUrl => {
                self.props.setTextImage(new File([croppedImageUrl], "uploaded_file.jpg", {type: "image/jpeg"}));
                self.setState({croppedImageUrl});
            })

        }
    }

    getCroppedImg(image, crop, fileName) {
        const canvas = document.createElement("canvas");
        const scaleX = image.naturalWidth / image.width;
        const scaleY = image.naturalHeight / image.height;
        canvas.width = crop.width;
        canvas.height = crop.height;
        const ctx = canvas.getContext("2d");

        ctx.drawImage(
            image,
            crop.x * scaleX,
            crop.y * scaleY,
            crop.width * scaleX,
            crop.height * scaleY,
            0,
            0,
            crop.width,
            crop.height
        );

        return new Promise((resolve, reject) => {
            canvas.toBlob(blob => {
                if (!blob) {
                    console.error("Canvas is empty");
                    return;
                }
                blob.name = fileName;
                resolve(blob);
            }, "image/jpeg");
        });
    }

    toggleCrop() {
        this.setState({showCrop: !this.state.showCrop});
    }

    render() {
        const {crop, croppedImageUrl, showCrop, src} = this.state;
        const {title, setTitle, removePlaceholder, editorContent, setText} = this.props;
        let url;

        if (croppedImageUrl != null) {
            window.URL.revokeObjectURL(croppedImageUrl);
            url = window.URL.createObjectURL(croppedImageUrl);
        }

        return (
            <div>
                <h1 style={{textAlign: 'center'}}>Add a text to read</h1>
                <TextField required fullWidth id="title-required" label="Title" value={title || ''}
                           onChange={setTitle} margin="normal"/>

                <input accept="image/*" id="outlined-button-file" name="file" type="file" style={{display: 'none'}}
                       onChange={this.onSelectFile}/>
                <label htmlFor="outlined-button-file">
                    <Button variant="outlined" component="span">
                        Upload image
                    </Button>
                </label>

                {src && !showCrop && (
                    <div>
                        <ReactCrop
                            src={src}
                            crop={crop}
                            onImageLoaded={this.onImageLoaded}
                            onComplete={this.onCropComplete}
                            onChange={this.onCropChange}
                        />
                        <div>
                            <button onClick={this.toggleCrop.bind(this)} className="btn btn-success">
                                <Check fontSize="small"/>
                            </button>
                        </div>
                        <br/>

                    </div>

                )}
                {showCrop && (
                        <div>
                            <img alt="Crop" style={{maxWidth: "100%"}} src={url}/>
                            <br/>

                        </div>
                )}

                <h6><a href="https://anatolt.ru/t/del-timestamp-srt.html" target="_blank">Subtitles?</a></h6>
                <div onClick={removePlaceholder}>
                    <Editor defaultContent={editorContent} actions={[]} actionBarClass="my-custom-class"
                            onChange={setText} onPaste={setText}/>
                </div>
            </div>
        );
    }
}

export default withRouter(connect((state) => ({}), mapDispatchToProps)(Text));