package org.raflab.studsluzba.utils;

import org.raflab.studsluzba.controllers.request.*;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.*;

import java.util.ArrayList;
import java.util.List;

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

    public static void updateStudentPodaci(StudentPodaci postojeci, StudentPodaciRequest request){
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

    public static void updateNastavnik(Nastavnik postojeci, NastavnikRequest request){
        postojeci.setIme(request.getIme());
        postojeci.setPrezime(request.getPrezime());
        postojeci.setSrednjeIme(request.getSrednjeIme());
        postojeci.setEmail(request.getEmail());
        postojeci.setBrojTelefona(request.getBrojTelefona());
        postojeci.setAdresa(request.getAdresa());
        postojeci.setZvanja(request.getZvanja());
        postojeci.setDatumRodjenja(request.getDatumRodjenja());
        postojeci.setPol(request.getPol());
        postojeci.setJmbg(request.getJmbg());
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

    public static StudijskiProgram toStudijskiProgram(StudijskiProgramRequest zahtev){
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

    public static StudijskiProgram updateStudijskiProgram(StudijskiProgram sp, StudijskiProgramRequest zahtev){
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

    public static SlusaPredmet toSlusaPredmet(SlusaPredmetRequest zahtev){
        SlusaPredmet sp = new SlusaPredmet();
        
        // postavi vezu sa StudentIndeks
        StudentIndeks studentIndeks = new StudentIndeks();
        studentIndeks.setId(zahtev.getStudentIndeksId());
        sp.setStudentIndeks(studentIndeks);
        
        // postavi vezu sa DrziPredmet
        DrziPredmet drziPredmet = new DrziPredmet();
        drziPredmet.setId(zahtev.getDrziPredmetId());
        sp.setDrziPredmet(drziPredmet);
        
        // postavi vezu sa UpisGodine ako moguce
        if (zahtev.getUpisGodineId() != null) {
            UpisGodine upisGodine = new UpisGodine();
            upisGodine.setId(zahtev.getUpisGodineId());
            sp.setUpisGodine(upisGodine);
        }
        
        // postavi vezu sa StudentVisokaUstanova ako moguce
        if (zahtev.getStudentVisokaUstanovaId() != null) {
            StudentVisokaUstanova studentVisokaUstanova = new StudentVisokaUstanova();
            studentVisokaUstanova.setId(zahtev.getStudentVisokaUstanovaId());
            sp.setStudentVisokaUstanova(studentVisokaUstanova);
        }
        
        return sp;
    }

}
