# Status Implementacije - Operacije za Studenta

## ❌ NIJE IMPLEMENTIRANO

### 1. Prosečna ocena studenata na predmetu za raspon godina
- **Endpoint**: `GET /api/predmet/{predmetId}/prosecna-ocena?od={godina}&do={godina}`
- **Status**: ❌ Nije implementirano
- **Specifikacija**: Pronalaženje prosečne ocene svih studenata koji su položili predmet u datom periodu

### 2. Prosečna ocena na ispitu
- **Endpoint**: `GET /api/ispit/{ispitId}/prosecna-ocena`
- **Status**: ❌ Nije implementirano
- **Specifikacija**: Pronalaženje prosečne ocene na određenom ispitu

### 3. Rezultati ispita (sortirani spisak)
- **Endpoint**: `GET /api/ispit/{ispitId}/rezultati`
- **Status**: ❌ Nije implementirano
- **Sortiranje**: 1. studijski program, 2. godina upisa, 3. broj indeksa
- **Specifikacija**: Vraćanje sortiranog spiska studenata sa ukupnim brojem poena (predispitne + ispit)

### 4. Selekcija ostvarenih poena na predispitnim obavezama
- **Endpoint**: `GET /api/predispitne-obaveze/student/{studentIndeksId}/predmet/{predmetId}`
- **Status**: ❌ Nije implementirano
- **Specifikacija**: Pronalaženje ostvarenih poena na predispitnim obavezama za studenta na određenom predmetu u školskoj godini

---

## 📝 NAPOMENE

- Sve implementirane operacije koriste paginirano vraćanje podataka gde je to potrebno
- Implementirane su Response DTO klase za čiste API odgovore
- Koristi se layered architecture: Controller → Service → Repository
- Sve operacije su transakcione gde je potrebno
- Specifikacija zahteva testove za svaku operaciju (Postman ili sličan alat)
