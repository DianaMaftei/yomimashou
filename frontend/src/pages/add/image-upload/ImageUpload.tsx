import React, {useState} from 'react';
import ReactCrop from 'react-image-crop';
import {useDispatch, useSelector} from 'react-redux';
import Check from 'mdi-react/CheckIcon';
import LocalSeeIcon from "mdi-react/LocalSeeIcon";
import IconButton from "../../../components/buttons/iconBtn/IconButton";
import {setCroppedImageUrlAction, setImageRefAction, setSrcAction, toggleCropAction} from "./imageUploadActions";
import {setTextImageAction} from "../addActions";

const onSelectFile = (e, dispatch) => {
    if (e.target.files && e.target.files.length > 0) {
        const reader = new FileReader();
        reader.addEventListener("load", () =>
            dispatch(setSrcAction(reader.result))
        );
        reader.readAsDataURL(e.target.files[0]);
    }
};

const makeClientCrop = (crop, imageRef, setCroppedImageUrl) => {
    if (imageRef && crop.width && crop.height) {
        getCroppedImg(
            imageRef,
            crop,
            "newFile.jpeg"
        ).then(croppedImageUrl => {
            setCroppedImageUrl(croppedImageUrl);
        })
    }
}

const setTextImage = (dispatch, croppedImageUrl) => {
    dispatch(toggleCropAction());
    dispatch(setTextImageAction(new File([croppedImageUrl],
        "uploaded_file.jpg", {type: "image/jpeg"})));
}

const getCroppedImg = (image, crop, fileName) => {
    const canvas = document.createElement("canvas");
    const scaleX = image.naturalWidth / image.width;
    const scaleY = image.naturalHeight / image.height;
    const pixelRatio = window.devicePixelRatio;
    const ctx = canvas.getContext("2d");
    canvas.width = crop.width * pixelRatio * scaleX;
    canvas.height = crop.height * pixelRatio * scaleY;

    ctx.setTransform(pixelRatio, 0, 0, pixelRatio, 0, 0);
    ctx.imageSmoothingQuality = 'high';

    ctx.drawImage(
        image,
        crop.x * scaleX,
        crop.y * scaleY,
        crop.width * scaleX,
        crop.height * scaleY,
        0,
        0,
        crop.width * scaleX,
        crop.height * scaleY
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

const getObjectUrlFromBlob = (croppedImageUrl) => {
    if (croppedImageUrl != null) {
        window.URL.revokeObjectURL(croppedImageUrl);
        const binaryData = [];
        binaryData.push(croppedImageUrl);
        return window.URL.createObjectURL(new Blob(binaryData, {type: "application/text"}));
    }
}

const ImageUpload = () => {
    const dispatch = useDispatch();
    const [crop, setCrop] = useState({width: 200, aspect: 16 / 9});
    const croppedImageUrl = useSelector(state => state.imageUpload.croppedImageUrl);
    const showCrop = useSelector(state => state.imageUpload.showCrop);
    const src = useSelector(state => state.imageUpload.src);
    const imageRef = useSelector(state => state.imageUpload.imageRef);

    return (
        <div>
            <input accept="image/*" id="upload-img" name="image" type="file" style={{display: 'none'}}
                   onChange={(e) => onSelectFile(e, dispatch)}/>
            <label htmlFor="upload-img" style={{display: 'flex', justifyContent: 'space-around'}}>
                <IconButton label="Upload image">
                    <LocalSeeIcon size="24"/>
                </IconButton>
            </label>

            {src && !showCrop && (
                <div>
                    <ReactCrop src={src} crop={crop}
                               onImageLoaded={(imageRef) => dispatch(setImageRefAction(imageRef))}
                               onComplete={(crop) => makeClientCrop(crop, imageRef,
                                   (croppedImageUrl) => dispatch(setCroppedImageUrlAction(croppedImageUrl)))}
                               onChange={setCrop}
                    />
                    <div>
                        <button onClick={() => setTextImage(dispatch, croppedImageUrl)} className="btn btn-success">
                            <Check size="20"/>
                        </button>
                    </div>
                </div>
            )}

            {showCrop && (
                <div>
                    <img alt="preview" style={{width: "100%"}} src={getObjectUrlFromBlob(croppedImageUrl)}/>
                </div>
            )}
        </div>
    );
}

export default ImageUpload;
