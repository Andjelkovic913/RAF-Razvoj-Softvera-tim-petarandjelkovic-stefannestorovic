# Struktura Testa - Vizuelni Prikaz

## 🎬 Tok Izvršavanja Testa

```
┌─────────────────────────────────────────────────────────────┐ 
│                    TEST IZVRŠAVANJE                         │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│ 1️⃣  ARRANGE (PRIPREMA)                                      │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Kreiraj test podatke:                                      │
│  ├─ String indeksShort = "RN1923"                          │
│  ├─ StudentIndeks mockIndeks = new StudentIndeks()         │
│  └─ StudentIndeksResponse mockResponse = new ...()         │
│                                                              │
│  Postavi ponašanje mock-a:                                  │
│  ├─ when(repo.find()).thenReturn(mockIndeks)               │
│  └─ when(mapper.map()).thenReturn(mockResponse)            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 2️⃣  ACT (IZVRŠAVANJE)                                       │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Pozovi metodu koju testiram:                              │
│  ├─ StudentIndeksResponse result =                         │
│  └─ studentService.fastSearch("RN1923")                    │
│                                                              │
│  Šta se dešava interno:                                     │
│  ├─ ParseUtils.parseIndeks("RN1923")                       │
│  │  └─ Vraća: ["RN", "19", "23"]                          │
│  ├─ repo.findStudentIndeks("RN", 2019, 23)                │
│  │  └─ Mock vraća: mockIndeks                             │
│  ├─ mapper.fromStudentIndexToResponse(mockIndeks)         │
│  │  └─ Mock vraća: mockResponse                           │
│  └─ Vraćeni rezultat: StudentIndeksResponse               │
│                                                              │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│ 3️⃣  ASSERT (PROVJERA)                                       │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Proveravaj rezultat:                                       │
│  ├─ assertNotNull(result)                                  │
│  │  └─ ✅ Rezultat nije null                              │
│  ├─ assertEquals(1L, result.getId())                       │
│  │  └─ ✅ ID je 1L                                        │
│  ├─ assertEquals(23, result.getBroj())                     │
│  │  └─ ✅ Broj je 23                                      │
│  ├─ assertEquals("RN", result.getStudProgramOznaka())     │
│  │  └─ ✅ Program je RN                                   │
│  └─ assertTrue(result.isAktivan())                         │
│     └─ ✅ Indeks je aktivan                               │
│                                                              │
│  Proveravaj pozive:                                         │
│  ├─ verify(repo, times(1)).findStudentIndeks(...)         │
│  │  └─ ✅ Repository je pozvan jednom                    │
│  └─ verify(mapper, times(1)).fromStudentIndexToResponse() │
│     └─ ✅ Mapper je pozvan jednom                         │
│                                                              │
└─────────────────────────────────────────────────────────────┘
                            ↓
                    ✅ TEST PASS
```

---

## 🏗️ Struktura Test Klase

```
StudentServiceTest.java
│
├─ @ExtendWith(MockitoExtension.class)
│  └─ Omogućava korišćenje Mockito anotacija
│
├─ @Mock zavisnosti
│  ├─ StudentIndeksRepository studentIndeksRepository
│  └─ EntityMappers entityMappers
│
├─ @InjectMocks servis
│  └─ StudentService studentService
│
└─ @Test metode
   ├─ findByIndeks_validIndeks()
   ├─ findByIndeks_invalidIndeks()
   ├─ findByIndeks_caseInsensitive()
   ├─ findByIndeks_invalidFormat()
   └─ findByIndeks_inactiveIndex()
```

---

## 🔄 Tok Podataka u Testu

```
┌──────────────────────────────────────────────────────────────┐
│                    ULAZNI PODACI                             │
│                  "RN1923" (String)                           │
└──────────────────────────────────────────────────────────────┘
                            ↓
┌──────────────────────────────────────────────────────────────┐
│              ParseUtils.parseIndeks()                        │
│  "RN1923" → ["RN", "19", "23"]                             │
└──────────────────────────────────────────────────────────────┘
                            ↓
┌──────────────────────────────────────────────────────────────┐
│         studentIndeksRepository.findStudentIndeks()          │
│  ("RN", 2019, 23) → StudentIndeks (mock)                   │
└──────────────────────────────────────────────────────────────┘
                            ↓
┌──────────────────────────────────────────────────────────────┐
│       entityMappers.fromStudentIndexToResponse()             │
│  StudentIndeks → StudentIndeksResponse (mock)               │
└──────────────────────────────────────────────────────────────┘
                            ↓
┌──────────────────────────────────────────────────────────────┐
│                    IZLAZNI PODACI                            │
│  StudentIndeksResponse(id=1, broj=23, godina=2019, ...)    │
└──────────────────────────────────────────────────────────────┘
```

---

## 🎯 Primeri Testova

### Test 1: Pronalaženje sa Validnim Indeksom

```
ULAZ: "RN1923"
  │
  ├─ ARRANGE
  │  ├─ Kreiraj mockIndeks sa id=1, broj=23, godina=2019
  │  ├─ Kreiraj mockResponse sa id=1, broj=23, godina=2019
  │  └─ Postavi: when(repo.find()).thenReturn(mockIndeks)
  │
  ├─ ACT
  │  └─ result = studentService.fastSearch("RN1923")
  │
  └─ ASSERT
     ├─ assertNotNull(result) ✅
     ├─ assertEquals(1L, result.getId()) ✅
     ├─ assertEquals(23, result.getBroj()) ✅
     ├─ assertEquals("RN", result.getStudProgramOznaka()) ✅
     └─ assertTrue(result.isAktivan()) ✅

REZULTAT: ✅ PASS
```

---

### Test 2: Pronalaženje Nepostojeći Studenta

```
ULAZ: "RN9999"
  │
  ├─ ARRANGE
  │  ├─ Kreiraj mock koji vraća null
  │  └─ Postavi: when(repo.find()).thenReturn(null)
  │
  ├─ ACT
  │  └─ assertThrows(EntityNotFoundException.class, () -> {
  │       studentService.fastSearch("RN9999")
  │     })
  │
  └─ ASSERT
     ├─ exception != null ✅
     ├─ exception.getMessage().contains("RN9999") ✅
     └─ verify(repo).findStudentIndeks(...) ✅

REZULTAT: ✅ PASS (exception je bačen kako se očekuje)
```

---

### Test 3: Case-Insensitive Pretraga

```
ULAZ: "rn1923" (mala slova)
  │
  ├─ ARRANGE
  │  ├─ Kreiraj mock sa id=2
  │  └─ Postavi: when(repo.find("RN", 2019, 23)).thenReturn(mock)
  │
  ├─ ACT
  │  └─ result = studentService.fastSearch("rn1923")
  │     (ParseUtils konvertuje u "RN1923")
  │
  └─ ASSERT
     ├─ assertNotNull(result) ✅
     ├─ assertEquals(2L, result.getId()) ✅
     ├─ assertEquals("RN", result.getStudProgramOznaka()) ✅
     └─ verify(repo).findStudentIndeks("RN", 2019, 23) ✅

REZULTAT: ✅ PASS (pronalazi se bez obzira na veličinu slova)
```

---

## 📊 Mockito Anotacije - Vizuelni Prikaz

```
┌─────────────────────────────────────────────────────────────┐
│                  @ExtendWith(MockitoExtension.class)         │
│  Omogućava korišćenje Mockito anotacija u testu             │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                         @Mock                                │
│  Kreira lažni objekat koji simulira pravu klasu             │
│                                                              │
│  Primeri:                                                    │
│  @Mock StudentIndeksRepository repo;                        │
│  @Mock EntityMappers mapper;                                │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                      @InjectMocks                            │
│  Automatski injektuje sve @Mock u ovu klasu                 │
│                                                              │
│  Primjer:                                                    │
│  @InjectMocks StudentService service;                       │
│  (service dobija repo i mapper kao zavisnosti)              │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                         @Test                                │
│  Označava metodu kao test koju Maven pokreće                │
│                                                              │
│  Primjer:                                                    │
│  @Test void findByIndeks_validIndeks() { ... }             │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                     @DisplayName                             │
│  Prikazuje lepši naziv testa u rezultatima                  │
│                                                              │
│  Primjer:                                                    │
│  @DisplayName("Pronalaženje sa validnim indeksom")         │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔍 Mockito Metodi - Vizuelni Prikaz

```
┌─────────────────────────────────────────────────────────────┐
│                  when(...).thenReturn(...)                   │
│  Postavi ponašanje mock-a                                   │
│                                                              │
│  Primjer:                                                    │
│  when(repo.find("RN", 2019, 23))                           │
│      .thenReturn(mockIndeks);                               │
│                                                              │
│  Šta znači:                                                  │
│  "Kada se pozove repo.find sa tim parametrima,             │
│   vrati mockIndeks"                                         │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                  verify(mock, times(N))                      │
│  Proveravaj da je metoda pozvan N puta                      │
│                                                              │
│  Primjer:                                                    │
│  verify(repo, times(1)).find("RN", 2019, 23);              │
│                                                              │
│  Šta znači:                                                  │
│  "Proveravaj da je repo.find pozvan tačno jednom"          │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                  assertNotNull(result)                       │
│  Proveravaj da rezultat nije null                           │
│                                                              │
│  Primjer:                                                    │
│  assertNotNull(result, "Rezultat ne sme biti null");       │
│                                                              │
│  Šta znači:                                                  │
│  "Ako je result null, test pada sa porukom"                │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                  assertEquals(expected, actual)              │
│  Proveravaj da su vrednosti jednake                         │
│                                                              │
│  Primjer:                                                    │
│  assertEquals(1L, result.getId(), "ID mora biti 1L");      │
│                                                              │
│  Šta znači:                                                  │
│  "Ako result.getId() != 1L, test pada sa porukom"          │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              assertThrows(Exception.class, ...)              │
│  Proveravaj da je exception bačen                           │
│                                                              │
│  Primjer:                                                    │
│  EntityNotFoundException ex = assertThrows(                 │
│      EntityNotFoundException.class,                         │
│      () -> studentService.fastSearch("RN9999")             │
│  );                                                          │
│                                                              │
│  Šta znači:                                                  │
│  "Očekujem da će biti bačena EntityNotFoundException"      │
└─────────────────────────────────────────────────────────────┘
```

---

## ✅ Checklist za Pisanje Testa

```
┌─────────────────────────────────────────────────────────────┐
│ CHECKLIST ZA PISANJE TESTA                                  │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│ ☐ Kreiraj test klasu sa @ExtendWith(MockitoExtension)     │
│ ☐ Kreiraj @Mock zavisnosti                                 │
│ ☐ Kreiraj @InjectMocks servis                              │
│ ☐ Napiši @Test metodu sa @DisplayName                      │
│ ☐ ARRANGE: Pripremi test podatke                           │
│ ☐ ARRANGE: Postavi ponašanje mock-a (when...thenReturn)   │
│ ☐ ACT: Pozovi metodu koju testiram                         │
│ ☐ ASSERT: Proveravaj rezultat (assertNotNull, assertEquals)│
│ ☐ ASSERT: Proveravaj pozive (verify)                       │
│ ☐ Dodaj detaljne komentare                                 │
│ ☐ Pokreni test i proveravaj da prolazi                     │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎓 Zaključak

Sada razumeš:
- ✅ Kako se testovi strukturiraju (ARRANGE, ACT, ASSERT)
- ✅ Kako se koriste mock objekti
- ✅ Kako se postavljaju očekivanja (when...thenReturn)
- ✅ Kako se proveravaju rezultati (assert, verify)
- ✅ Kako se pišu detaljni komentari

**Spreman si da napraviš testove za sve specifikacije! 🚀**
