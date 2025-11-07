package org.raflab.studsluzba.utils;

import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentPodaci;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.model.dtos.StudentDTO;
import org.springframework.stereotype.Component;

@Component
public class EntityMappers {
	
	public static StudentDTO fromStudentPodaciToDTO(StudentPodaci sp) {
		StudentDTO s = new StudentDTO();
		s.setIdStudentPodaci(sp.getId());
		s.setIme(sp.getIme());
		s.setPrezime(sp.getPrezime());
		return s;
		
	}
	
	public static StudentDTO fromStudentIndeksToDTO(StudentIndeks si) {
		StudentDTO s = fromStudentPodaciToDTO(si.getStudent());	
		s.setIdIndeks(si.getId());
		s.setGodinaUpisa(si.getGodina());
		s.setBroj(si.getBroj());
		s.setStudProgramOznaka(si.getStudProgramOznaka());
		s.setAktivanIndeks(si.isAktivan());
		return s;
		
	}

    public StudentIndeksResponse fromStudentIndexToResponse(StudentIndeks si) {
        if (si == null) {
            return null;
        }
        StudentIndeksResponse response = new StudentIndeksResponse();
        response.setId(si.getId());
        response.setBroj(si.getBroj());
        response.setGodina(si.getGodina());
        response.setStudProgramOznaka(si.getStudProgramOznaka());
        response.setNacinFinansiranja(si.getNacinFinansiranja());
        response.setAktivan(si.isAktivan());
        response.setVaziOd(si.getVaziOd());
        response.setOstvarenoEspb(si.getOstvarenoEspb());
        response.setStudijskiProgram(si.getStudijskiProgram());
        response.setStudent(si.getStudent());
        return response;
    }

    public StudentPodaciResponse fromStudentPodaciToResponse(StudentPodaci sp) {
        if (sp == null) {
            return null;
        }

        StudentPodaciResponse response = new StudentPodaciResponse();
        response.setId(sp.getId());
        response.setIme(sp.getIme());
        response.setPrezime(sp.getPrezime());
        response.setSrednjeIme(sp.getSrednjeIme());
        response.setJmbg(sp.getJmbg());
        response.setDatumRodjenja(sp.getDatumRodjenja());
        response.setMestoRodjenja(sp.getMestoRodjenja());
        response.setMestoPrebivalista(sp.getMestoPrebivalista());
        response.setDrzavaRodjenja(sp.getDrzavaRodjenja());
        response.setDrzavljanstvo(sp.getDrzavljanstvo());
        response.setNacionalnost(sp.getNacionalnost());
        response.setPol(sp.getPol());
        response.setAdresa(sp.getAdresa());
        response.setBrojTelefonaMobilni(sp.getBrojTelefonaMobilni());
        response.setBrojTelefonaFiksni(sp.getBrojTelefonaFiksni());
        response.setEmailPrivatni(sp.getEmailPrivatni());
        response.setEmailFakultetski(sp.getEmailFakultetski());
        response.setBrojLicneKarte(sp.getBrojLicneKarte());
        response.setLicnuKartuIzdao(sp.getLicnuKartuIzdao());
        response.setMestoStanovanja(sp.getMestoStanovanja());
        response.setAdresaStanovanja(sp.getAdresaStanovanja());

        return response;
    }

    public NastavnikResponse fromNastavnikToResponse(Nastavnik nastavnik){
        if(nastavnik == null){
            return null;
        }

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

    public StudijskiProgramResponse fromStudijskiProgramToResponse(StudijskiProgram sp){
        if (sp == null) {
            return null;
        }
        StudijskiProgramResponse resp = new StudijskiProgramResponse();
        resp.setId(sp.getId());
        resp.setOznaka(sp.getOznaka());
        resp.setNaziv(sp.getNaziv());
        resp.setGodinaAkreditacije(sp.getGodinaAkreditacije());
        resp.setZvanje(sp.getZvanje());
        resp.setTrajanjeGodina(sp.getTrajanjeGodina());
        resp.setTrajanjeSemestara(sp.getTrajanjeSemestara());
        resp.setVrstaStudija(sp.getVrstaStudija());
        resp.setUkupnoEspb(sp.getUkupnoEspb());
        resp.setSkolskaGodina(sp.getSkolskaGodina());
        return resp;
    }
}
