$(document).ready(function () {
  let original = document.querySelector(".comment")
  duplicateFraktion = original.cloneNode(true)
  text = document.querySelector(".speechSection #text").value
  document.querySelectorAll(".comment").forEach(function (element) {
    let fraktionen = []
    element.querySelectorAll("#fraktion").forEach(function (fraktion) {
      fraktionen.push(fraktion.value)
    })
    let kommentar = {
      pos: parseInt(element.querySelector("#position").value),
      abgeordneter: element.querySelector("#abgeordneter").value,
      text: element.querySelector("#kommentarText").value,
      fraktionen: fraktionen,
    }
    kommentarListe.push(kommentar)
  })

  $(".commentList").on("click", ".removeBtn", function () {
    $(this).parent().parent().remove()
  })
})

function addFraktion() {
  let parent = document.querySelector(".fraktionen")
  let original = parent.querySelector("label")
  let duplicate = original.cloneNode(true)
  let lastChield = parent.lastElementChild
  duplicate.querySelector("input").value = ""
  parent.insertBefore(duplicate, lastChield)
}

function addComment() {
  let commentList = document.querySelector(".commentList")
  let duplicate = duplicateFraktion.cloneNode(true)
  duplicate.querySelector("#kommentarText").value = ""
  duplicate.querySelector("#position").value = ""
  duplicate.querySelector("#abgeordneter").value = ""
  let fraktion = duplicate.querySelector(".fraktionen")
  let counter = 0
  fraktion.querySelectorAll("label").forEach(function (label) {
    label.querySelector("input").value = ""
    if (counter !== 0) {
      label.remove()
    }
    counter++
  })
  commentList.appendChild(duplicate)
}

function cursorPosition() {
  let text = document.querySelector(".speechSection #text")
  let cursorPosition = text.selectionStart
  let textBefore = text.value.substring(0, cursorPosition)
  document.querySelector("#cursorPosition").innerText = textBefore.length
}

function saveSpeech() {
  let speech = {}
  let newKommentarListe = []
  let urlParams = new URLSearchParams(window.location.search)
  let id = urlParams.get("id")
  let changed = false
  if (id !== null) {
    speech["_id"] = id
  }

  speech["abgeordneter"] = parseInt(
    document.querySelector("#abgeordneter").value
  )
  let sitzung = {}
  sitzung["sitzungsnummer"] = parseInt(
    document.querySelector("#sitzungsnummer").value
  )
  sitzung["date"] = document.querySelector("#sitzungsdatum").value
  sitzung["sitzungsbeginn"] = document.querySelector("#sitzungsbeginn").value
  sitzung["sitzungsende"] = document.querySelector("#sitzungsende").value
  sitzung["wahlperiode"] = parseInt(
    document.querySelector("#wahlperiode").value
  )
  speech["sitzung"] = sitzung

  let agenda = {}
  agenda["agenda_nr"] = parseInt(
    document.querySelector("#tagesordnungspunkt").value
  )
  agenda["titel"] = document.querySelector("#topTitel").value
  speech["agenda"] = agenda

  document.querySelectorAll(".comment").forEach(function (element) {
    let fraktionen = []
    element.querySelectorAll("#fraktion").forEach(function (fraktion) {
      fraktionen.push(fraktion.value)
    })
    let kommentar = {
      pos: parseInt(element.querySelector("#position").value),
      abgeordneter: element.querySelector("#abgeordneter").value,
      text: element.querySelector("#kommentarText").value,
      fraktionen: fraktionen,
    }
    newKommentarListe.push(kommentar)
  })
  if (kommentarListe.length !== newKommentarListe.length) {
    changed = true
  } else if (kommentarListe.length === newKommentarListe.length) {
    for (let i = 0; i < kommentarListe.length; i++) {
      if (
        kommentarListe[i].position !== newKommentarListe[i].position ||
        kommentarListe[i].abgeordneter !== newKommentarListe[i].abgeordneter ||
        kommentarListe[i].text !== newKommentarListe[i].text ||
        kommentarListe[i].fraktionen.length !==
          newKommentarListe[i].fraktionen.length
      ) {
        changed = true
        break
      }
      for (let j = 0; j < kommentarListe[i].fraktionen.length; j++) {
        if (
          kommentarListe[i].fraktionen[j] !== newKommentarListe[i].fraktionen[j]
        ) {
          changed = true
          break
        }
      }
    }
  }

  if (text !== document.querySelector(".speechSection #text").value) {
    changed = true
  }

  if (changed) {
    speech["kommentare"] = newKommentarListe
    speech["text"] = document.querySelector(".speechSection #text").value
  }

  console.log(speech)

  $.ajax({
    type: "POST",
    url: "/parliamentbrowser/api/data/speech/save",
    data: JSON.stringify(speech),
    contentType: "application/json",
  })
    .done(function (data) {
      if (data !== null) {
        window.location.href = "/reden/" + data.id
      }
    })
    .fail(function (jqXHR, textStatus) {
      console.log("Error: " + textStatus)
    })
}

function deleteSpeech() {
  let urlParams = new URLSearchParams(window.location.search)
  let id = urlParams.get("id")
  $.ajax({
    method: "DELETE",
    url: "/parliamentbrowser/api/data/speech/delete",
    contentType: "application/json",
    headers: {
      id: id,
    },
  })
    .done(function () {
      console.log("Success")
      window.location.href = "/reden"
    })
    .fail(function (jqXHR, textStatus) {
      console.log("Error: " + textStatus)
    })
}
