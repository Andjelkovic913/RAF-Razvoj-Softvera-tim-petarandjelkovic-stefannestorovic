package org.raflab.studsluzba.utils;

import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.*;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMappers {

    private final Converters converters;

    public EntityMappers(Converters converters) {
        this.converters = converters;
    }
	
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
        response.setStudent(fromStudentPodaciToResponse(si.getStudent()));
        response.setStudijskiProgram(fromStudijskiProgramToBasicDTO(si.getStudijskiProgram()));
        return response;
    }

    public StudentIndeksDTO fromStudentIndeksToIndeksDTO(StudentIndeks si) {
        if (si == null) {
            return null;
        }
        StudentIndeksDTO dto = new StudentIndeksDTO();
        dto.setId(si.getId());
        dto.setBroj(si.getBroj());
        dto.setGodina(si.getGodina());
        dto.setStudProgramOznaka(si.getStudProgramOznaka());
        dto.setNacinFinansiranja(si.getNacinFinansiranja());
        dto.setAktivan(si.isAktivan());
        dto.setVaziOd(si.getVaziOd());
        dto.setOstvarenoEspb(si.getOstvarenoEspb());
        dto.setStudent(fromStudentPodaciToResponse(si.getStudent()));
        dto.setStudijskiProgram(fromStudijskiProgramToBasicDTO(si.getStudijskiProgram()));
        return dto;
    }

    public StudentPodaciResponse fromStudentPodaciToResponse(StudentPodaci sp) {
        if (sp == null) {
            return null;
        }

        StudentPodaciResponse response = new StudentPodaciResponse();
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
        resp.setSkolskaGodina(fromSkolskaGodinaToDTO(sp.getSkolskaGodina()));
        return resp;
    }

    public StudijskiProgramBasicDTO fromStudijskiProgramToBasicDTO(StudijskiProgram sp) {
        if (sp == null) {
            return null;
        }
        StudijskiProgramBasicDTO dto = new StudijskiProgramBasicDTO();
        dto.setId(sp.getId());
        dto.setOznaka(sp.getOznaka());
        dto.setNaziv(sp.getNaziv());
        dto.setGodinaAkreditacije(sp.getGodinaAkreditacije());
        dto.setZvanje(sp.getZvanje());
        dto.setTrajanjeGodina(sp.getTrajanjeGodina());
        dto.setTrajanjeSemestara(sp.getTrajanjeSemestara());
        dto.setVrstaStudija(sp.getVrstaStudija());
        dto.setUkupnoEspb(sp.getUkupnoEspb());
        return dto;
    }

    public SkolskaGodinaDTO fromSkolskaGodinaToDTO(SkolskaGodina sg) {
        if (sg == null) {
            return null;
        }
        SkolskaGodinaDTO dto = new SkolskaGodinaDTO();
        dto.setId(sg.getId());
        dto.setNaziv(sg.getNaziv());
        dto.setAktivna(sg.isAktivna());
        return dto;
    }

    public SlusaPredmetDTO fromSlusaPredmetToDTO(SlusaPredmet sp) {
        if (sp == null) {
            return null;
        }
        SlusaPredmetDTO dto = new SlusaPredmetDTO();
        dto.setId(sp.getId());
        dto.setStudentIndeksId(sp.getStudentIndeks() != null ? sp.getStudentIndeks().getId() : null);
        dto.setDrziPredmet(sp.getDrziPredmet() != null ? converters.toDrziPredmetResponse(sp.getDrziPredmet()) : null);
        dto.setUpisGodineId(sp.getUpisGodine() != null ? sp.getUpisGodine().getId() : null);
        dto.setStudentVisokaUstanovaId(sp.getStudentVisokaUstanova() != null ? sp.getStudentVisokaUstanova().getId() : null);
        return dto;
    }

    public List<SlusaPredmetDTO> fromSlusaPredmetiToDTO(List<SlusaPredmet> slusaPredmeti) {
        if (slusaPredmeti == null) {
            return null;
        }
        return slusaPredmeti.stream()
                .map(this::fromSlusaPredmetToDTO)
                .collect(Collectors.toList());
    }

    public PredmetDTO fromPredmetToDTO(Predmet p){
        if (p == null) {
            return null;
        }
        PredmetDTO dto = new PredmetDTO();
        dto.setId(p.getId());
        dto.setSifra(p.getSifra());
        dto.setNaziv(p.getNaziv());
        dto.setSemestar(p.getSemestar());
        dto.setEspb(p.getEspb());
        dto.setObavezan(p.isObavezan());
        dto.setStudProgramId(p.getStudProgram() != null ? p.getStudProgram().getId() : null);
        return dto;
    }
    public List<PredmetDTO> fromPredmetiToDTO(List<Predmet> predmeti){
        if (predmeti == null) {
            return null;
        }
        return predmeti.stream()
                .map(this::fromPredmetToDTO)
                .collect(Collectors.toList());
    }

    public PredmetResponse fromPredmetToResponse(Predmet p) {
        if (p == null) return null;
        PredmetResponse r = new PredmetResponse();
        r.setSifra(p.getSifra());
        r.setNaziv(p.getNaziv());
        r.setEspb(p.getEspb());
        r.setSemestar(p.getSemestar());
        r.setFondCasovaVezbe(p.getFondCasovaVezbe());
        r.setFondCasovaPredavanja(p.getFondCasovaPredavanja());
        r.setObavezan(p.isObavezan());
        r.setOpis(p.getOpis());
        return r;
    }

    public PrijavljeniStudentDTO fromPrijavaIspitaToPrijavljeniStudentDTO(PrijavaIspita prijavaIspita) {
        if (prijavaIspita == null || prijavaIspita.getStudentIndeks() == null) {
            return null;
        }
        StudentIndeks studentIndeks = prijavaIspita.getStudentIndeks();
        StudentPodaci studentPodaci = studentIndeks.getStudent();

        PrijavljeniStudentDTO dto = new PrijavljeniStudentDTO();
        dto.setStudentIndeksId(studentIndeks.getId());
        if (studentPodaci != null) {
            dto.setIme(studentPodaci.getIme());
            dto.setPrezime(studentPodaci.getPrezime());
        }
        dto.setStudProgramOznaka(studentIndeks.getStudProgramOznaka());
        dto.setGodina(studentIndeks.getGodina());
        dto.setBroj(studentIndeks.getBroj());
        dto.setIndeks(formatIndeks(studentIndeks));
        return dto;
    }

    public List<PrijavljeniStudentDTO> fromPrijaveIspitaToPrijavljeniStudentiDTO(List<PrijavaIspita> prijave) {
        if (prijave == null) {
            return null;
        }
        return prijave.stream()
                .map(this::fromPrijavaIspitaToPrijavljeniStudentDTO)
                .collect(Collectors.toList());
    }

    private String formatIndeks(StudentIndeks indeks) {
        if (indeks == null || indeks.getStudProgramOznaka() == null) {
            return null;
        }
        int shortYear = indeks.getGodina() % 100;
        return String.format("%s%d/%02d", indeks.getStudProgramOznaka(), indeks.getBroj(), shortYear);
    }
}
