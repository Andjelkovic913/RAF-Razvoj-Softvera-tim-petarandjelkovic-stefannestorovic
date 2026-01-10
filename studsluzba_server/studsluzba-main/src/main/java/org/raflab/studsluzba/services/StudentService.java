package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.dtos.StudentDTO;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.raflab.studsluzba.utils.ParseUtils;
import org.raflab.studsluzba.utils.CoderLoader;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentPodaciRepository studentPodaciRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final StudijskiProgramRepository studijskiProgramRepository;
    private final StudentSrednjaSkolaRepository studentSrednjaSkolaRepository;
    private final StudentVisokaUstanovaRepository studentVisokaUstanovaRepository;
    private final UpisGodineRepository upisGodineRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final PrijavaIspitaRepository prijavaIspitaRepository;
    private final UplataRepository uplataRepository;
    private final PredispitneObavezeRepository predispitneObavezeRepository;
    private final IzlazakNaIspitRepository izlazakNaIspitRepository;
    private final Converters converters;
    private final EntityMappers entityMappers;
    private final CoderLoader coderLoader;

    // StudentPodaci operations
    public Long createStudentPodaci(StudentPodaciRequest request) {
        // Provera duplikata pre kreiranja
        validateStudentPodaciForCreate(request);
        
        StudentPodaci studentPodaci = converters.toStudentPodaci(request);
        StudentPodaci saved = studentPodaciRepository.save(studentPodaci);
        return saved.getId();
    }
    
    /**
     * Validira da ne postoje duplikati prilikom kreiranja novog studenta
     */
    private void validateStudentPodaciForCreate(StudentPodaciRequest request) {
        // Provera JMBG
        if (request.getJmbg() != null && !request.getJmbg().trim().isEmpty()) {
            if (studentPodaciRepository.findByJmbg(request.getJmbg()).isPresent()) {
                throw new RuntimeException("Student sa JMBG-om " + request.getJmbg() + " već postoji");
            }
        }
        
        // Provera broja lične karte
        if (request.getBrojLicneKarte() != null && !request.getBrojLicneKarte().trim().isEmpty()) {
            if (studentPodaciRepository.findByBrojLicneKarte(request.getBrojLicneKarte()).isPresent()) {
                throw new RuntimeException("Student sa brojem lične karte " + request.getBrojLicneKarte() + " već postoji");
            }
        }
        
        // Provera fakultetskog email-a
        if (request.getEmailFakultetski() != null && !request.getEmailFakultetski().trim().isEmpty()) {
            if (studentPodaciRepository.findByEmailFakultetski(request.getEmailFakultetski()).isPresent()) {
                throw new RuntimeException("Student sa fakultetskim email-om " + request.getEmailFakultetski() + " već postoji");
            }
        }
    }

    public List<StudentPodaciResponse> getAllStudentPodaci() {
        return studentPodaciRepository.findAll().stream()
                .map(entityMappers::fromStudentPodaciToResponse)
                .collect(Collectors.toList());
    }

    public Page<StudentPodaciResponse> getAllStudentPodaciPaginated(Integer page, Integer size) {
        return studentPodaciRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()))
                .map(entityMappers::fromStudentPodaciToResponse);
    }

    // selekcija studenta (njegovih licnih podataka) preko ID-a
    public StudentPodaciResponse getStudentPodaciById(Long id) {
        return studentPodaciRepository.findById(id)
                .map(entityMappers::fromStudentPodaciToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Student sa ID " + id + " ne postoji"));
    }

    public StudentPodaciResponse updateStudentPodaci(Long id, StudentPodaciRequest request) {
        StudentPodaci student = studentPodaciRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student ne postoji"));
        
        // Provera duplikata prilikom update-a
        validateStudentPodaciForUpdate(id, request);
        
        converters.updateStudentPodaci(student, request);
        StudentPodaci updated = studentPodaciRepository.save(student);
        return entityMappers.fromStudentPodaciToResponse(updated);
    }
    
    /**
     * Validira da ne postoje duplikati prilikom update-a studenta
     */
    private void validateStudentPodaciForUpdate(Long id, StudentPodaciRequest request) {
        // Provera JMBG
        if (request.getJmbg() != null && !request.getJmbg().trim().isEmpty()) {
            if (studentPodaciRepository.existsByJmbgAndIdNot(request.getJmbg(), id)) {
                throw new RuntimeException("Student sa JMBG-om " + request.getJmbg() + " već postoji");
            }
        }
        
        // Provera broja licne karte
        if (request.getBrojLicneKarte() != null && !request.getBrojLicneKarte().trim().isEmpty()) {
            if (studentPodaciRepository.existsByBrojLicneKarteAndIdNot(request.getBrojLicneKarte(), id)) {
                throw new RuntimeException("Student sa brojem lične karte " + request.getBrojLicneKarte() + " već postoji");
            }
        }
        
        // Provera fakultetskog email-a
        if (request.getEmailFakultetski() != null && !request.getEmailFakultetski().trim().isEmpty()) {
            if (studentPodaciRepository.existsByEmailFakultetskiAndIdNot(request.getEmailFakultetski(), id)) {
                throw new RuntimeException("Student sa fakultetskim email-om " + request.getEmailFakultetski() + " već postoji");
            }
        }
    }

    /**
     * Manualno brisanje studenta i svih povezanih podataka.
     * Briše se od najdubljih zavisnosti ka roditeljskim entitetima.
     */
    @Transactional
    public void deleteStudentPodaci(Long id) {
        StudentPodaci student = studentPodaciRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student sa ID " + id + " ne postoji"));
        
        // 1. Dohvati sve indekse ovog studenta
        List<StudentIndeks> indeksi = studentIndeksRepository.findStudentIndeksiForStudentPodaciId(id);
        
        // 2. Za svaki indeks obrisi sve zavisne entitete
        for (StudentIndeks indeks : indeksi) {
            deleteStudentIndeksDependencies(indeks.getId());
        }
        
        // 3. Obrisi sve indekse
        studentIndeksRepository.deleteAll(indeksi);
        
        // 4. Obrisi StudentSrednjaSkola zapise
        List<StudentSrednjaSkola> srednjeSkole = studentSrednjaSkolaRepository.findByStudentId(id);
        studentSrednjaSkolaRepository.deleteAll(srednjeSkole);
        
        // 5. Obrisi StudentVisokaUstanova zapise
        List<StudentVisokaUstanova> visokeUstanove = studentVisokaUstanovaRepository.findByStudentId(id);
        studentVisokaUstanovaRepository.deleteAll(visokeUstanove);
        
        // 6. Na kraju obrisi samog studenta
        studentPodaciRepository.delete(student);
    }
    
    /**
     * Brise sve zavisne entitete za dati StudentIndeks.
     * Redosled brisanja: od najdubljih zavisnosti ka plicim.
     */
    private void deleteStudentIndeksDependencies(Long studentIndeksId) {
        // 1. Dohvati sve SlusaPredmet za ovaj indeks
        List<SlusaPredmet> slusaniPredmeti = slusaPredmetRepository.findByStudentIndeksId(studentIndeksId);
        
        // 2. Za svaki SlusaPredmet obrisi PredispitneObaveze i IzlazakNaIspit
        for (SlusaPredmet sp : slusaniPredmeti) {
            // Obrisi PredispitneObaveze
            List<PredispitneObaveze> predispitne = predispitneObavezeRepository.findBySlusaPredmetId(sp.getId());
            predispitneObavezeRepository.deleteAll(predispitne);
            
            // Obrisi IzlazakNaIspit
            List<IzlazakNaIspit> izlasci = izlazakNaIspitRepository.findBySlusaPredmetId(sp.getId());
            izlazakNaIspitRepository.deleteAll(izlasci);
        }
        
        // 3. Obrisi sve SlusaPredmet
        slusaPredmetRepository.deleteAll(slusaniPredmeti);
        
        // 4. Obrisi sve PrijavaIspita (IzlazakNaIspit je vec obrisan iznad)
        List<PrijavaIspita> prijave = prijavaIspitaRepository.findByStudentIndeksId(studentIndeksId);
        prijavaIspitaRepository.deleteAll(prijave);
        
        // 5. Obrisi sve UpisGodine
        List<UpisGodine> upisi = upisGodineRepository.findByStudentIndeksId(studentIndeksId);
        upisGodineRepository.deleteAll(upisi);
        
        // 6. Obrisi sve Uplate
        List<Uplata> uplate = uplataRepository.findByStudentIndeksId(studentIndeksId);
        uplataRepository.deleteAll(uplate);
    }
 
    //  selekcija studenta (njegovih licnih podataka) preko broja indeksa, studProgramOznaka i godine
    public StudentPodaciResponse getStudentPodaciByBroj(int broj, String studProgramOznaka, Integer godina) {
        StudentIndeks si = studentIndeksRepository.findStudentIndeks(studProgramOznaka.toUpperCase(), godina, broj);
        if (si == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Student sa indeksom %s %d/%d ne postoji", studProgramOznaka, broj, godina));
        }
        return entityMappers.fromStudentPodaciToResponse(si.getStudent());
    }

    public StudentPodaciResponse updateStudentPodaciByIndeks(String studProgramOznaka, int godina, int broj, StudentPodaciRequest request) {
        StudentIndeks indeks = studentIndeksRepository.findStudentIndeks(studProgramOznaka.toUpperCase(), godina, broj);
        if (indeks == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Student sa indeksom %s %d/%d ne postoji", studProgramOznaka, broj, godina));
        }

        StudentPodaci studentPodaci = indeks.getStudent();
        converters.updateStudentPodaci(studentPodaci, request);
        StudentPodaci saved = studentPodaciRepository.save(studentPodaci);
        return entityMappers.fromStudentPodaciToResponse(saved);
    }

    // StudentIndeks operations
    @Transactional
    public Long saveIndeks(StudentIndeksRequest request) {
        // Validate study program code against coder file
        if (!coderLoader.isValidStudProgramOznaka(request.getStudProgramOznaka())) {
            throw new RuntimeException("Invalid study program code: " + request.getStudProgramOznaka() + 
                    ". Valid codes are: " + coderLoader.getValidStudProgramOznake());
        }
        
        // Provera da student vec nema aktivan indeks na istom studijskom programu
        validateStudentIndeksForCreate(request);
        
        StudentIndeks studentIndeks = converters.toStudentIndeks(request);

        int nextBroj = findNextBroj(request.getGodina(), request.getStudProgramOznaka());
        studentIndeks.setBroj(nextBroj); // trazi koji sve brojevi indeksa vec postoje i uzima nekorisceni

        StudentPodaci studentPodaci = studentPodaciRepository.findById(request.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student sa ID " + request.getStudentId() + " ne postoji"));
        studentIndeks.setStudent(studentPodaci);

        List<StudijskiProgram> studijskiProgrami = studijskiProgramRepository.findByOznaka(request.getStudProgramOznaka());
        if (studijskiProgrami.isEmpty()) {
            throw new RuntimeException("No StudijskiProgram found with oznaka: " + request.getStudProgramOznaka());
        }
        studentIndeks.setStudijskiProgram(studijskiProgrami.get(0));
        // trazi StudentskiProgram entitet na koji ce da ga poveze na osnovu oznake

        try {
            StudentIndeks savedStudentIndeks = studentIndeksRepository.save(studentIndeks);
            return savedStudentIndeks.getId();
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Duplicate entry for broj, godina, and studProgramOznaka", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving the StudentIndeks.", e);
        }
    }
    
    /**
     * Validira da student vec nema indeks u istoj godini ILI na istom studijskom programu
     */
    private void validateStudentIndeksForCreate(StudentIndeksRequest request) {
        // Provera da li student vec ima indeks u istoj godini ILI na istom studijskom programu
        if (studentIndeksRepository.existsByStudentIdAndGodinaOrStudProgramOznakaAndIdNot(
                request.getStudentId(), 
                request.getGodina(), 
                request.getStudProgramOznaka(),
                null)) {
            throw new RuntimeException("Student sa ID " + request.getStudentId() + 
                " vec ima indeks u godini " + request.getGodina() + 
                " ili na studijskom programu " + request.getStudProgramOznaka() + 
                ". Student ne moze imati vise od jednog indeksa u istoj godini niti na istom studijskom programu.");
        }
    }

    @Transactional(readOnly = true)
    public int findNextBroj(int godina, String studProgramOznaka) {
        List<Integer> brojeviList = studentIndeksRepository.findBrojeviByGodinaAndStudProgramOznaka(godina, studProgramOznaka);
        return findNextAvailableNumber(brojeviList);
    }

    private int findNextAvailableNumber(List<Integer> brojeviList) {
        if (brojeviList == null || brojeviList.isEmpty()) {
            return 1;
        }

        List<Integer> sorted = brojeviList.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        int expected = 1;
        for (int num : sorted) {
            if (num != expected) {
                return expected;
            }
            expected++;
        }
        return expected;
    }

    public StudentIndeksResponse getStudentIndeksById(Long id) {
        return studentIndeksRepository.findById(id)
                .map(entityMappers::fromStudentIndexToResponse)
                .orElse(null);
    }

    public List<StudentIndeksResponse> getIndeksiForStudentPodaciId(Long idStudentPodaci) {
        return studentIndeksRepository.findStudentIndeksiForStudentPodaciId(idStudentPodaci).stream()
                .map(entityMappers::fromStudentIndexToResponse)
                .collect(Collectors.toList());
    }

    // 2.1 - pronalaženje studenta preko skraćenog broja indeksa (npr. "RN1923")
    public StudentIndeksResponse fastSearch(String indeksShort) {
        String[] parsedData = ParseUtils.parseIndeks(indeksShort);
        if (parsedData == null) {
            throw new IllegalArgumentException("Neispravan format indeksa: " + indeksShort);
        }
        StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000 + Integer.parseInt(parsedData[1]), Integer.parseInt(parsedData[2]));
        if (si == null) {
            throw new EntityNotFoundException("Student sa indeksom " + indeksShort + " ne postoji");
        }
        return entityMappers.fromStudentIndexToResponse(si);
    }

    // 2.2 - pronalaženje studenta preko email-a
    public StudentIndeksResponse emailSearch(String studEmail) {
        String[] parsedData = ParseUtils.parseEmail(studEmail);
        if (parsedData == null) {
            throw new IllegalArgumentException("Neispravan format emaila: " + studEmail);
        }
        StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000 + Integer.parseInt(parsedData[1]), Integer.parseInt(parsedData[2]));
        if (si == null) {
            throw new EntityNotFoundException("Student sa emailom " + studEmail + " ne postoji");
        }
        return entityMappers.fromStudentIndexToResponse(si);
    }

    // 7 - selekcija studenata na osnovu imena i/ili prezimena (može samo ime, ili samo prezime ili oba da se unesu), paginirano
    public Page<StudentDTO> search(String ime, String prezime, String studProgram, Integer godina, Integer broj, Integer page, Integer size) {
        Page<StudentIndeks> siList = studentIndeksRepository.findStudentIndeks(ime, prezime, studProgram, godina, broj, PageRequest.of(page, size, Sort.by("id").descending()));
        return siList.map(EntityMappers::fromStudentIndeksToDTO);
    }

    public StudentIndeks findByStudentIdAndAktivan(Long studentPodaciId) {
        return studentIndeksRepository.findAktivanStudentIndeksiByStudentPodaciId(studentPodaciId);
    }

    /**
     * Brise StudentIndeks i sve zavisne entitete (kaskadno brisanje).
     */
    @Transactional
    public void deleteStudentIndeks(Long id) {
        if (!studentIndeksRepository.existsById(id)) {
            throw new EntityNotFoundException("StudentIndeks sa ID " + id + " ne postoji");
        }
        
        // Prvo obrisi sve zavisne entitete
        deleteStudentIndeksDependencies(id);
        
        // Zatim obrisi sam indeks
        studentIndeksRepository.deleteById(id);
    }

    /**
     * Aktivira indeks sa datim ID-om i deaktivira sve ostale indekse istog studenta.
     * Samo jedan indeks moze biti aktivan u isto vreme.
     */
    @Transactional
    public void aktivirajIndeks(Long studentIndeksId) {
        StudentIndeks indeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new EntityNotFoundException("StudentIndeks sa ID " + studentIndeksId + " ne postoji"));
        
        // Dohvati sve indekse ovog studenta
        List<StudentIndeks> sviIndeksi = studentIndeksRepository.findAllByStudentId(indeks.getStudent().getId());
        
        // Deaktiviraj sve indekse
        for (StudentIndeks si : sviIndeksi) {
            si.setAktivan(false);
        }
        studentIndeksRepository.saveAll(sviIndeksi);
        
        // Aktiviraj trazeni indeks
        indeks.setAktivan(true);
        studentIndeksRepository.save(indeks);
    }

    /**
     * Returns optimized data for student reports.
     * Single query that fetches ime, prezime, and godinaUpisa (from first UpisGodine).
     */
    @Transactional(readOnly = true)
    public List<org.raflab.studsluzba.dtos.StudentReportDTO> getStudentsForReport() {
        return upisGodineRepository.findAllStudentsForReport();
    }
}

