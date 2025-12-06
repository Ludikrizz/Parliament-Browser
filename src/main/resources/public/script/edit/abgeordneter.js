let duplicateInst
$(document).ready(function () {
  $("#selectedImage")
    .change(function () {
      let image = document.getElementById("selectedImage").value
      let imageContainer = document.querySelectorAll(".imageDisplayer")
      imageContainer.forEach(function (element) {
        element.style.display = "none"
      })

      document.getElementById(image).style.display = "block"
    })
    .trigger("change")

  $(".sectionList").on("click", ".removeBtn", function () {
    $(this).parent().remove()
  })

  let originalMandat = document.querySelector(".mandate .sectionDisplayer")
  duplicateMandat = originalMandat.cloneNode(true)
  let originalFM = document.querySelector(
    ".fraktionsMitgliedschaften .sectionDisplayer"
  )
  duplicateFM = originalFM.cloneNode(true)
  let originalInst = document.querySelector(".institutionen .sectionDisplayer")
  duplicateInst = originalInst.cloneNode(true)
})

function addMandat() {
  let mandateList = document.querySelector(".mandate .sectionList")
  let duplicate = duplicateMandat.cloneNode(true)
  duplicate.querySelector("#mandateStart").value = ""
  duplicate.querySelector("#mandateEnd").value = ""
  duplicate.querySelector("#mandateTyp").value = ""
  duplicate.querySelector("#mandateWahlperiode").value = ""
  mandateList.appendChild(duplicate)
}

function addFM() {
  let fmList = document.querySelector(".fraktionsMitgliedschaften .sectionList")
  let duplicate = duplicateFM.cloneNode(true)
  duplicate.querySelector("#fraktionsMitgliedschaft").value = ""
  duplicate.querySelector("#fmFromDate").value = ""
  duplicate.querySelector("#fmToDate").value = ""
  duplicate.querySelector("#fmWahlperiode").value = ""
  fmList.appendChild(duplicate)
}

function addInst() {
  let instList = document.querySelector(".institutionen .sectionList")
  let duplicate = duplicateInst.cloneNode(true)
  duplicate.querySelector("#instArt").value = ""
  duplicate.querySelector("#instTitel").value = ""
  duplicate.querySelector("#instFunktion").value = ""
  duplicate.querySelector("#instStartDate").value = ""
  instList.appendChild(duplicate)
}

function saveAb() {
  let urlParams = new URLSearchParams(window.location.search)
  let id = urlParams.get("id")
  let abgeordneter = {}
  if (id != null) {
    abgeordneter["_id"] = id
  }
  abgeordneter["nachname"] = document.getElementById("nachname").value.trim()
  abgeordneter["vorname"] = document.getElementById("vorname").value.trim()
  abgeordneter["ortszusatz"] = document
    .getElementById("ortszusatz")
    .value.trim()
  abgeordneter["adelssuffix"] = document
    .getElementById("adelssuffix")
    .value.trim()
  abgeordneter["anrede"] = document.getElementById("anrede").value.trim()
  abgeordneter["akademischer_titel"] = document
    .getElementById("akadTitel")
    .value.trim()
  abgeordneter["geburtsdatum"] = document.getElementById("geburtsdatum").value
  abgeordneter["geburtsort"] = document
    .getElementById("geburtsort")
    .value.trim()
  abgeordneter["sterbedatum"] = document.getElementById("sterbedatum").value
  abgeordneter["geschlecht"] = document
    .getElementById("geschlecht")
    .value.trim()
  abgeordneter["religion"] = document.getElementById("religion").value.trim()
  abgeordneter["beruf"] = document.getElementById("beruf").value.trim()
  abgeordneter["vita"] = document.getElementById("vita").value.trim()
  abgeordneter["partei"] = document.getElementById("partei").value.trim()

  let mandateList = document.querySelectorAll(".mandate .sectionDisplayer")
  let mandates = []
  mandateList.forEach(function (element) {
    let mandate = {}
    mandate["wahlperiode"] = parseInt(
      element.querySelector("#mandateWahlperiode").value
    )
    isNaN(mandate["wahlperiode"])
      ? (mandate["wahlperiode"] = -1)
      : mandate["wahlperiode"]
    mandate["mandatsart"] = element.querySelector("#mandateTyp").value
    mandate["start_date"] = element.querySelector("#mandateStart").value
    mandate["end_date"] = element.querySelector("#mandateEnd").value
    mandates.push(mandate)
  })
  abgeordneter["mandate"] = mandates

  let fmList = document.querySelectorAll(
    ".fraktionsMitgliedschaften .sectionDisplayer"
  )
  let fms = []
  fmList.forEach(function (element) {
    let fm = {}
    fm["fraktion"] = element
      .querySelector("#fraktionsMitgliedschaft")
      .value.trim()
    fm["start_date"] = element.querySelector("#fmFromDate").value
    fm["end_date"] = element.querySelector("#fmToDate").value
    fm["wahlperiode"] = parseInt(element.querySelector("#fmWahlperiode").value)
    isNaN(fm["wahlperiode"]) ? (fm["wahlperiode"] = -1) : fm["wahlperiode"]
    fms.push(fm)
  })
  abgeordneter["fraktionsmitgliedschaften"] = fms

  let pictureList = document.querySelectorAll(".imageDisplayer")
  let pictures = []
  let priorityPicture = ""
  if (pictureList.length !== 0) {
    priorityPicture = document.querySelector("#selectedImage").value
  }
  pictureList.forEach(function (element) {
    let picture = {}
    picture["local_url"] = element.getAttribute("id")
    picture["remote_url"] = element
      .querySelector("img")
      .getAttribute("remoteurl")
    picture["location"] = element.querySelector("#location").value.trim()
    picture["date"] = element.querySelector("#date").value.trim()
    picture["photographer"] = element
      .querySelector("#photographer")
      .value.trim()
    if (element.getAttribute("id") === priorityPicture) {
      picture["priority"] = -2
    } else {
      picture["priority"] = -1
    }
    pictures.push(picture)
  })

  let instList = document.querySelectorAll(".institutionen .sectionDisplayer")
  let institutionen = []
  instList.forEach(function (element) {
    let inst = {}
    inst["inst_art"] = element.querySelector("#instArt").value.trim()
    inst["titel"] = element.querySelector("#instTitel").value.trim()
    inst["funktion"] = element.querySelector("#instFunktion").value.trim()
    inst["start_date"] = element.querySelector("#instStartDate").value
    institutionen.push(inst)
  })
  abgeordneter["institutionen"] = institutionen
  abgeordneter["pictures"] = pictures
  console.log(abgeordneter)

  $.ajax({
    method: "POST",
    url: "/parliamentbrowser/api/data/representative/save",
    contentType: "application/json",
    data: JSON.stringify(abgeordneter),
  })
    .done(function (data) {
      console.log("Success")
      if (data !== null) {
        location.href = "/abgeordnete/" + data.id
      }
    })
    .fail(function (jqXHR, textStatus) {
      console.log("Error: " + textStatus)
    })
}

function deleteAb() {
  let urlParams = new URLSearchParams(window.location.search)
  let id = urlParams.get("id")
  $.ajax({
    method: "DELETE",
    url: "/parliamentbrowser/api/data/representative/delete",
    contentType: "application/json",
    headers: {
      id: id,
    },
  })
    .done(function () {
      console.log("Success")
      location.redirect("/abgeordnete")
    })
    .fail(function (jqXHR, textStatus) {
      console.log("Error: " + textStatus)
    })
}
