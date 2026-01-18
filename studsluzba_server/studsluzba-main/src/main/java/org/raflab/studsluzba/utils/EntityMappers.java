package org.raflab.studsluzba.utils;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.dtos.*;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EntityMappers {

	private final SlusaPredmetRepository slusaPredmetRepository;

	
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
        dto.setStudent(fromStudentPodaciToStudentPodaciDTO(si.getStudent()));
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

    public StudentPodaciDTO fromStudentPodaciToStudentPodaciDTO(StudentPodaci sp) {
        if (sp == null) {
            return null;
        }
        StudentPodaciDTO dto = new StudentPodaciDTO();
        dto.setId(sp.getId());
        dto.setIme(sp.getIme());
        dto.setPrezime(sp.getPrezime());
        dto.setSrednjeIme(sp.getSrednjeIme());
        dto.setJmbg(sp.getJmbg());
        dto.setDatumRodjenja(sp.getDatumRodjenja());
        dto.setMestoRodjenja(sp.getMestoRodjenja());
        dto.setMestoPrebivalista(sp.getMestoPrebivalista());
        dto.setDrzavaRodjenja(sp.getDrzavaRodjenja());
        dto.setDrzavljanstvo(sp.getDrzavljanstvo());
        dto.setNacionalnost(sp.getNacionalnost());
        dto.setPol(sp.getPol());
        dto.setAdresa(sp.getAdresa());
        dto.setBrojTelefonaMobilni(sp.getBrojTelefonaMobilni());
        dto.setBrojTelefonaFiksni(sp.getBrojTelefonaFiksni());
        dto.setEmailPrivatni(sp.getEmailPrivatni());
        dto.setEmailFakultetski(sp.getEmailFakultetski());
        dto.setBrojLicneKarte(sp.getBrojLicneKarte());
        dto.setLicnuKartuIzdao(sp.getLicnuKartuIzdao());
        dto.setMestoStanovanja(sp.getMestoStanovanja());
        dto.setAdresaStanovanja(sp.getAdresaStanovanja());
        return dto;
    }

    public DrziPredmetDTO fromDrziPredmetToDTO(DrziPredmet dp) {
        if (dp == null) {
            return null;
        }
        DrziPredmetDTO dto = new DrziPredmetDTO();
        dto.setId(dp.getId() != null ? dp.getId().intValue() : 0);
        if (dp.getNastavnik() != null) {
            TeacherDTO teacher = new TeacherDTO();
            teacher.setId(dp.getNastavnik().getId() != null ? dp.getNastavnik().getId().intValue() : null);
            teacher.setEmail(dp.getNastavnik().getEmail());
            teacher.setFirstName(dp.getNastavnik().getIme());
            teacher.setLastName(dp.getNastavnik().getPrezime());
            dto.setTeacher(teacher);
        }
        if (dp.getPredmet() != null) {
            SubjectDTO subject = new SubjectDTO();
            subject.setId(dp.getPredmet().getId() != null ? dp.getPredmet().getId().intValue() : null);
            subject.setName(dp.getPredmet().getNaziv());
            dto.setSubject(subject);
        }
        // dto.setClassType - field not present in DrziPredmet model
        return dto;
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
        response.setZvanja(nastavnik.getZvanja() != null ? 
            nastavnik.getZvanja().stream()
                .map(this::fromNastavnikZvanjeToResponse)
                .collect(Collectors.toSet()) : null);
        
        if (nastavnik.getZvanja() != null && !nastavnik.getZvanja().isEmpty()) {
            response.setTrenutnoZvanje(nastavnik.getZvanja().stream()
                .filter(NastavnikZvanje::isAktivno)
                .findFirst()
                .map(NastavnikZvanje::getZvanje)
                .orElse(null));
        }
        
        response.setDatumRodjenja(nastavnik.getDatumRodjenja());
        response.setPol(nastavnik.getPol());
        response.setJmbg(nastavnik.getJmbg());
        return response;
    }

    public List<NastavnikResponse> fromNastavnikListToResponse(Iterable<Nastavnik> nastavnici) {
        List<NastavnikResponse> responses = new java.util.ArrayList<>();
        nastavnici.forEach(n -> responses.add(fromNastavnikToResponse(n)));
        return responses;
    }

    private NastavnikZvanjeResponse fromNastavnikZvanjeToResponse(NastavnikZvanje zvanje) {
        if (zvanje == null) {
            return null;
        }
        NastavnikZvanjeResponse response = new NastavnikZvanjeResponse();
        response.setId(zvanje.getId());
        response.setDatumIzbora(zvanje.getDatumIzbora());
        response.setNaucnaOblast(zvanje.getNaucnaOblast());
        response.setUzaNaucnaOblast(zvanje.getUzaNaucnaOblast());
        response.setZvanje(zvanje.getZvanje());
        response.setAktivno(zvanje.isAktivno());
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
        dto.setDrziPredmet(sp.getDrziPredmet() != null ? fromDrziPredmetToDTO(sp.getDrziPredmet()) : null);
        dto.setUpisGodineId(sp.getUpisGodine() != null ? sp.getUpisGodine().getId() : null);
        return dto;
    }

    public DrziPredmetResponse fromDrziPredmetToResponse(DrziPredmet dp) {
        if (dp == null) {
            return null;
        }
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
        return r;
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
        r.setId(p.getId());
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
        Ispit ispit = prijavaIspita.getIspit();

        PrijavljeniStudentDTO dto = new PrijavljeniStudentDTO();
        dto.setPrijavaId(prijavaIspita.getId());  // Set prijavaIspitaId
        dto.setStudentIndeksId(studentIndeks.getId());
        if (studentPodaci != null) {
            dto.setIme(studentPodaci.getIme());
            dto.setPrezime(studentPodaci.getPrezime());
        }
        dto.setStudProgramOznaka(studentIndeks.getStudProgramOznaka());
        dto.setGodina(studentIndeks.getGodina());
        dto.setBroj(studentIndeks.getBroj());
        dto.setIndeks(formatIndeks(studentIndeks));
        
        if (ispit != null) {
            if (ispit.getPredmet() != null) {
                dto.setPredmetNaziv(ispit.getPredmet().getNaziv());
                
                // Find SlusaPredmet for this student and predmet
                List<SlusaPredmet> slusaPredmeti = slusaPredmetRepository.findAllByStudentIndeksIdAndPredmetId(
                    studentIndeks.getId(), 
                    ispit.getPredmet().getId()
                );
                if (!slusaPredmeti.isEmpty()) {
                    // Take the first one if multiple exist (student enrolled with multiple professors)
                    SlusaPredmet slusaPredmet = slusaPredmeti.get(0);
                    dto.setSlusaPredmetId(slusaPredmet.getId());
                    // predispitneObaveze would come from IzlazakNaIspit if it exists
                    dto.setPredispitneObaveze(0.0);  // Default, will be updated from existing result if any
                } else {
                    // Log warning - student registered for exam but not enrolled in subject
                    System.err.println("WARNING: Student " + studentIndeks.getId() + 
                        " is registered for exam but not enrolled in subject " + ispit.getPredmet().getId());
                }
            }
            if (ispit.getIspitniRok() != null) {
                dto.setIspitniRokNaziv(ispit.getIspitniRok().getTipRoka() != null 
                    ? ispit.getIspitniRok().getTipRoka().name() 
                    : null);
            }
        }
        
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

    public UpisGodineResponse fromUpisGodineToResponse(UpisGodine ug) {
        if (ug == null) {
            return null;
        }
        UpisGodineResponse response = new UpisGodineResponse();
        response.setId(ug.getId());
        response.setDatumUpisa(ug.getDatumUpisa());
        response.setGodina(ug.getGodina());
        response.setPrenetiESPB(ug.getPrenetiESPB());
        
        if (ug.getStudentIndeks() != null) {
            response.setStudentIndeks(fromStudentIndeksToIndeksDTO(ug.getStudentIndeks()));
        }
        
        if (ug.getSkolskaGodina() != null) {
            response.setSkolskaGodina(fromSkolskaGodinaToDTO(ug.getSkolskaGodina()));
        }
        
        return response;
    }

    public List<UpisGodineResponse> fromUpisGodineListToResponse(List<UpisGodine> upisGodineList) {
        if (upisGodineList == null) {
            return null;
        }
        return upisGodineList.stream()
                .map(this::fromUpisGodineToResponse)
                .collect(Collectors.toList());
    }

    public StudentBySrednjaSkolaResponse fromStudentSrednjaSkolaToResponse(StudentSrednjaSkola sss, StudentIndeks studentIndeks) {
        if (sss == null) {
            return null;
        }
        StudentBySrednjaSkolaResponse response = new StudentBySrednjaSkolaResponse();
        
        // StudentPodaci data
        if (sss.getStudent() != null) {
            response.setStudentPodaciId(sss.getStudent().getId());
            response.setIme(sss.getStudent().getIme());
            response.setPrezime(sss.getStudent().getPrezime());
            response.setSrednjeIme(sss.getStudent().getSrednjeIme());
            response.setJmbg(sss.getStudent().getJmbg());
            response.setDatumRodjenja(sss.getStudent().getDatumRodjenja());
        }
        
        // SrednjaSkola data
        if (sss.getSrednjaSkola() != null) {
            response.setSrednjaSkolaId(sss.getSrednjaSkola().getId());
            response.setSrednjaSkolaNaziv(sss.getSrednjaSkola().getNaziv());
            response.setSrednjaSkolaMesto(sss.getSrednjaSkola().getMesto());
        }
        
        // StudentSrednjaSkola data
        response.setDatumZavrsetka(sss.getDatumZavrsetka());
        response.setUspehIzSrednjeSkole(sss.getUspehIzSrednjeSkole());
        
        return response;
    }

    public SrednjaSkolaResponse fromSrednjaSkolaToResponse(SrednjaSkola ss) {
        if (ss == null) {
            return null;
        }
        SrednjaSkolaResponse response = new SrednjaSkolaResponse();
        response.setId(ss.getId());
        response.setNaziv(ss.getNaziv());
        response.setVrsta(ss.getVrsta() != null ? ss.getVrsta().toString() : null);
        response.setMesto(ss.getMesto());
        response.setNapomena(ss.getNapomena());
        response.setSifarnik(ss.getSifarnik());
        return response;
    }

    public List<SrednjaSkolaResponse> fromSrednjaSkolaListToResponse(List<SrednjaSkola> srednjeSKole) {
        if (srednjeSKole == null) {
            return null;
        }
        return srednjeSKole.stream()
                .map(this::fromSrednjaSkolaToResponse)
                .collect(Collectors.toList());
    }

    public StudentSrednjaSkolaResponse fromStudentSrednjaSkolaToDetailResponse(StudentSrednjaSkola sss) {
        if (sss == null) {
            return null;
        }
        StudentSrednjaSkolaResponse response = new StudentSrednjaSkolaResponse();
        response.setId(sss.getId());
        
        if (sss.getStudent() != null) {
            response.setStudentPodaciId(sss.getStudent().getId());
            response.setStudentIme(sss.getStudent().getIme());
            response.setStudentPrezime(sss.getStudent().getPrezime());
        }
        
        if (sss.getSrednjaSkola() != null) {
            response.setSrednjaSkolaId(sss.getSrednjaSkola().getId());
            response.setSrednjaSkolaNaziv(sss.getSrednjaSkola().getNaziv());
            response.setSrednjaSkolaMesto(sss.getSrednjaSkola().getMesto());
        }
        
        response.setDatumUpisa(sss.getDatumUpisa());
        response.setDatumZavrsetka(sss.getDatumZavrsetka());
        response.setUspehSaPrijemnog(sss.getUspehSaPrijemnog());
        response.setUspehIzSrednjeSkole(sss.getUspehIzSrednjeSkole());
        
        return response;
    }

    public List<StudentSrednjaSkolaResponse> fromStudentSrednjaSkolaListToDetailResponse(List<StudentSrednjaSkola> studentSrednjaSkolas) {
        if (studentSrednjaSkolas == null) {
            return null;
        }
        return studentSrednjaSkolas.stream()
                .map(this::fromStudentSrednjaSkolaToDetailResponse)
                .collect(Collectors.toList());
    }

    public IzlazakNaIspitResponse fromIzlazakNaIspitToResponse(IzlazakNaIspit izlazak) {
        if (izlazak == null) {
            return null;
        }
        IzlazakNaIspitResponse response = new IzlazakNaIspitResponse();
        response.setId(izlazak.getId());
        // provere
        if (izlazak.getIspit() != null) {
            response.setIspitId(izlazak.getIspit().getId());
            response.setDatumOdrzavanja(izlazak.getIspit().getDatumOdrzavanja());
            
            if (izlazak.getIspit().getDrziPredmet() != null && izlazak.getIspit().getDrziPredmet().getPredmet() != null) {
                response.setPredmetNaziv(izlazak.getIspit().getDrziPredmet().getPredmet().getNaziv());
                response.setPredmetSifra(izlazak.getIspit().getDrziPredmet().getPredmet().getSifra());
            }
            
            if (izlazak.getIspit().getIspitniRok() != null && izlazak.getIspit().getIspitniRok().getTipRoka() != null) {
                response.setIspitniRok(izlazak.getIspit().getIspitniRok().getTipRoka().name());
            }
        }

        if (izlazak.getStudentIndeks() != null) {
            response.setStudentIndeksId(izlazak.getStudentIndeks().getId());
            response.setBroj(izlazak.getStudentIndeks().getBroj());
            response.setGodina(izlazak.getStudentIndeks().getGodina());
            response.setStudProgramOznaka(izlazak.getStudentIndeks().getStudProgramOznaka());
            
            // Dodaj ime i prezime studenta
            if (izlazak.getStudentIndeks().getStudent() != null) {
                response.setIme(izlazak.getStudentIndeks().getStudent().getIme());
                response.setPrezime(izlazak.getStudentIndeks().getStudent().getPrezime());
            }
        }
        // rezultati
        response.setOsvojeniPredispitniPoeni(izlazak.getOsvojeniPredispitniPoeni());
        response.setOsvojeniIspitniPoeni(izlazak.getOsvojeniIspitniPoeni());
        response.setUkupnoPoena(izlazak.getUkupnoPoena());
        response.setOcena(izlazak.getOcena());
        response.setPonistio(izlazak.isPonistio());
        response.setNapomena(izlazak.getNapomena());
        
        return response;
    }

    public ObnovaGodineResponse fromObnovaGodineToResponse(ObnovaGodine obnovaGodine) {
        if (obnovaGodine == null) {
            return null;
        }
        ObnovaGodineResponse response = new ObnovaGodineResponse();
        response.setId(obnovaGodine.getId());
        response.setDatumUpisa(obnovaGodine.getDatumUpisa());
        response.setGodina(obnovaGodine.getGodina());
        response.setPrenetiESPB(obnovaGodine.getPrenetiESPB());
        response.setRazlogObnove(obnovaGodine.getRazlogObnove());
        
        if (obnovaGodine.getStudentIndeks() != null) {
            response.setStudentIndeksId(obnovaGodine.getStudentIndeks().getId());
            response.setBroj(obnovaGodine.getStudentIndeks().getBroj());
            response.setGodinaStudija(obnovaGodine.getStudentIndeks().getGodina());
            response.setStudProgramOznaka(obnovaGodine.getStudentIndeks().getStudProgramOznaka());
        }
        
        if (obnovaGodine.getSkolskaGodina() != null) {
            response.setSkolskaGodinaId(obnovaGodine.getSkolskaGodina().getId());
            response.setSkolskaGodinaNaziv(obnovaGodine.getSkolskaGodina().getNaziv());
        }
        
        if (obnovaGodine.getPrethodniUpis() != null) {
            response.setPrethodniUpisId(obnovaGodine.getPrethodniUpis().getId());
        }
        
        return response;
    }

    public UplataResponse fromUplataToResponse(Uplata uplata) {
        if (uplata == null) {
            return null;
        }
        UplataResponse response = new UplataResponse();
        response.setId(uplata.getId());
        response.setStudentIndeksId(uplata.getStudentIndeks().getId());
        response.setSkolskaGodinaId(uplata.getSkolskaGodina().getId());
        response.setDatumUplate(uplata.getDatumUplate());
        response.setIznosRsd(uplata.getIznosRsd());
        response.setSrednjiKurs(uplata.getSrednjiKurs());
        response.setSkolarinaUEur(uplata.getSkolarinaUEur());
        response.setTip(uplata.getTip());
        response.setStatus(uplata.getStatus());
        response.setPozivNaBroj(uplata.getPozivNaBroj());
        response.setOpis(uplata.getOpis());
        return response;
    }

    public StudentPaymentDTO fromUplataToStudentPaymentDTO(Uplata uplata) {
        if (uplata == null) {
            System.out.println("DEBUG [EntityMappers]: Uplata is null");
            return null;
        }
        
        System.out.println("DEBUG [EntityMappers]: Processing uplata ID: " + uplata.getId());
        System.out.println("DEBUG [EntityMappers]: Uplata skolskaGodina object: " + uplata.getSkolskaGodina());
        
        StudentPaymentDTO dto = new StudentPaymentDTO();
        dto.setId(uplata.getId());
        dto.setDatumUplate(uplata.getDatumUplate().toLocalDate());
        dto.setIznos(java.math.BigDecimal.valueOf(uplata.getIznosRsd()));
        dto.setSvrhaUplate(uplata.getOpis() != null ? uplata.getOpis() : uplata.getTip().toString());
        
        if (uplata.getSkolskaGodina() != null) {
            String naziv = uplata.getSkolskaGodina().getNaziv();
            System.out.println("DEBUG [EntityMappers]: SkolskaGodina naziv: " + naziv);
            dto.setSkolskaGodina(naziv);
        } else {
            System.out.println("DEBUG [EntityMappers]: SkolskaGodina is NULL!");
            dto.setSkolskaGodina("");
        }
        
        dto.setNacinPlacanja(uplata.getTip().toString());
        dto.setStatus(uplata.getStatus().toString());
        
        System.out.println("DEBUG [EntityMappers]: Final DTO skolskaGodina: " + dto.getSkolskaGodina());
        return dto;
    }

    public List<StudentPaymentDTO> fromUplateToStudentPaymentDTOs(List<Uplata> uplate) {
        if (uplate == null) {
            return null;
        }
        return uplate.stream()
                .map(this::fromUplataToStudentPaymentDTO)
                .collect(Collectors.toList());
    }

    public VisokoskolskaUstanovaResponse fromVisokoskolskaUstanovaToResponse(VisokoskolskaUstanova vu) {
        if (vu == null) {
            return null;
        }
        VisokoskolskaUstanovaResponse response = new VisokoskolskaUstanovaResponse();
        response.setId(vu.getId());
        response.setNaziv(vu.getNaziv());
        response.setTipSkole(vu.getTipSkole());
        response.setSifarnik(vu.getSifarnik());
        return response;
    }

    public List<VisokoskolskaUstanovaResponse> fromVisokoskolskaUstanovaListToResponse(List<VisokoskolskaUstanova> ustanove) {
        if (ustanove == null) {
            return null;
        }
        return ustanove.stream()
                .map(this::fromVisokoskolskaUstanovaToResponse)
                .collect(Collectors.toList());
    }

    public StudentVisokaUstanovaResponse fromStudentVisokaUstanovaToResponse(StudentVisokaUstanova svu) {
        if (svu == null) {
            return null;
        }
        StudentVisokaUstanovaResponse response = new StudentVisokaUstanovaResponse();
        response.setId(svu.getId());
        
        if (svu.getStudent() != null) {
            response.setStudentPodaciId(svu.getStudent().getId());
            response.setStudentIme(svu.getStudent().getIme());
            response.setStudentPrezime(svu.getStudent().getPrezime());
        }
        
        if (svu.getVisokaUstanova() != null) {
            response.setVisokaUstanovaId(svu.getVisokaUstanova().getId());
            response.setVisokaUstanovaNaziv(svu.getVisokaUstanova().getNaziv());
            response.setVisokaUstanovaTipSkole(svu.getVisokaUstanova().getTipSkole());
        }
        
        response.setDatumUpisa(svu.getDatumUpisa());
        response.setDatumZavrsetka(svu.getDatumZavrsetka());
        response.setNapomena(svu.getNapomena());
        
        return response;
    }

    public List<StudentVisokaUstanovaResponse> fromStudentVisokaUstanovaListToResponse(List<StudentVisokaUstanova> studentVisokeUstanove) {
        if (studentVisokeUstanove == null) {
            return null;
        }
        return studentVisokeUstanove.stream()
                .map(this::fromStudentVisokaUstanovaToResponse)
                .collect(Collectors.toList());
    }
}
