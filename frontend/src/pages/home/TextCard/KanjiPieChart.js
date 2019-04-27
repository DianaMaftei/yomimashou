import React from 'react';
import "./TextCard.css";
import { Cell, Pie, PieChart } from "recharts";

export default ({kanjiCountByLevel, className}) => {
    if (!kanjiCountByLevel) return <div/>;

    const COLORS = {
        N1: "#FFBB28",
        N2: "#fff059",
        N3: "#a4de6c",
        N4: "#8dd1e1",
        N5: "#8884d8",
        unknown: "#FF560D"
    };

    let data = [];
    Object.keys(kanjiCountByLevel).forEach(function (key) {
        if (key.startsWith("N") || key.startsWith("unknown")) {
            data.push({
                name: key,
                value: kanjiCountByLevel[key],
                color: COLORS[key]
            });
        }
    });

    return (
        <span id="kanji-pie-chart" className={className}>
            <PieChart width={30} height={30}>
                <Pie stroke="none" data={data} outerRadius={15} dataKey="value">
                    {
                        data.map((entry, index) => <Cell key={index} fill={entry.color}/>)
                    }
                </Pie>
            </PieChart>
        </span>
    );
}