# Detaljni Plan Potrebnih Testova za Projekat Studentske Službe

## Pregled
Prema specifikaciji projekta, potrebno je testirati tri glavne oblasti:
- **Operacije nad studentima** – 17p
- **Operacije nad predmetima i studijskim programima** – 5p
- **Operacije nad ispitima i ispitnim rokovima** – 8p

---

## 1. TESTOVI ZA STUDENTE (17 poena)

### 1.1 Selekcija studenta preko broja indeksa
- `StudentServiceTest.findByIndeks_validIndeks()` - pronalaženje studenta
- `StudentServiceTest.findByIndeks_invalidIndeks()` - vraćanje null za nepostojeći indeks
- `StudentServiceTest.findByIndeks_caseInsensitive()` - case-insensitive pretraga

### 1.2 Selekcija položenih ispita (paginirano)
- `StudentServiceTest.findPassedExams_validStudent()` - pronalaženje položenih
- `StudentServiceTest.findPassedExams_pagination()` - testiranje paginacije
- `StudentServiceTest.findPassedExams_noPassed()` - student bez položenih

### 1.3 Selekcija nepoloženih ispita (paginirano)
- `StudentServiceTest.findFailedExams_validStudent()` - pronalaženje nepoloženih
- `StudentServiceTest.findFailedExams_pagination()` - testiranje paginacije
- `StudentServiceTest.findFailedExams_excludesPassed()` - ne uključuje položene

### 1.4 Pregled upisanih godina
- `StudentServiceTest.findEnrolledYears_validStudent()` - pronalaženje upisanih godina
- `StudentServiceTest.findEnrolledYears_chronological()` - sortirane hronološki
- `StudentServiceTest.findEnrolledYears_noEnrollments()` - student bez upisa

### 1.5 Upis studenta na godinu
- `StudentServiceTest.enrollStudent_validEnrollment()` - uspešan upis
- `StudentServiceTest.enrollStudent_marksSubjectsAsUnpassed()` - predmeti inicijalno nepoloženi
- `StudentServiceTest.enrollStudent_duplicateEnrollment()` - sprečava dupli upis
- `StudentServiceTest.enrollStudent_espbValidation()` - validacija ESPB

### 1.6 Pregled obnovljenih godina
- `StudentServiceTest.findRenewedYears_validStudent()` - pronalaženje obnovljenih
- `StudentServiceTest.findRenewedYears_noRenewals()` - student bez obnova

### 1.7 Obnova godine za studenta
- `StudentServiceTest.renewYear_validRenewal()` - uspešna obnova
- `StudentServiceTest.renewYear_espbLimit_valid()` - dozvoljava do 60 ESPB
- `StudentServiceTest.renewYear_espbLimit_exceeded()` - odbija ako je > 60 ESPB
- `StudentServiceTest.renewYear_includesFailedAndNextYear()` - uključuje nepoložene i naredne

### 1.8 Dodavanje nove uplate
- `StudentServiceTest.addPayment_validPayment()` - uspešna uplata
- `StudentServiceTest.addPayment_fetchesExchangeRate()` - dohvata kurs iz API-ja
- `StudentServiceTest.addPayment_apiFailure()` - rukovanje greškom pri API pozivu

### 1.9 Selekcija preostalog iznosa za uplatu
- `StudentServiceTest.getRemainingPayment_noPaid()` - 3000€ za nove studente
- `StudentServiceTest.getRemainingPayment_partiallyPaid()` - preostalo nakon uplate
- `StudentServiceTest.getRemainingPayment_fullyPaid()` - 0€ za plaćene
- `StudentServiceTest.getRemainingPayment_inEurosAndDinars()` - konverzija valuta

### 1.10 Selekcija studenata po imenu/prezimenu (paginirano)
- `StudentServiceTest.findByName_byFirstName()` - pretraga po imenu
- `StudentServiceTest.findByName_byLastName()` - pretraga po prezimenu
- `StudentServiceTest.findByName_byBoth()` - pretraga po oba
- `StudentServiceTest.findByName_caseInsensitive()` - case-insensitive
- `StudentServiceTest.findByName_pagination()` - testiranje paginacije

### 1.11 Selekcija studenata po srednjoj školi
- `StudentServiceTest.findBySecondarySchool_validSchool()` - pronalaženje studenata
- `StudentServiceTest.findBySecondarySchool_noStudents()` - nema studenata
- `StudentServiceTest.findBySecondarySchool_onlyEnrolled()` - samo upisani

---

## 2. TESTOVI ZA PREDMETE I STUDIJSKE PROGRAME (5 poena)

### 2.1 Spisak predmeta na studijskom programu
- `PredmetServiceTest.findByStudyProgram_validProgram()` - pronalaženje predmeta
- `PredmetServiceTest.findByStudyProgram_sortedBySemester()` - sortirani po semestru
- `PredmetServiceTest.findByStudyProgram_noSubjects()` - program bez predmeta

### 2.2 Spisak svih predmeta sa svim podacima
- `PredmetServiceTest.findAll_returnsAllSubjects()` - svi predmeti
- `PredmetServiceTest.findAll_completeData()` - svi podaci (šifra, naziv, opis, ESPB, semestar, fond)
- `PredmetServiceTest.findAll_sortedByName()` - sortirani po nazivu

### 2.3 Dodavanje novog predmeta na studijski program
- `PredmetServiceTest.addSubject_validSubject()` - uspešno dodavanje
- `PredmetServiceTest.addSubject_uniqueCode()` - šifra je jedinstvena
- `PredmetServiceTest.addSubject_duplicateCode()` - odbija duplu šifru
- `PredmetServiceTest.addSubject_validEspbAndSemester()` - validacija ESPB i semestra

### 2.4 Prosečna ocena studenata na predmetu za raspon godina
- `PredmetServiceTest.getAverageGrade_validRange()` - prosečna ocena
- `PredmetServiceTest.getAverageGrade_onlyPassed()` - samo položeni
- `PredmetServiceTest.getAverageGrade_correctCalculation()` - ispravna matematička sredina
- `PredmetServiceTest.getAverageGrade_noStudents()` - nema studenata

---

## 3. TESTOVI ZA ISPITE I ISPITNE ROKOVE (8 poena)

### 3.1 Svi prijavljeni studenti za ispit
- `IspitServiceTest.findEnrolledStudents_validExam()` - pronalaženje prijavljenih
- `IspitServiceTest.findEnrolledStudents_sortedByIndex()` - sortirani po indeksu
- `IspitServiceTest.findEnrolledStudents_noStudents()` - nema prijavljenih

### 3.2 Prosečna ocena na ispitu
- `IspitServiceTest.getAverageGrade_validExam()` - prosečna ocena
- `IspitServiceTest.getAverageGrade_onlyPassed()` - samo položeni
- `IspitServiceTest.getAverageGrade_noGrades()` - nema ocena

### 3.3 Prijava ispita
- `IspitServiceTest.enrollExam_validEnrollment()` - uspešna prijava
- `IspitServiceTest.enrollExam_duplicateEnrollment()` - sprečava duplu prijavu
- `IspitServiceTest.enrollExam_studentNotEnrolled()` - error ako ne sluša predmet
- `IspitServiceTest.enrollExam_invalidData()` - error za nevalidne podatke

### 3.4 Dodavanje izlaska na ispit sa osvojenim poenima
- `IspitServiceTest.addExamResult_passed_51points()` - položen sa 51+ poena
- `IspitServiceTest.addExamResult_failed_50points()` - nije položen sa <51 poena
- `IspitServiceTest.addExamResult_marksAsPassed()` - označava predmet kao položen
- `IspitServiceTest.addExamResult_generatesGrade()` - generiše ocenu
- `IspitServiceTest.addExamResult_includesPreExamPoints()` - uključuje predispitne poene

### 3.5 Rezultati ispita u sortiranom spisu
- `IspitServiceTest.getExamResults_validExam()` - pronalaženje rezultata
- `IspitServiceTest.getExamResults_sortedCorrectly()` - sortirani (program → godina → broj)
- `IspitServiceTest.getExamResults_totalPoints()` - uključuje ukupne poene
- `IspitServiceTest.getExamResults_noResults()` - nema rezultata

### 3.6 Selekcija ostvarenih poena na predispinim obavezama
- `IspitServiceTest.getPreExamPoints_validStudent()` - pronalaženje poena
- `IspitServiceTest.getPreExamPoints_multipleObligations()` - više obaveza
- `IspitServiceTest.getPreExamPoints_correctSum()` - ispravna suma
- `IspitServiceTest.getPreExamPoints_noPoints()` - nema poena

### 3.7 Broj pokušaja polaganja predmeta
- `IspitServiceTest.getAttemptCount_neverTaken()` - 0 pokušaja
- `IspitServiceTest.getAttemptCount_multipleAttempts()` - više pokušaja
- `IspitServiceTest.getAttemptCount_onlyCountsExams()` - broji samo izlaske

---

## Rezime Potrebnih Test Klasa

### Nove test klase koje nedostaju:
1. **StudentServiceTest.java** - ~35 test metoda
2. **PredmetServiceTest.java** - ~15 test metoda
3. **IspitServiceTest.java** - ~25 test metoda

### Postojeće test klase (parcijalno pokrivene):
- ✅ **ParseUtilsTest.java** - pomoćne funkcije
- ✅ **StudentIndeksServiceTest.java** - pronalaženje sledećeg broja
- ✅ **DrziPredmetServiceTest.java** - mapiranje nastavnika

### Ukupno potrebnih test metoda: ~75 test metoda
