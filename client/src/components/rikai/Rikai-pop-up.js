/*
 Rikai React component
 Copyright (C) 2017 Diana Maftei
 URL to come

 ---

 Originally based on Rikaikun
 Copyright (C) 2010 Erek Speed
 http://code.google.com/p/rikaikun/

 ---

 Originally based on rikaichan 1.07
 by Jonathan Zarate
 http://www.polarcloud.com/

 ---

 Originally based on RikaiXUL 0.4 by Todd Rudick
 http://www.rikai.com/
 http://rikaixul.mozdev.org/

 ---

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 ---

 Please do not change or remove any of the copyrights or links to web pages
 when modifying any of the files. - Jon

 */
import React, {Component} from "react";
import WordList from "./WordList";
import NameList from "./NameList";
import Kanji from "./Kanji";

import '../../style/rikai.css';

class RikaiPopUp extends Component {

    shouldComponentUpdate(nextProps) {
        const differentResult = this.props.result !== nextProps.result;

        // console.log("this: ", this.props);
        // console.log("next: ", nextProps);

        // console.log(differentResult);
        return differentResult;
    }

    render() {
        if (!this.props.result) return <div></div>;

        let popUp = null;
        let title = '';

        if (this.props.result.type === 'words') {
            title = 'Word Dictionary';
            popUp = <WordList resultList={this.props.result.resultList} limit={this.props.limit}/>;
        } else if (this.props.result.type === 'kanji') {
            title = 'Kanji Dictionary';
            popUp = <Kanji result={this.props.result.resultList[0]}/>;
        } else if (this.props.result.type === 'names') {
            title = 'Names Dictionary';
            popUp = <NameList resultList={this.props.result.resultList} limit={this.props.limit}/>;
        }

        return (
            <div id="rikai-window">
                <div className="rikai-top">
                    <span className="rikai-title">{title}</span>
                    <span className="closeBtn" onClick={this.props.hidePopup}>&#x2716;</span>
                </div>
                <div className="rikai-display">
                    {popUp}
                </div>
            </div>
        );
    }
}

export default RikaiPopUp;