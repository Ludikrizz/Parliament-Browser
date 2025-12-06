
<html lang="de">
<head>
    <title>InsightBundestag</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <meta charset="UTF-8">
    <meta name="description" content="Visualisierung von Abgeordneten und Reden im Bundestag">
    <meta name="author" content="Kester Rumke">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="script/analyzeFunctions.js"></script>
    <script src="script/user/userLogin.js"></script>
</head>

<body>
<header>
    <nav>
        <ul>
            <li id="current"><a href="/">Home</a></li>
            <li><a href="/abgeordnete">Abgeordnete</a></li>
            <li><a href="/reden">Reden</a></li>
            <li><a href="/charts">Charts</a></li>
        </ul>
    </nav>

    <#import 'login.ftl' as l>
    <@l.login loggedIn=loggedIn!false/>
</header>
<div class="container">
    <div class="homeContainer">
        <h1>Willkommen bei dem Browser für den Deutschen Bundestag</h1>
    </div>
    <div class="interface">
        <#if missingProtocols == 0>
            <h2>Browser auf neustem Stand</h2>
        <#else>
            <div>
                <h2>Browser nicht auf neustem Stand</h2>
                <#if createSession!false && nlpProcessing!false>
                    <button class="submitButton" onclick="downloadProtocolsAndAnalyze()">Protokolle herunterladen <br> und verarbeiten</button>
                </#if>
            </div>
            <p>Es fehlen ${missingProtocols} Protokolle.</p>
        </#if>
        <#if unprocessedSpeeches == 0>
            <h2>Alle Reden erfolgreich verarbeitet</h2>
        <#else>
            <div>
                <h2>Reden verarbeiten</h2>
                <#if nlpProcessing!false>
                    <button class="submitButton" onclick="processMissingSpeeches()">Reden verarbeiten</button>
                </#if>
            </div>
            <p>Es fehlen ${unprocessedSpeeches} Reden. Das kann daran liegen, dass ein Fehler bei der Verarbeitung aufgetreten ist oder die Rede händisch bearbeitet wurde und neu verarbeitet werden muss.</p>
        </#if>
    </div>
</div>


</body>

</html>
