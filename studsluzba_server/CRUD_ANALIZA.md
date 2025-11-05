# Analiza CRUD operacija u projektu Studsluzba

## 📋 Implementirane CRUD operacije

### 1. **StudentPodaci** ✅
- **CREATE**: ✅ `POST /api/student/add` - Kreiranje novog studenta
- **READ**: ✅ `GET /api/student/all` - Lista svih studenata
- **READ**: ✅ `GET /api/student/svi` - Lista sa paginacijom
- **READ**: ✅ `GET /api/student/podaci/{id}` - Dohvatanje po ID-u
- **UPDATE**: ❌ Nedostaje
- **DELETE**: ❌ Nedostaje

### 2. **StudentIndeks** ✅ (Delimično)
- **CREATE**: ✅ `POST /api/student/saveindeks` - Kreiranje novog indeksa
- **READ**: ✅ `GET /api/student/indeks/{id}` - Dohvatanje po ID-u
- **READ**: ✅ `GET /api/student/indeksi/{idStudentPodaci}` - Lista indeksa za studenta
- **READ**: ✅ `GET /api/student/fastsearch` - Pretraga po indeksu (rn1923)
- **READ**: ✅ `GET /api/student/emailsearch` - Pretraga po email-u
- **READ**: ✅ `GET /api/student/search` - Napredna pretraga
- **READ**: ✅ `GET /api/student/profile/{studentIndeksId}` - Profil studenta
- **READ**: ✅ `GET /api/student/webprofile/{studentIndeksId}` - Web profil
- **UPDATE**: ❌ Nedostaje
- **DELETE**: ❌ Nedostaje

### 3. **Nastavnik** ✅ (Delimično)
- **CREATE**: ✅ `POST /api/nastavnik/add` - Kreiranje novog nastavnika
- **READ**: ✅ `GET /api/nastavnik/all` - Lista svih nastavnika
- **READ**: ✅ `GET /api/nastavnik/{id}` - Dohvatanje po ID-u
- **READ**: ✅ `GET /api/nastavnik/search` - Pretraga po imenu i prezimenu
- **UPDATE**: ❌ Nedostaje
- **DELETE**: ❌ Nedostaje

### 4. **Predmet** ✅ (Samo READ)
- **CREATE**: ❌ Nedostaje
- **READ**: ✅ `GET /api/predmet/all` - Lista svih predmeta
- **READ**: ✅ `GET /api/predmet/all/{godinaAkreditacije}` - Predmeti za godinu akreditacije
- **UPDATE**: ❌ Nedostaje
- **DELETE**: ❌ Nedostaje

### 5. **StudijskiProgram** ✅ (Samo READ)
- **CREATE**: ❌ Nedostaje
- **READ**: ✅ `GET /api/studprogram/all/sorted` - Lista sortirana po godini
- **READ**: ✅ `GET /api/studprogram/oznaka/all` - Lista oznaka programa
- **UPDATE**: ❌ Nedostaje
- **DELETE**: ❌ Nedostaje

### 6. **DrziPredmet** ✅ (Delimično)
- **CREATE**: ✅ `POST /api/drzi-predmet/add` - Kreiranje (batch)
- **CREATE**: ✅ `POST /api/professor/raspodelanastave/drzipredmet/add` - Kreiranje
- **READ**: ✅ `GET /api/professor/raspodelanastave/drzipredmet/aktivna/nastavnik/{idNastavnika}` - Predmeti za nastavnika
- **DELETE**: ✅ `DELETE /api/professor/raspodelanastave/drzipredmet/{id}` - Brisanje
- **READ**: ❌ Nedostaje dohvatanje po ID-u
- **READ**: ❌ Nedostaje lista svih
- **UPDATE**: ❌ Nedostaje

### 7. **SlusaPredmet** ✅ (Delimično)
- **CREATE**: ✅ `POST /api/professor/raspodelanastave/slusapredmet/add` - Kreiranje
- **READ**: ✅ `GET /api/professor/raspodelanastave/slusapredmetaktivna/{idPredmeta}/{idNastavnika}` - Studenti koji slušaju
- **READ**: ✅ `GET /api/professor/raspodelanastave/slusapredmetaktivna/{idDrziPredmet}` - Studenti za DrziPredmet
- **READ**: ✅ `GET /api/professor/raspodelanastave/neslusapredmetaktivna/{idDrziPredmet}` - Studenti koji ne slušaju
- **READ**: ❌ Nedostaje dohvatanje po ID-u
- **READ**: ❌ Nedostaje lista svih
- **UPDATE**: ❌ Nedostaje
- **DELETE**: ❌ Nedostaje

---

## ❌ Nedostajuće CRUD operacije (prema specifikaciji)

### 8. **Ispit** ❌
- **CREATE**: ❌ Nedostaje - Kreiranje ispita
- **READ**: ❌ Nedostaje - Lista ispita
- **READ**: ❌ Nedostaje - Dohvatanje po ID-u
- **READ**: ❌ Nedostaje - Ispiti za predmet
- **READ**: ❌ Nedostaje - Ispiti za ispitni rok
- **UPDATE**: ❌ Nedostaje - Ažuriranje ispita
- **DELETE**: ❌ Nedostaje - Brisanje ispita

### 9. **IspitniRok** ❌
- **CREATE**: ❌ Nedostaje - Kreiranje ispitnog roka
- **READ**: ❌ Nedostaje - Lista ispitnih rokova
- **READ**: ❌ Nedostaje - Aktivni ispitni rokovi
- **READ**: ❌ Nedostaje - Dohvatanje po ID-u
- **UPDATE**: ❌ Nedostaje - Ažuriranje ispitnog roka
- **DELETE**: ❌ Nedostaje - Brisanje ispitnog roka

### 10. **PrijavaIspita** ❌
- **CREATE**: ❌ Nedostaje - Prijava studenta na ispit
- **READ**: ❌ Nedostaje - Lista prijava
- **READ**: ❌ Nedostaje - Prijave za studenta
- **READ**: ❌ Nedostaje - Prijave za ispit
- **UPDATE**: ❌ Nedostaje - Ažuriranje prijave
- **DELETE**: ❌ Nedostaje - Otkazivanje prijave

### 11. **IzlazakNaIspit** ❌
- **CREATE**: ❌ Nedostaje - Evidencija izlaska na ispit
- **READ**: ❌ Nedostaje - Lista izlazaka
- **READ**: ❌ Nedostaje - Izlasci za studenta
- **READ**: ❌ Nedostaje - Izlasci za ispit
- **UPDATE**: ❌ Nedostaje - Unos ocene i poena
- **DELETE**: ❌ Nedostaje - Brisanje izlaska

### 12. **PredispitneObaveze** ❌
- **CREATE**: ❌ Nedostaje - Kreiranje predispitnih obaveza
- **READ**: ❌ Nedostaje - Lista obaveza
- **READ**: ❌ Nedostaje - Obaveze za predmet
- **READ**: ❌ Nedostaje - Obaveze za studenta
- **UPDATE**: ❌ Nedostaje - Unos bodova
- **DELETE**: ❌ Nedostaje - Brisanje obaveza

### 13. **UpisGodine** ❌
- **CREATE**: ❌ Nedostaje - Upis godine
- **READ**: ❌ Nedostaje - Lista upisa
- **READ**: ❌ Nedostaje - Upisi za studenta
- **READ**: ❌ Nedostaje - Upisi za školsku godinu
- **UPDATE**: ❌ Nedostaje - Ažuriranje upisa
- **DELETE**: ❌ Nedostaje - Brisanje upisa

### 14. **ObnovaGodine** ❌
- **CREATE**: ❌ Nedostaje - Obnova godine
- **READ**: ❌ Nedostaje - Lista obnova
- **READ**: ❌ Nedostaje - Obnove za studenta
- **UPDATE**: ❌ Nedostaje - Ažuriranje obnove
- **DELETE**: ❌ Nedostaje - Brisanje obnove

### 15. **SkolskaGodina** ❌
- **CREATE**: ❌ Nedostaje - Kreiranje školske godine
- **READ**: ❌ Nedostaje - Lista školske godine
- **READ**: ❌ Nedostaje - Aktivna školska godina
- **READ**: ❌ Nedostaje - Dohvatanje po ID-u
- **UPDATE**: ❌ Nedostaje - Ažuriranje školske godine
- **DELETE**: ❌ Nedostaje - Brisanje školske godine

### 16. **Uplata** ❌
- **CREATE**: ❌ Nedostaje - Evidencija uplate
- **READ**: ❌ Nedostaje - Lista uplata
- **READ**: ❌ Nedostaje - Uplate za studenta
- **READ**: ❌ Nedostaje - Uplate po statusu
- **UPDATE**: ❌ Nedostaje - Ažuriranje statusa uplate
- **DELETE**: ❌ Nedostaje - Brisanje uplate

### 17. **Grupa** ❌
- **CREATE**: ❌ Nedostaje - Kreiranje grupe
- **READ**: ❌ Nedostaje - Lista grupa
- **READ**: ❌ Nedostaje - Grupe za studijski program
- **UPDATE**: ❌ Nedostaje - Ažuriranje grupe
- **DELETE**: ❌ Nedostaje - Brisanje grupe

### 18. **SrednjaSkola** ❌
- **CREATE**: ❌ Nedostaje - Kreiranje srednje škole
- **READ**: ❌ Nedostaje - Lista srednjih škola
- **UPDATE**: ❌ Nedostaje
- **DELETE**: ❌ Nedostaje

### 19. **StudentSrednjaSkola** ❌
- **CREATE**: ❌ Nedostaje - Povezivanje studenta sa srednjom školom
- **READ**: ❌ Nedostaje - Srednja škola za studenta
- **UPDATE**: ❌ Nedostaje
- **DELETE**: ❌ Nedostaje

### 20. **VisokoskolskaUstanova** ❌
- **CREATE**: ❌ Nedostaje - Kreiranje visokoškolske ustanove
- **READ**: ❌ Nedostaje - Lista ustanova
- **UPDATE**: ❌ Nedostaje
- **DELETE**: ❌ Nedostaje

### 21. **StudentVisokaUstanova** ❌
- **CREATE**: ❌ Nedostaje - Povezivanje studenta sa visokom ustanovom
- **READ**: ❌ Nedostaje - Visoka ustanova za studenta
- **UPDATE**: ❌ Nedostaje
- **DELETE**: ❌ Nedostaje

### 22. **NastavnikZvanje** ❌
- **CREATE**: ❌ Nedostaje - Dodavanje zvanja nastavniku
- **READ**: ❌ Nedostaje - Zvanja za nastavnika
- **UPDATE**: ❌ Nedostaje
- **DELETE**: ❌ Nedostaje

### 23. **NastavnikObrazovanje** ❌
- **CREATE**: ❌ Nedostaje - Dodavanje obrazovanja nastavniku
- **READ**: ❌ Nedostaje - Obrazovanje za nastavnika
- **UPDATE**: ❌ Nedostaje
- **DELETE**: ❌ Nedostaje

---

## 📊 Rezime

### Implementirano:
- ✅ **StudentPodaci**: CREATE, READ (3 verzije)
- ✅ **StudentIndeks**: CREATE, READ (7 verzija)
- ✅ **Nastavnik**: CREATE, READ (3 verzije)
- ✅ **Predmet**: READ (2 verzije)
- ✅ **StudijskiProgram**: READ (2 verzije)
- ✅ **DrziPredmet**: CREATE, DELETE, READ (1 verzija)
- ✅ **SlusaPredmet**: CREATE, READ (3 verzije)

### Nedostaje (UPDATE operacije):
- ❌ **StudentPodaci**: UPDATE, DELETE
- ❌ **StudentIndeks**: UPDATE, DELETE
- ❌ **Nastavnik**: UPDATE, DELETE
- ❌ **Predmet**: CREATE, UPDATE, DELETE
- ❌ **StudijskiProgram**: CREATE, UPDATE, DELETE

### Potpuno nedostaje (bez CRUD operacija):
- ❌ **Ispit** (0/7 operacija)
- ❌ **IspitniRok** (0/6 operacija)
- ❌ **PrijavaIspita** (0/6 operacija)
- ❌ **IzlazakNaIspit** (0/6 operacija)
- ❌ **PredispitneObaveze** (0/6 operacija)
- ❌ **UpisGodine** (0/6 operacija)
- ❌ **ObnovaGodine** (0/5 operacija)
- ❌ **SkolskaGodina** (0/6 operacija)
- ❌ **Uplata** (0/6 operacija)
- ❌ **Grupa** (0/5 operacija)
- ❌ **SrednjaSkola** (0/4 operacije)
- ❌ **StudentSrednjaSkola** (0/4 operacije)
- ❌ **VisokoskolskaUstanova** (0/4 operacije)
- ❌ **StudentVisokaUstanova** (0/4 operacije)
- ❌ **NastavnikZvanje** (0/4 operacije)
- ❌ **NastavnikObrazovanje** (0/4 operacije)

---

## 🎯 Preporuke za implementaciju

### Prioritet 1 (Visok - Kritično za funkcionalnost):
1. **Ispit** - Neophodan za prijave i izlaske
2. **IspitniRok** - Neophodan za organizaciju ispita
3. **PrijavaIspita** - Osnovna funkcionalnost sistema
4. **IzlazakNaIspit** - Evidencija rezultata
5. **SkolskaGodina** - Osnovni entitet za organizaciju

### Prioritet 2 (Srednji):
6. **UpisGodine** - Upis studenata
7. **ObnovaGodine** - Obnova studija
8. **PredispitneObaveze** - Praćenje obaveza
9. **Uplata** - Finansijsko upravljanje
10. **Grupa** - Organizacija nastave

### Prioritet 3 (Nizak - Kompletiranje):
11. **StudentPodaci**: UPDATE, DELETE
12. **StudentIndeks**: UPDATE, DELETE
13. **Nastavnik**: UPDATE, DELETE
14. **Predmet**: CREATE, UPDATE, DELETE
15. **StudijskiProgram**: CREATE, UPDATE, DELETE
16. **SrednjaSkola** i **StudentSrednjaSkola**
17. **VisokoskolskaUstanova** i **StudentVisokaUstanova**
18. **NastavnikZvanje** i **NastavnikObrazovanje**

---

**Ukupno implementirano**: ~7 entiteta sa delimičnim CRUD operacijama
**Ukupno nedostaje**: 16 entiteta bez CRUD operacija + UPDATE/DELETE za postojeće

