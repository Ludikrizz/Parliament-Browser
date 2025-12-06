const interval = 5000 // 5 Sekunden
function downloadProtocolsAndAnalyze() {
  let interfaceDiv = $(".interface")
  interfaceDiv.empty()
  let downloadProgressDiv = $("<div>").addClass("downloadProgress")
  downloadProgressDiv.append($("<h2>").text("Fortschritt des Downloads"))
  let progressBarContainer = $("<div>").addClass("progressBarContainer")
  progressBarContainer.append(
    $("<div>")
      .addClass("progressBar")
      .append($("<div>").addClass("progressBarFill"))
  )
  progressBarContainer.append($("<h3>").addClass("progressBarText").text("0%"))
  downloadProgressDiv.append(progressBarContainer)

  let processProgressDiv = $("<div>").addClass("processProgress")
  processProgressDiv.append($("<h2>").text("Fortschritt der Analyse"))
  let progressProcessBarContainer = $("<div>").addClass("progressBarContainer")
  progressProcessBarContainer.append(
    $("<div>")
      .addClass("progressBar")
      .append($("<div>").addClass("progressBarFill"))
  )
  progressProcessBarContainer.append(
    $("<h3>").addClass("progressBarText").text("0%")
  )
  processProgressDiv.append(progressProcessBarContainer)

  interfaceDiv.append(downloadProgressDiv)
  interfaceDiv.append(processProgressDiv)

  intervalID = setInterval(progess, interval)
  $.ajax({
    method: "POST",
    url: "/parliamentbrowser/api/data/processing/missingProtocols",
    dataType: "json",
  })
    .done(function () {
      console.log("Success")
      location.reload()
    })
    .fail(function (jqXHR, textStatus) {
      console.log("Error: " + textStatus)
    })
}

function processMissingSpeeches() {
  let interfaceDiv = $(".interface")
  interfaceDiv.empty()

  let processProgressDiv = $("<div>").addClass("processProgress")
  processProgressDiv.append($("<h2>").text("Fortschritt der Analyse"))
  let progressProcessBarContainer = $("<div>").addClass("progressBarContainer")
  progressProcessBarContainer.append(
    $("<div>")
      .addClass("progressBar")
      .append($("<div>").addClass("progressBarFill"))
  )
  progressProcessBarContainer.append(
    $("<h3>").addClass("progressBarText").text("0%")
  )
  processProgressDiv.append(progressProcessBarContainer)

  interfaceDiv.append(processProgressDiv)
  intervalID = setInterval(progess, interval)

  $.ajax({
    method: "POST",
    url: "/parliamentbrowser/api/data/processing/missingSpeeches",
    dataType: "json",
  })
    .done(function () {
      console.log("Success")
      location.reload()
    })
    .fail(function (jqXHR, textStatus) {
      console.log("Error: " + textStatus)
    })
}

function progess() {
  $.ajax({
    method: "GET",
    dataType: "json",
    url: "/parliamentbrowser/api/data/processing/progress",
  })
    .done(function (data) {
      console.log("success")
      let progressBarProcess = $(".processProgress .progressBarFill")
      progressBarProcess.css(
        "width",
        (246 * data.processingProgress) / 100 + "px"
      )
      $(".processProgress .progressBarText").text(data.processingProgress + "%")
      let progressBarDownlaod = $(".downloadProgress .progressBarFill")
      progressBarDownlaod.css("width", 246 * data.downloadProgress + "px")
      $(".downloadProgress .progressBarText").text(
        data.downloadProgress * 100 + "%"
      )
    })
    .fail(function (jqXHR, textStatus) {
      console.log("Error: " + textStatus)
    })
}
