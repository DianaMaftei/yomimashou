import React from 'react';
import { Cell, Label, Pie, PieChart } from "recharts";

const DeckMasteryIndicator = () => {
    const data1 = [
        {name: 'Group A', value: 100, stroke: "black", fill: "black"},
        {name: 'Group B', value: 0, stroke: "transparent", fill: "transparent"},
    ];
    const data2 = [
        {name: 'Group A', value: 15, stroke: "transparent", fill: "transparent"},
        {name: 'Group B', value: 85, stroke: "#95C9D9", fill: "#95C9D9"},
    ];

    return (
        <PieChart width={80} height={80}>
            <Pie
                data={data1}
                innerRadius={30}
                outerRadius={35}
                startAngle={90}
                endAngle={450}
                dataKey="value"
            >
                {
                    data1.map((entry, index) => <Cell background key={`cell-${index}`}/>)
                }
            </Pie>
            <Pie
                data={data2}
                cornerRadius={30}
                innerRadius={30}
                outerRadius={36}
                startAngle={90}
                endAngle={450}
                dataKey="value"
            >
                {
                    data2.map((entry, index) => <Cell background key={`cell-${index}`}/>)
                }
                <Label value="85%" offset={0} position="center"/>
            </Pie>
        </PieChart>
    );
};

export default DeckMasteryIndicator;
