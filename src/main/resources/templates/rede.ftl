<html lang="de">

<head>
    <title>InsightBundestag</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
    <meta charset="UTF-8">
    <meta name="description" content="Visualisierung von Abgeordneten und Reden im Bundestag">
    <meta name="author" content="Kimon Raschke"> <#-- erweitert und Modifiziert von Abgabe 4-->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="../script/user/userLogin.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

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

    <h1 id="abgeordneterName">
        <#if (rede.getAbgeordneter().getAnrede())?has_content>
            ${rede.getAbgeordneter().getAnrede()}
        </#if>${rede.getAbgeordneter().getVorname()} ${rede.getAbgeordneter().getName()}</h1>

    <div class="image">
        <img src=${"../" + rede.getAbgeordneter().getPrimaryPicture().getLocalURL()}/>
    </div>

    <#if editSpeech!false>
        <button id="editABButton" class="submitButton" onclick="location.href='/edit/rede?id=${rede.getID()}'">Rede Bearbeiten</button>
    </#if>

    <div class="steckbrief">

        <#if (rede.getDate())?has_content>
            <p>
                <bold>Datum:</bold> ${rede.getSitzung().getDate()}</p>
        </#if>
        <#if (rede.getSitzung().getSitzungsnummer())?has_content>
            <p>
                <bold>Sitzungsnummer:</bold> ${rede.getSitzung().getSitzungsnummer()}</p>
        </#if>
        <#if (rede.getSitzung().getWahlperiode())?has_content>
            <p>
                <bold>Wahlperiode:</bold> ${rede.getSitzung().getWahlperiode().getNumber()}</p>
        </#if>
        <#if (rede.getSitzung().getSitzungsbeginn())?has_content>
            <p>
                <bold>Sitzungsbeginn:</bold> ${rede.getSitzung().getSitzungsbeginn()}</p>
        </#if>
        <#if (rede.getSitzung().getSitzungsende())?has_content>
            <p>
                <bold>Sitzungsbeginn:</bold> ${rede.getSitzung().getSitzungsende()}</p>
        </#if>
        <#if (rede.getTagesordnung().getTitel())?has_content>
            <p>
                <bold>Tagesordnungspunkt:</bold> ${rede.getTagesordnung().getTitel()}</p>
        </#if>
        <#if (rede.getLaenge())?has_content>
            <p>
                <bold>Wörter:</bold> ${rede.getLaenge()}</p>
        </#if>
        <#if (rede.getAbgeordneter().getPartei())?has_content>
            <p>
                <bold>Partei:</bold> ${rede.getAbgeordneter().getPartei().getName()}
            </p>
        </#if>
        <#if (rede.getAbgeordneter().getCurrentFraktion())?has_content>
            <p>
                <bold>Fraktion:</bold> ${rede.getAbgeordneter().getCurrentFraktion().getName()}</p>
        </#if>

    </div>


    <div class="legende">
        <h2>Named Entities:</h2>
        <div class="namedEntities">
            <p>
                <mark class="PER_NLP">PERSON</mark>
                - Personen (auch Fiktionale)
            </p>
            <p>
                <mark class="ORG_NLP">ORG</mark>
                - Firmen, Agenturen, Institutionen, etc.
            </p>
            <p>
                <mark class="LOC_NLP">LOC</mark>
                - Nicht GPE-Standorte, wie Gebirge, Gewässer
            </p>
            <p>
                <mark class="MISC_NLP">MISC</mark>
                - Dinge, die nicht unter eine andere Kategorie fallen
            </p>
        </div>
    </div>


    <div class="textContainer">
        ${rede.getPreprocessedText()}
    </div>

</div>
</body>

</html>
