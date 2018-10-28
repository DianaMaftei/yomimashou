import React from 'react';
import TextCard from "./TextCard/TextCard";
import "./home.css";
import BricksLayout from "./BricksLayout/BricksLayout";

const getTexts = () => {
    const listOfText = [
        {
            id: 0,
            title: 'A new story',
            date: new Date(Date.UTC(2012, 11, 20, 3, 0, 0)),
            tags: ['N3', 'story'],
            image: "ghost-cat.jpg",
            user: "diana",
            level:0,
            description: "A story about a ghost",
            excerpt: "設紙ウ乾経62泳ムハ際告コリ次安カケト替欠ね日店レクツオ意罪天くルぞ東何療尾ンん。事エキ縮経よしフ生71意すトづ介明モラ多記づこ長仏コケ本低クハ毎83東アツキ厚図ごイれ頭18現ンげ掲95料季磨だ。著クみゅも質力ヨメ素属ぐぎ青絡用変ミ相政ぜ経栄くこ海品際ラ企稿きば前鹿ふけこク目会ぎて必訴ノヲソ者由レツモロ位立もゃ本円ニアコイ手助こ会団帯ぜみ観録商はなつえ。"
        },
        {
            id: 1,
            title: 'Dir en grey - Kasumi',
            date: new Date(Date.UTC(2015, 12, 3, 3, 0, 0)),
            tags: ['N1', 'lyrics'],
            user: "diana",
            level: 4,
            description: "Dir en grey - Kasumi",
            excerpt: "林檎飴片手に泣いていた、、、　月読み葬の闇へ　\n" +
            "「ねえママは何処にいるの？」　見目形　目に焼き付けて抱き締め\n" +
            " \n" +
            "虫が鳴き騒めく八月の祇園坂と扇子屋\n" +
            "小さなこの子が望む微笑んだ　五月は来ない\n" +
            " \n" +
            "紙風船を空へ高くそこには涙が溢れて\n" +
            "紅い飴玉想い出がほら　一緒に溶けて無くなる\n" +
            " \n" +
            "目を覚ます、小さな泣き声が　響く午前四時頃\n" +
            "大好きな絵本を読み寝かし付け　暗闇の中さよなら\n"
        },
        {
            id: 2,
            title: 'Naruto - S2E3',
            date: new Date(Date.UTC(2017, 5, 12, 3, 0, 0)),
            tags: ['N2', 'script'],
            image: "ghost-cat.jpg",
            level: 3,
            user: "diana",
            description: "Naruto - S2E3",
            excerpt: "型ひ解83提ヒヌリタ染功ウタ的宮シナモ忘量くばぴご惑神ょ長政ヱロヤス野売トざと村利ちづつ氷流のぐも福葉条ル喬薦ば北川て良学八覚しぱ。活アホヱ回切町ノイカモ容銘ほ武済チウイ転超なんク速地ソニロ起62山ば渥太レテヨヤ残測こざ記著紹警ヒケノヱ医気単機でうのぴ寄件9略テ代古らげゅっ。暮リ社検そこ船筆情タ菊8佐ずこうぜ無洋わゃづル光化チノナマ属台4日ノミヌ対合でけく作充救敬常みぎんを。"
        },
        {
            id: 3,
            title: 'ご感想掲示板',
            date: new Date(),
            tags: ['N4', 'news'],
            image: "ghost-cat.jpg",
            level: 1,
            user: "diana",
            description: "something or another",
            excerpt: "ネットメディア「イーストポスト」の記者である東雲樹（北川景子）は拡散を続けているフェイクニュースの出所を探ろうとする。\n" +
            "嘘のニュースを作ったのは誰なのか。そして、その情報を拡散させたのは誰なのか。\n" +
            "樹が取材を進める中、事態は思わぬ方向へと展開していく。\n" +
            "\n" +
            "何が本当で何が嘘かもわからない世界の中で、樹はフェイクニュースに立ち向かい、事実をつかみ、伝えることができるのか。\n" +
            "一連のフェイクニュース騒動の果てに、樹が見つけた真実とは？！"
        }
    ];

    return listOfText;
};

export default () => {
    let bricks = getTexts().map(text => <TextCard key={text.id} text={text}/>);
    return (
        <div className="home-container">
            <BricksLayout bricks={bricks} reRender={true}/>
        </div>
    );
}