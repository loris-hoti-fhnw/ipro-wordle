# Wordle

Individuelles Softwareprojekt im Rahmen des IPRO-Moduls an der FHNW.

---

## Projektübersicht

In diesem Projekt wird eine eigene Web-Version des Spiels Wordle entwickelt.

Das Ziel besteht darin, ein vollständiges und einfach bedienbares Wordle umzusetzen und dabei die im Studium erlernten Kenntnisse in den Bereichen Softwareentwicklung und User Interaction praktisch anzuwenden.

Das Projekt besteht aus einem Vue.js-Frontend und einem Java-Spring-Boot-Backend. Die Kommunikation zwischen beiden Anwendungen erfolgt über eine REST-Schnittstelle. Die Anwendung wurde zusätzlich auf Microsoft Azure bereitgestellt und kann dadurch ohne lokale Entwicklungsumgebung über das Web verwendet werden.

---

## Spielprinzip

Bei jedem neuen Spiel wird zufällig ein Wort mit genau fünf Buchstaben ausgewählt.

Der Spieler hat maximal sechs Versuche, dieses Wort zu erraten.

Nach jeder Eingabe werden die fünf Buchstaben ausgewertet:

- **Grün:** Der Buchstabe ist korrekt und befindet sich an der richtigen Position.
- **Gelb:** Der Buchstabe kommt im Wort vor, befindet sich aber an einer anderen Position.
- **Grau:** Der Buchstabe kommt im Lösungswort nicht vor.

Das Spiel endet, sobald das richtige Wort gefunden wurde oder alle sechs Versuche aufgebraucht wurden.

Nach Abschluss des Spiels wird das Lösungswort angezeigt und ein neues Spiel kann gestartet werden.

---

## Architektur

Das Projekt ist in Frontend und Backend aufgeteilt.

```text
                        HTTPS / REST
+-----------------------+          +----------------------+
|                       |          |                      |
| Vue Frontend          | <------> | Spring Boot Backend  |
| Azure Static Website  |          | Azure App Service    |
|                       |          |                      |
+-----------------------+          +----------+-----------+
                                              |
                                              |
                                     +--------v--------+
                                     |                 |
                                     | H2-Datenbank    |
                                     |                 |
                                     +-----------------+
```

### Frontend

Das Frontend übernimmt die Darstellung und Bedienung des Spiels.

Verwendete Technologien:

- Vue.js
- TypeScript
- Bootstrap
- Axios
- Vue Router

Zu den Aufgaben des Frontends gehören:

- Starten eines Spiels
- Eingabe eines Wortes
- Darstellung der Buchstaben in einzelnen Kacheln
- Anzeige bereits abgegebener Versuche
- Farbliche Darstellung des Feedbacks
- Anzeige einer virtuellen Tastatur
- Anzeige der Statistik während des Spiels
- Anzeige von bis zu zwei Hints pro Spiel
- Anzeige des Spielresultats
- Starten eines neuen Spiels

### Backend

Das Backend enthält die eigentliche Spiellogik.

Verwendete Technologien:

- Java
- Spring Boot
- Spring Data JPA / Hibernate
- H2

Das Backend ist unter anderem verantwortlich für:

- Erstellen eines neuen Spiels
- Laden der verfügbaren Wörter
- Zufällige Auswahl eines Lösungsworts
- Prüfen eines Versuchs
- Erstellen des Feedbacks
- Zählen der Versuche
- Erkennen eines gewonnenen oder verlorenen Spiels
- Prüfen, ob ein eingegebenes Wort in der Wortliste vorhanden ist
- Berechnen einfacher Spielstatistiken
- Verwalten von maximal zwei Hints pro Spiel
- Persistente Speicherung der Spieldaten

---

## Ablauf eines Spiels

Ein Spiel läuft vereinfacht folgendermassen ab:

```text
Neues Spiel starten
        |
        v
Backend wählt zufälliges Wort
        |
        v
Spiel wird gespeichert
        |
        v
Frontend erhält Spiel-ID
        |
        v
Spieler gibt 5 Buchstaben ein
        |
        v
Versuch wird an Backend geschickt
        |
        v
Backend prüft die Buchstaben
        |
        v
Frontend zeigt farbiges Feedback
        |
        v
Gewonnen / Verloren / nächster Versuch
```

---

## REST-Schnittstelle

Frontend und Backend kommunizieren über HTTP mit Hilfe von Axios.

### Neues Spiel

```http
GET /game/start
```

Erstellt ein neues Spiel und gibt dessen ID zurück.

Beispiel:

```json
{
  "gameId": 12
}
```

### Versuch senden

```http
POST /game/guess
```

Beispiel einer Anfrage:

```json
{
  "gameId": 12,
  "guess": "APFEL"
}
```

Beispiel einer Antwort:

```json
{
  "feedback": "GYYRG",
  "completed": false,
  "success": false,
  "solutionWord": "XXXXX"
}
```

Das Feedback enthält für jeden Buchstaben einen Status:

| Zeichen | Bedeutung |
|---|---|
| `G` | richtige Position |
| `Y` | im Wort, aber falsche Position |
| `R` | nicht im Wort |

### Statistik laden

```http
GET /game/stats
```

Liefert einfache Statistikwerte zu den abgeschlossenen Spielen, zum Beispiel die Anzahl gespielter Spiele, die Gesamtzahl der Versuche und den Durchschnitt.

### Hint verwenden

```http
GET /game/{gameId}/hint
```

Liefert einen Buchstaben aus dem Lösungswort. Pro Spiel können maximal zwei Hints verwendet werden.

### Validierung von Eingaben

Bevor ein Versuch gezählt wird, prüft das Backend, ob das eingegebene Wort in der vorhandenen Wortliste vorkommt. Ungültige Wörter werden abgelehnt und zählen nicht als Versuch.

---

# Anforderungen

Die Anforderungen wurden während des Projekts konkretisiert und können bei Bedarf angepasst werden.

## Kernanforderungen

| ID | Anforderung | Akzeptanzkriterium | Status |
|---|---|---|---|
| FA-01 | Ein neues Spiel kann gestartet werden. | Das Backend erstellt ein neues Spiel und liefert eine Spiel-ID. | ✅ |
| FA-02 | Das Spiel verwendet Wörter mit fünf Buchstaben. | Jedes verwendete Lösungswort besitzt genau fünf Buchstaben. | ✅ |
| FA-03 | Das Lösungswort wird zufällig bestimmt. | Bei einem neuen Spiel wird ein Wort aus der Wortliste ausgewählt. | ✅ |
| FA-04 | Der Spieler kann einen Versuch eingeben. | Eine Eingabe mit fünf Buchstaben kann an das Backend gesendet werden. | ✅ |
| FA-05 | Es stehen maximal sechs Versuche zur Verfügung. | Nach dem sechsten erfolglosen Versuch wird das Spiel beendet. | ✅ |
| FA-06 | Buchstaben werden nach ihrer Position ausgewertet. | Jeder Buchstabe erhält ein Feedback. | ✅ |
| FA-07 | Ein korrektes Lösungswort beendet das Spiel. | Bei Übereinstimmung wird das Spiel als gewonnen markiert. | ✅ |
| FA-08 | Ein erfolgloses Spiel wird beendet. | Nach sechs falschen Versuchen wird das Spiel als verloren markiert. | ✅ |
| FA-09 | Der Spielzustand wird persistent gespeichert. | Ein Spiel wird in der Datenbank gespeichert. | ✅ |
| FA-10 | Die Anzahl benötigter Versuche wird aufgezeichnet. | Die Versuchszahl wird für einen Spieldurchgang gespeichert. | ✅ |
| FA-11 | Eine Statistik über die Versuche wird erstellt. | Gespielte Spiele, Gesamtversuche und durchschnittliche Versuche werden angezeigt. | ✅ |

---

## Anforderungen an die Benutzeroberfläche

| ID | Anforderung | Status |
|---|---|---|
| UI-01 | Jeder Buchstabe wird in einer eigenen Kachel dargestellt. | ✅ |
| UI-02 | Jede Eingabe besteht aus fünf Kacheln. | ✅ |
| UI-03 | Abgegebene Versuche bleiben sichtbar. | ✅ |
| UI-04 | Korrekte Buchstaben werden grün dargestellt. | ✅ |
| UI-05 | Buchstaben an der falschen Position werden gelb dargestellt. | ✅ |
| UI-06 | Nicht enthaltene Buchstaben werden grau dargestellt. | ✅ |
| UI-07 | Unter dem Spielfeld wird eine virtuelle Tastatur dargestellt. | ✅ |
| UI-08 | Beim Tippen wird die entsprechende Taste kurz hervorgehoben. | ✅ |
| UI-09 | Nach einem Versuch bleibt die Eingabe fokussiert. | ✅ |
| UI-10 | Nach Spielende wird das Lösungswort angezeigt. | ✅ |
| UI-11 | Nach Spielende kann direkt ein neues Spiel gestartet werden. | ✅ |
| UI-12 | Die wichtigsten Spielregeln werden während des Spiels angezeigt. | ✅ |
| UI-13 | Pro Spiel können maximal zwei Hints verwendet werden. | ✅ |
| UI-14 | Die Statistik wird während des Spiels sichtbar angezeigt. | ✅ |

---

## Mögliche weitere Erweiterungen

Folgende Funktionen sind nicht Teil des notwendigen Grundumfangs und können abhängig von der verbleibenden Projektzeit ergänzt werden:

- Anzahl gewonnener und verlorener Spiele
- Anzeige verbleibender Versuche
- Zurücksetzen der Statistik
- Dark Mode
- Weitere optische Verbesserungen
- Barrierefreihet
- Setzen der Anzahl Buchstaben/Versuche

---

# Installation und Start

## Voraussetzungen

Für die lokale Ausführung werden benötigt:

- Java
- Node.js und npm
- H2
- Git

Für die Entwicklung wird die IntelliJ IDEA verwendet.

---

## Backend starten

Das Backend kann direkt über IntelliJ IDEA gestartet werden.

Standardmässig ist das Backend erreichbar unter:

```text
http://localhost:8080
```

---

## Frontend starten

Backend kommunikationspfad ändern

```
Unter ./Wordle-Frontend/wordle-frontend/src/services/api.ts
```


Frontend Pfad öffnen:

```bash
Cd ./Wordle-Frontend/wordle-frontend
```

Abhängigkeiten installieren:

```bash
npm install
```

Danach den Entwicklungsserver starten:

```bash
npm run dev
```

Die genaue Adresse des Frontends wird beim Start durch Vite angezeigt.

---

# Deployment

Die Anwendung wurde auf **Microsoft Azure** bereitgestellt, damit sie ohne lokale Entwicklungsumgebung im Web verwendet werden kann.

Frontend und Backend werden getrennt gehostet:

| Teil | Azure-Dienst | Aufgabe |
|---|---|---|
| Frontend | Azure Storage Static Website | Stellt die gebaute Vue-Anwendung aus dem `dist`-Ordner bereit. |
| Backend | Azure App Service | Führt die Java-Spring-Boot-Anwendung und die REST-Schnittstelle aus. |
| Datenhaltung | H2 | Speichert die Spieldaten des Backends. |

## Ablauf des Deployments

### Backend

Das Spring-Boot-Backend wurde als Azure App Service mit Java 21 bereitgestellt. Das Maven-Projekt wird zuerst gebaut und anschliessend auf den App Service deployed.

Öffentliche Backend-Adresse:

```text
https://worlde-backend-1788530434134.azurewebsites.net
```

### Frontend

Für das Vue-Frontend wird zuerst ein Production-Build erstellt:

```bash
npm install
npm run build
```

Vite erzeugt dadurch den Ordner `dist`. Der Inhalt dieses Ordners wird in den `$web`-Container einer Azure Storage Static Website hochgeladen. Dadurch werden `index.html`, JavaScript, CSS und weitere statische Dateien direkt über Azure bereitgestellt.

Das Frontend verwendet in `api.ts` die öffentliche URL des Azure-Backends anstelle von `localhost`, damit die REST-Anfragen auch nach dem Deployment funktionieren.

Öffentliche Frontend-Adresse:

```text
https://loriswordle2026.z1.web.core.windows.net/
```

### Verbindung zwischen Frontend und Backend

Da Frontend und Backend unter unterschiedlichen Adressen laufen, wurde für das Backend CORS so konfiguriert, dass Anfragen von der öffentlichen Frontend-Adresse erlaubt sind.

Nach dem Deployment wurden die wichtigsten Abläufe erneut online geprüft, unter anderem Spielstart, Wortprüfung, Hints, Statistik sowie Gewinn- und Verlustzustand.

---

# Tests

Die zentralen Abläufe des Spiels werden während der Entwicklung manuell und mit einfachen Unit-Tests im Backend getestet.

### Automatisierte Tests

| Testfall | Erwartetes Verhalten |
|---|---|
| Neues Spielobjekt erstellen | Das Spiel startet mit 0 Versuchen und ist nicht abgeschlossen. |
| Richtiges Lösungswort eingeben | Das Spiel wird als gewonnen und abgeschlossen markiert. |
| Sechster falscher Versuch | Das Spiel wird nach dem sechsten Versuch beendet. |
| Ungültiges Wort | Ein Wort, das nicht in der Wortliste vorkommt, wird abgelehnt. |

### Manuelle Tests

| Testfall | Erwartetes Verhalten |
|---|---|
| Neues Spiel starten | Eine neue Spiel-ID wird erzeugt. |
| Wort mit fünf Buchstaben eingeben | Der Versuch wird verarbeitet. |
| Buchstabe an richtiger Position | Kachel wird grün. |
| Buchstabe an falscher Position | Kachel wird gelb. |
| Nicht vorhandener Buchstabe | Kachel wird grau. |
| Versuch abschicken | Eingabefeld ist danach wieder fokussiert. |
| Hint verwenden | Ein Buchstabe des Lösungsworts wird angezeigt. |
| Zwei Hints verwenden | Danach kann kein weiterer Hint verwendet werden. |
| Spiel beenden | Das Lösungswort wird angezeigt. |
| Neues Spiel auswählen | Spielfeld wird geleert und neues Spiel gestartet. |
| Statistik anzeigen | Gespielte Spiele, Versuche insgesamt und Durchschnitt werden dargestellt. |
| Online-Version öffnen | Frontend lädt über Azure und kann Requests an das Azure-Backend senden. |

---

# Kompetenzbereiche

Für das Projekt wurden die Kompetenzbereiche **Software** und **User Interaction** gewählt.

## Software

### Analyse

Für die Anwendung werden funktionale Anforderungen definiert und mit Akzeptanzkriterien ergänzt.

Dabei werden unter anderem folgende Fragen betrachtet:

- Welche Informationen benötigt ein Spiel?
- Wie wird ein Versuch ausgewertet?
- Wann endet ein Spiel?
- Welche Daten müssen gespeichert werden?
- Welche Informationen werden zwischen Frontend und Backend übertragen?

### Design

Frontend, Backend und Datenbank werden voneinander getrennt.

Die Backend-Anwendung verwendet eine strukturierte Aufteilung in:

- Controller
- Service
- Repository
- Model
- DTO

Für die Kommunikation zwischen Frontend und Backend wird eine REST-Schnittstelle verwendet.

Testfälle werden definiert, mit denen die wichtigsten Spielabläufe geprüft werden können.

### Umsetzung

Die Anwendung wird mit Java Spring Boot und Vue.js implementiert.

Die Spieldaten werden persistent gespeichert und das Frontend kommuniziert über Axios mit der REST-Schnittstelle.

Während der Entwicklung werden die einzelnen Funktionen schrittweise implementiert und getestet.

---

## User Interaction

### Analyse

Bestehende Wordle-Anwendungen wurden betrachtet, um bekannte Interaktionskonzepte zu untersuchen.

Dabei wurden insbesondere folgende Elemente betrachtet:

- Darstellung mit Buchstabenkacheln
- Farbiges Feedback
- Begrenzte Anzahl Versuche
- Tastatureingabe
- Anzeige des Spielendes

### Design

Die Bedienung der Anwendung soll möglichst einfach und ohne zusätzliche Erklärung verständlich sein.

Der Benutzer soll direkt erkennen können:

- wo die Eingabe erfolgt
- wie viele Buchstaben benötigt werden
- welches Feedback ein Versuch erhalten hat
- wann das Spiel beendet wurde

Für die Darstellung werden fünf einzelne Buchstabenkacheln und drei klar unterscheidbare Feedbackfarben verwendet.

### Umsetzung

Das Interaktionsdesign wurde mit Vue.js und Bootstrap umgesetzt.

Eingaben werden direkt als einzelne Buchstaben in fünf Kacheln dargestellt. Bereits abgeschlossene Versuche bleiben sichtbar.

Zusätzlich wird eine virtuelle Tastatur angezeigt. Beim Drücken einer Taste wird der entsprechende Buchstabe kurz hervorgehoben.

---

# Aktueller Stand

Das Projekt befindet sich in einer fortgeschrittenen Entwicklungsphase.

### Umgesetzt

- [x] Spring-Boot-Backend
- [x] Vue.js-Frontend
- [x] REST-Kommunikation
- [x] Persistente Speicherung der Spiele
- [x] Zufällige Auswahl eines Lösungsworts
- [x] Wortliste
- [x] Fünf Buchstaben pro Versuch
- [x] Begrenzung auf sechs Versuche
- [x] Auswertung der Buchstaben
- [x] Farbiges Feedback
- [x] Buchstabenkacheln
- [x] Virtuelle Tastatur
- [x] Automatischer Eingabefokus
- [x] Gewinn- und Verlustzustand
- [x] Anzeige des Lösungsworts
- [x] Start eines neuen Spiels
- [x] Statistik über gespielte Spiele und Versuche
- [x] Durchschnittliche Anzahl Versuche
- [x] Validierung gegen erlaubte Wörter aus der Wortliste
- [x] Zwei Hints pro Spiel
- [x] Anzeige der Spielregeln im Frontend
- [x] Einfache Unit-Tests für zentrale Backend-Logik

### Noch offen

- [x] Deployment der Anwendung im Web
- [x] Abschluss und Kontrolle der Dokumentation

---

# Einsatz von KI

Im Projekt wurde ChatGPT als unterstützendes Werkzeug eingesetzt.

Die Unterstützung umfasste:

- Erklärungen zu Java, Spring Boot, Vue.js und TypeScript
- Hilfe bei Deployment-Fehler
- Vorschläge für einfachere Implementierungen
- Vorschläge für Frontend verschönerung
- Unterstützung bei der Strukturierung und Umformulierung der Dokumentation/Projektplanung

Die verwendeten Lösungen wurden vor der Übernahme geprüft und an das eigene Projekt angepasst.

---

# Quellen und Hilfsmittel

- Unterlagen des IPRO-Moduls der FHNW
- Aufgabenbeschreibung «Individuelles Projekt – Wordle»
- Vue.js Dokumentation
- Spring Boot Dokumentation
- Bootstrap Dokumentation
- Axios Dokumentation
- Microsoft Azure Dokumentation
- Eigene frühere Projekte
- Reddit
- ChatGPT