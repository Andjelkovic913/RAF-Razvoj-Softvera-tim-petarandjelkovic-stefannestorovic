# Kako Pisati Testove - Detaljno Objašnjenje

## 📚 Uvod

Testovi su važni jer:
- ✅ Proveravaju da li kod radi kako treba
- ✅ Sprečavaju greške pri budućim promenama
- ✅ Dokumentuju kako se kod koristi
- ✅ Čine kod pouzdanijim

---

## 🏗️ Struktura Testa

Svaki test ima tri dela (AAA pattern):

```
1. ARRANGE (Priprema)   - Postavi test podatke
2. ACT (Izvršavanje)    - Pozovi metodu koju testiram
3. ASSERT (Provjera)    - Provjeri rezultat
```

---

## 🔧 Koraci za Pisanje Testa

### Korak 1: Kreiraj Test Klasu

```java
@ExtendWith(MockitoExtension.class)  // Omogućava Mockito
class StudentServiceTest {
    // Test metode ide ovde
}
```

**Šta znači `@ExtendWith(MockitoExtension.class)`?**
- Omogućava korišćenje `@Mock` i `@InjectMocks` anotacija
- Automatski inicijalizuje mock objekte pre svakog testa

---

### Korak 2: Kreiraj Mock Zavisnosti

```java
@Mock
private StudentIndeksRepository studentIndeksRepository;

@Mock
private EntityMappers entityMappers;

@InjectMocks
private StudentService studentService;
```

**Šta su mock objekti?**
- `@Mock` - Kreira lažni objekat koji simulira pravu klasu
- `@InjectMocks` - Automatski injektuje sve `@Mock` u ovu klasu

**Primer:**
```
Pravi kod:
    StudentService koristi StudentIndeksRepository
    
Test kod:
    StudentService koristi MOCK StudentIndeksRepository
    (ne pristupa stvarnoj bazi, samo simulira odgovore)
```

---

### Korak 3: Napiši Test Metodu

```java
@Test
@DisplayName("Pronalaženje studenta sa validnim indeksom")
void findByIndeks_validIndeks() {
    // ARRANGE, ACT, ASSERT
}
```

**Šta znači svaka anotacija?**
- `@Test` - Označava da je ovo test metoda
- `@DisplayName` - Prikazuje lepši naziv u rezultatima

---

## 📝 Detaljno Objašnjenje Svakog Dela

### ARRANGE (Priprema)

```java
// 1. Pripremi ulazne podatke
String indeksShort = "RN1923";

// 2. Kreiraj mock objekat sa test podacima
StudentIndeks mockStudentIndeks = new StudentIndeks();
mockStudentIndeks.setId(1L);
mockStudentIndeks.setBroj(23);
mockStudentIndeks.setGodina(2019);
mockStudentIndeks.setStudProgramOznaka("RN");

// 3. Postavi ponašanje mock objekata
// "Kada se pozove findStudentIndeks sa (RN, 2019, 23), vrati mockStudentIndeks"
when(studentIndeksRepository.findStudentIndeks("RN", 2019, 23))
        .thenReturn(mockStudentIndeks);
```

**Šta se dešava?**
1. Kreiramo test podatke
2. Kažemo mock-u kako da se ponaša
3. Spreman je test za izvršavanje

---

### ACT (Izvršavanje)

```java
// Pozovi metodu koju testiram
StudentIndeksResponse result = studentService.fastSearch(indeksShort);
```

**Šta se dešava?**
1. Poziva se `studentService.fastSearch("RN1923")`
2. Servis interno poziva `studentIndeksRepository.findStudentIndeks(...)`
3. Mock vraća `mockStudentIndeks` (ne pristupa stvarnoj bazi)
4. Servis vraća `StudentIndeksResponse`

---

### ASSERT (Provjera)

```java
// Proveri da li je rezultat null
assertNotNull(result, "Rezultat ne sme biti null");

// Provjeri da li je ID ispravan
assertEquals(1L, result.getId(), "ID studenta mora biti 1L");

// Provjeri da li je indeks aktivan
assertTrue(result.isAktivan(), "Indeks mora biti aktivan");

// Provjeri da je repository pozvan tačno jednom
verify(studentIndeksRepository, times(1)).findStudentIndeks("RN", 2019, 23);
```

**Šta znači svaka provjera?**
- `assertNotNull()` - Proverava da rezultat nije null
- `assertEquals()` - Proverava da su vrednosti jednake
- `assertTrue()` - Proverava da je uslov ispunjen
- `verify()` - Proverava da je metoda pozvan

---

## 🎯 Praktični Primeri

### Primer 1: Test sa Validnim Podacima

```java
@Test
@DisplayName("Pronalaženje studenta sa validnim indeksom")
void findByIndeks_validIndeks() {
    // ARRANGE
    String indeksShort = "RN1923";
    StudentIndeks mockIndeks = new StudentIndeks();
    mockIndeks.setId(1L);
    
    when(studentIndeksRepository.findStudentIndeks("RN", 2019, 23))
            .thenReturn(mockIndeks);
    
    StudentIndeksResponse mockResponse = new StudentIndeksResponse();
    mockResponse.setId(1L);
    
    when(entityMappers.fromStudentIndexToResponse(mockIndeks))
            .thenReturn(mockResponse);

    // ACT
    StudentIndeksResponse result = studentService.fastSearch(indeksShort);

    // ASSERT
    assertNotNull(result);
    assertEquals(1L, result.getId());
}
```

---

### Primer 2: Test sa Exception-om

```java
@Test
@DisplayName("Pronalaženje nepostojeći studenta baca exception")
void findByIndeks_invalidIndeks() {
    // ARRANGE
    String indeksShort = "RN9999";
    
    // Kažemo mock-u da vrati null (student ne postoji)
    when(studentIndeksRepository.findStudentIndeks("RN", 2099, 99))
            .thenReturn(null);

    // ACT & ASSERT
    // Očekujemo da će biti bačena EntityNotFoundException
    EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> studentService.fastSearch(indeksShort),
            "Trebalo bi baciti exception"
    );
    
    // Proveravamo poruku
    assertTrue(exception.getMessage().contains("RN9999"));
}
```

---

### Primer 3: Test sa Verifikacijom Poziva

```java
@Test
@DisplayName("Proverava da je repository pozvan")
void findByIndeks_verifiesRepositoryCalled() {
    // ARRANGE
    String indeksShort = "RN1923";
    StudentIndeks mockIndeks = new StudentIndeks();
    
    when(studentIndeksRepository.findStudentIndeks("RN", 2019, 23))
            .thenReturn(mockIndeks);
    
    when(entityMappers.fromStudentIndexToResponse(mockIndeks))
            .thenReturn(new StudentIndeksResponse());

    // ACT
    studentService.fastSearch(indeksShort);

    // ASSERT
    // Proveravamo da je repository pozvan tačno jednom
    verify(studentIndeksRepository, times(1))
            .findStudentIndeks("RN", 2019, 23);
    
    // Proveravamo da nema drugih poziva
    verifyNoMoreInteractions(studentIndeksRepository);
}
```

---

## 📊 Česti Mockito Metodi

| Metod | Opis | Primer |
|-------|------|--------|
| `when(...).thenReturn(...)` | Postavi ponašanje mock-a | `when(repo.find()).thenReturn(data)` |
| `verify(mock, times(1))` | Proverava da je pozvan N puta | `verify(repo, times(1)).find()` |
| `verify(mock, never())` | Proverava da NIJE pozvan | `verify(repo, never()).delete()` |
| `assertNotNull()` | Proverava da nije null | `assertNotNull(result)` |
| `assertEquals()` | Proverava da su jednaki | `assertEquals(1, result.getId())` |
| `assertTrue()` | Proverava da je true | `assertTrue(result.isActive())` |
| `assertThrows()` | Proverava da je exception bačen | `assertThrows(Exception.class, ...)` |

---

## 🚀 Pokretanje Testova

```bash
# Pokreni sve testove
mvn clean test

# Pokreni samo jednu test klasu
mvn test -Dtest=StudentServiceTest

# Pokreni samo jednu test metodu
mvn test -Dtest=StudentServiceTest#findByIndeks_validIndeks
```

---

## ✅ Rezultati Testova

```
[INFO] Running org.raflab.studsluzba.services.StudentServiceTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**Šta znači?**
- `Tests run: 5` - Pokrenuto 5 testova
- `Failures: 0` - Nema neuspešnih testova
- `Errors: 0` - Nema grešaka pri kompajliranju
- `BUILD SUCCESS` - Sve je prošlo uspešno

---

## 💡 Saveti za Pisanje Dobrih Testova

1. **Jedan test = Jedna stvar**
   - Svaki test testira samo jednu funkcionalnost
   - Ne testira više stvari u jednom testu

2. **Jasni Nazivi**
   - `findByIndeks_validIndeks()` - Jasno šta se testira
   - Ne `test1()`, `test2()` - Nejasno

3. **Detaljni Komentari**
   - Objasni šta test radi
   - Objasni zašto je važan

4. **Proveravaj Graničnih Slučajeva**
   - Validni podaci ✅
   - Nevalidni podaci ❌
   - Prazni podaci ⚠️
   - Null vrednosti 🔍

5. **Koristi Meaningful Assertions**
   - Ne samo `assertTrue(true)`
   - Koristi `assertEquals()`, `assertNotNull()`, itd.

---

## 📚 Dodatni Resursi

- [JUnit 5 Dokumentacija](https://junit.org/junit5/)
- [Mockito Dokumentacija](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)

---

## 🎓 Zaključak

Testovi su:
- ✅ Važni za kvalitet koda
- ✅ Laki za pisanje kada razumeš strukturu
- ✅ Štite od budućih grešaka
- ✅ Dokumentuju kako se kod koristi

**Sada znaš kako pisati testove! 🎉**
