import React, { Component } from "react";
import {apiUrl} from "../../../../../../AppUrl";
import grid from "../../../../../../resources/grid.png";
import solidWhite from "../../../../../../resources/solid-white.jpg";
import { fabric } from "fabric";
import "./kanjiDrawPad.css";
import LightbulbOnIcon from 'mdi-react/LightbulbOnOutlineIcon';
import EraserIcon from 'mdi-react/EraserIcon';
import BackButton from "../../../../../`common/buttons/backBtn/BackButton";
import colors from "../../../../../../style/colorConstants";

let canvas;
let showHint = true;

function createCanvas() {
    canvas = new fabric.Canvas('canvas');
    canvas.isDrawingMode = true;
    canvas.freeDrawingBrush.width = 12;
    canvas.freeDrawingBrush.color = colors.yomiDarkBlue;
}

function setBackgroundImage() {
    canvas.setBackgroundImage(grid, canvas.renderAll.bind(canvas), {
        scaleX: 0.2,
        scaleY: 0.2
    });
}

function setOverlayImage(url) {
    canvas.setOverlayImage(url, canvas.renderAll.bind(canvas), {
        originX: 'left',
        originY: 'top',
        scaleX: 2.2,
        scaleY: 2.2,
        opacity: 0.3
    });
}

function clearCanvas(character) {
    let url = getOverlayUrl(character);
    canvas.clear();
    setOverlayImage(url);
    setBackgroundImage();
}

function getOverlayUrl(character) {
    let overlay;
    let hintBtn = document.getElementById("toggle-hint");

    if (showHint) {
        overlay = apiUrl + '/api/dictionary/kanji/svg/' + getKanjiCode(character) + '.svg';
        hintBtn.style.color = colors.yomiLightRed;
    } else {
        overlay = solidWhite;
        hintBtn.style.color = colors.yomiDarkBlue;
    }

    return overlay;
}

function toggleHint(character) {
    showHint = !showHint;
    setOverlayImage(getOverlayUrl(character));
}

function initDrawingPad(character) {
    createCanvas();
    setBackgroundImage();
    setOverlayImage(getOverlayUrl(character));
}

function getKanjiCode(character) {
    let hex = character.charCodeAt().toString(16);
    if (hex.length === 4) {
        return '0' + hex;
    } else {
        return hex;
    }
}

class KanjiDrawPad extends Component {
    componentDidMount() {
        if(this.props.open) {
            showHint = true;
            initDrawingPad(this.props.character)
        }    else {
            if(canvas) {
                canvas.clear();
                canvas.dispose();
                canvas = null;
            }
        }
    }

    render() {
        return (
            <div id="rikai-window" style={this.props.style} className="elevation-lg">
                <div className="rikai-display">
                    <div className="rikai-kanji-pad-top">
                        <BackButton/>
                    </div>
                    <div className="draw-pad">
                        <canvas id="canvas" width="240" height="240"/>
                        <div id="canvas-buttons">
                            <button id='clear-canvas' onClick={() => clearCanvas(this.props.character)}>
                                <EraserIcon size={36}/>
                            </button>
                            <button id='toggle-hint' onClick={() => toggleHint(this.props.character)}>
                                <LightbulbOnIcon size={36}/>
                            </button>
                        </div>

                    </div>
                </div>
            </div>
        )
    }
}

export default KanjiDrawPad;