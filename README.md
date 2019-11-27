# Event System

Projekt polegający na stworzeniu zestawu klas pozwalających na modelowanie zdarzeń.
- Określenie zachowania na wypadek zaistnienia zdarzenia jest możliwe przy użyciu funkcji EventTarget.subscribe.
- Zaistnienie zdarzenia można zasygnalizować przy użyciu funkcji EventTarget.emit.
- Informacje pomiędzy nadawcą a odbiorcą zdarzenia mogą być przekazywane przy użyciu pól obiektu dziedziczącego z klasy Event.
- Przekazywanie zdarzenia w górę od obiektu dziecka do rodzica, tzw. "bąbelkowanie"
- anulowanie i zatrzymywanie "bąbelkowania" zdarzenia