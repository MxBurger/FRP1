## Concurrency vs Parallelism

**Parallelität (Parallelism):**
- Techniken zur Beschleunigung von Programmen durch gleichzeitige Ausführung von Berechnungen
- Erfordert mehrere CPUs
- Berechnungseinheiten müssen unabhängig sein
- Ziel: Steigerung der Laufzeiteffizienz

```mermaid
---
config
---
flowchart LR
  A[Programm] --> B[Task₁]
  A[Programm] --> C[Task₂]
  B --> End@{ shape: sm-circ }
  C --> End

  classDef gray fill:gray;
  class B gray
  classDef lime fill:lime;
  class C lime
```

**Nebenläufigkeit (Concurrency):**
- Programm macht Fortschritte bei mehreren Aufgaben zur gleichen Zeit
- Nebenläufige Programme können auf einer einzelnen CPU ausgeführt werden, können aber von mehreren CPUs profitieren
- Ziel: Effiziente Interaktion mit mehreren externen Agenten ermöglichen
- Nebenläufigkeit schließt Parallelität mit ein

```mermaid
---
config
---
flowchart LR
  A[Programm] --> B[T₁₁]
  B --> C[T₁₂]
  A --> D[T₂₁]
  D --> E[T₂₂]
  C --> End@{ shape: sm-circ }
  E --> End

  style A margin-left:50px
  style B margin-left:150px
  style C margin-left:100px
  style D margin-left:200px
  classDef gray fill:gray;
  classDef lime fill:lime;
  class B,C gray
  class D,E lime
```


**Im wesentlichen:**
- Parallelität bezieht sich auf die tatsächlich gleichzeitige Ausführung von Aufgaben (z.B. auf mehreren Prozessorkernen)
- Nebenläufigkeit beschreibt die Fähigkeit eines Programms, mehrere Aufgaben zu verwalten und voranzutreiben, auch wenn sie nicht unbedingt exakt gleichzeitig ausgeführt werden