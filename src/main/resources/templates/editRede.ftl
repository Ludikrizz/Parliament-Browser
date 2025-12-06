
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
    <script src="../script/edit/rede.js"></script>
</head>

<body>
<header>
    <nav>
        <ul>
            <li><a href="/">Home</a></li>
            <li><a href="/abgeordnete">Abgeordnete</a></li>
            <li id="current"><a href="/reden">Reden</a></li>
            <li><a href="/charts">Charts</a></li>
        </ul>
    </nav>

    <#import 'login.ftl' as l>
    <@l.login loggedIn=loggedIn!false/>
</header>
<div class="container">
    <div class="upperSection">
        <div class="mainProfileEdit">
            <label for="abgeordneter">Abgeordneter:
                <select id="abgeordneter" name="abgeordneter">
                    <#list abgeordnete as abgeordneter>
                        <option value="${abgeordneter.getStringID()}" <#if rede?? && abgeordneter.getStringID() == rede.getAbgeordneter().getStringID()> selected </#if>>${abgeordneter.getVorname()} ${abgeordneter.getName()}</option>
                    </#list>
                </select>
            </label>
            <h3>Sitzung:</h3>
            <label for="sitzungsnummer">Sitzungsnummer:
                <input type="number" id="sitzungsnummer" name="sitzungsnummer" <#if rede??>value="${rede.getSitzung().getSitzungsnummer()}"</#if>>
            </label>
            <label for="wahlperiode">Wahlperiode:
                <input type="number" id="wahlperiode" name="wahlperiode" <#if rede??>value="${rede.getSitzung().getWahlperiode().getNumber()}"</#if>>
            </label>
            <label for="sitzungsdatum">Datum:
                <input type="date" id="sitzungsdatum" name="sitzungsdatum" <#if rede??>value="${rede.getSitzung().getDate()?string('yyyy-MM-dd')}"</#if>>
            </label>
            <label for="sitzungsbeginn">Beginn:
                <input type="time" id="sitzungsbeginn" name="sitzungsbeginn" <#if rede??>value="${rede.getSitzung().getSitzungsbeginn()}"</#if>>
            </label>
            <label for="sitzungsende">Ende:
                <input type="time" id="sitzungsende" name="sitzungsende" <#if rede??>value="${rede.getSitzung().getSitzungsende()}"</#if>>
            </label>
            <h3>Tagesordnungspunkt</h3>
            <label for="tagesordnungspunkt">Nummer:
                <input type="number" id="tagesordnungspunkt" name="tagesordnungspunkt" <#if rede??>value="${rede.getTagesordnung().getTagesordnungspunkt()}"</#if>>
            </label>
            <label for="topTitel">Text:
                <input type="text" id="topTitel" name="topTitel" <#if rede??>value="${rede.getTagesordnung().getTitel()}"</#if>>
            </label>
        </div>
    </div>

    <div class="speechSection">
        <h2>Text</h2>
        <label for="text">
            <textarea id="text" name="text" rows="30" onmouseup="cursorPosition()"><#if rede??>${rede.getText()}</#if></textarea>
        </label>
        <span>
            <h5>Cursor Position: </h5>
            <h5 id="cursorPosition"></h5>
        </span>
        <div class="commentList">
            <h2>Kommentare:</h2>
            <#if rede??>
                <#list rede.getKommentare() as kommentar>
                    <div class="comment">
                        <label class="resizable" for="kommentarText">Text:
                            <input type="text" id="kommentarText" name="kommentarText" value="${kommentar.getText()}">
                        </label>
                        <label for="abgeordneter">Abgeordneter:
                            <select id="abgeordneter" name="abgeordneter">
                                <#if !kommentar.getAbgeordneter()??>
                                    <option disabled selected value> -- select --</option>
                                </#if>
                                <#list abgeordnete as abgeordneter>
                                    <option value="${abgeordneter.getStringID()}" <#if kommentar.getAbgeordneter()?? && abgeordneter.getStringID() == kommentar.getAbgeordneter().getStringID()> selected </#if>>${abgeordneter.getVorname()} ${abgeordneter.getName()}</option>
                                </#list>
                            </select>
                        </label>
                        <div class="resizable fraktionen">
                            <p>Fraktionen: </p>
                            <#list kommentar.getFraktionen() as fraktion>
                                <label for="fraktion">
                                    <input type="text" id="fraktion" name="fraktion" value="${fraktion.getName()}">
                                </label>
                            </#list>
                            <button class="submitButton addFraktionBtn" onclick="addFraktion()">+</button>
                        </div>
                        <label for="position">Position:
                            <input type="number" id="position" name="position" value="${kommentar.getPos()}">
                        </label>
                        <div>
                            <button class="submitButton buttonRed removeBtn">⎯</button>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="comment">
                    <label class="resizable" for="kommentarText">Text:
                        <input type="text" id="kommentarText" name="kommentarText">
                    </label>
                    <label for="abgeordneter">Abgeordneter:
                        <select id="abgeordneter" name="abgeordneter">
                            <option disabled selected value> -- select --</option>
                            <#list abgeordnete as abgeordneter>
                                <option value="${abgeordneter.getStringID()}" >${abgeordneter.getVorname()} ${abgeordneter.getName()}</option>
                            </#list>
                        </select>
                    </label>
                    <div class="resizable fraktionen">
                        <p>Fraktionen: </p>
                        <label for="fraktion">
                            <input type="text" id="fraktion" name="fraktion">
                        </label>
                        <button class="submitButton addFraktionBtn">+</button>
                    </div>
                    <label for="position">Position:
                        <input type="number" id="position" name="position">
                    </label>
                    <div>
                        <button class="submitButton buttonRed removeBtn">⎯</button>
                    </div>
                </div>
            </#if>
        </div>
        <div>
            <button class="submitButton addBtn" onclick="addComment()">Kommentar Hinzufügen</button>
        </div>

        <div class="buttons">
            <button class="submitButton" onclick="saveSpeech()">Speichern</button>
            <#if rede?? && deleteSpeech!false>
                <button class="submitButton buttonRed" onclick="deleteSpeech()">Löschen</button>
            </#if>
        </div>
    </div>

</div>

</body>

</html>
