# Status Implementacije - Operacije za Studenta

## ✅ IMPLEMENTIRANO

### 1. Selekcija studenta preko broja indeksa
- **Endpoint**: `GET /api/student/indeks/{id}`
- **Endpoint**: `GET /api/student/fastsearch?indeksShort={broj}`
- **Status**: ✅ Implementirano
- **Lokacija**: `StudentController.java`, `StudentService.java`

### 2. Selekcija svih položenih ispita za broj indeksa studenta, paginirano
- **Endpoint**: `GET /api/izlazak-na-ispit/student-indeks/{id}/passed?page=0&size=10`
- **Status**: ✅ Implementirano
- **Lokacija**: `IzlazakNaIspitController.java`, `IzlazakNaIspitService.java`
- **Repository Query**: `findPassedExamsByStudentIndeks()`

### 3. Selekcija svih nepoloženih ispita za broj indeksa studenta, paginirano
- **Endpoint**: `GET /api/izlazak-na-ispit/student-indeks/{id}/failed?page=0&size=10`
- **Status**: ✅ Implementirano
- **Lokacija**: `IzlazakNaIspitController.java`, `IzlazakNaIspitService.java`
- **Repository Query**: `findFailedExamsByStudentIndeks()`

### 4. Selekcija studenata na osnovu imena i/ili prezimena, paginirano
- **Endpoint**: `GET /api/student/search?ime={ime}&prezime={prezime}&page=0&size=10`
- **Status**: ✅ Implementirano
- **Lokacija**: `StudentController.java`, `StudentService.java`

### 5. Selekcija svih upisanih studenata koji su završili određenu srednju školu
- **Endpoint**: `GET /api/student/srednja-skola/{srednjaSkolaId}`
- **Status**: ✅ Implementirano
- **Lokacija**: `StudentController.java`, `StudentSrednjaSkolaService.java`

---

## ❌ NIJE IMPLEMENTIRANO

### 6. Pregled svih upisanih godina za broj indeksa
- **Endpoint**: `GET /api/upis-godine/student/{brojIndeksa}`
- **Status**: ✅ Implementirano
- **Lokacija**: `UpisGodineController.java`, `UpisGodineService.java`
- **Metod**: `getUpisGodineByBrojIndeksa()`

### 7. Upis studenta na godinu
- **Endpoint**: `POST /api/upis-godine/add`
- **Status**: ✅ Implementirano
- **Opis**: Dodaje novi upis za studenta i predmete koje sluša (inicijalno nepoloženi)
- **Lokacija**: `UpisGodineController.java`, `UpisGodineService.java`
- **Metod**: `createUpisGodine()`
- **Request DTO**: `UpisGodineRequest` sa `studentIndeksId`, `godinaStudija`, `skolskaGodinaId`, `listaPremetaIds[]`

### 8. Pregled obnovljenih godina za broj indeksa
- **Potreban Endpoint**: `GET /api/obnova-godine/student-indeks/{studentIndeksId}`
- **Status**: ❌ Nije implementirano
- **Potrebno**:
  - Dodati metod u `ObnovaGodineRepository`
  - Dodati metod u `ObnovaGodineService`
  - Dodati endpoint u `ObnovaGodineController`

### 9. Obnova godine za studenta
- **Potreban Endpoint**: `POST /api/obnova-godine/obnovi`
- **Status**: ❌ Nije implementirano
- **Opis**: Omogućiti izbor nepoloženih + predmeta iz naredne godine (max 60 ESPB)
- **Potrebno**:
  - Request DTO sa `studentIndeksId`, `godinaStudija`, `listaPremetaIds[]`
  - Validacija: zbir ESPB ≤ 60
  - Kreiranje `ObnovaGodine` zapisa
  - Kreiranje `SlusaPredmet` zapisa za izabrane predmete
  - Transakciona obrada

### 10. Dodavanje nove uplate
- **Potreban Endpoint**: `POST /api/uplata/dodaj`
- **Status**: ❌ Nije implementirano
- **Opis**: Čuva datum uplate, iznos u dinarima i srednji kurs (dohvaćen sa API-ja)
- **Potrebno**:
  - Request DTO sa `studentIndeksId`, `iznosDinara`
  - Integracija sa eksternim API-jem: `https://kurs.resenje.org/api/v1/currencies/eur/rates/today`
  - Automatsko dohvatanje srednjeg kursa
  - Kreiranje `Uplata` zapisa
  - Service metod za poziv eksternog API-ja

### 11. Selekcija preostalog iznosa za uplatu
- **Potreban Endpoint**: `GET /api/uplata/preostalo/{studentIndeksId}`
- **Status**: ❌ Nije implementirano
- **Opis**: Prikazuje preostali iznos u EUR i RSD (školarina = 3000 EUR)
- **Potrebno**:
  - Response DTO sa `preostaloEUR`, `preostaloRSD`, `ukupnoUplacenoEUR`, `ukupnoUplacenoRSD`
  - Konstanta: `SKOLARINA = 3000 EUR`
  - Agregacija svih uplata za studenta
  - Konverzija EUR ↔ RSD
  - Kalkulacija preostalog iznosa

---

## 📊 STATISTIKA

- **Implementirano**: 7 / 11 (64%)
- **Nije implementirano**: 4 / 11 (36%)

---

## 🔧 PRIORITET IMPLEMENTACIJE

### Visok prioritet:
1. **Dodavanje nove uplate** - finansijska funkcionalnost
2. **Selekcija preostalog iznosa za uplatu** - finansijska funkcionalnost

### Srednji prioritet:
3. **Obnova godine za studenta** - akademska funkcionalnost (max 60 ESPB)

### Niži prioritet:
4. **Pregled obnovljenih godina** - informativna funkcionalnost

---

## 📝 NAPOMENE

- Sve implementirane operacije koriste paginirano vraćanje podataka gde je to potrebno
- Implementirane su Response DTO klase za čiste API odgovore
- Koristi se layered architecture: Controller → Service → Repository
- Sve operacije su transakcione gde je potrebno
