/**
 * schaltet die anzeige der tagesordnungspunkte um, indem das entsprechende element ein- oder ausgeblendet wird
 * @param {string} id
 */
function toggleTagesordnung(id) {
  var div = document.getElementById(id)
  var toggleText = document.getElementById(id + "1")

  if (div.style.display === "none") {
    div.style.display = "block"
    toggleText.innerHTML =
      '<i class="fa-solid fa-caret-down"></i> Tagesordnungspunkte ausblenden'
  } else {
    div.style.display = "none"
    toggleText.innerHTML =
      '<i class="fa-solid fa-caret-down"></i> Tagesordnungspunkte anzeigen'
  }
}

/**
 * schaltet die Anzeige der Rede punkte um, indem das entsprechende element ein- oder ausgeblendet wird
 * und die reden mithilfe von fetchReden(id, top, sitzung, wp) holt
 * @param {string} id
 * @param top
 * @param sitzung
 * @param wp
 */
function toggleReden(id, top, sitzung, wp) {
  var div = document.getElementById(id)
  var toggleText = document.getElementById(id + "1")

  if (div.style.display === "none") {
    div.style.display = "block"
    fetchReden(id, top, sitzung, wp)
    toggleText.innerHTML =
      '<i class="fa-solid fa-caret-down"></i>' +
      " bitte ausblenden vor dem oeffnen eines anderen Tagesordnungspunkts"
  } else {
    div.style.display = "none"
    toggleText.innerHTML =
      '<i class="fa-solid fa-caret-down"></i>' +
      "Alle Reden zu diesem Tagesordnungspunkt anzeigen"
  }
}

/**
 * holt sich json fuer speeches aus den APIs im RestHelper und zeigt sie in einer liste an
 * @param id
 * @param top
 * @param sitzung
 * @param wp
 */
function fetchReden(id, top, sitzung, wp) {
  $.ajax({
    url: "/parliamentbrowser/api/data/speeches",
    method: "GET",
    data: { top: top, sitzung: sitzung, wp: wp },
    success: function (response) {
      // parsed json zu js objekten
      var responseData = JSON.parse(response)
      var redeHtml = ""

      // geht durch die json und erstellt string mit list elementen zum Reden auflisten und aufrufen
      responseData.forEach(function (rede) {
        redeHtml +=
          "<li>" +
          "<a href=" +
          "/reden/" +
          rede._id +
          ">" +
          rede._id +
          "</a>" +
          "</li>"
      })

      // fuegt den string in den redenList div ein
      $(".redenList").html("<ul>" + redeHtml + "</ul>")
    },
  })
}
