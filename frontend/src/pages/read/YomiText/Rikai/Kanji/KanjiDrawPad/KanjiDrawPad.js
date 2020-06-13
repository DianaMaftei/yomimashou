import React from "react";
import {apiUrl} from "../../../../../../AppUrl";
import grid from "../../../../../../resources/grid.png";
import solidWhite from "../../../../../../resources/solid-white.jpg";
import { fabric } from "fabric";
import "./KanjiDrawPad.css";
import DeleteOutlineIcon from 'mdi-react/DeleteOutlineIcon';
import LightbulbOnIcon from 'mdi-react/LightbulbOnOutlineIcon';

let canvas;
let showHint = true;

function createCanvas() {
    canvas = new fabric.Canvas('c1');
    canvas.isDrawingMode = true;
    canvas.freeDrawingBrush.width = 15;
    canvas.freeDrawingBrush.color = 'black';
}

function setBackgroundImage() {
    canvas.setBackgroundImage(grid, canvas.renderAll.bind(canvas), {
        scaleX: 0.25,
        scaleY: 0.25
    });
}

function setOverlayImage(url) {
    canvas.setOverlayImage(url, canvas.renderAll.bind(canvas), {
        originX: 'left',
        originY: 'top',
        scaleX: 2.85,
        scaleY: 2.85,
        opacity: 0.4
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
    if (showHint) {
        overlay = apiUrl + '/api/dictionary/kanji/svg/' + getKanjiCode(character) + '.svg';
    } else {
        overlay = solidWhite;
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

export default ({character, open}) => {
    if(open) {
        showHint = true;
        initDrawingPad(character)
    } else {
        if(canvas) {
            canvas.clear();
            canvas.dispose();
            canvas = null;
        }
    }

    return (
        <div className="draw-pad">
            <canvas id="c1" width="300" height="300" style={{border: '1px solid black  !important'}}/>
            <div id="canvas-buttons">
                <button id='clear-canvas' onClick={() => clearCanvas(character)}>
                    <DeleteOutlineIcon size={24}/>
                </button>
                <button id='toggle-hint' onClick={() => toggleHint(character)}>
                    <LightbulbOnIcon size={24}/>
                </button>
            </div>

        </div>
    );
}