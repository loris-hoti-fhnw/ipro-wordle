# Projektplanung

## Übersicht

Für das IPRO-Projekt wurde ein Gesamtaufwand von ungefähr **180 Stunden** eingeplant.

Die Planung wurde in grössere Arbeitspakete unterteilt. Die Struktur orientiert sich an einer Issue-basierten Projektplanung, wie sie in GitLab verwendet werden kann.

Da GitHub in diesem Projekt hauptsächlich für die Versionsverwaltung verwendet wurde, wurde die Projektplanung separat dokumentiert und nicht über GitHub Issues geführt.

Der Fokus liegt deshalb nicht auf einer minutengenauen Zeiterfassung, sondern auf der sinnvollen Aufteilung des Gesamtaufwands auf die einzelnen Projektphasen.

---

## Arbeitspakete

| ID | Bereich | Arbeitspaket | Beschreibung | Aufwand |
|---|---|---|---|---:|
| AP-01 | Projektstart | Kickoff und Aufgabenstellung analysieren | Aufgabenstellung verstehen, Rahmenbedingungen klären und erste Projektziele definieren. | 8 h |
| AP-02 | Analyse | Anforderungen und Spielregeln definieren | Funktionale Anforderungen des Wordle-Spiels definieren, insbesondere Wortlänge, Anzahl Versuche, Feedbacklogik, Statistik und Persistenz. | 12 h |
| AP-03 | Setup | Entwicklungsumgebung und Technologie-Stack | Spring Boot, Vue, TypeScript, H2, Git und GitHub einrichten sowie die grundlegende Projektstruktur erstellen. | 12 h |
| AP-04 | Design | Architektur, Datenmodell und API planen | Aufbau von Frontend und Backend planen, Game-Modell definieren sowie REST-Endpunkte, DTOs, Service und Repository strukturieren. | 12 h |
| AP-05 | Backend | Wordle-Kernlogik implementieren | Neues Spiel starten, zufälliges Lösungswort laden, Eingaben verarbeiten, Feedback erzeugen und Spielende erkennen. | 18 h |
| AP-06 | Backend | Persistenz umsetzen | Speicherung der Spiele über Spring Data JPA und H2 umsetzen und Datenbankzugriffe integrieren. | 10 h |
| AP-07 | Frontend | Grundstruktur und API-Anbindung | Vue-Router, Views, Axios-Service und Kommunikation mit dem Spring-Boot-Backend umsetzen. | 16 h |
| AP-08 | Frontend | Spieloberfläche umsetzen | GameBoard, Eingabefelder, virtuelle Tastatur, Layout sowie visuelle Rückmeldungen für die Buchstaben implementieren. | 14 h |
| AP-09 | Funktionalität | Validierung und Fehlerbehandlung | Prüfen, ob ein eingegebenes Wort in der Wortliste existiert, ungültige Eingaben abfangen und verständliche Rückmeldungen anzeigen. | 8 h |
| AP-10 | Funktionalität | Statistik implementieren | Anzahl gespielter Spiele, Gesamtzahl der Versuche und durchschnittliche Versuchsanzahl berechnen und im Frontend anzeigen. | 10 h |
| AP-11 | Funktionalität | Hint-System und UX-Erweiterungen | Zwei Hinweise pro Spiel, Spielinformationen und kleinere Verbesserungen an Bedienung und Benutzerfeedback umsetzen. | 8 h |
| AP-12 | Qualität | Tests und Debugging | Backend-Tests mit JUnit und Mockito erstellen, manuelle Tests durchführen und Fehler in Frontend, Backend und Datenbank beheben. | 14 h |
| AP-13 | Deployment | Anwendung auf Azure bereitstellen | Backend auf Azure App Service deployen, Frontend als Azure Storage Static Website veröffentlichen sowie CORS und Online-Kommunikation konfigurieren. | 12 h |
| AP-14 | Dokumentation | Projektdokumentation erstellen | README, Anforderungen, Architektur, technische Entscheidungen, Tests, Deployment und Kompetenznachweise dokumentieren. | 16 h |
| AP-15 | Abschluss | Finalisierung und Reserve | Gesamtsystem testen, kleinere Verbesserungen umsetzen, Dokumentation prüfen und Reserve für unerwartete Probleme einplanen. | 10 h |

**Gesamtaufwand geschätzt: 180 Stunden**

---

## Projektphasen

Die Arbeitspakete lassen sich grob in folgende Projektphasen unterteilen:

### 1. Analyse und Planung

In der ersten Phase wurden die Aufgabenstellung analysiert, Anforderungen definiert und die technische Umsetzung geplant.

Dazu gehörten insbesondere:

- Definition der Wordle-Spielregeln
- Auswahl der Technologien
- Planung der Architektur
- Festlegung der Datenstruktur
- Planung der REST-Schnittstellen

Geplanter Aufwand: **44 Stunden**

---

### 2. Umsetzung

Der grösste Teil des Projekts bestand aus der eigentlichen Implementierung.

Dazu gehörten:

- Spring-Boot-Backend
- Spiellogik
- H2-Persistenz
- Vue-Frontend
- API-Kommunikation
- GameBoard
- virtuelle Tastatur
- Eingabevalidierung
- Statistik
- Hint-System

Geplanter Aufwand: **84 Stunden**

---

### 3. Qualitätssicherung und Deployment

Nach der grundlegenden Umsetzung wurde die Anwendung getestet, Fehler wurden behoben und die Anwendung online bereitgestellt.

Dazu gehörten:

- Unit-Tests
- manuelle Funktionstests
- Fehleranalyse
- TypeScript-Buildfehler beheben
- Datenbankprobleme beheben
- Azure App Service konfigurieren
- Azure Storage Static Website einrichten
- CORS konfigurieren
- Online-Funktion testen

Geplanter Aufwand: **26 Stunden**

---

### 4. Dokumentation und Abschluss

Zum Abschluss wurde das Projekt dokumentiert und nochmals als Gesamtsystem überprüft.

Dazu gehörten:

- README
- Architekturübersicht
- Anforderungen
- Testdokumentation
- Deployment-Dokumentation
- Kompetenznachweise
- Projektplanung
- Abschlusskontrolle

Geplanter Aufwand: **26 Stunden**

---

## Umgang mit der Zeiterfassung

Zu Beginn des Projekts wurde versucht, den Aufwand sehr detailliert auf einzelne Tage und Arbeitsblöcke aufzuteilen.

Diese Form der Planung war jedoch als zu detailliert und wenig übersichtlich.

Deshalb wurde die Planung später auf grössere Arbeitspakete umgestellt.

---

## Anpassungen während des Projekts

Während der Umsetzung wurden einzelne Anforderungen erweitert oder angepasst.

Beispiele:

- Ergänzung einer Statistik
- Validierung der eingegebenen Wörter
- Einführung eines Hint-Systems
- Erweiterung des Game-Layouts
- zusätzliche Backend-Tests
- Deployment des Backends auf Azure App Service
- Wechsel beim Frontend-Deployment von Azure Static Web Apps zu Azure Storage Static Website aufgrund von Einschränkungen des Azure-Student-Abonnements

---

## Feedback

Anfangs hatte ich den Aufwand unterschätzt und wurde von meinem Betreuer darauf hingewiesen, dass ich mich mehr bemühen sollte. Diesen Ratschlag nahm ich sehr ernst und setzt ihn auch direkt um.
Es gab immer wieder Probleme und Änderungen am Projekt die mich mehr Zeit gekostet haben als erwartet, vorallem das Deployment & die Planung, jedoch habe ich auch genau an diesen Punkten am meisten gelernt.
Ich hatte davor noch nie ein Deployment von einem Projekt gemacht und habe dadurch sehr viel gelernt.
