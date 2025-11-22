# Analiza nedostajućih CRUD operacija po entitetima

## ❌ Entiteti sa **NEPOTPUNIM** CRUD operacijama

### 1. **StudentVisokaUstanova** - ⚠️ NEMA KONTROLERA! NEDOSTAJU SVE OPERACIJE
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

### 2. **StudentSrednjaSkola** - ⚠️ NEMA KONTROLERA! NEDOSTAJU SVE OPERACIJE
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

## 📝 Napomene

- ✅ **Svi kritični entiteti** sada imaju kompletne CRUD operacije
- Opciono: StudentVisokaUstanova i StudentSrednjaSkola za dodatnu funkcionalnost
