/**
 * zeichnet eine Bubblechart basierend auf den übergebenen daten (start/end-datum + suchwort)
 * @param uniqueId identifikator
 */
function drawBubbleChart(uniqueId) {
  // setzt die dimensionen und ränder des graphen
  var marginBubble = { top: 20, right: 20, bottom: 20, left: 35 },
    widthBubble = 450 - marginBubble.left - marginBubble.right,
    heightBubble = 550 - marginBubble.top - marginBubble.bottom

  // fügt das <svg>-element zum body der seite hinzu und fügt ein <g>-element hinzu
  var svg3 = d3
    .select("#bubbleChart-" + uniqueId)
    .append("svg")
    .attr("width", widthBubble + marginBubble.left + marginBubble.right + 200)
    .attr("height", heightBubble + marginBubble.top + marginBubble.bottom)
    .append("g")
    .attr(
      "transform",
      "translate(" + marginBubble.left + "," + marginBubble.top + ")"
    )

  // lädt die daten aus der json datei
  d3.json("data/dataBubbleChart.json", function (error, jsonData) {
    if (error) throw error

    // setzt data auf entities list in der json
    var data = jsonData[0].entities

    // erstellt X axis
    // definiert die skalierung und den bereich in der x axis
    var x = d3
      .scaleLinear()
      .domain([
        0,
        d3.max(data, function (d) {
          return d.count
        }),
      ])
      .range([0, widthBubble])
    // fuegt <g> zum svg hinzu und platziert und setzt skala der x axis
    svg3
      .append("g")
      .attr("transform", "translate(0," + heightBubble + ")")
      .call(d3.axisBottom(x))

    // erstellt Y axis
    // definiert die skalierung und den bereich in der y axis
    var y = d3
      .scaleLinear()
      .domain([
        0,
        d3.max(data, function (d) {
          return d.count
        }),
      ])
      .range([heightBubble, 0])
    // fuegt <g> zum svg hinzu und platziert und setzt skala der y axis
    svg3.append("g").call(d3.axisLeft(y))

    // erstellt skalierung für bubble size
    var z = d3
      .scaleLinear()
      .domain([
        0,
        d3.max(data, function (d) {
          return d.count
        }),
      ])
      .range([0, 20])

    // farbschema für bubbles nach lable
    var color = d3
      .scaleOrdinal()
      .domain(
        data.map(function (d) {
          return d.label
        })
      )
      .range(["#BD81F5", "#00ABB0", "#FE855F", "#FE93A4"])

    // erstellt bubbles
    // fuegt g element zu svg hinzu und erstelle circle elemente mit x/y/z koordinaten, radius und farbe
    svg3
      .append("g")
      .selectAll("dot")
      .data(data)
      .enter()
      .append("circle")
      .attr("cx", function (d) {
        return x(d.count)
      })
      .attr("cy", function (d) {
        return y(d.count)
      })
      .attr("r", function (d) {
        return z(d.count)
      })
      .style("fill", function (d) {
        return color(d.label)
      })
      .style("opacity", "0.7")
      .attr("stroke", "white")
      .style("stroke-width", "2px")

    // erstellt legende
    var legend = svg3
      .selectAll(".legend")
      .data(["LOC", "MISC", "ORG", "PER"])
      .enter()
      .append("g")
      .attr("class", "legend")
      .attr("transform", function (d, i) {
        return "translate(" + (widthBubble + 200) + "," + i * 20 + ")"
      })

    // erstellt rechtecke mit der farbe fuer die legende
    legend
      .append("rect")
      .attr("x", 4)
      .attr("width", 16)
      .attr("height", 18)
      .style("fill", color)

    // setzt legenden text
    legend
      .append("text")
      .attr("x", 2)
      .attr("y", 8)
      .attr("dy", ".35em")
      .style("text-anchor", "end")
      .text(function (d) {
        return d
      })
  })
}
