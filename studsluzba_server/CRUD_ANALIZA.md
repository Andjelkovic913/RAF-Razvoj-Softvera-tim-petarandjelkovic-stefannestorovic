# Analiza CRUD operacija u projektu Studsluzba

## 📋 Implementirane CRUD operacije

### 1. **StudentPodaci** ✅
- **CREATE**: ✅ `POST /api/student/add` - Kreiranje novog studenta
- **READ**: ✅ `GET /api/student/all` - Lista svih studenata
- **READ**: ✅ `GET /api/student/svi` - Lista sa paginacijom
- **READ**: ✅ `GET /api/student/podaci/{id}` - Dohvatanje po ID-u
- **UPDATE**: ✅ `PUT /api/student/update/{id}` - Ažuriranje studenta
- **DELETE**: ✅ `DELETE /api/student/{id}` - Brisanje studenta

### 2. **StudentIndeks** ✅ 
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
- **DELETE**: ✅ `DELETE /api/student/indeks/{id}` - Brisanje indeksa po ID-u

### 3. **Nastavnik** ✅ 
- **CREATE**: ✅ `POST /api/nastavnik/add` - Kreiranje novog nastavnika
- **READ**: ✅ `GET /api/nastavnik/all` - Lista svih nastavnika
- **READ**: ✅ `GET /api/nastavnik/{id}` - Dohvatanje po ID-u
- **READ**: ✅ `GET /api/nastavnik/search` - Pretraga po imenu i prezimenu
- **UPDATE**: ✅ `PUT /api/nastavnik/update/{id}` - Ažuriranje nastavnika
- **DELETE**: ✅ `DELETE /api/nastavnik/delete/{id}` - Brisanje nastavnika

### 4. **Predmet** ✅ 
- **CREATE**: ✅ `POST /api/predmet/add` - Kreiranje novog predmeta
- **READ**: ✅ `GET /api/predmet/svi` - Lista svih predmeta
- **READ**: ✅ `GET /api/predmet/by-program/{programId}` - Predmeti po studijskom programu
- **READ**: ✅ `GET /api/predmet/{id}` - Dohvatanje po ID-u
- **UPDATE**: ✅ `PUT /api/predmet/{id}` - Ažuriranje predmeta
- **DELETE**: ✅ `DELETE /api/predmet/{id}` - Brisanje predmeta

### 5. **StudijskiProgram** ✅
- **CREATE**: ✅ `POST /api/studijskiprogram/add` - Kreiranje studijskog programa
- **READ**: ✅ `GET /api/studijskiprogram/all/sorted` - Lista sortirana po godini
- **READ**: ✅ `GET /api/studijskiprogram/oznaka/all` - Lista oznaka programa
- **UPDATE**: ✅ `POST /api/studijskiprogram/update` - Ažuriranje programa
- **DELETE**: ✅ `DELETE /api/studijskiprogram/{id}` - Brisanje programa

### 6. **DrziPredmet** ✅ 
- **CREATE**: ✅ `POST /api/drzipredmet/add` - Kreiranje veze između nastavnika i predmeta
- **READ**: ✅ `GET /api/drzipredmet/{id}` - Dohvatanje po ID-u
- **READ**: ✅ `GET /api/drzipredmet/all` - Lista svih veza
- **UPDATE**: ✅ `PUT /api/drzipredmet/{id}` - Ažuriranje veze
- **DELETE**: ✅ `DELETE /api/drzipredmet/{id}` - Brisanje veze

### 7. **SlusaPredmet** ✅ 
- **CREATE**: ✅ `POST /add` - Kreiranje novog SlusaPredmet
- **READ**: ✅ `GET /slusapredmet/{id}` - Dohvatanje po ID-u
- **READ**: ✅ `GET /slusapredmet/all` - Lista svih SlusaPredmet veza
- **UPDATE**: ✅ `PUT /slusapredmet/{id}` - Ažuriranje
- **DELETE**: ✅ `DELETE /slusapredmet/{id}` - Brisanje

### 8. **SrednjaSkola** ✅ (Potpuno)
- **CREATE**: ✅ `POST /api/srednja-skola/add` - Kreiranje nove srednje škole
- **READ**: ✅ `GET /api/srednja-skola/{id}` - Dohvatanje po ID-u
- **READ**: ✅ `GET /api/srednja-skola/all` - Lista svih srednjih škola
- **UPDATE**: ✅ `PUT /api/srednja-skola/{id}` - Ažuriranje srednje škole
- **DELETE**: ✅ `DELETE /api/srednja-skola/{id}` - Brisanje srednje škole

### 9. **StudentSrednjaSkola** ✅ (Potpuno)
- **CREATE**: ✅ `POST /api/student-srednja-skola/add` - Dodela srednje škole studentu
- **READ**: ✅ `GET /api/student-srednja-skola/{id}` - Dohvatanje po ID-u
- **READ**: ✅ `GET /api/student-srednja-skola/student/{studentPodaciId}` - Sve srednje škole za studenta
- **UPDATE**: ✅ `PUT /api/student-srednja-skola/{id}` - Ažuriranje veze
- **DELETE**: ✅ `DELETE /api/student-srednja-skola/{id}` - Brisanje veze

### 10. **VisokoskolskaUstanova** ✅ (Potpuno)
- **CREATE**: ✅ `POST /api/visokoskolska-ustanova/add` - Kreiranje nove visokoškolske ustanove
- **READ**: ✅ `GET /api/visokoskolska-ustanova/{id}` - Dohvatanje po ID-u
- **READ**: ✅ `GET /api/visokoskolska-ustanova/all` - Lista svih visokoškolskih ustanova
- **UPDATE**: ✅ `PUT /api/visokoskolska-ustanova/{id}` - Ažuriranje visokoškolske ustanove
- **DELETE**: ✅ `DELETE /api/visokoskolska-ustanova/{id}` - Brisanje visokoškolske ustanove

### 11. **StudentVisokaUstanova** ✅ (Potpuno)
- **CREATE**: ✅ `POST /api/student-visoka-ustanova/add` - Dodela visokoškolske ustanove studentu (prenosi)
- **READ**: ✅ `GET /api/student-visoka-ustanova/{id}` - Dohvatanje po ID-u
- **READ**: ✅ `GET /api/student-visoka-ustanova/student/{studentPodaciId}` - Sve visokoškolske ustanove za studenta
- **UPDATE**: ✅ `PUT /api/student-visoka-ustanova/{id}` - Ažuriranje veze
- **DELETE**: ✅ `DELETE /api/student-visoka-ustanova/{id}` - Brisanje veze

---

## 📊 Rezime

| Entitet | Status | Napomena |
|---------|--------|---------|
| StudentPodaci | ✅ Potpuno | Sve CRUD operacije implementirane |
| StudentIndeks | ✅ Potpuno | Sve CRUD operacije implementirane |
| Nastavnik | ✅ Potpuno | Sve CRUD operacije implementirane |
| Predmet | ✅ Potpuno | Sve CRUD operacije implementirane |
| StudijskiProgram | ✅ Potpuno | Sve CRUD operacije implementirane |
| DrziPredmet | ✅ Potpuno | Sve CRUD operacije implementirane |
| SlusaPredmet | ✅ Potpuno | Sve CRUD operacije implementirane |
| SrednjaSkola | ✅ Potpuno | Sve CRUD operacije implementirane |
| StudentSrednjaSkola | ✅ Potpuno | Sve CRUD operacije implementirane |
| VisokoskolskaUstanova | ✅ Potpuno | Sve CRUD operacije implementirane |
| StudentVisokaUstanova | ✅ Potpuno | Sve CRUD operacije implementirane |

**Zaključak**: Sve osnovne CRUD operacije su sada potpuno implementirane za sve entitete.

