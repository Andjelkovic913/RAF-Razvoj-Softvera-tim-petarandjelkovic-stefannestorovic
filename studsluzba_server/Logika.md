# Logicki deo aplikacije

## Student
### StudentPodaci
- Kada se kreira novi student: 
    [x] Validacija duplikata (JMBG, broj lične karte, fakultetski email) - IMPLEMENTIRANO
    [] Dodaju mu se i indeks i broj indeksa, koje predmete slusa, na kom je smeru, kada je upisao skolsku godinu, u koje je srednje je isao i koju je zapravo zavrsio, na kojim fakultetima je bio prethodno i koje predmete je preneo?
- Ažuriranje studenta
    [x] Validacija duplikata prilikom update-a - IMPLEMENTIRANO
- Brisanje studenta
    [x] Brisu se i svi redovi asocirani sa njim u svim tabelama - IMPLEMENTIRANO (nije kaskadno brisanje):
        - StudentIndeks (svi indeksi studenta)
        - Za svaki indeks: SlusaPredmet, PredispitneObaveze, IzlazakNaIspit, PrijavaIspita, UpisGodine, Uplata
        - StudentSrednjaSkola (sve srednje škole studenta)
        - StudentVisokaUstanova (sve visokoškolske ustanove studenta)
- Dohvatanje studenta po ID-u

### StudentIndeks
- Kreiranje novog indeksa:
    [x] Automatsko generisanje broja indeksa (findNextBroj) - IMPLEMENTIRANO
    [x] Validacija da student nema indeks u istoj godini ILI na istom studijskom programu - IMPLEMENTIRANO
    [x] Novi indeks je po default-u neaktivan (aktivan=false) - IMPLEMENTIRANO
- Aktiviranje indeksa:
    [x] PUT /api/student/aktiviraj/{studentIndeksId} - aktivira indeks i deaktivira sve ostale indekse istog studenta - IMPLEMENTIRANO
- Dohvatanje indeksa po ID-u
- Dohvatanje indeksa po studentu
- Brisanje indeksa po ID-u
    [x] Kaskadno brisanje indeksa - IMPLEMENTIRANO:
        - SlusaPredmet, PredispitneObaveze, IzlazakNaIspit, PrijavaIspita, UpisGodine, Uplata

### Nastavnik
- Kada se kreira novi nastavnik:
    [] Dodati mu da Drzi neki Predmet?
- Brisanje nastavnika:
    [x] Kaskadno brisanje - IMPLEMENTIRANO:
        - DrziPredmet (i sve zavisnosti: SlusaPredmet, Ispit, PredispitneObaveze, IzlazakNaIspit, PrijavaIspita)
        - NastavnikZvanje
        - NastavnikObrazovanje

### Predmet
- Brisanje predmeta:
    [x] Kaskadno brisanje - IMPLEMENTIRANO:
        - DrziPredmet (i sve zavisnosti: SlusaPredmet, Ispit, PredispitneObaveze, IzlazakNaIspit, PrijavaIspita)
        - Ispit (direktno vezani za predmet)

### StudijskiProgram
- Brisanje studijskog programa:
    [x] Kaskadno brisanje - IMPLEMENTIRANO:
        - StudentIndeks (i sve zavisnosti)
        - Predmet (i sve zavisnosti) 

---

## ⚠️ POTENCIJALNI PROBLEMI ZA UI (JavaFXML)

### 1. CASCADE DELETE - Nedostaje za većinu entiteta

**Kritično - Brisanje će pasti sa FK constraint error:**

| Entitet | Zavisni entiteti | Status |
|---------|------------------|--------|
| **StudentPodaci** | StudentIndeks, StudentSrednjaSkola, StudentVisokaUstanova | ✅ IMPLEMENTIRANO |
| **StudentIndeks** | SlusaPredmet, PrijavaIspita, UpisGodine, Uplata, PredispitneObaveze, IzlazakNaIspit | ✅ IMPLEMENTIRANO |
| **Nastavnik** | DrziPredmet, NastavnikObrazovanje, NastavnikZvanje | ✅ IMPLEMENTIRANO |
| **Predmet** | DrziPredmet, SlusaPredmet, Ispit, PredispitneObaveze | ✅ IMPLEMENTIRANO |
| **StudijskiProgram** | Predmet, StudentIndeks | ✅ IMPLEMENTIRANO |
| **SkolskaGodina** | StudijskiProgram, UpisGodine, IspitniRok | ❌ NEDOSTAJE |
| **IspitniRok** | Ispit, PrijavaIspita | ❌ NEDOSTAJE |
| **Ispit** | IzlazakNaIspit | ❌ NEDOSTAJE |
| **SlusaPredmet** | PredispitneObaveze, IzlazakNaIspit | ❌ NEDOSTAJE |
| **SrednjaSkola** | StudentSrednjaSkola | ❌ NEDOSTAJE |
| **VisokoskolskaUstanova** | StudentVisokaUstanova | ❌ NEDOSTAJE |

### 2. VALIDACIJA DUPLIKATA - Nedostaje za većinu entiteta

| Entitet | Jedinstvena polja | Status |
|---------|-------------------|--------|
| **StudentPodaci** | JMBG, brojLicneKarte, emailFakultetski | ✅ IMPLEMENTIRANO |
| **StudentIndeks** | broj+godina+studProgramOznaka | ✅ IMPLEMENTIRANO (unique constraint) |
| **Nastavnik** | email? JMBG? | ❌ NEDOSTAJE |
| **Predmet** | sifra | ✅ IMPLEMENTIRANO |
| **StudijskiProgram** | oznaka+godina? | ❌ NEDOSTAJE |
| **SrednjaSkola** | naziv? | ❌ NEDOSTAJE |
| **VisokoskolskaUstanova** | naziv? | ❌ NEDOSTAJE |

### 3. BUSINESS LOGIC VALIDACIJE - Nedostaje

- [] **Upis godine**: Student ne može upisati godinu ako nema dovoljno ESPB
- [] **Prijava ispita**: Student ne može prijaviti ispit ako nije slušao predmet
- [] **Prijava ispita**: Student ne može prijaviti ispit ako ga je već položio
- [] **Ocena**: Ocena mora biti između 5-10
- [] **ESPB**: Automatski update ostvarenoEspb kada student položi ispit
- [x] **Aktivan indeks**: Samo jedan indeks može biti aktivan po studentu - IMPLEMENTIRANO (PUT /aktiviraj/{id})
- [] **Obnova godine**: Validacija uslova za obnovu

### 4. UI SPECIFIČNI PROBLEMI

- [] **Dropdown/ComboBox popunjavanje**: Potrebni endpointi za liste (nastavnici, predmeti, programi...)
- [] **Paginacija**: Velike tabele moraju biti paginirane
- [] **Pretraga**: Potrebna pretraga za sve glavne entitete
- [] **Validacija forme**: Frontend validacija pre slanja na backend
- [] **Error handling**: Prikaz grešaka korisniku (npr. "Student sa ovim JMBG-om već postoji")
- [] **Confirmation dialogs**: Potvrda pre brisanja
- [] **Loading states**: Indikatori učitavanja

### 5. PREPORUČENE AKCIJE

1. ~~**HITNO**: Implementirati cascade delete za StudentIndeks, Nastavnik, Predmet~~ ✅ ZAVRSENO
2. **HITNO**: Dodati validaciju duplikata za Nastavnik (email/JMBG)
3. **SREDNJE**: Implementirati business logic validacije za upis/prijavu ispita
4. **SREDNJE**: Dodati soft delete umesto hard delete (polje `deleted` ili `aktivan`)
5. **NISKO**: Dodati audit log (ko je kada šta menjao)
