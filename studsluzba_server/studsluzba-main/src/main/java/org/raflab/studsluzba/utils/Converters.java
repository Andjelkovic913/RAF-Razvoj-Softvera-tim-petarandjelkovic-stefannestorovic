package org.raflab.studsluzba.utils;

import org.raflab.studsluzba.controllers.request.*;
import org.raflab.studsluzba.dtos.NastavnikRequest;
import org.raflab.studsluzba.dtos.PredmetRequest;
import org.raflab.studsluzba.dtos.StudentIndeksRequest;
import org.raflab.studsluzba.dtos.StudentPodaciRequest;
import org.raflab.studsluzba.model.*;
import org.springframework.stereotype.Component;

@Component
public class Converters {

    public Nastavnik toNastavnik(NastavnikRequest nastavnikRequest) {
        Nastavnik nastavnik = new Nastavnik();
        nastavnik.setIme(nastavnikRequest.getIme());
        nastavnik.setPrezime(nastavnikRequest.getPrezime());
        nastavnik.setSrednjeIme(nastavnikRequest.getSrednjeIme());
        nastavnik.setEmail(nastavnikRequest.getEmail());
        nastavnik.setBrojTelefona(nastavnikRequest.getBrojTelefona());
        nastavnik.setAdresa(nastavnikRequest.getAdresa());
        nastavnik.setDatumRodjenja(nastavnikRequest.getDatumRodjenja());
        nastavnik.setPol(nastavnikRequest.getPol());
        nastavnik.setJmbg(nastavnikRequest.getJmbg());
        return nastavnik;
    }

    public StudentPodaci toStudentPodaci(StudentPodaciRequest request) {
        StudentPodaci studentPodaci = new StudentPodaci();
        studentPodaci.setIme(request.getIme());
        studentPodaci.setPrezime(request.getPrezime());
        studentPodaci.setSrednjeIme(request.getSrednjeIme());
        studentPodaci.setJmbg(request.getJmbg());
        studentPodaci.setDatumRodjenja(request.getDatumRodjenja());
        studentPodaci.setMestoRodjenja(request.getMestoRodjenja());
        studentPodaci.setMestoPrebivalista(request.getMestoPrebivalista());
        studentPodaci.setDrzavaRodjenja(request.getDrzavaRodjenja());
        studentPodaci.setDrzavljanstvo(request.getDrzavljanstvo());
        studentPodaci.setNacionalnost(request.getNacionalnost());
        studentPodaci.setPol(request.getPol());
        studentPodaci.setAdresa(request.getAdresa());
        studentPodaci.setBrojTelefonaMobilni(request.getBrojTelefonaMobilni());
        studentPodaci.setBrojTelefonaFiksni(request.getBrojTelefonaFiksni());
        studentPodaci.setEmailPrivatni(request.getEmailPrivatni());
        studentPodaci.setEmailFakultetski(request.getEmailFakultetski());
        studentPodaci.setBrojLicneKarte(request.getBrojLicneKarte());
        studentPodaci.setLicnuKartuIzdao(request.getLicnuKartuIzdao());
        studentPodaci.setMestoStanovanja(request.getMestoStanovanja());
        studentPodaci.setAdresaStanovanja(request.getAdresaStanovanja());
        return studentPodaci;
    }

    public void updateStudentPodaci(StudentPodaci postojeci, StudentPodaciRequest request){
        postojeci.setIme(request.getIme());
        postojeci.setPrezime(request.getPrezime());
        postojeci.setSrednjeIme(request.getSrednjeIme());
        postojeci.setJmbg(request.getJmbg());
        postojeci.setDatumRodjenja(request.getDatumRodjenja());
        postojeci.setMestoRodjenja(request.getMestoRodjenja());
        postojeci.setMestoPrebivalista(request.getMestoPrebivalista());
        postojeci.setDrzavaRodjenja(request.getDrzavaRodjenja());
        postojeci.setDrzavljanstvo(request.getDrzavljanstvo());
        postojeci.setNacionalnost(request.getNacionalnost());
        postojeci.setPol(request.getPol());
        postojeci.setBrojTelefonaMobilni(request.getBrojTelefonaMobilni());
        postojeci.setBrojTelefonaFiksni(request.getBrojTelefonaFiksni());
        postojeci.setEmailPrivatni(request.getEmailPrivatni());
        postojeci.setEmailFakultetski(request.getEmailFakultetski());
        postojeci.setBrojLicneKarte(request.getBrojLicneKarte());
        postojeci.setLicnuKartuIzdao(request.getLicnuKartuIzdao());
        postojeci.setMestoStanovanja(request.getMestoStanovanja());
        postojeci.setAdresaStanovanja(request.getAdresaStanovanja());
    }

    public void updateNastavnik(Nastavnik postojeci, NastavnikRequest request){
        if (request.getIme() != null) {
            postojeci.setIme(request.getIme());
        }
        if (request.getPrezime() != null) {
            postojeci.setPrezime(request.getPrezime());
        }
        if (request.getSrednjeIme() != null) {
            postojeci.setSrednjeIme(request.getSrednjeIme());
        }
        if (request.getEmail() != null) {
            postojeci.setEmail(request.getEmail());
        }
        if (request.getBrojTelefona() != null) {
            postojeci.setBrojTelefona(request.getBrojTelefona());
        }
        if (request.getAdresa() != null) {
            postojeci.setAdresa(request.getAdresa());
        }
        if (request.getDatumRodjenja() != null) {
            postojeci.setDatumRodjenja(request.getDatumRodjenja());
        }
        if (request.getPol() != null) {
            postojeci.setPol(request.getPol());
        }
        if (request.getJmbg() != null) {
            postojeci.setJmbg(request.getJmbg());
        }
    }

    public StudentIndeks toStudentIndeks(StudentIndeksRequest studentIndeksRequest) {
        StudentIndeks studentIndeks = new StudentIndeks();
        studentIndeks.setGodina(studentIndeksRequest.getGodina());
        studentIndeks.setStudProgramOznaka(studentIndeksRequest.getStudProgramOznaka());
        studentIndeks.setNacinFinansiranja(studentIndeksRequest.getNacinFinansiranja());
        // Novi indeks je uvek neaktivan po default-u, aktivira se preko posebnog API poziva
        studentIndeks.setAktivan(false);
        studentIndeks.setVaziOd(studentIndeksRequest.getVaziOd());
        return studentIndeks;
    }

    public StudijskiProgram toStudijskiProgram(StudijskiProgramRequest zahtev){
        StudijskiProgram sp = new StudijskiProgram();
        sp.setOznaka(zahtev.getOznaka());
        sp.setNaziv(zahtev.getNaziv());
        sp.setGodinaAkreditacije(zahtev.getGodinaAkreditacije());
        sp.setZvanje(zahtev.getZvanje());
        sp.setTrajanjeGodina(zahtev.getTrajanjeGodina());
        sp.setTrajanjeSemestara(zahtev.getTrajanjeSemestara());
        sp.setVrstaStudija(zahtev.getVrstaStudija());
        sp.setUkupnoEspb(zahtev.getUkupnoEspb());
        return sp;
    }

    public StudijskiProgram updateStudijskiProgram(StudijskiProgram sp, StudijskiProgramRequest zahtev){
        sp.setOznaka(zahtev.getOznaka());
        sp.setNaziv(zahtev.getNaziv());
        sp.setGodinaAkreditacije(zahtev.getGodinaAkreditacije());
        sp.setZvanje(zahtev.getZvanje());
        sp.setTrajanjeGodina(zahtev.getTrajanjeGodina());
        sp.setTrajanjeSemestara(zahtev.getTrajanjeSemestara());
        sp.setVrstaStudija(zahtev.getVrstaStudija());
        sp.setUkupnoEspb(zahtev.getUkupnoEspb());
        return sp;
    }

    public DrziPredmet toDrziPredmet(DrziPredmetAddRequest request) {
        DrziPredmet dp = new DrziPredmet();
        
        // Set associations using IDs - service will resolve full entities
        Predmet predmet = new Predmet();
        predmet.setId(request.getPredmetId());
        dp.setPredmet(predmet);
        
        Nastavnik nastavnik = new Nastavnik();
        nastavnik.setId(request.getNastavnikId());
        dp.setNastavnik(nastavnik);
        
        return dp;
    }

    public SlusaPredmet toSlusaPredmet(SlusaPredmetRequest zahtev){
        SlusaPredmet sp = new SlusaPredmet();
        
        // postavi vezu sa StudentIndeks
        StudentIndeks studentIndeks = new StudentIndeks();
        studentIndeks.setId(zahtev.getStudentIndeksId());
        sp.setStudentIndeks(studentIndeks);
        
        //postavi vezu sa DrziPredmet
        DrziPredmet drziPredmet = new DrziPredmet();
        drziPredmet.setId(zahtev.getDrziPredmetId());
        sp.setDrziPredmet(drziPredmet);
        
        // postavi vezu sa UpisGodine ako moguce
        if (zahtev.getUpisGodineId() != null) {
            UpisGodine upisGodine = new UpisGodine();
            upisGodine.setId(zahtev.getUpisGodineId());
            sp.setUpisGodine(upisGodine);
        }
        
        return sp;
    }

    public Ispit toIspit(IspitRequest ispitRequest) {
        Ispit ispit = new Ispit();
        ispit.setDatumOdrzavanja(ispitRequest.getDatumOdrzavanja());
        ispit.setVremePocetka(ispitRequest.getVremePocetka());
        ispit.setVremeZavrsetka(ispitRequest.getVremeZavrsetka());
        ispit.setZakljucen(ispitRequest.isZakljucen());
        return ispit;
    }

    public Predmet toPredmet(PredmetRequest predmetRequest) {
        Predmet predmet = new Predmet();
        predmet.setSifra(predmetRequest.getSifra());
        predmet.setNaziv(predmetRequest.getNaziv());
        predmet.setOpis(predmetRequest.getOpis());
        predmet.setEspb(predmetRequest.getEspb());
        predmet.setSemestar(predmetRequest.getSemestar());
        predmet.setFondCasovaVezbe(predmetRequest.getFondCasovaVezbe());
        predmet.setFondCasovaPredavanja(predmetRequest.getFondCasovaPredavanja());
        predmet.setObavezan(predmetRequest.isObavezan());
        return predmet;
    }

    public PrijavaIspita toPrijavaIspita(PrijavaIspitaRequest request) {
        PrijavaIspita prijavaIspita = new PrijavaIspita();
        prijavaIspita.setDatumPrijave(request.getDatumPrijave());
        return prijavaIspita;
    }

    public UpisGodine toUpisGodine(UpisGodineRequest request) {
        UpisGodine upisGodine = new UpisGodine();
        upisGodine.setDatumUpisa(request.getDatumUpisa());
        upisGodine.setGodina(request.getGodina());
        upisGodine.setPrenetiESPB(request.getPrenetiESPB());
        
        StudentIndeks studentIndeks = new StudentIndeks();
        studentIndeks.setId(request.getStudentIndeksId());
        upisGodine.setStudentIndeks(studentIndeks);
        
        SkolskaGodina skolskaGodina = new SkolskaGodina();
        skolskaGodina.setId(request.getSkolskaGodinaId());
        upisGodine.setSkolskaGodina(skolskaGodina);
        
        return upisGodine;
    }

}
