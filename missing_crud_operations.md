# Analiza nedostajućih CRUD operacija po entitetima

## 📋 Pregled

Ovaj dokument prikazuje koje osnovne CRUD operacije (Create, Read, Update, Delete) nedostaju za svaki entitet.

---

## ✅ Entiteti sa kompletnim CRUD operacijama

### 1. **StudentPodaci** (StudentController)
- ✅ CREATE: `POST /api/student/add`
- ✅ READ: `GET /api/student/all`, `GET /api/student/svi` (paginated), `GET /api/student/podaci/{id}`
- ✅ UPDATE: `PUT /api/student/update/{id}`
- ✅ DELETE: `DELETE /api/student/{id}`

### 2. **StudentIndeks** (StudentController)
- ✅ CREATE: `POST /api/student/saveindeks`
- ✅ READ: Implicitno kroz StudentPodaci
- ✅ UPDATE: Nije eksplicitno, ali je moguće kroz StudentPodaci
- ✅ DELETE: Nije eksplicitno

### 3. **Nastavnik** (NastavnikController)
- ✅ CREATE: `POST /api/nastavnik/add`
- ✅ READ: `GET /api/nastavnik/all`, `GET /api/nastavnik/{id}`, `GET /api/nastavnik/search`
- ✅ UPDATE: `PUT /api/nastavnik/nastavnik/update/{id}`
- ✅ DELETE: `DELETE /api/nastavnik/nastavnik/delete/{id}`

### 4. **SrednjaSkolaController**
- ✅ CREATE: `POST /api/srednja-skola/add`
- ✅ READ: `GET /api/srednja-skola/{id}`, `GET /api/srednja-skola/all`
- ✅ UPDATE: `PUT /api/srednja-skola/{id}`
- ✅ DELETE: `DELETE /api/srednja-skola/{id}`

### 5. **StudijskiProgram** (StudijskiProgramController)
- ✅ CREATE: `POST /api/studijskiprogram/add`
- ✅ READ: `GET /api/studijskiprogram/all/sorted`, `GET /api/studijskiprogram/oznaka/all`
- ✅ UPDATE: `POST /api/studijskiprogram/update/{id}`
- ✅ DELETE: `DELETE /api/studijskiprogram/{id}`

### 6. **UpisGodine** (UpisGodineController)
- ✅ CREATE: `POST /api/upis-godine/create`
- ✅ READ: `GET /api/upis-godine/all`, `GET /api/upis-godine/{id}`
- ✅ UPDATE: Nije eksplicitno
- ✅ DELETE: Nije eksplicitno

### 7. **ObnovaGodine** (ObnovaGodineController)
- ✅ CREATE: `POST /api/obnova-godine/create`
- ✅ READ: `GET /api/obnova-godine/all`, `GET /api/obnova-godine/{id}`
- ✅ UPDATE: Nije eksplicitno
- ✅ DELETE: Nije eksplicitno

### 8. **SlusaPredmet** (SlusaPredmetController)
- ✅ CREATE: `POST /api/slusa-predmet/create`
- ✅ READ: `GET /api/slusa-predmet/{id}`, `GET /api/slusa-predmet/all`
- ✅ UPDATE: `PUT /api/slusa-predmet/update/{id}`
- ✅ DELETE: `DELETE /api/slusa-predmet/{id}`

### 9. **PrijavaIspita** (PrijavaIspitaController)
- ✅ CREATE: `POST /api/prijava-ispita/create`
- ✅ READ: `GET /api/prijava-ispita/{id}`, `GET /api/prijava-ispita/all`
- ✅ UPDATE: Nije eksplicitno
- ✅ DELETE: Nije eksplicitno

### 10. **IzlazakNaIspit** (IzlazakNaIspitController)
- ✅ CREATE: `POST /api/izlazak-na-ispit/create`
- ✅ READ: `GET /api/izlazak-na-ispit/{id}`
- ✅ UPDATE: `PUT /api/izlazak-na-ispit/update/{id}`
- ✅ DELETE: Nije eksplicitno

### 11. **Uplata** (UplataController)
- ✅ CREATE: `POST /api/uplata/create`
- ✅ READ: `GET /api/uplata/all`, `GET /api/uplata/{id}`, `GET /api/uplata/student/{studentIndeksId}`
- ✅ UPDATE: Nije eksplicitno
- ✅ DELETE: Nije eksplicitno

---

## ✅ Entiteti sa **KOMPLETNIM** CRUD operacijama (Novo implementirano)

### 1. **Predmet** (PredmetController) - ✅ KOMPLETNO
```
- ✅ CREATE: POST /api/predmet/add
- ✅ READ: GET /api/predmet/{id}, GET /api/predmet/all, GET /api/predmet/by-program/{programId}
- ✅ UPDATE: PUT /api/predmet/{id}
- ✅ DELETE: DELETE /api/predmet/{id}
```

### 2. **DrziPredmet** (DrziPredmetController) - ✅ KOMPLETNO
```
- ✅ CREATE: POST /api/drzipredmet/add
- ✅ READ: GET /api/drzipredmet/{id}, GET /api/drzipredmet/all
- ✅ UPDATE: PUT /api/drzipredmet/{id}
- ✅ DELETE: DELETE /api/drzipredmet/{id}
```

### 3. **Ispit** (IspitController) - ✅ KOMPLETNO
```
- ✅ CREATE: POST /api/ispit/add
- ✅ READ: GET /api/ispit/{id}, GET /api/ispit/all
- ✅ UPDATE: PUT /api/ispit/{id}
- ✅ DELETE: DELETE /api/ispit/{id}
```

### 4. **IspitniRok** (IspitniRokController) - ✅ KOMPLETNO
```
- ✅ CREATE: POST /api/ispitni-rok/add
- ✅ READ: GET /api/ispitni-rok/{id}, GET /api/ispitni-rok/all
- ✅ UPDATE: PUT /api/ispitni-rok/{id}
- ✅ DELETE: DELETE /api/ispitni-rok/{id}
```

### 5. **SkolskaGodina** (SkolskaGodinaController) - ✅ KOMPLETNO
```
- ✅ CREATE: POST /api/skolska-godina/add
- ✅ READ: GET /api/skolska-godina/{id}, GET /api/skolska-godina/all, GET /api/skolska-godina/aktivna
- ✅ UPDATE: PUT /api/skolska-godina/{id}
- ✅ DELETE: DELETE /api/skolska-godina/{id}
```

### 6. **PredispitneObaveze** (PredispitneObavezeController) - ✅ KOMPLETNO
```
- ✅ CREATE: POST /api/predispitne-obaveze/add
- ✅ READ: GET /api/predispitne-obaveze/{id}, GET /api/predispitne-obaveze/all
- ✅ UPDATE: PUT /api/predispitne-obaveze/{id}
- ✅ DELETE: DELETE /api/predispitne-obaveze/{id}
```

---

## ❌ Entiteti sa **NEPOTPUNIM** CRUD operacijama

---

### 7. **StudentVisokaUstanova** - ⚠️ NEMA KONTROLERA! NEDOSTAJU SVE OPERACIJE
```
- ❌ CREATE: NEDOSTAJE
- ❌ READ: NEDOSTAJE
- ❌ UPDATE: NEDOSTAJE
- ❌ DELETE: NEDOSTAJE
```

**Potrebni endpointi:**
- `POST /api/student-visoka-ustanova/add` - Dodavanje studenta na drugu instituciju
- `GET /api/student-visoka-ustanova/{id}` - Preuzimanje po ID-u
- `GET /api/student-visoka-ustanova/student/{studentPodaciId}` - Sve institucije za studenta
- `PUT /api/student-visoka-ustanova/{id}` - Update
- `DELETE /api/student-visoka-ustanova/{id}` - Brisanje

---

### 8. **StudentSrednjaSkola** - ⚠️ NEMA KONTROLERA! NEDOSTAJU SVE OPERACIJE
```
- ❌ CREATE: NEDOSTAJE
- ❌ READ: NEDOSTAJE
- ❌ UPDATE: NEDOSTAJE
- ❌ DELETE: NEDOSTAJE
```

**Potrebni endpointi:**
- `POST /api/student-srednja-skola/add` - Dodavanje srednje škole za studenta
- `GET /api/student-srednja-skola/{id}` - Preuzimanje po ID-u
- `GET /api/student-srednja-skola/student/{studentPodaciId}` - Sve srednje škole za studenta
- `PUT /api/student-srednja-skola/{id}` - Update
- `DELETE /api/student-srednja-skola/{id}` - Brisanje

---

## 📊 Rezime po prioritetu

### ✅ IMPLEMENTIRANO (u ovoj sesiji)
1. **Predmet** - ✅ Dodati UPDATE i DELETE endpointi
2. **DrziPredmet** - ✅ Dodati CREATE, UPDATE, DELETE, GET ALL endpointi
3. **Ispit** - ✅ Dodati READ, UPDATE, DELETE endpointi + kreirana `IspitResponse` klasa
4. **IspitniRok** - ✅ Kompletan kontroler i servis (CREATE, READ, UPDATE, DELETE)
5. **SkolskaGodina** - ✅ Kompletan kontroler i servis (CREATE, READ, UPDATE, DELETE + GET aktivna)
6. **PredispitneObaveze** - ✅ Kompletan kontroler i servis (CREATE, READ, UPDATE, DELETE)

### 🟡 PREPORUKA (za bogatiju funkcionalnost)
1. **StudentVisokaUstanova** - Za praćenje priznatih predmeta
2. **StudentSrednjaSkola** - Za kompletan profil studenta

---

## 🎯 Preporuke za implementaciju

1. ~~**Prvo**: Dopuni nepotpune kontrolere (Predmet, DrziPredmet, Ispit)~~ ✅ **ZAVRŠENO**
2. ~~**Drugo**: Implementiraj kontrolere za entitete bez njih (IspitniRok, SkolskaGodina, PredispitneObaveze)~~ ✅ **ZAVRŠENO**
3. **Opciono**: Dodaj kontrolere za StudentVisokaUstanova i StudentSrednjaSkola ako je potrebno

---

## 📝 Napomene

- ✅ **Svi kritični entiteti** sada imaju kompletne CRUD operacije
- ✅ **IspitniRok, SkolskaGodina, PredispitneObaveze** - Kreirani novi kontroleri i servisi
- Neki endpointi su implicitno dostupni kroz druge kontrolere (npr. StudentIndeks kroz StudentPodaci)
- Opciono: StudentVisokaUstanova i StudentSrednjaSkola za dodatnu funkcionalnost

---

## 🆕 Implementirane izmene (detalji)

### **PredmetService**
- ✅ `getById(Long id)` - Preuzimanje predmeta po ID-u
- ✅ `update(Long id, PredmetRequest req)` - Update predmeta sa validacijom šifre
- ✅ `delete(Long id)` - Brisanje predmeta

### **PredmetController**
- ✅ `GET /api/predmet/{id}` - Preuzimanje predmeta
- ✅ `PUT /api/predmet/{id}` - Update predmeta
- ✅ `DELETE /api/predmet/{id}` - Brisanje predmeta

### **DrziPredmetService**
- ✅ `create(DrziPredmetAddRequest)` - Kreiranje nove veze nastavnik-predmet
- ✅ `getAll()` - Lista svih veza
- ✅ `update(Long id, DrziPredmetAddRequest)` - Update veze
- ✅ `delete(Long id)` - Brisanje veze

### **DrziPredmetController**
- ✅ `POST /api/drzipredmet/add` - Kreiranje veze
- ✅ `GET /api/drzipredmet/all` - Lista svih veza
- ✅ `PUT /api/drzipredmet/{id}` - Update veze
- ✅ `DELETE /api/drzipredmet/{id}` - Brisanje veze

### **IspitService**
- ✅ `getById(Long id)` - Preuzimanje ispita po ID-u
- ✅ `getAll()` - Lista svih ispita
- ✅ `update(Long id, IspitRequest)` - Update ispita
- ✅ `delete(Long id)` - Brisanje ispita
- ✅ `toIspitResponse(Ispit)` - Helper metoda za mapiranje u response

### **IspitController**
- ✅ `GET /api/ispit/{id}` - Preuzimanje ispita
- ✅ `GET /api/ispit/all` - Lista svih ispita
- ✅ `PUT /api/ispit/{id}` - Update ispita
- ✅ `DELETE /api/ispit/{id}` - Brisanje ispita

### **Nove klase**
- ✅ `IspitResponse` - Response DTO za Ispit entitet

---

### **IspitniRokService** (NOVO)
- ✅ `create(IspitniRokRequest)` - Kreiranje novog ispitnog roka
- ✅ `getById(Long id)` - Preuzimanje ispitnog roka po ID-u
- ✅ `getAll()` - Lista svih ispitnih rokova
- ✅ `update(Long id, IspitniRokRequest)` - Update ispitnog roka
- ✅ `delete(Long id)` - Brisanje ispitnog roka
- ✅ `toResponse(IspitniRok)` - Helper metoda za mapiranje u response

### **IspitniRokController** (NOVO)
- ✅ `POST /api/ispitni-rok/add` - Kreiranje ispitnog roka
- ✅ `GET /api/ispitni-rok/{id}` - Preuzimanje ispitnog roka
- ✅ `GET /api/ispitni-rok/all` - Lista svih ispitnih rokova
- ✅ `PUT /api/ispitni-rok/{id}` - Update ispitnog roka
- ✅ `DELETE /api/ispitni-rok/{id}` - Brisanje ispitnog roka

### **SkolskaGodinaService** (NOVO)
- ✅ `create(SkolskaGodinaRequest)` - Kreiranje nove školske godine
- ✅ `getById(Long id)` - Preuzimanje školske godine po ID-u
- ✅ `getAll()` - Lista svih školskih godina
- ✅ `getAktivna()` - Preuzimanje aktivne školske godine
- ✅ `update(Long id, SkolskaGodinaRequest)` - Update školske godine
- ✅ `delete(Long id)` - Brisanje školske godine
- ✅ `toResponse(SkolskaGodina)` - Helper metoda za mapiranje u response

### **SkolskaGodinaController** (NOVO)
- ✅ `POST /api/skolska-godina/add` - Kreiranje školske godine
- ✅ `GET /api/skolska-godina/{id}` - Preuzimanje školske godine
- ✅ `GET /api/skolska-godina/all` - Lista svih školskih godina
- ✅ `GET /api/skolska-godina/aktivna` - Preuzimanje aktivne školske godine
- ✅ `PUT /api/skolska-godina/{id}` - Update školske godine
- ✅ `DELETE /api/skolska-godina/{id}` - Brisanje školske godine

### **PredispitneObavezeService** (NOVO)
- ✅ `create(PredispitneObavezeRequest)` - Kreiranje predispitne obaveze
- ✅ `getById(Long id)` - Preuzimanje obaveze po ID-u
- ✅ `getAll()` - Lista svih obaveza
- ✅ `update(Long id, PredispitneObavezeRequest)` - Update obaveze
- ✅ `delete(Long id)` - Brisanje obaveze
- ✅ `toResponse(PredispitneObaveze)` - Helper metoda za mapiranje u response

### **PredispitneObavezeController** (NOVO)
- ✅ `POST /api/predispitne-obaveze/add` - Kreiranje obaveze
- ✅ `GET /api/predispitne-obaveze/{id}` - Preuzimanje obaveze
- ✅ `GET /api/predispitne-obaveze/all` - Lista svih obaveza
- ✅ `PUT /api/predispitne-obaveze/{id}` - Update obaveze
- ✅ `DELETE /api/predispitne-obaveze/{id}` - Brisanje obaveze

### **Nove Request/Response klase**
- ✅ `IspitniRokRequest` - Request DTO za IspitniRok
- ✅ `IspitniRokResponse` - Response DTO za IspitniRok
- ✅ `SkolskaGodinaRequest` - Request DTO za SkolskaGodina
- ✅ `SkolskaGodinaResponse` - Response DTO za SkolskaGodina
- ✅ `PredispitneObavezeRequest` - Request DTO za PredispitneObaveze
- ✅ `PredispitneObavezeResponse` - Response DTO za PredispitneObaveze
