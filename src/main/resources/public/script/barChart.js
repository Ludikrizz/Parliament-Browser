/**
 * zeichnet eine Barchart basierend auf den bereitgestellten daten (start/end-datum + suchwort)
 * @param uniqueId identifikator
 */
function drawBarChart(uniqueId) {
  // setzt die dimensionen und ränder des graphen
  var marginBar = { top: 60, right: 40, bottom: 130, left: 80 },
    widthBar = 1600 - marginBar.left - marginBar.right,
    heightBar = 900 - marginBar.top - marginBar.bottom

  // Fügt das <svg> element zum body der seite hinzu und fügt ein <g> element hinzu
  var svg4 = d3
    .select("#barChart-" + uniqueId)
    .append("svg")
    .attr("width", widthBar + marginBar.left + marginBar.right)
    .attr("height", heightBar + marginBar.top + marginBar.bottom)
    .append("g")
    .attr(
      "transform",
      "translate(" + marginBar.left + "," + marginBar.top + ")"
    )

  // lädt die daten aus der JSON datei
  d3.json("data/dataBarChart.json", function (error, data) {
    if (error) throw error

    // erstellt X axis
    // definiert die skalierung und den bereich in der x axis fuer die balken
    var x = d3
      .scaleBand()
      .range([0, widthBar])
      .domain(
        data.map(function (d) {
          return d.vorname + " " + d.nachname
        })
      )
      .padding(0.15)
    // fuegt <g> zum svg hinzu und platziert die textelemente auf der x skala
    svg4
      .append("g")
      .attr("transform", "translate(0," + heightBar + ")")
      .call(d3.axisBottom(x))
      .selectAll("text")
      .attr("transform", "translate(-10,0)rotate(-45)")
      .style("text-anchor", "end")

    // erstellt Y axis
    var y = d3
      .scaleLinear()
      .domain([
        0,
        d3.max(data, function (d) {
          return d.speechesCount // skaliert die Y skala basierend auf max value
        }),
      ])
      .range([heightBar, 0])
    svg4.append("g").call(d3.axisLeft(y))

    // erstellt balken
    // nimmt alle bar elem in svg, bindet die daten und erstellt die bar basierend x und y koordinate
    // und der breite/hoehe der bar
    svg4
      .selectAll("bar")
      .data(data)
      .enter()
      .append("rect")
      .attr("x", function (d) {
        return x(d.vorname + " " + d.nachname) // x koordinaten
      })
      .attr("y", function (d) {
        // y koordinaten
        return y(d.speechesCount)
      })
      .attr("width", x.bandwidth()) // skalierung der breite fuer bars
      .attr("height", function (d) {
        return heightBar - y(d.speechesCount) // hoehe basierend auf count
      })
      .attr("fill", "rgba(15,51,180,0.7)") // farbe
      // mouse hover effect der das bild anzeigt
      .on("mouseover", function (d) {
        svg4
          .append("image")
          .attr("xlink:href", d.picture_url)
          .attr("x", x(d.vorname + " " + d.nachname) + x.bandwidth() / 2 - 47)
          .attr("y", y(d.speechesCount) - 65)
          .attr("width", 95)
          .attr("height", 95)
      })
      .on("mouseout", function () {
        svg4.select("image").remove()
      })
  })
}
