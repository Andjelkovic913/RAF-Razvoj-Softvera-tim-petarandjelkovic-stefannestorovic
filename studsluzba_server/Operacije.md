Pored osnovnih CRUD operacija u koje spadaju snimanje entiteta, preuzimanje na osnovu
ključa, brisanje, potrebno je podržati i sledeće funkcionalnosti (grupisano po entitetima).
Student
-
-
-
-
-
-
-
-
selekcija studenta (njegovih ličnih podataka) preko broja indeksa
selekcija svih položenih ispita za broj indeksa studenta, paginirano
selekcija svih nepoloženih ispita za broj indeksa studenta, paginirano
pregled svih upisanih godina za broj indeksa
upis studenta na godinu (dodati novi upis za studenta i predmete koje sluša, a koji će
inicijalno biti nepoloženi)
pregled obnovljenih godina za broj indeksa
obnova godine za studenta gde je potrebno omogućiti da se, pored nepoloženih,
izaberu predmeti iz naredne godine. Maksimalni zbir ESPB poena može biti 60. Dodaje
se nova obnova godine za studenta i predmeti koje sluša, a koji će inicijalno biti
nepoloženi.
dodavanje nove uplate (čuva se datum uplate, iznos u dinarima i srednji kurs). Trenutni
srednji kurs ne treba da ima predefinisanu vrednost, nego ga treba dohvatiti api
pozivom: https://kurs.resenje.org/api/v1/currencies/eur/rates/today
-
-
-
selekcija preostalog iznosa za uplatu u evrima i dinarima. Iznos školarine je
predefinisana vrednost od 3000e
selekcija studenata na osnovu imena i/ili prezimena (može samo ime, ili samo prezime
ili oba da se unesu), paginirano
selekcija svih upisanih studenata koji su završili određenu srednju školu
Studijski program i predmet
-
-
-
spisak predmeta na studijskom programu
spisak svih predmeta sa svim podacima o predmetu
dodavanje novog predmetra na studijski program, zadaje se šifra predmeta koja mora
biti jedinstvena
prosečna ocena studenata na predmetu za raspon godina (na primer zadaje se godine
2015-2018 i vraća se prosečna ocena svih studenata koji su položili predmet u tom
periodu)
Ispit
-
-
-
-
-
-
-
-
svi prijavljeni studenti za ispit
prosečna ocena na ispitu
prijava ispita za izabrani ispitni rok i predmet koji student sluša
dodavanje izlaska na ispit sa osvojenim poenima i čuvanje predmeta u položenima ako
je zbir predispitnih obaveza i rezultata na ispitu minimalno 51 poen
rezultati ispita u vidu sortiranog spiska studenata (sortirati prvo po stud programu, zatim
po godini upisa, pa po broju) sa ukupnim brojem poena na ispitu (predispitne obaveze
plus poeni sa ispita)
selekcija ostvarenih poena na predispinim obavezama za studenta na određenom
predmetu u školskoj godini
za datog studenta i predmet vratiti koliko je puta student polagao predmet (izašao na
ispit)