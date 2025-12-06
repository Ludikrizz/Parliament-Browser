/**
 * zeichnet eine Horizontal Barchart basierend auf den übergebenen daten (start/end-datum + suchwort)
 * @param uniqueId identifikator
 */
function drawHorizontalBarChart(uniqueId) {
  // setzt die dimensionen und ränder des graphen
  var marginHorizontal = { top: 20, right: 30, bottom: 60, left: 90 },
    widthHorizontal = 480 - marginHorizontal.left - marginHorizontal.right,
    heightHorizontal = 580 - marginHorizontal.top - marginHorizontal.bottom

  // fügt das <svg>-element zum body der seite hinzu und fügt ein <g>-element hinzu
  var svg1 = d3
    .select("#horizontalBarChart-" + uniqueId)
    .append("svg")
    .attr(
      "width",
      widthHorizontal + marginHorizontal.left + marginHorizontal.right
    )
    .attr(
      "height",
      heightHorizontal + marginHorizontal.top + marginHorizontal.bottom
    )
    .append("g")
    .attr(
      "transform",
      "translate(" + marginHorizontal.left + "," + marginHorizontal.top + ")"
    )

  // lädt die daten aus der json datei
  d3.json("data/dataHorizontalBarChart.json", function (error, jsonData) {
    if (error) throw error

    // setzt data auf pos list in der json
    var data = jsonData[0].pos

    // filtert pos daten mit count = 0
    data.filter(function (d) {
      return d.count > 0
    })

    // erstelle X Axis
    // definiert die skalierung und den bereich in der x axis fuer die balken
    var x = d3
      .scaleLinear()
      .domain([
        0,
        d3.max(data, function (d) {
          return d.count
        }),
      ])
      .range([0, widthHorizontal])
    // fuegt <g> zum svg hinzu und platziert die textelemente auf der x skala
    svg1
      .append("g")
      .attr("transform", "translate(0," + heightHorizontal + ")")
      .call(d3.axisBottom(x))
      .selectAll("text")
      .attr("transform", "translate(-10,0)rotate(-45)")
      .style("text-anchor", "end")

    // erstelle Y Axis/Skala
    var y = d3
      .scaleBand()
      .range([0, heightHorizontal])
      .domain(
        data.map(function (d) {
          return d.label
        })
      )
      .padding(0.1)
    svg1.append("g").call(d3.axisLeft(y))

    // erstellt balken
    // nimmt alle bar elem in svg, bindet die daten und erstellt die bar basierend x und y koordinate
    // und der breite/hoehe der bar
    svg1
      .selectAll("bar")
      .data(data)
      .enter()
      .append("rect")
      .attr("class", "bar")
      .attr("y", function (d) {
        return y(d.label)
      })
      .attr("width", function (d) {
        return x(d.count)
      })
      .attr("height", y.bandwidth())
      .attr("fill", "rgba(15,51,180,0.7)")
  })
}
