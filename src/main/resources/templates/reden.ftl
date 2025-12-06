<html lang="de">

<head>
    <title>InsightBundestag</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <meta charset="UTF-8">
    <meta name="description" content="Visualisierung von Abgeordneten und Reden im Bundestag">
    <meta name="author" content="Kimon Raschke">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="script/toggleFunctions.js"></script>
    <script src="script/user/userLogin.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<body>
<header>
    <nav>
        <ul>
            <li id="current"><a href="/">Home</a></li>
            <li><a href="/abgeordnete">Abgeordnete</a></li>
            <li id="current"><a href="/reden">Reden</a></li>
            <li><a href="/charts">Charts</a></li>
        </ul>
    </nav>

    <#import 'login.ftl' as l>
    <@l.login loggedIn=loggedIn!false/>
</header>

<div class="search-and-calender">
    <div class="search-input">
        <form action="/reden" method="get">
            <input class="searchInput" type="text" name="text" placeholder="Suchterm...">
            <button id="searchButton"><i class="fa-solid fa-magnifying-glass"></i></button>
        </form>
    </div>
    <#if createSpeech!false>
        <div class="addABButtonContainer">
            <button class="submitButton" onclick="location.href='/edit/rede'">Rede Hinzufügen</button>
        </div>
    </#if>
</div>

<div class="container">
    <div class="list">
        <div class="protocolList">
            <#if reden?has_content>
                <div class="listElement listHeader">
                    <p>Redner:</p>
                    <span>
                        <p class="abgeordnetenListPartei">Partei:</p>
                        <p class="abgeordnetenListSitzungsnummer">Sitzungsnummer:</p>
                    </span>
                </div>
                <#list reden as r>
                    <div class="listElementReden">
                        <a href="/reden/${r.getID()}">
                            ${r.getAbgeordneter().getVorname()} ${r.getAbgeordneter().getName()}</a>
                        <span>
                            <p class="abgeordnetenListPartei">${r.getAbgeordneter().getPartei().getName()}</p>
                            <p class="abgeordnetenListSitzungsnummer">${r.getSitzung().getSitzungsnummer()}</p>
                        </span>
                    </div>
                </#list>
            <#else>
                <div class="listElement listHeader">
                    <p>Sitzung:</p>
                    <p>Beschreibung:</p>
                    <p>Veröffentlichung:</p>
                </div>
                <#list sitzungen as s>
                    <div class="listElement">
                        <p>
                            ${s.getWahlperiode().getNumber()}/${s.getSitzungsnummer()}
                        </p>
                        <div class="descriptions">
                            <p style="font-weight: bold">
                                ${s.getSitzungsnummer()}.
                                Sitzung, ${s.getWahlperiode().getNumber()}.
                                Wahlperiode, ${s.getDate()}
                            </p>
                            <p>
                            <div class="pdfButton" title="Protokoll PDF anzeigen">
                                <i class="fa-regular fa-file-pdf"></i> I PDF
                            </div>
                            <#if editSession!false>
                                <div class="pdfButton" titel="Sitzung allgemein bearbeiten" onclick="location.href='/edit/sitzung?snumber=${s.getSitzungsnummer()}&wp=${s.getWahlperiode().getNumber()}'">
                                    Sitzung bearbeiten
                                </div>
                            </#if>
                            <div class="tagesordnungButton" onclick="toggleTagesordnung('${s}')"
                                 title="Tagesordnung anzeigen/ausblenden">
                                <p id='${s}1'>
                                    <i class="fa-solid fa-caret-down"></i> Tagesordnungspunkte anzeigen
                                </p>
                            </div>
                            <div id='${s}' style="display: none;">
                                <div class="tagesordnungList">
                                    <#list s.getTagesordnungspunkte() as t>
                                        <p style="font-weight: bold">
                                            ${t.getTagesordnungspunkt()}.
                                            ${t.getTitel()}
                                        </p>
                                        <p>
                                        <div class="pdfButton" title="Tagesordnung Punkt PDF anzeigen">
                                            <i class="fa-regular fa-file-pdf"></i> I PDF
                                        </div>
                                        <div class="redenButton"
                                             onclick="toggleReden('${t.getTagesordnungspunkt()}'+'${s.getSitzungsnummer()}'+'${t.getTitel()}'+'${s.getDate()}',
                                                     '${t.getTagesordnungspunkt()}',
                                                     '${s.getSitzungsnummer()}', '${s.getWahlperiode().getNumber()}')"
                                             title="Reden zum Tagesordnung Punkt anzeigen/ausblenden">

                                            <p id="${t.getTagesordnungspunkt()}${s.getSitzungsnummer()}${t.getTitel()}${s.getDate()}1">
                                                <i class="fa-solid fa-caret-down"></i>
                                                Alle Reden zu diesem Tagesordnungspunkt anzeigen
                                            </p>
                                        </div>
                                        <div id="${t.getTagesordnungspunkt()}${s.getSitzungsnummer()}${t.getTitel()}${s.getDate()}"
                                             style="display: none;">
                                            <div class="redenList" id="redenList">
                                            </div>
                                        </div>
                                    </#list>
                                </div>
                            </div>
                        </div>
                        <p>${s.getDate()}</p>
                    </div>
                </#list>
            </#if>
        </div>
    </div>
</div>
</body>
</html>
