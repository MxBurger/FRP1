## Concurrency vs Parallelism

**Parallelität (Parallelism):**
- Techniken zur Beschleunigung von Programmen durch gleichzeitige Ausführung von Berechnungen
- Erfordert mehrere CPUs
- Berechnungseinheiten müssen unabhängig sein
- Ziel: Steigerung der Laufzeiteffizienz

![assets/Parallel.png](assets/Parallel.png)

**Nebenläufigkeit (Concurrency):**
- Programm macht Fortschritte bei mehreren Aufgaben zur gleichen Zeit
- Nebenläufige Programme können auf einer einzelnen CPU ausgeführt werden, können aber von mehreren CPUs profitieren
- Ziel: Effiziente Interaktion mit mehreren externen Agenten ermöglichen
- Nebenläufigkeit schließt Parallelität mit ein

![assets/Concurrent.png](assets/Parallel.png)

**Im wesentlichen:**
- Parallelität bezieht sich auf die tatsächlich gleichzeitige Ausführung von Aufgaben (z.B. auf mehreren Prozessorkernen)
- Nebenläufigkeit beschreibt die Fähigkeit eines Programms, mehrere Aufgaben zu verwalten und voranzutreiben, auch wenn sie nicht unbedingt exakt gleichzeitig ausgeführt werden

## Reaktive Systeme
- Definition von *reaktiv*: “Showing a response to a stimulus. Pupils are reactive to light” *[Oxford Dictionaries]*

- Das Reaktive Manifesto: Beschreibt die Eigenschaften von reaktiven Systemen
![alt text](assets/ReactiveSystem.png)

- Nachrichtenbasiert (Message-driven): Reagiert auf Events
  - Reaktive Systeme basieren auf asynchroner Nachrichtenübermittlung → keine Blockierung
  - Gewährleistet lose Kopplung von Komponenten → Standort-Transparenz
  - MessageQueues ermöglichen Lastmanagement
  - Auch Fehler werden über Nachrichten kommuniziert
- Elastizität (Elasticity): Reagiert auf Last
  - Das System bleibt auch unter wechselnder Arbeitslast reaktionsfähig
  - Vertikale Skalierung (Scale up): Nutzung mehrerer Kerne, Erhöhung des Arbeitsspeichers
  - Horizontale Skalierung (Scale out): Nutzung mehrerer Server-Knoten
  - Dafür muss erfüllt sein:
    - Minimierung veränderbarer Zustände
    - Standort-Transparenz und Resilienz
- Resilienz (Resilience): Reagiert auf Fehler
  - Das System bleibt auch bei Auftreten von Fehlern reaktionsfähig
  - Teile des Systems können ausfallen und sich erholen, ohne das Gesamtsystem zu gefährden
  - Die Wiederherstellung jeder Komponente wird an eine andere Komponente delegiert
  - Dafür muss erfüllt sein:
    - Kapselung des Zustands
    - Überwachungshierarchien (Supervisor-Konzepte)
- Reaktionsfähigkeit (Responsiveness): Reagiert auf User *(User muss nicht Mensch sein)*
  - System muss Echtzeit-Interaktion mit Agents unter Last und bei Auftreten von Fehlern gewährleisten
  - Voraussetzungen: Nachrichtenbasierte Architektur, Elastizität, Resilienz