<html lang="de">
<head>
    <title>InsightBundestag</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
    <meta charset="UTF-8">
    <meta name="description" content="Visualisierung von Abgeordneten und Reden im Bundestag">
    <meta name="author" content="Genadij Vorontsov">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="../script/user/userLogin.js"></script>
    <script src="../script/edit/sitzung.js"></script>
</head>

<body>

<header>
    <nav>
        <ul>
            <li><a href="/">Home</a></li>
            <li><a href="/abgeordnete">Abgeordnete</a></li>
            <li><a href="/reden">Reden</a></li>
            <li><a href="/charts">Charts</a></li>
        </ul>
    </nav>

    <#import 'login.ftl' as l>
    <@l.login loggedIn=loggedIn!false/>
</header>

<div class="container">

    <div class="upperSection">
        <div class="mainProfileEdit">
            <h3>Sitzung:</h3>
            <label for="sitzungsnummer">Sitzungsnummer:
                <input type="number" id="sitzungsnummer" name="sitzungsnummer" <#if sitzung??>value="${sitzung.getSitzungsnummer()}" oldNumber="${sitzung.getSitzungsnummer()}"</#if>>
            </label>
            <label for="wahlperiode">Wahlperiode:
                <input type="number" id="wahlperiode" name="wahlperiode" <#if sitzung??>value="${sitzung.getWahlperiode().getNumber()}" oldWP="${sitzung.getWahlperiode().getNumber()}"</#if>>
            </label>
            <label for="sitzungsdatum">Datum:
                <input type="date" id="sitzungsdatum" name="sitzungsdatum" <#if sitzung??>value="${sitzung.getDate()?string('yyyy-MM-dd')}"</#if>>
            </label>
            <label for="sitzungsbeginn">Beginn:
                <input type="time" id="sitzungsbeginn" name="sitzungsbeginn" <#if sitzung??>value="${sitzung.getSitzungsbeginn()}"</#if>>
            </label>
            <label for="sitzungsende">Ende:
                <input type="time" id="sitzungsende" name="sitzungsende" <#if sitzung??>value="${sitzung.getSitzungsende()}"</#if>>
            </label>
            <h3>Tagesordnungspunkte</h3>
            <div class="topList">
                <#if sitzung??>
                    <#list sitzung.getTagesordnungspunkte() as top>
                        <div class="topContainer">
                            <label for="tagesordnungspunkt">Nummer:
                                <input type="number" id="tagesordnungspunkt" name="tagesordnungspunkt" value="${top.getTagesordnungspunkt()}" oldTOP="${top.getTagesordnungspunkt()}">
                            </label>
                            <label for="topTitel">Text:
                                <input type="text" id="topTitel" name="topTitel" value="${top.getTitel()}">
                            </label>
                        </div>
                    </#list>
                </#if>
            </div>

        </div>
    </div>
    <div class="buttons">
        <button class="submitButton" onclick="saveTOP()">Speichern</button>
        <#if deleteSession!false>
            <button class="submitButton buttonRed" onclick="deleteTOP()">Löschen</button>
        </#if>
    </div>
</div>

</body>

</html>
