$(document).ready(function () {
  let original = document.querySelector(".topContainer")
  duplicateTOP = original.cloneNode(true)
})

function saveTOP() {
  let sitzung = {}
  let tops = []
  document.querySelectorAll(".topContainer").forEach(function (element) {
    let top = {
      agenda_nr: parseInt(element.querySelector("#tagesordnungspunkt").value),
      titel: element.querySelector("#topTitel").value,
      oldTOP: parseInt(
        element.querySelector("#tagesordnungspunkt").getAttribute("oldTOP")
      ),
    }
    tops.push(top)
  })
  sitzung["sitzungsnummer"] = parseInt(
    document.querySelector("#sitzungsnummer").value
  )
  sitzung["old_sitzungsnummer"] = parseInt(
    document.querySelector("#sitzungsnummer").getAttribute("oldNumber")
  )
  sitzung["date"] = document.querySelector("#sitzungsdatum").value
  sitzung["sitzungsbeginn"] = document.querySelector("#sitzungsbeginn").value
  sitzung["sitzungsende"] = document.querySelector("#sitzungsende").value
  sitzung["wahlperiode"] = parseInt(
    document.querySelector("#wahlperiode").value
  )
  sitzung["old_wahlperiode"] = parseInt(
    document.querySelector("#wahlperiode").getAttribute("oldWP")
  )
  sitzung["tops"] = tops
  console.log(sitzung)

  $.ajax({
    url: "/parliamentbrowser/api/data/sitzung/save",
    type: "POST",
    data: JSON.stringify(sitzung),
    contentType: "application/json",
  })
    .done(function () {
      console.log("Success")
      location.reload()
    })
    .fail(function (jqXHR, textStatus) {
      console.log("Error: " + textStatus)
    })
}

function deleteTOP(element) {
  let wp = parseInt(
    document.querySelector("#wahlperiode").getAttribute("oldWP")
  )
  let sn = parseInt(
    document.querySelector("#sitzungsnummer").getAttribute("oldNumber")
  )

  $.ajax({
    url: "/parliamentbrowser/api/data/sitzung/delete/",
    type: "DELETE",
    headers: {
      wahlperiode: wp,
      sitzungsnummer: sn,
    },
  })
    .done(function () {
      console.log("Success")
      element.remove()
    })
    .fail(function (jqXHR, textStatus) {
      console.log("Error: " + textStatus)
    })
}
