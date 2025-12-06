/**
 * nimmt start/enddatum, suchbegriff und erstellt die charts basierend darauf
 * @param {string} start startdatum
 * @param {string} end enddatum
 * @param {string} search suchterm
 */
function searchFunction(start, end, search) {
  var uniqueId = start + "-" + end + "-" + search
  var searchResultsDiv = document.getElementById("searchResults")

  // erstellt ein neues panel für jedes suchergebnis
  var panel = document.createElement("div")
  panel.classList.add("panel")
  panel.innerHTML =
    "<br><p id='panelText'>" +
    " Hier sind die Suchergebnisse für " +
    search +
    " von: " +
    start +
    " bis: " +
    end +
    "</p>" +
    '        <div class="chart-row">\n' +
    '            <div id="radarChart-' +
    uniqueId +
    '"></div>\n' +
    '            <div id="horizontalBarChart-' +
    uniqueId +
    '"></div>\n' +
    '            <div id="bubbleChart-' +
    uniqueId +
    '"></div>\n' +
    '        <div class="chart-row-2">\n' +
    '            <div id="sunburst-' +
    uniqueId +
    '"></div>\n' +
    '            <div id="barChart-' +
    uniqueId +
    '"></div>\n' +
    "</div>" +
    "        </div>"

  // fügt dass panel and den container an
  searchResultsDiv.appendChild(panel)

  //ruft chart funktionen auf
  drawBarChart(uniqueId)
  drawRadarChart(uniqueId)
  drawSunburstChart(uniqueId)
  drawBubbleChart(uniqueId)
  drawHorizontalBarChart(uniqueId)
}

// fügt event listener an, um bei enter im search field die funktion aufzurufen
document.addEventListener("DOMContentLoaded", function () {
  document
    .getElementById("searchInput")
    .addEventListener("keyup", function (event) {
      if (event.key === "Enter") {
        writeData(
          document.getElementById("startDate").value,
          document.getElementById("endDate").value,
          document.getElementById("searchInput").value
        )
      }
    })
})

/**
 * Implementation und Dokumentation Kimon Raschke
 * ueberprüft, ob das startdatum vor dem enddatum liegt und tauscht es gegebenenfalls aus
 */
function validateDate() {
  var startDate = document.getElementById("startDate").value
  var endDate = document.getElementById("endDate").value

  if (startDate > endDate) {
    document.getElementById("startDate").value = endDate
    document.getElementById("endDate").value = startDate
  }
}

/**
 * Implementation und dokumentation Kimon Raschke
 * holt sich die json data aus den APIs im RestHelper und updated die json files
 * @param start
 * @param end
 * @param search
 */
function writeData(start, end, search) {
  // liste, der aufrufe
  var ajaxRequests = []

  /**
   * fuehrt die ajax anfragen aus, um die daten zu veraendern
   * @param url
   * @param updateUrl
   * @param start
   * @param end
   * @param search
   * @returns
   */
  function makeAjaxRequest(url, updateUrl, start, end, search) {
    // erstellt die url fuer die spark apis die, die daten holt
    var requestUrl =
      url + "?text=" + search + "&fromDate=" + start + "&toDate=" + end

    return $.ajax({
      type: "GET",
      url: requestUrl,
      success: function (data) {
        // erstellt den string fuer die json
        var jsonString = JSON.stringify(data)
        // ruft die spark auf, um die strings in die jsons zu schreiben
        $.ajax({
          type: "POST",
          url: updateUrl,
          data: { jsonData: jsonString },
          success: function () {
            console.log("updated successfully.")
          },
        })
      },
    })
  }

  // fuegt die requests der liste hinzu
  ajaxRequests.push(
    makeAjaxRequest(
      "/parliamentbrowser/api/data/speeches/entities",
      "/updateDataBubbleChart",
      start,
      end,
      search
    )
  )

  ajaxRequests.push(
    makeAjaxRequest(
      "/parliamentbrowser/api/data/speeches/topics",
      "/updateDataSunburst",
      start,
      end,
      search
    )
  )

  ajaxRequests.push(
    makeAjaxRequest(
      "/parliamentbrowser/api/data/speeches/pos",
      "/updateDataHorizontalBarChart",
      start,
      end,
      search
    )
  )

  ajaxRequests.push(
    makeAjaxRequest(
      "/parliamentbrowser/api/data/speakers/speech-distribution",
      "/updateDataBarChart",
      start,
      end,
      search
    )
  )

  ajaxRequests.push(
    makeAjaxRequest(
      "/parliamentbrowser/api/data/speeches/sentiments",
      "/updateDataRadarChart",
      start,
      end,
      search
    )
  )

  // ruft, die liste der requests auf und ruft dann die searchFunction auf
  $.when.apply($, ajaxRequests).then(function () {
    searchFunction(start, end, search)
  })
}
