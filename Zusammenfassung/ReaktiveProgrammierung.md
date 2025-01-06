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

![assets/Concurrent.png](assets/Concurrent.png)

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

## Herkömmliche Techniken für nebenläufige Programmierung
- Low-Level-Konstrukte für nebenläufige Ausführungen:
  - Prozesse
  - Threads
- Kommunikationsmittel:
  - Gemeinsamer Speicher (Shared Memory) → Synchronisation erforderlich
  - Nachrichtenaustausch (Message Passing) → verteilte Systeme
- **Probleme:** Fehleranfällig und auf niedriger Abstraktionsebene
  - Deadlocks
  - Race Conditions
  - Data Races
  - Starvation

Grundlegendes Wissen über Low-Level-Ansätze ist essentiell, um High-Level-Konzepte zu verstehen :smile:

## Prozesse und Threads
- Ein Prozess führt die Anweisungen eines Programms aus:
  - Teilt sich CPU und andere Ressourcen mit anderen Prozessen
  - Hat seinen eigenen Speicherbereich
- Die Anweisungen eines Prozesses werden nebenläufig in mehreren Threads ausgeführt:
  - Threads teilen sich den Speicher des Prozesses, zu dem sie gehören
  - Threads kommunizieren durch Schreiben und Lesen im Speicher
- Die Anweisungen eines Prozesses werden nebenläufig in mehreren Threads ausgeführt:
  - Threads teilen sich den Speicher des Prozesses, zu dem sie gehören
  - Threads kommunizieren durch Schreiben und Lesen im Speicher
- Threads können auf verschiedenen Prozessoren (Kernen) ausgeführt werden:
  - Prozessoren schreiben nicht direkt in den Hauptspeicher
  - Sie nutzen Caches zur Verbesserung der Lese-/Schreib-Performance
  - Ein Prozessor kann nicht auf den Cache eines anderen Prozessors zugreifen
- Java-Threads werden direkt auf Betriebssystem-Threads abgebildet ( ab Java21 auch das Konzept von VirtualThreads, das bleibt jetzt aber außen vor)
- Scala übernimmt das Thread-Modell von Java
  
![assets/MemoryAccess.png](assets/MemoryAccess.png)

## Java Memory Model (JMM)
- Scala übernimmt das Speichermodell von der JVM.
- Das Speichermodell definiert, wann Schreibzugriffe auf eine Variable für andere Threads sichtbar sind.
- Der Compiler und die Laufzeitumgebung führen verschiedene Optimierungen durch, um Performance zu gewinnen:
  - Register können als Zwischenspeicher verwendet werden
  - Daten können in Hierarchien von Caches geschrieben werden
  - Bytecode-Anweisungen können umgeordnet werden
- Die Regeln des JMM definieren, wie Threads über den Speicher interagieren:
  - Programm-Reihenfolge (Program order): Programmoptimierungen durch den Compiler dürfen die serielle Semantik innerhalb eines Threads nicht verändern
  - Locks: Sperren für die gleiche Synchronisationsvariable dürfen sich nicht überlappen
  - Volatile fields: Ein Schreibzugriff auf ein volatiles Feld ist sofort für alle Threads sichtbar
  - Thread-Start: Alle Aktionen in einem Thread werden nach dem Aufruf von start() ausgeführt