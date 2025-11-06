package org.raflab.studsluzba.utils;

import org.raflab.studsluzba.controllers.request.*;
import org.raflab.studsluzba.controllers.response.DrziPredmetResponse;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Converters {

    public static Nastavnik toNastavnik(NastavnikRequest nastavnikRequest) {
        Nastavnik nastavnik = new Nastavnik();
        nastavnik.setIme(nastavnikRequest.getIme());
        nastavnik.setPrezime(nastavnikRequest.getPrezime());
        nastavnik.setSrednjeIme(nastavnikRequest.getSrednjeIme());
        nastavnik.setEmail(nastavnikRequest.getEmail());
        nastavnik.setBrojTelefona(nastavnikRequest.getBrojTelefona());
        nastavnik.setAdresa(nastavnikRequest.getAdresa());
        nastavnik.setZvanja(nastavnikRequest.getZvanja());
        nastavnik.setDatumRodjenja(nastavnikRequest.getDatumRodjenja());
        nastavnik.setPol(nastavnikRequest.getPol());
        nastavnik.setJmbg(nastavnikRequest.getJmbg());
        return nastavnik;
    }

    public static Predmet toPredmet(PredmetRequest predmetRequest) {
        Predmet predmet = new Predmet();
        predmet.setSifra(predmetRequest.getSifra());
        predmet.setNaziv(predmetRequest.getNaziv());
        predmet.setOpis(predmetRequest.getOpis());
        predmet.setEspb(predmetRequest.getEspb());
        predmet.setSemestar(predmetRequest.getSemestar());
        predmet.setFondCasovaVezbe(predmetRequest.getFondCasovaVezbe());
        predmet.setFondCasovaPredavanja(predmetRequest.getFondCasovaPredavanja());
        predmet.setStudProgram(predmetRequest.getStudProgram());
        predmet.setObavezan(predmetRequest.isObavezan());
        return predmet;
    }

    public static Ispit toIspit(IspitRequest ispitRequest) {
        Ispit ispit = new Ispit();
        ispit.setDatumOdrzavanja(ispitRequest.getDatumOdrzavanja());
        ispit.setVremePocetka(ispitRequest.getVremePocetka());
        ispit.setVremeZavrsetka(ispitRequest.getVremeZavrsetka());
        ispit.setDrziPredmet(ispitRequest.getDrziPredmet());
        ispit.setPredmet(ispitRequest.getPredmet());
        ispit.setIspitniRok(ispitRequest.getIspitniRok());
        ispit.setSkolskaGodina(ispitRequest.getSkolskaGodina());
        ispit.setZakljucen(ispit.isZakljucen());
        return ispit;
    }

    public static NastavnikResponse toNastavnikResponse(Nastavnik nastavnik)
    {
        NastavnikResponse response = new NastavnikResponse();
        response.setId(nastavnik.getId());
        response.setIme(nastavnik.getIme());
        response.setPrezime(nastavnik.getPrezime());
        response.setSrednjeIme(nastavnik.getSrednjeIme());
        response.setEmail(nastavnik.getEmail());
        response.setBrojTelefona(nastavnik.getBrojTelefona());
        response.setAdresa(nastavnik.getAdresa());
        response.setZvanja(nastavnik.getZvanja());

        response.setDatumRodjenja(nastavnik.getDatumRodjenja());
        response.setPol(nastavnik.getPol());
        response.setJmbg(nastavnik.getJmbg());
        return response;
    }



    public static List<NastavnikResponse> toNastavnikResponseList(Iterable<Nastavnik> nastavnikIterable) {
        List<NastavnikResponse> nastavnikResponses = new ArrayList<>();

        nastavnikIterable.forEach((nastavnik) -> {
            nastavnikResponses.add(toNastavnikResponse(nastavnik));
        });
        return nastavnikResponses;
    }

    public static StudentPodaci toStudentPodaci(StudentPodaciRequest request) {
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

    public static StudentIndeks toStudentIndeks(StudentIndeksRequest studentIndeksRequest) {
        StudentIndeks studentIndeks = new StudentIndeks();
        studentIndeks.setGodina(studentIndeksRequest.getGodina());
        studentIndeks.setStudProgramOznaka(studentIndeksRequest.getStudProgramOznaka());
        studentIndeks.setNacinFinansiranja(studentIndeksRequest.getNacinFinansiranja());
        studentIndeks.setAktivan(studentIndeksRequest.isAktivan());
        studentIndeks.setVaziOd(studentIndeksRequest.getVaziOd());
        return studentIndeks;
    }

    public static DrziPredmetResponse toDrziPredmetResponse(DrziPredmet dp) {
        DrziPredmetResponse r = new DrziPredmetResponse();
        r.setId(dp.getId());

        if (dp.getNastavnik() != null) {
            r.setNastavnikId(dp.getNastavnik().getId());
            r.setNastavnikIme(dp.getNastavnik().getIme());
            r.setNastavnikPrezime(dp.getNastavnik().getPrezime());
            r.setNastavnikEmail(dp.getNastavnik().getEmail());
        }
        if (dp.getPredmet() != null) {
            r.setPredmetId(dp.getPredmet().getId());
            r.setPredmetNaziv(dp.getPredmet().getNaziv());
            r.setPredmetSifra(dp.getPredmet().getSifra());
        }
        if (dp.getSkolskaGodina() != null) {
            r.setSkolskaGodinaId(dp.getSkolskaGodina().getId());
            r.setSkolskaGodinaNaziv(dp.getSkolskaGodina().getNaziv());
        }
        return r;
    }
}
