import React from 'react';
import { Cell, Label, Pie, PieChart } from "recharts";

const DeckMasteryIndicator = () => {
    const data = [
        {name: 'Group A', value: 50, stroke: "black"},
        {name: 'Group B', value: 50, stroke: "red"},
    ];
    const COLORS = ['#0088FE'];

    return (
        <PieChart width={80} height={80}>
            <Pie
                data={data}
                innerRadius={30}
                outerRadius={30}
                startAngle={90}
                endAngle={450}
                fill="purple"
                dataKey="value"
            >
                {
                    data.map((entry, index) => <Cell background key={`cell-${index}`}
                                                     fill={COLORS[index % COLORS.length]}/>)
                }
                <Label value="85%" offset={0} position="center"/>
            </Pie>
        </PieChart>
    );
};

export default DeckMasteryIndicator;
