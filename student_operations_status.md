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
- **Endpoint**: `GET /api/obnova-godine/student/{studentIndeksId}`
- **Status**: ✅ Implementirano
- **Lokacija**: `ObnovaGodineController.java`, `ObnovaGodineService.java`
- **Metod**: `getObnovaGodineByStudentIndeks()`
- **Vraća**: Lista svih obnovljenih godina sa detaljima (datum, godina, ESPB, razlog, školska godina)

### 9. Obnova godine za studenta
- **Endpoint**: `POST /api/obnova-godine/obnovi`
- **Status**: ✅ Implementirano
- **Opis**: Omogućava obnovu godine sa automatskom detekcijom nepoloženih predmeta + izbor dodatnih predmeta (max 60 ESPB)
- **Lokacija**: `ObnovaGodineController.java`, `ObnovaGodineService.java`
- **Metod**: `createObnovaGodine()`
- **Request DTO**: `ObnovaGodineRequest` sa `studentIndeksId`, `godina`, `skolskaGodinaId`, `nextYearDrziPredmetIds[]`, `prenetiESPB`, `razlogObnove`
- **Implementirano**:
  - ✅ Automatska detekcija nepoloženih predmeta iz istorije studenta
  - ✅ Validacija: zbir ESPB ≤ 60
  - ✅ Validacija: može obnoviti samo trenutnu godinu
  - ✅ Validacija: ne može obnoviti istu godinu u istoj školskoj godini
  - ✅ Dozvoljava obnovu iste godine u različitim školskim godinama
  - ✅ Kreiranje `ObnovaGodine` zapisa
  - ✅ Kreiranje `SlusaPredmet` zapisa za sve predmete (nepoložene + dodatne)
  - ✅ Transakciona obrada
- **Dodatni Endpoint**: `GET /api/obnova-godine/failed-subjects/{studentIndeksId}` - pronalazi nepoložene predmete sa ESPB detaljima

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

- **Implementirano**: 16 / 22 (73%)
- **Nije implementirano**: 6 / 22 (27%)

### Razrada po Kategorijama:

**Student (9/11 - 82%)**
- ✅ 9 operacija implementirano
- ❌ 2 operacije (uplate) 

**Studijski program i predmet (3/4 - 75%)**
- ✅ 3 operacije implementirano
- ❌ 1 operacija (prosečna ocena za raspon godina)

**Ispit (4/7 - 57%)**
- ✅ 4 operacije implementirano (prijavljeni studenti, prijava ispita, izlazak na ispit, broj pokušaja)
- ❌ 3 operacije (prosečna ocena, rezultati ispita, predispitne obaveze)

---

## 🔧 PRIORITET IMPLEMENTACIJE

### Visok prioritet:
1. **Dodavanje nove uplate** - finansijska funkcionalnost
2. **Selekcija preostalog iznosa za uplatu** - finansijska funkcionalnost

---

## ❌ NIJE IMPLEMENTIRANO

### 10. Dodavanje nove uplate
- **Endpoint**: `POST /api/uplata/dodaj`
- **Status**: ❌ Nije implementirano
- **Opis**: Čuva datum uplate, iznos u dinarima i srednji kurs (dohvaćen sa API-ja)
- **Potrebno**:
  - Request DTO sa `studentIndeksId`, `iznosDinara`
  - Integracija sa eksternim API-jem: `https://kurs.resenje.org/api/v1/currencies/eur/rates/today`
  - Automatsko dohvatanje srednjeg kursa
  - Kreiranje `Uplata` zapisa

### 11. Selekcija preostalog iznosa za uplatu
- **Endpoint**: `GET /api/uplata/preostalo/{studentIndeksId}`
- **Status**: ❌ Nije implementirano
- **Opis**: Prikazuje preostali iznos u EUR i RSD (školarina = 3000 EUR)
- **Potrebno**:
  - Response DTO sa `preostaloEUR`, `preostaloRSD`, `ukupnoUplacenoEUR`, `ukupnoUplacenoRSD`
  - Konstanta: `SKOLARINA = 3000 EUR`
  - Agregacija svih uplata za studenta
  - Konverzija EUR ↔ RSD

### 12. Spisak predmeta na studijskom programu
- **Endpoint**: `GET /api/predmet/by-program/{programId}`
- **Status**: ✅ Implementirano
- **Lokacija**: `PredmetController.java`, `PredmetService.java`
- **Dodatno**: Opcioni parametar `obavezan` za filtriranje

### 13. Spisak svih predmeta sa svim podacima
- **Endpoint**: `GET /api/predmet/svi`
- **Status**: ✅ Implementirano
- **Lokacija**: `PredmetController.java`, `PredmetService.java`

### 14. Dodavanje novog predmeta na studijski program
- **Endpoint**: `POST /api/predmet/add`
- **Status**: ✅ Implementirano
- **Lokacija**: `PredmetController.java`, `PredmetService.java`
- **Request DTO**: `PredmetRequest`

### 15. Prosečna ocena studenata na predmetu za raspon godina
- **Endpoint**: `GET /api/predmet/{predmetId}/prosecna-ocena?od={godina}&do={godina}`
- **Status**: ❌ Nije implementirano

### 16. Svi prijavljeni studenti za ispit
- **Endpoint**: `GET /api/prijava-ispita/{ispitId}/students`
- **Status**: ✅ Implementirano
- **Lokacija**: `PrijavaIspitaController.java`, `PrijavaIspitaService.java`
- **Response**: `List<PrijavljeniStudentDTO>`

### 17. Prosečna ocena na ispitu
- **Endpoint**: `GET /api/ispit/{ispitId}/prosecna-ocena`
- **Status**: ❌ Nije implementirano

### 18. Prijava ispita
- **Endpoint**: `POST /api/prijava-ispita/add`
- **Status**: ✅ Implementirano
- **Lokacija**: `PrijavaIspitaController.java`, `PrijavaIspitaService.java`
- **Request DTO**: `PrijavaIspitaRequest`

### 19. Dodavanje izlaska na ispit
- **Endpoint**: `POST /api/izlazak-na-ispit`
- **Status**: ✅ Implementirano
- **Lokacija**: `IzlazakNaIspitController.java`, `IzlazakNaIspitService.java`
- **Request DTO**: `IzlazakNaIspitRequest`
- **Logika**: Implementirana logika za čuvanje položenog predmeta

### 20. Rezultati ispita (sortirani spisak)
- **Endpoint**: `GET /api/ispit/{ispitId}/rezultati`
- **Status**: ❌ Nije implementirano
- **Sortiranje**: 1. studijski program, 2. godina upisa, 3. broj indeksa

### 21. Selekcija ostvarenih poena na predispitnim obavezama
- **Endpoint**: `GET /api/predispitne-obaveze/student/{studentIndeksId}/predmet/{predmetId}`
- **Status**: ❌ Nije implementirano

### 22. Broj puta koliko je student polagao predmet
- **Endpoint**: `GET /api/izlazak-na-ispit/student/{studentIndeksId}/predmet/{predmetId}/broj-pokusaja`
- **Status**: ✅ Implementirano
- **Lokacija**: `IzlazakNaIspitController.java`, `IzlazakNaIspitService.java`
- **Response**: Broj pokušaja (Long)

---

## 📝 NAPOMENE

- Sve implementirane operacije koriste paginirano vraćanje podataka gde je to potrebno
- Implementirane su Response DTO klase za čiste API odgovore
- Koristi se layered architecture: Controller → Service → Repository
- Sve operacije su transakcione gde je potrebno
- Specifikacija zahteva testove za svaku operaciju (Postman ili sličan alat)
