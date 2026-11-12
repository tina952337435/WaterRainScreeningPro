{
title: {
text: '折线图测试'
},
tooltip : {
trigger: 'axis'
},
legend: {
data: ['国家级', '省级', '州市级']
},
xAxis: [
{
type: 'category',
boundaryGap: false,
data: ['2019上半年', '2019下半年', '2020上半年', '2020下半年', '2021上半年', '2021下半年'],
axisLabel: {
interval:0,
rotate:30,
}
}
],
yAxis: [
{
type: 'value'
}
],
series: [
{
name: '国家级',
type: 'line',
stack: 'Total',
data: [120, 132, 101, 134, 90, 230, 210]
},
{
name: '省级',
type: 'line',
stack: 'Total',
data: [220, 182, 191, 234, 290, 330, 310]
},
{
name: '州市级',
type: 'line',
stack: 'Total',
data: [150, 232, 201, 154, 190, 330, 410]
}
]
}