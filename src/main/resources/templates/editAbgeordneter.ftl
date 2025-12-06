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
    <script src="../script/edit/abgeordneter.js"></script>
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
<div class="container">
    <div class="upperSection">
        <div class="mainProfileEdit">
            <label for="vorname">Vorname:
                <input type="text" id="vorname" name="vorname" <#if abgeordneter??> value="${abgeordneter.getVorname()}" </#if>>
            </label>
            <label for="nachname">Nachname:
                <input type="text" id="nachname" name="nachname" <#if abgeordneter??> value="${abgeordneter.getName()}" </#if>>
            </label>
            <label for="anrede">Anrede:
                <input type="text" id="anrede" name="anrede" <#if abgeordneter?? &&abgeordneter.getAnrede()?has_content> value="${abgeordneter.getAnrede()}" </#if>>
            </label>
            <label for="ortszusatz">Ortszusatz:
                <input type="text" id="ortszusatz" name="ortszusatz" <#if abgeordneter?? &&abgeordneter.getOrtszusatz()?has_content > value="${abgeordneter.getOrtszusatz()}" </#if>>
            </label>
            <label for="adelssuffix">Adelssuffix:
                <input type="text" id="adelssuffix" name="adelssuffix" <#if abgeordneter?? &&abgeordneter.getAdelssuffix()?has_content > value="${abgeordneter.getAdelssuffix()}" </#if>>
            </label>
            <label for="akadTitel">Akademischer Titel:
                <input type="text" id="akadTitel" name="akadTitel" <#if abgeordneter?? && abgeordneter.getAkadTitel()?has_content> value="${abgeordneter.getAkadTitel()}" </#if>>
            </label>
            <label for="geburtsdatum">Geburtsdatum:
                <input type="date" id="geburtsdatum" name="geburtsdatum" <#if abgeordneter?? && abgeordneter.getGeburtsDatum()?has_content> value="${abgeordneter.getGeburtsDatum()?string('yyyy-MM-dd')}" </#if>>
            </label>
            <label for="geburtsort">Geburtsort:
                <input type="text" id="geburtsort" name="geburtsort" <#if abgeordneter?? && abgeordneter.getGeburtsOrt()?has_content> value="${abgeordneter.getGeburtsOrt()}" </#if>>
            </label>
            <label for="sterbedatum">Sterbedatum:
                <input type="date" id="sterbedatum" name="sterbedatum" <#if abgeordneter?? && abgeordneter.getSterbeDatum()?has_content> value="${abgeordneter.getSterbeDatum()?string('yyyy-MM-dd')}" </#if>>
            </label>
            <label for="geschlecht">Geschlecht:
                <select id="geschlecht" name="geschlecht">
                    <option value="MAENNLICH" <#if abgeordneter?? && abgeordneter.getGeschlecht()?has_content && abgeordneter.getGeschlecht() == "MAENNLICH"> selected </#if>>Männlich</option>
                    <option value="WEIBLICH" <#if abgeordneter?? && abgeordneter.getGeschlecht()?has_content && abgeordneter.getGeschlecht() == "WEIBLICH"> selected </#if>>Weiblich</option>
                </select>
            </label>
            <label for="religion">Religion:
                <input type="text" id="religion" name="religion" <#if abgeordneter?? && abgeordneter.getReligion()?has_content> value="${abgeordneter.getReligion()}" </#if>>
            </label>
            <label for="beruf">Beruf:
                <input type="text" id="beruf" name="beruf" <#if abgeordneter?? && abgeordneter.getBeruf()?has_content> value="${abgeordneter.getBeruf()}" </#if>>
            </label>
            <label for="partei">Partei:
                <input type="text" id="partei" name="partei" <#if abgeordneter?? && abgeordneter.getPartei()?has_content> value="${abgeordneter.getPartei().getName()}" </#if>>
            </label>
        </div>
        <div class="profileImage">
            <#if abgeordneter?? && abgeordneter.listPictures()?has_content>
                <#list abgeordneter.listPictures() as image>
                    <div class="imageDisplayer" id="${image.getLocalURLBackend()}">
                        <div class="imageContainer">
                            <img src="../../${image.getLocalURL()}" alt="Profilbild" remoteurl="${image.getRemoteURL()}">
                            <label for="location">Location:
                                <input type="text" id="location" name="location" value="${image.getLocation()}">
                            </label>
                            <label for="date">Datum:
                                <input type="text" id="date" name="date" value="${image.getDate()}">
                            </label>
                            <label for="photographer">Fotograf(in):
                                <input type="text" id="photographer" name="photographer" value="${image.getPhotographer()}">
                            </label>
                        </div>
                    </div>
                </#list>
                <div>
                    <label for="selectedImage">Profilbild auswählen:
                        <select id="selectedImage" name="selectedImage">
                            <#assign counter= 0>
                            <#list abgeordneter.listPictures() as image>
                                <option value="${image.getLocalURLBackend()}">Image ${counter + 1}</option>
                                <#assign counter = counter + 1>
                            </#list>
                        </select>
                    </label>
                </div>

            <#else>
                <div class="imageContainer">
                    <img src = "../data/pictures/placeholder.jpg" alt="PlaceholderProfilbild">
                </div>

            </#if>

        </div>

    </div>

    <div class="vita">
        <label for="vita">Vita:
            <textarea id="vita" name="vita" rows="7">
                <#if abgeordneter??>${abgeordneter.getVita()} </#if>
            </textarea>
        </label>
    </div>

    <div class="lowerSection">
        <div class="sectionElement mandate">
            <h2>Mandate</h2>
            <div class="sectionList">
                <#if abgeordneter?? && abgeordneter.listMandate()?has_content>
                    <#list abgeordneter.listMandate() as mandate>
                        <div class="sectionDisplayer">
                            <label for="mandateTyp">Mandat:
                                <select id="mandateTyp" name="mandateTyp">
                                    <option value="DIREKTWAHL" <#if mandate.getTyp() == "DIREKTWAHL"> selected </#if>>Direktwahl</option>
                                    <option value="LANDESLISTE" <#if mandate.getTyp() == "LANDESLISTE"> selected </#if>>Landesliste</option>

                                </select>
                            </label>
                            <label for="mandateStart">Start:
                                <input type="date" id="mandateStart" name="mandateStart" <#if mandate.getWahlperiode().getStartDate()?has_content>value="${mandate.getWahlperiode().getStartDate()?string('yyyy-MM-dd')}"</#if>">
                            </label>
                            <label for="mandateEnd">Ende:
                                <input type="date" id="mandateEnd" name="mandateEnd" <#if mandate.getWahlperiode().getEndeDate()?has_content>value="${mandate.getWahlperiode().getEndeDate()?string('yyyy-MM-dd')}"</#if>>
                            </label>
                            <label for="mandateWahlperiode">Wahlperiode:
                                <input type="number" id="mandateWahlperiode" name="mandateWahlperiode" value="${mandate.getWahlperiode().getNumber()}">
                            </label>
                            <button class="submitButton buttonRed removeBtn">⎯</button>
                        </div>
                    </#list>
                <#else>
                    <div class="sectionDisplayer">
                        <label for="mandateTyp">Mandat:
                            <select id="mandateTyp" name="mandateTyp">
                                <option value="DIREKTWAHL">Direktwahl</option>
                                <option value="LANDESLISTE">Landesliste</option>

                            </select>
                        </label>
                        <label for="mandateStart">Start:
                            <input type="date" id="mandateStart" name="mandateStart">
                        </label>
                        <label for="mandateEnd">Ende:
                            <input type="date" id="mandateEnd" name="mandateEnd">
                        </label>
                        <label for="mandateWahlperiode">Wahlperiode:
                            <input type="number" id="mandateWahlperiode" name="mandateWahlperiode">
                        </label>
                        <button class="submitButton buttonRed removeBtn">⎯</button>
                    </div>
                </#if>
            </div>
            <div class="addSectionElement">
                <button class="submitButton" id="addMandat" onclick="addMandat()">Mandat hinzufügen</button>
            </div>
        </div>

        <div class="sectionElement fraktionsMitgliedschaften">
            <h2>Fraktionsmitgliedschaft</h2>
            <div class="sectionList">
                <#if abgeordneter?? && abgeordneter.listFraktionsmitgliedschaften()?has_content>
                    <#list abgeordneter.listFraktionsmitgliedschaften() as fm>
                        <div class="sectionDisplayer">
                            <label for="fraktionsMitgliedschaft">Fraktion:
                                <input type="text" id="fraktionsMitgliedschaft" name="fraktionsMitgliedschaft" value="${fm.getFraktion().getName()}">
                            </label>
                            <label for="fmFromDate">Start:
                                <input type="date" id="fmFromDate" name="fmFromDate" <#if fm.fromDate()?has_content>value="${fm.fromDate()?string('yyyy-MM-dd')}"</#if>>
                            </label>
                            <label for="fmToDate">Ende:
                                <input type="date" id="fmToDate" name="fmToDate" <#if fm.toDate()?has_content>value="${fm.toDate()?string('yyyy-MM-dd')}"</#if>>
                            </label>
                            <label for="fmWahlperiode">Wahlperiode:
                                <input type="number" id="fmWahlperiode" name="fmWahlperiode" value="${fm.getWahlperiode().getNumber()}">
                            </label>
                            <button class="submitButton buttonRed removeBtn">⎯</button>
                        </div>
                    </#list>
                <#else>
                    <div class="sectionDisplayer">
                        <label for="fraktionsMitgliedschaft">Fraktion:
                            <input type="text" id="fraktionsMitgliedschaft" name="fraktionsMitgliedschaft">
                        </label>
                        <label for="fmFromDate">Start:
                            <input type="date" id="fmFromDate" name="fmFromDate">
                        </label>
                        <label for="fmToDate">Ende:
                            <input type="date" id="fmToDate">
                        </label>
                        <label for="fmWahlperiode">Wahlperiode:
                            <input type="number" id="fmWahlperiode" name="fmWahlperiode">
                        </label>
                        <button class="submitButton buttonRed removeBtn">⎯</button>
                    </div>
                </#if>
            </div>
            <div class="addSectionElement">
                <button class="submitButton" onclick="addFM()">Fraktionsmitgliedschaft hinzufügen</button>
            </div>
        </div>

        <div class="sectionElement institutionen">
            <h2>Institutionen in der 20 Wahlperiode</h2>
            <div class="sectionList">
                <#if abgeordneter?? && abgeordneter.listInstitutionen()?has_content>
                    <#list abgeordneter.listInstitutionen() as inst>
                        <div class="sectionDisplayer">
                            <label for="instArt">Institution:
                                <input type="text" id="instArt" name="instArt" value="${inst.getInstArt()}">
                            </label>
                            <label for="instTitel">Titel
                                <input type="text" id="instTitel" name="instTitel" value="${inst.getTitel()}">
                            </label>
                            <label for="instFunktion">Funktion:
                                <input type="text" id="instFunktion" name="instFunktion" value="${inst.getFunktion()}">
                            </label>
                            <label for="instStartDate">Start:
                                <input type="date" id="instStartDate" name="instStartDate" <#if inst.getStartDate()?has_content>value="${inst.getStartDate()?string('yyyy-MM-dd')}"</#if>">
                            </label>
                            <button class="submitButton buttonRed removeBtn">⎯</button>
                        </div>
                    </#list>
                <#else>
                    <div class="sectionDisplayer">
                        <label for="instArt">Institution:
                            <input type="text" id="instArt" name="instArt">
                        </label>
                        <label for="instTitel">Titel
                            <input type="text" id="instTitel" name="instTitel">
                        </label>
                        <label for="instFunktion">Funktion:
                            <input type="text" id="instFunktion" name="instFunktion">
                        </label>
                        <label for="instStartDate">Start:
                            <input type="date" id="instStartDate" name="instStartDate">
                        </label>
                        <button class="submitButton buttonRed removeBtn">⎯</button>
                    </div>
                </#if>
            </div>
            <div class="addSectionElement">
                <button class="submitButton" onclick="addInst()">Institution hinzufügen</button>
            </div>
        </div>
    </div>

    <div class="buttons">
        <button class="submitButton" onclick="saveAb()">Speichern</button>
        <#if abgeordneter?? && deleteRep!false>
            <button class="submitButton buttonRed" onclick="deleteAb()">Löschen</button>
        </#if>
    </div>
</div>

</body>

</html>