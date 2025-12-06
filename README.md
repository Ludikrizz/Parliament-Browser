# Parliament_Browser_04_1


Benutzer-Handbuch:
Das Programm läuft mit folgender Klasse src/main/java/org/group_04_01/RunServer.java
Die Seite aufrufen mit http://localhost:4567

In der Navigationsleiste oben kann man zwischen den verschiedenen Seiten(Home, Abgeordnete, Reden, Charts) wechseln.
Bei den Abgeordneten kann man nach Namen suchen und auf die einzelnen Abgeordneten klicken, um mehr Informationen zu erhalten.
Die Reden haben auch eine Suchfunktion.
Bei den Charts muss man ein Start-, Enddatum und ein Suchbegriff eingeben, damit die Charts angezeigt werden.

Anmelden kann man sich, indem man oben rechts auf den Button "User" klickt. 

Die Login-Daten für Admin sind: 
Nutzername: Admin
Passwort: admin

Je nachdem welche Rechte ein Nutzer hat, kann er verschiedene Funktionen nutzen.
Als Admin hat man alle Funktionen zur Verfügung. 

Dieser kann auf der Home Seite die NLP-Analyse starten.
Die Links für die NLP-Verarbeitung sind in der Klasse src/main/java/org/group_04_01/dataprocessing/helper/NLPAnalyzer.java zu finden.

Zudem kann er bei den Abgeordneneten und Reden neue Abgeordnete, Reden hinzufügen, bearbeiten und löschen.
Und wenn er oben rechts auf User klickt und in die Einstellungen geht, kann er neue Nutzer hinzufügen,
bearbeiten, löschen, Gruppen ändern und Rechte verwalten.

Man kann sich wieder ausloggen, indem man auf User und auf Logout klickt.
