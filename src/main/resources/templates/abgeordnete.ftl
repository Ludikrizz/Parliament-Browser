<html lang="de">

<head>
    <title>InsightBundestag</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <meta charset="UTF-8">
    <meta name="description" content="Visualisierung von Abgeordneten und Reden im Bundestag">
    <meta name="author" content="Kimon Raschke"> <#-- erweitert und Modifiziert von Abgabe 4-->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="script/user/userLogin.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<body>
<header>
    <nav>
        <ul>
            <li><a href="/">Home</a></li>
            <li id="current"><a href="/abgeordnete">Abgeordnete</a></li>
            <li><a href="/reden">Reden</a></li>
            <li><a href="/charts">Charts</a></li>
        </ul>
    </nav>

    <#import 'login.ftl' as l>
    <@l.login loggedIn=loggedIn!false/>
</header>
<div class="search-and-calender">
    <div class="search-input">
        <form>
            <input class="searchInput" type="text" name="vorname" placeholder="Vorname...">
            <input class="searchInput" type="text" name="nachname" placeholder="Nachname...">
            <button id="searchButton"><i class="fa-solid fa-magnifying-glass"></i></button>
        </form>
    </div>
    <#if createRep!false>
        <div class="addABButtonContainer">
            <button class="submitButton" onclick="location.href='/edit/abgeordneter'">Abgeordneten Hinzufügen</button>
        </div>
    </#if>
</div>
<div class="container">
    <div class="list">
        <div class="listElement listHeader">
            <p>Name:</p>
            <span>
                <p class="abgeordnetenListPartei">Partei:</p>
                <p class="abgeordnetenListFraktion">Fraktion in der aktuellen Wahlperiode:</p>
            </span>
        </div>
        <#list abgeordnete as ab>
            <div class="listElement">
                <a href="/abgeordnete/${ab.getStringID()}"> ${ab.getVorname()} ${ab.getName()}</a>
                <span>
                    <#if ab.getPartei()??>
                        <p class="abgeordnetenListPartei">${ab.getPartei().getName()}</p>
                    <#else>
                        <p class="abgeordnetenListPartei"></p>
                    </#if>
                    <#if (ab.getCurrentFraktion())??>
                        <p class="abgeordnetenListFraktion">${ab.getCurrentFraktion().getName()}</p>
                    <#else>
                        <p class="abgeordnetenListFraktion"></p>
                    </#if>
                </span>
            </div>
        </#list>
    </div>
</div>
</body>

</html>
