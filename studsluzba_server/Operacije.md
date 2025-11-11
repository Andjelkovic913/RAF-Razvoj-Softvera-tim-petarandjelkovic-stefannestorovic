Pored osnovnih CRUD operacija (snimanje, preuzimanje po ključu, brisanje), potrebno je podržati sledeće funkcionalnosti. Stavke su označene statusom implementacije.

### Student
- [x] selekcija studenata po imenu i/ili prezimenu, paginirano
  - Endpoint: `GET /api/student/search?ime=&prezime=&studProgram=&godina=&broj=&page=&size=`
- [x] selekcija studenta preko broja indeksa
  - Implementirano kroz brzu pretragu: `GET /api/student/fastsearch?indeksShort=RA-23-001` i pretragu po emailu: `GET /api/student/emailsearch?studEmail=...`
  - Napomena: Postoji i servisna metoda za precizno traženje po `studProgramOznaka/godina/broj`.
- [ ] selekcija svih položenih ispita za broj indeksa studenta, paginirano
- [ ] selekcija svih nepoloženih ispita za broj indeksa studenta, paginirano
- [ ] pregled svih upisanih godina za broj indeksa
- [ ] upis studenta na godinu (dodati upis i predmete koje sluša; inicijalno nepoloženi)
- [ ] pregled obnovljenih godina za broj indeksa
- [ ] obnova godine (uz izbor nepoloženih i predmeta iz naredne godine, maksimalno 60 ESPB)
- [ ] dodavanje nove uplate (datum, iznos RSD, srednji kurs EUR)
  - Napomena: Model i repozitorijum postoje (`Uplata`), ali nema endpointa niti integracije sa kurs API‐jem.
  - Kurs API: `https://kurs.resenje.org/api/v1/currencies/eur/rates/today`
- [ ] selekcija preostalog iznosa za uplatu (u evrima i dinarima; školarina fiksno 3000€)
- [ ] selekcija svih upisanih studenata koji su završili određenu srednju školu

### Studijski program i predmet
- [x] spisak predmeta na studijskom programu
  - Endpointi: `GET /api/predmet/by-program/{programId}`, `GET /api/predmet/all/{programId}`
- [x] spisak svih predmeta sa svim podacima
  - Endpoint: `GET /api/predmet/svi`
- [x] dodavanje novog predmeta na studijski program, šifra jedinstvena
  - Endpoint: `POST /api/predmet/add` (validacija jedinstvene šifre u servisu)
- [ ] prosečna ocena studenata na predmetu za raspon godina (npr. 2015–2018)

### Ispit
- [ ] svi prijavljeni studenti za ispit
- [ ] prosečna ocena na ispitu
- [ ] prijava ispita za izabrani ispitni rok i predmet koji student sluša
- [ ] dodavanje izlaska na ispit sa osvojenim poenima i upis u položenima ako je ukupno ≥ 51
- [ ] rezultati ispita: sortirani spisak (stud. program → godina upisa → broj) sa ukupnim poenima
- [ ] selekcija ostvarenih poena na predispitnim obavezama za studenta na predmetu u šk. godini
- [ ] za datog studenta i predmet vratiti broj polaganja (broj izlazaka na ispit)

Napomene:
- Mnogi modeli i repozitorijumi postoje (`UpisGodine`, `ObnovaGodine`, `Uplata`, `PrijavaIspita`, `IzlazakNaIspit`, `PredispitneObaveze`), ali za gore neoznačene stavke trenutno ne postoje javni endpointi u kontrolerima.
- Postoje profil endpointi za studenta (`GET /api/student/profile/{studentIndeksId}`, `GET /api/student/webprofile/{studentIndeksId}`) koji vraćaju pregled slušanih predmeta u aktivnoj školskoj godini, ali ne pokrivaju liste položenih/nepoloženih ispita niti uplate.