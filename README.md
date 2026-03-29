# 🏛️ Parlament Datenverarbeitung

Dieses Projekt bietet eine umfassende Verarbeitung von Parlamentsdaten des Deutschen Bundestags. Diese werden analysiert, in einer MongoDB-Datenbank gespeichert und über eine REST-API bereitgestellt. Es umfasst Funktionen für Web Scraping, XML-Parsing, komplexe NLP-Analysen via UIMA und Frontend-Management.

## 🚀 Hauptfunktionen

- **Datenaufnahme**: Ruft Parlamentsdaten und Protokolle von externen Quellen (Webseite des Deutschen Bundestages/Open Data) ab der 19. Legislaturperiode mithilfe von Web-Scraping-Techniken (Jsoup) ab. Beinhaltet eine Prüfung auf bereits vorhandene Daten.
- **XML-Parsing**: Parsen XML-Daten aus Parlamentssitzungen, um relevante Informationen (Reden, Kommentare, Redner, Fraktionen) zu extrahieren und strukturiert abzubilden.
- **Datenspeicherung**: Speichert verarbeitete Daten in einer MongoDB-Datenbank. Umfasst Collections für Reden, Protokolle und Abgeordnete (inkl. Metadaten und Bildern).
- **NLP-Analyse**: Integriert eine UIMA-Pipeline via DUUI (Docker Unified UIMA Interface) für:
  - **Tokenisierung, POS-Tagging, Named Entity Recognition (NER)** mittels spaCy.
  - **Topic Modeling** mittels ParlBERT-v2.
  - **Sentiment Analyse** mittels GerVaderSentiment.
- **REST-API**: Stellt eine RESTful API unter Verwendung des Spark-Frameworks bereit, dokumentiert via Swagger.io.
- **Benutzer- & Rechteverwaltung**: Implementiert ein rollenbasiertes Zugriffssystem für Login, Feature-Management und Bearbeitungsrechte.
- **Datenvisualisierung (Frontend)**: Interaktive Visualisierungen mittels **d3.js**:
  - **Sunburst-Diagramme** für Topics.
  - **Bar-Charts** für POS-Verteilungen und Redner-Statistiken.
  - **Radar-Charts** für Sentiment-Analysen.
  - **Bubble-Charts** für Named Entities.
  - Volltextansicht mit Highlighting von Entities und Sentiment-Icons.
- **Export-Funktion**: Generierung von LaTeX-basierten Parlamentsprotokollen (PDF-Vorschau) inklusive Inhaltsverzeichnis und Bildern.
- **Umfassender Datenabruf**: Bietet Endpunkte zum Abruf und zur Filterung von Sitzungsdaten nach Zeiträumen, Rednern, Fraktionen und NLP-Merkmalen.

## 🛠️ Technologie-Stack

- **Backend**: Java
- **Web-Framework**: Spark Java
- **Frontend**: FreeMarker, JavaScript, jQuery
- **Visualisierung**: d3.js
- **Datenbank**: MongoDB
- **XML-Parsing**: dom4j, jaxen
- **Web Scraping**: Jsoup
- **NLP**: Docker Unified UIMA Interface (DUUI), spaCy, ParlBERT-v2, GerVaderSentiment
- **API Dokumentation**: Swagger.io
- **JSON-Verarbeitung**: Jackson
- **Build-Tool**: Maven
- **Testing**: JUnit 5

## 📦 Erste Schritte

Diese Anleitung führt Sie durch die Einrichtung und lokale Ausführung des Projekts.

### Voraussetzungen

- Java 11 oder höher
- Maven
- MongoDB installiert und laufend
- Docker (für die NLP/DUUI-Komponente)

### Installation

1.  **Klonen Sie das Repository:**

    ```bash
    git clone <repository_url>
    cd <project_directory>
    ```

2.  **Konfigurieren Sie die MongoDB-Verbindung:**

    - Erstellen Sie eine Datei namens `auth.txt` im Verzeichnis `src/main/resources/`.
    - Fügen Sie Ihre MongoDB-Verbindungsdaten im folgenden Format hinzu:

      ```
      username=<ihr_mongodb_benutzername>
      password=<ihr_mongodb_passwort>
      database=<ihr_mongodb_datenbankname>
      ```

3.  **Bauen Sie das Projekt mit Maven:**

    ```bash
    mvn clean install
    ```

### Lokal Ausführen

1.  **Führen Sie die Datei `RunServer.java` aus:**

    - Navigieren Sie in Ihrer IDE zu `src/main/java/org/group_04_01/RunServer.java`.
    - Machen Sie einen Rechtsklick und wählen Sie "Run" oder "Debug".

2.  **Zugriff auf die API und das Frontend:**

    - Der Server läuft unter `http://localhost:4567`.
    - API-Dokumentation (Swagger) ist verfügbar (entsprechend Konfiguration).
    - Sie können Tools wie Postman oder curl verwenden, um mit den API-Endpunkten zu interagieren.

## 📂 Projektstruktur

```text
├── pom.xml                           # Maven Projektkonfigurationsdatei
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── org
│   │   │   │   ├── v_04_01
│   │   │   │   │   ├── RunServer.java            # Einstiegspunkt zum Starten des Servers
│   │   │   │   │   ├── dataprocessing
│   │   │   │   │   │   ├── DataFactory.java        # Datenverarbeitung und Datenbankinteraktion
│   │   │   │   │   │   ├── datastructure
│   │   │   │   │   │   │   ├── implementation      # Implementierungen von Datenstrukturen (Rede, Sitzung, etc.)
│   │   │   │   │   │   │   ├── interface           # Interfaces für Datenstrukturen
│   │   │   │   │   │   ├── helper
│   │   │   │   │   │   │   ├── Webscraper.java       # Web-Scraping-Logik (Jsoup)
│   │   │   │   │   │   │   ├── NLPHandler.java       # Anbindung an DUUI/Docker
│   │   │   │   │   ├── database
│   │   │   │   │   │   ├── MongoDBHandler.java     # MongoDB-Datenbank-Handler
│   │   │   │   │   ├── rest
│   │   │   │   │   │   ├── RESTHelper.java         # REST-API-Endpunkte mittels Spark
│   │   │   │   │   ├── security
│   │   │   │   │   │   ├── Password.java           # Passwort-Hashing und -Verifizierung
│   │   ├── resources
│   │   │   ├── auth.txt                  # MongoDB-Authentifizierungsdetails
│   │   │   ├── freemarker                # FreeMarker-Template-Dateien (Frontend)
│   └── test
│       └── java
│           └── Test.java        # Beispielhafte JUnit-Tests
```
