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
        <#if (abgeordneter.getAnrede())?has_content>
            ${abgeordneter.getAnrede()}
        </#if>${abgeordneter.getVorname()} ${abgeordneter.getName()}</h1>
    <div class="image">
        <img src=${"../" + abgeordneter.getPrimaryPicture().getLocalURL()}/>
    </div>
    <#if editRep!false>
        <button id="editABButton" class="submitButton"
                onclick="location.href='/edit/abgeordneter?id=${abgeordneter.getStringID()}'">Abgeordneten Bearbeiten
        </button>
    </#if>
    <div class="steckbrief">
        <#if (abgeordneter.getOrtszusatz())?has_content>
            <p>
                <bold>Ortszusatz:</bold> ${abgeordneter.getOrtszusatz()}</p>
        </#if>
        <#if (abgeordneter.getAdelssuffix())?has_content>
            <p>
                <bold>Adelssuffix:</bold> ${abgeordneter.getAdelssuffix()}</p>
        </#if>
        <#if (abgeordneter.getAkadTitel())?has_content>
            <p>
                <bold>Akademischer Titel:</bold> ${abgeordneter.getAkadTitel()}</p>
        </#if>
        <#if (abgeordneter.getGeburtsDatum())?has_content>
            <p>
                <bold>Geburtsdatum:</bold> ${abgeordneter.getGeburtsDatum()}</p>
        </#if>
        <#if (abgeordneter.getGeburtsOrt())?has_content>
            <p>
                <bold>Geburtsort:</bold> ${abgeordneter.getGeburtsOrt()}</p>
        </#if>
        <#if (abgeordneter.getSterbeDatum())?has_content>
            <p>
                <bold>Sterbedatum:</bold> ${abgeordneter.getSterbeDatum()}</p>
        </#if>
        <#if (abgeordneter.getGeschlecht())?has_content>
            <p>
                <bold>Geschlecht:</bold> ${abgeordneter.getGeschlecht()}</p>
        </#if>
        <#if (abgeordneter.getReligion())?has_content>
            <p>
                <bold>Religion:</bold> ${abgeordneter.getReligion()}</p>
        </#if>
        <#if (abgeordneter.getBeruf())?has_content>
            <p>
                <bold>Beruf:</bold> ${abgeordneter.getBeruf()}</p>
        </#if>
        <#if (abgeordneter.getPartei())?has_content>
            <p>
                <bold>Partei:</bold> ${abgeordneter.getPartei().getName()}</p>
        <#else >
            <p>
                <bold>Partei:</bold>
                Parteilos
            </p>
        </#if>
        <#if (abgeordneter.getVita())?has_content>
            <p>
                <bold>Vita:</bold> ${abgeordneter.getVita()}</p>
        </#if>
    </div>
    <div class="abgeordneterMandate">
        <h2>Mandate + Fraktionszugehörigkeit</h2>
        <table>
            <tr>
                <th>Wahlperiode</th>
                <th>Mandartsart</th>
                <th>Fraktion</th>
            </tr>
            <#assign fraktionsmitgliedschaften = abgeordneter.listFraktionsmitgliedschaftenAsList()>
            <#list abgeordneter.listMandate() as m>
                <tr>
                    <td>${m.getWahlperiode().getNumber()}</td>
                    <td>${m.getTyp()}</td>
                    <td>
                        <#if fraktionsmitgliedschaften?has_content>
                            ${fraktionsmitgliedschaften[0].getFraktion().getName()}
                        </#if>
                    </td>
                </tr>
            </#list>
        </table>
    </div>

    <div class="abgeordneterInst">
        <h2>Institutionen</h2>
        <table>
            <tr>
                <th>Institution</th>
                <th>Titel</th>
                <th>Funktion</th>
                <th>Beigetreten am</th>
            </tr>
            <#list abgeordneter.listInstitutionen() as i>
                <tr>
                    <td>${i.getInstArt()}</td>
                    <td>${i.getTitel()}</td>
                    <td>${i.getFunktion()}</td>
                    <td>${i.getStartDate()}</td>
                </tr>
            </#list>
        </table>
    </div>


    <div class="abgeordneterReden">
        <h2>Reden</h2>
        <table>
            <thead>
            <tr>
                <th>Sitzung</th>
                <th>Datum</th>
            </tr>
            </thead>
            <tbody>
            <#list abgeordneter.listReden() as r>
                <tr onclick="location.href='/reden/${r.getID()}'">
                    <td>${r.getSitzung().getSitzungsnummer()}</td>
                    <td>${r.getDate()}</td>

                </tr>
            </#list>
            </tbody>
        </table>
    </div>

</div>
</body>

</html>
