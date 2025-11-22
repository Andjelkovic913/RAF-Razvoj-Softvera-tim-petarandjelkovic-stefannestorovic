# Ispravke StudentIndeksServiceTest

## Problem
Testovi u `StudentIndeksServiceTest` nisu se pokretali ispravno jer su bili organizovani u `@Nested` klasu koja nije bila vidljiva test runner-u.

## Rešenja Primenjena

### 1. Premještanje Testova iz @Nested Klase
- **Prije**: Testovi su bili u `@Nested` klasi `FindBrojTests`
- **Sada**: Svi testovi su prebačeni direktno u glavnu test klasu

### 2. Preimenovanje Test Metoda
Dodao sam prefiks `findBroj_` svim test metodama za bolji prikaz u test rezultatima:
- `emptyListReturns1()` → `findBroj_emptyListReturns1()`
- `contiguousReturnsNext()` → `findBroj_contiguousReturnsNext()`
- `gapInMiddle()` → `findBroj_gapInMiddle()`
- `notStartingAtOne()` → `findBroj_notStartingAtOne()`
- `unorderedInput()` → `findBroj_unorderedInput()`

### 3. Uklanjanje Nekorišćenih Importa
- Uklonjen `import org.junit.jupiter.api.Nested;` jer više nije potreban

## Rezultati

### Prije Ispravke
```
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

### Sada
```
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

## Svi Testovi u StudentIndeksServiceTest

1. ✅ `findBroj_emptyListReturns1()` - Prazna lista vraća 1
2. ✅ `findBroj_contiguousReturnsNext()` - Kontinualna sekvenca vraća sledeći broj
3. ✅ `findBroj_gapInMiddle()` - Praznina u sredini vraća prvi nedostajući broj
4. ✅ `findBroj_notStartingAtOne()` - Sekvenca koja ne počinje od 1 vraća 1
5. ✅ `findBroj_unorderedInput()` - Nesortirana lista se ispravno obrađuje
6. ✅ `findByStudentIdAndAktivan()` - Pronalaženje aktivnog indeksa studenta

## Ukupni Rezultati Svih Testova

```
[INFO] Running org.raflab.studsluzba.utils.ParseUtilsTest
[INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0

[INFO] Running org.raflab.studsluzba.services.StudentIndeksServiceTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0

[INFO] Running org.raflab.studsluzba.services.DrziPredmetServiceTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0

[INFO] Results:
[INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
