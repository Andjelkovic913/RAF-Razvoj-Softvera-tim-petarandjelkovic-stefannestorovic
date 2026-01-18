## Kada nece da se pokrene zbog verzije Jave:
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
mvn clean install

## Najlakse za pokretanje klijenta:
mvn clean compile
mvn javafx:run

## Servis pokretanje
export JAVA_HOME=$(/usr/libexec/java_home -v 11) && mvn clean compile

#### * Za svaku funkcionalnost napisati za koji tacno deo specifikacije je napravljen kako bi se lakse proverilo da li radi ono sto treba i da bi se lakse znalo cemu sluzi i da li je pokriveno


- [x] Pretraga studenata: Dukic ili Đukić (FIXED - dodato normalizaciju dijakritika u StudentIndeksRepository query)
### Uz pomoc String Utils foldera
- [x] Ne exportuje izvestaj sa svim slovima latinice Đ, Ć (FIXED - DejaVu Sans font)
- [x] Search imena treba da moze da trazi i bez potpunog imena (Nik, Nikola, Ni ...)
- [x] Prikazati Srednju skolu kada se gleda profil studenta (FIXED - dodato fetchStudentHighSchool u StudentService)

## Student 
- [x] Prijava ispita kroz novi pop up iz profila studenta (FIXED - PrijavaIspitaPopupController + prijavaIspitaPopup.fxml)
- [x] Uverenje o studiranju - implementirano (uverenjeOStudiranju1.jrxml)
- [x] Uverenje o položenim ispitima - implementirano (uverenjeOStudiranju.jrxml)

---

## Predmeti i Studijski program
- [x] Prijava studenta na predmet (SlusaPredmet) - novi prozor u menu Predmeti (FIXED - PrijavaStudentaController + SlusaPredmetService + DrziPredmetService)
- [x] Prikaz prosečne ocene na predmetu za raspon godina (UI + izveštaj) - JRXML template kreiran (prosecnaOcenaNaPredmetu.jrxml) - PERA uradio treba da se merguje projekat
- [x] Prijavljivanje studenata na predmete 
- [x] Lista prijavljenih studenata po predmetima 

## Ispiti
- [x] Kada se klikne na Prozor Ispiti - rezultati onda su imena u tabeli null null (FIXED - server IzlazakNaIspitResponse + EntityMappers)
- [x] Omoguciti da se u samom prozoru izabere predmet i smer pored broja i godine kada se student prijavljuje na ispit ![img_4.png](img_4.png)
- [x] Kada se unose rezultati ispita - dodato validacija i debug logging za prijavaId (FIXED - RezultatiIspitaController + IspitService)
- [x] Novi pop up za prijavu na ispit

## Other
- [x] Dodati dugme za Izvestaj tamo gde fali - Predmeti po Studijskom Programu, u prozoru ispitni rokovi treba da uvedemo generisanje izvestaja, u prozoru Ispiti - rezultati promeniti naziv ljubicastog dugmeta samo da pise Izvestaj ,jos negde sigurno - TREBALO bi da je sve tu

- [x] Surline biblioteke - PERA uradio treba da se merguje projekat

Da znam:
Key Points:
* No ReportService usage: Your current implementation directly uses JasperReports API instead of the ReportService class (which exists in your codebase but isn't being used here)
* Synchronous process: Everything happens on the JavaFX UI thread, which could freeze the UI for large datasets
* Fixed output location: PDF always saves to the same filename in the project root
* No viewer: Unlike ReportService.showReport(), this directly exports to PDF without showing a preview window



📊 Priority Order (Recommended)
Priority	Task	Complexity	Impact
~~P1~~	~~3.4 Fix unos rezultata 500 error~~	~~Medium~~	DONE ✅
~~P1~~	~~3.5 Fix validacija pri kreiranju ispita~~	~~Medium~~	DONE ✅ (već implementirano)
✅ P2	3.2 Fix null null imena	Low	DONE ✅
P3	3.3 Predmet/smer selection	Medium	Medium
P3	1.1 Prijava ispita popup	Medium	Medium
P4	1.2 Uverenje o studiranju	Medium	Low
P4	1.3 Uverenje o položenim ispitima	High	Low
P4	2.1 Prosečna ocena UI + Report	High	Low
P5	4.1 Add Izvestaj buttons	Low	Low


------------


# 📋 **COMPREHENSIVE IMPLEMENTATION PLAN**

### **1.3 Uverenje o položenim ispitima grupisano po godini**
**Problem:** Missing JRXML report for passed exams grouped by year  
**Solution:**
- Create new `uverenjeOPolozenimIspitima.jrxml` with:
  - Student header info
  - Exams grouped by "godina studija" (1, 2, 3, 4)
  - Per group: predmet, ESPB, ocena, datum
  - Footer with total ESPB and average grade
- Add [handlePrintTranscript()](cci:1://file:///Users/stefannestorovic/RAFF/3_Godina/5_Semestar/Razvoj%20Softvera/Projekat/Klijent/src/main/java/org/raflab/studsluzbadesktopclient/controllers/StudentProfileController.java:244:4-247:5) implementation in [StudentProfileController](cci:2://file:///Users/stefannestorovic/RAFF/3_Godina/5_Semestar/Razvoj%20Softvera/Projekat/Klijent/src/main/java/org/raflab/studsluzbadesktopclient/controllers/StudentProfileController.java:14:0-260:1)
- Need server endpoint to get exams grouped by year OR client-side grouping

---

## **Category 2: PREDMETI**

### **2.1 Prosečna ocena na predmetu za raspon godina (UI + Report)**
**Problem:** Missing functionality to display/export average grade for date range  
**Solution:**
- **Server side:**
  - Add endpoint `GET /api/izlazak-na-ispit/predmet/{id}/prosecna-ocena?godinaOd=X&godinaDo=Y`
  - Query: `SELECT AVG(ocena) FROM izlazak_na_ispit WHERE predmet_id = ? AND YEAR(datum) BETWEEN ? AND ?`
- **Client side:**
  - Add UI in `PredmetViewController` with:
    - DatePicker for godinaOd/godinaDo
    - Label to display result
    - Button "Generiši izveštaj" for PDF
  - Create simple `prosecnaOcenaPredmet.jrxml`

---

## **Category 3: ISPITI (Bug Fixes)**

### **3.2 Fix null null imena u rezultatima tabeli** ✅ DONE
**Solution:** Added `ime`/`prezime` fields to server's `IzlazakNaIspitResponse` and mapping in `EntityMappers.fromIzlazakNaIspitToResponse()`

---

### **3.3 Omogući izbor predmeta i smera pri prijavi ispita**
**Problem:** `PrijavaIspitaController` only has broj/godina, no predmet/smer selection  
**Location:** `PrijavaIspitaController.java`, `prijavaIspita.fxml`  
**Solution:**
- Add ComboBox for studijski program (smer)
- Add ComboBox for predmet (filtered by smer)
- Update search to find exams for that specific predmet
- Pre-filter available exams based on student's eligible subjects

---

### **3.4 Fix unos rezultata (500 error: id must not be null)**
**Problem:** Server expects `prijavaIspitaId` but client sends `ispitId`  
**Location:** `RezultatiIspitaController.handleSaveResult()`, `PrijavaIspitaService.unosRezultata()`  
**Root Cause:** Looking at [IzlazakNaIspitRequest.java](cci:7://file:///Users/stefannestorovic/RAFF/3_Godina/5_Semestar/Razvoj%20Softvera/Projekat/Za%20commit/studsluzba_server/src/main/java/org/raflab/studsluzba/controllers/request/IzlazakNaIspitRequest.java:0:0-0:0) - it needs:
- `prijavaIspitaId` (NOT ispitId!)
- `slusaPredmetId`
- points and grade
  **Solution:**
- Client needs to get `prijavaIspitaId` from the selected `PrijavljeniStudentDTO`
- Currently DTO has `prijavaId` - use this
- Fix the request mapping in `PrijavaIspitaService.unosRezultata()`

---

### **3.5 Fix validacija pri kreiranju ispita (missing fields)**
**Problem:** Server `IspitRequest` requires:
- `@NotNull datumOdrzavanja` ✓
- `@NotNull vremePocetka` ✗ (client sends null if empty)
- `@NotNull vremeZavrsetka` ✗ (client sends null if empty)
- `@NotNull drziPredmetId` ✓
- `@NotNull predmetId` ✗ (CLIENT DOESN'T SEND THIS!)
- `@NotNull ispitniRokId` ✓
- `@NotNull skolskaGodinaId` ✗ (CLIENT DOESN'T SEND THIS!)

**Location:** [IspitService.create()](cci:1://file:///Users/stefannestorovic/RAFF/3_Godina/5_Semestar/Razvoj%20Softvera/Projekat/Klijent/src/main/java/org/raflab/studsluzbadesktopclient/services/IspitService.java:80:4-110:5) (client) line 81-111  
**Solution:**
- Add time fields validation (require vremePocetka/vremeZavrsetka or make them optional on server)
- Get `predmetId` from DrziPredmet selection (need to fetch or store it)
- Get `skolskaGodinaId` from selected IspitniRok (need to fetch current school year)
- Update client `IspitDTO` to include missing fields

---

## **Category 4: OTHER (UI Improvements)**

### **4.1 Add Izvestaj buttons where missing**
**Locations:**
1. **Predmeti po Studijskom Programu** - `PredmetViewController.fxml`
  - Add "Izveštaj" button to generate `sviPredmetiPoStPr.pdf`
2. **Ispitni Rokovi** - `IspitniRokoviController`
  - Add report generation (list of all exams in that period)
3. **Ispiti - Rezultati** - `RezultatiIspitaController`
  - Rename purple button from "Štampaj zapisnik" to just "Izveštaj"


---

# 🔧 **Files to Modify**

### **Server Side:**
1. `IspitRequest.java` - Consider making vremePocetka/vremeZavrsetka optional
2. `IzlazakNaIspitService.java` - Add prosečna ocena endpoint
3. (Optional) Add new DTO for grouped exams by year

### **Client Side:**
1. [IspitService.java](cci:7://file:///Users/stefannestorovic/RAFF/3_Godina/5_Semestar/Razvoj%20Softvera/Projekat/Klijent/src/main/java/org/raflab/studsluzbadesktopclient/services/IspitService.java:0:0-0:0) - Add missing fields (predmetId, skolskaGodinaId)
2. `PrijavaIspitaService.java` - Fix unosRezultata to use prijavaIspitaId
3. `PrijavaIspitaController.java` - Add predmet/smer ComboBoxes
4. `RezultatiIspitaController.java` - Fix result saving, use correct IDs
5. [StudentProfileController.java](cci:7://file:///Users/stefannestorovic/RAFF/3_Godina/5_Semestar/Razvoj%20Softvera/Projekat/Klijent/src/main/java/org/raflab/studsluzbadesktopclient/controllers/StudentProfileController.java:0:0-0:0) - Implement certificate generation
6. `PredmetViewController.java` - Add prosečna ocena UI + report button

### **New JRXML Files:**
1. `uverenjeOStudiranju.jrxml`
2. `uverenjeOPolozenimIspitima.jrxml`
3. (Optional) `prosecnaOcenaPredmet.jrxml`

---

**Please review this plan and let me know which items you want me to execute first, or if you want me to proceed with all P1 (blocking bugs) first.**

------------------