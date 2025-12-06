/**
 * zeichnet eine sunburst chart basierend auf den übergebenen daten (start/end-datum + suchwort)
 * @param uniqueId identifikator
 */
function drawSunburstChart(uniqueId) {
  // variablen für <svg> elemente
  var widthSunburst = 750
  var heightSunburst = 750
  var radiusSunburst = Math.min(widthSunburst, heightSunburst) / 2
  var colorSunburst = d3.scaleOrdinal(d3.schemeCategory20c) // farbschema

  // fügt das <svg>-element zum body der seite hinzu und fügt ein <g>-element hinzu
  var g = d3
    .select("#sunburst-" + uniqueId)
    .append("svg")
    .attr("width", widthSunburst)
    .attr("height", heightSunburst)
    .append("g")
    .attr(
      "transform",
      "translate(" + widthSunburst / 2 + "," + heightSunburst / 2 + ")"
    )

  // sunburst partition erstellen und größe festlegen
  var partition = d3.partition().size([2 * Math.PI, radiusSunburst])

  // lädt die daten aus der json datei
  d3.json("data/dataSunburst.json", function (error, data) {
    if (error) throw error

    // json struktur in hierarchische daten umwandeln und sortieren
    var root = d3
      .hierarchy({ children: data[0].topics }) // nimmt daten aus topic list von json und erstell hierarchy
      .sum(function (d) {
        return d.score
      })
      .sort(function (a, b) {
        return a.value - b.value // sortiert arcs basierend auf größe
      })

    partition(root) // wende partition auf wurzel an

    // setze die winkel fuer die arcs
    var arc = d3
      .arc()
      .startAngle(function (d) {
        return d.x0
      })
      .endAngle(function (d) {
        return d.x1
      })
      .innerRadius(function (d) {
        return d.y0 * 0.8
      })
      .outerRadius(function (d) {
        return d.y1
      })

    // für jedes element ein <g> element hinzufügen, dann <path> elemente anhängen und linien zeichnen
    var node = g
      .selectAll("g")
      .data(root.descendants()) // bindet wurzel nachfahren mit daten
      .enter()
      .append("g")
      .attr("class", "node")
    node
      .append("path")
      .attr("display", function (d) {
        // setze tiefe des pfades
        return d.depth ? null : "none"
      })
      .attr("d", arc)
      .style("stroke", "#ffffff")
      .style("fill", function (d) {
        return colorSunburst(d.data.label)
      })

    // fuegt label textelemente für segmente größer als einer bestimmten schwelle an
    node
      .append("text")
      .filter(function (d) {
        return d.x1 - d.x0 > 0.03 // min size eines arcs fuer ein text element
      })
      .attr("transform", function (d) {
        return (
          "translate(" + arc.centroid(d) + ")rotate(" + textRotation(d) + ")"
        )
      })
      .attr("text-anchor", "middle")
      .attr("dy", ".3em")
      .text(function (d) {
        return d.parent ? d.data.label : ""
      })
      .style("font", "12px times")
  })

  /**
   * berechnet winkel für jedes label basierend auf seiner Position im sunburst
   * vermeidet auf dem kopf stehende labels
   * @param {Node} d
   * @return {Number}
   */
  function textRotation(d) {
    var angle = ((d.x0 + d.x1) / Math.PI) * 90 // berechne winkel
    return angle < 180 ? angle - 90 : angle + 90 // aendere orientierung je nachdem ob angle >180<
  }
}
