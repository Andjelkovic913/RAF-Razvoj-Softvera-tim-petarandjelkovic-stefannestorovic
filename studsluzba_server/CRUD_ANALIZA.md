# Analiza CRUD operacija u projektu Studsluzba

## 📋 Implementirane CRUD operacije

### 1. **StudentPodaci** ✅
- **CREATE**: ✅ `POST /api/student/add` - Kreiranje novog studenta
- **READ**: ✅ `GET /api/student/all` - Lista svih studenata
- **READ**: ✅ `GET /api/student/svi` - Lista sa paginacijom
- **READ**: ✅ `GET /api/student/podaci/{id}` - Dohvatanje po ID-u
- **UPDATE**: ✅ `PUT /api/student/update/{id}` - Ažuriranje studenta
- **DELETE**: ✅ `DELETE /api/student/{id}` - Brisanje studenta

### 2. **StudentIndeks** ✅ (Delimično)
- **CREATE**: ✅ `POST /api/student/saveindeks` - Kreiranje novog indeksa
- **READ**: ✅ `GET /api/student/indeks/{id}` - Dohvatanje po ID-u
- **READ**: ✅ `GET /api/student/indeksi/{idStudentPodaci}` - Lista indeksa za studenta
- **READ**: ✅ `GET /api/student/fastsearch` - Pretraga po indeksu (rn1923)
- **READ**: ✅ `GET /api/student/emailsearch` - Pretraga po email-u
- **READ**: ✅ `GET /api/student/search` - Napredna pretraga
- **READ**: ✅ `GET /api/student/profile/{studentIndeksId}` - Profil studenta
- **READ**: ✅ `GET /api/student/webprofile/{studentIndeksId}` - Web profil
- **READ**: ✅ `GET /api/student/webprofile/email` - Web profil po email-u (parsira indeks iz emaila)
- **UPDATE**: ✅ `PUT /api/student/update/{studProgramOznaka}/{godina}/{broj}` - Ažuriranje podataka studenta po indeksu
- **DELETE**: ❌ Nedostaje

### 3. **Nastavnik** ✅ (Delimično)
- **CREATE**: ✅ `POST /api/nastavnik/add` - Kreiranje novog nastavnika
- **READ**: ✅ `GET /api/nastavnik/all` - Lista svih nastavnika
- **READ**: ✅ `GET /api/nastavnik/{id}` - Dohvatanje po ID-u
- **READ**: ✅ `GET /api/nastavnik/search` - Pretraga po imenu i prezimenu
- **UPDATE**: ✅ `PUT /api/nastavnik/update/{id}` - Ažuriranje nastavnika
- **DELETE**: ✅ `DELETE /api/nastavnik/delete/{id}` - Brisanje nastavnika

### 4. **Predmet** ✅ (Samo READ)
- **CREATE**: ❌ Nedostaje
- **READ**: ✅ `GET /api/professor/raspodelanastave/drzipredmet/aktivna/nastavnik/{idNastavnika}` - Aktivni predmeti za nastavnika
- **READ**: ✅ `GET /api/professor/raspodelanastave/slusapredmetaktivna/{idPredmeta}/{idNastavnika}` - Studenti na predmetu
- **UPDATE**: ❌ Nedostaje
- **DELETE**: ❌ Nedostaje

### 5. **StudijskiProgram** ✅
- **CREATE**: ✅ `POST /api/studijskiprogram/add` - Kreiranje studijskog programa
- **READ**: ✅ `GET /api/studijskiprogram/all/sorted` - Lista sortirana po godini
- **READ**: ✅ `GET /api/studijskiprogram/oznaka/all` - Lista oznaka programa
- **UPDATE**: ✅ `POST /api/studijskiprogram/update` - Ažuriranje programa
- **DELETE**: ✅ `DELETE /api/studijskiprogram/{id}` - Brisanje programa

### 6. **DrziPredmet** ✅ (Delimično)
- **CREATE**: ✅ `POST /api/professor/raspodelanastave/drzipredmet/add` - Kreiranje veze
- **CREATE**: ✅ `POST /api/professor/raspodelanastave/drzipredmet/add` - Kreiranje (batch)
- **READ**: ✅ `GET /api/professor/raspodelanastave/drzipredmet/aktivna/nastavnik/{idNastavnika}` - Predmeti za nastavnika
- **DELETE**: ✅ `DELETE /api/professor/raspodelanastave/drzipredmet/{id}` - Brisanje
- **READ**: ❌ Nedostaje dohvatanje po ID-u
- **READ**: ❌ Nedostaje lista svih
- **UPDATE**: ❌ Nedostaje

### 7. **SlusaPredmet** ✅ (Potpuno implementirano) (`/api/slusapredmet`):
- **CREATE**: ✅ `POST /add` - Kreiranje novog SlusaPredmet
- **READ**: ✅ `GET /slusapredmet/{id}` - Dohvatanje po ID-u
- **READ**: ✅ `GET /slusapredmet/all` - Lista svih SlusaPredmet veza
- **UPDATE**: ✅ `PUT /slusapredmet/{id}` - Ažuriranje
- **DELETE**: ✅ `DELETE /slusapredmet/{id}` - Brisanje

