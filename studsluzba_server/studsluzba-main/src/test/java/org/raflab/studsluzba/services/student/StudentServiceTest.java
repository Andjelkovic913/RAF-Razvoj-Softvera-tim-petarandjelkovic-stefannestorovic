package org.raflab.studsluzba.services.student;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.services.StudentService;
import org.raflab.studsluzba.utils.EntityMappers;

import jakarta.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test klasa za StudentService - testira sve operacije vezane za studente
 * 
 * STRUKTURA TESTA:
 * 1. @ExtendWith(MockitoExtension.class) - omogućava korišćenje Mockito anotacija
 * 2. @Mock - kreira mock objekat za zavisnosti (repository, mapper, itd.)
 * 3. @InjectMocks - automatski injektuje mock zavisnosti u service
 * 4. @Test - označava metodu kao test
 * 5. @DisplayName - prikazuje lepši naziv testa u rezultatima
 * 
 * KAKO FUNKCIONIŠU TESTOVI:
 * - Arrange: Postavi mock podatke (when(...).thenReturn(...))
 * - Act: Pozovi metodu koju testiram
 * - Assert: Proveri rezultat (assertEquals, assertNotNull, itd.)
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentIndeksRepository studentIndeksRepository;

    @Mock
    private EntityMappers entityMappers;

    // ========== SERVICE ZA TESTIRANJE ==========
    // @InjectMocks automatski injektuje sve @Mock zavisnosti u ovaj service
    // Sada možemo testirati StudentService bez stvarne baze podataka
    @InjectMocks
    private StudentService studentService;

    // ========== TESTOVI ZA 1.1: SELEKCIJA STUDENTA PREKO BROJA INDEKSA ==========
    // Specifikacija: Pronalaženje studenta preko broja indeksa (npr. "RN1923")
    // Metoda: studentService.fastSearch(String indeksShort)
    // Koristi ParseUtils.parseIndeks() za parsiranje indeksa

    /**
     * TEST 1: Pronalaženje studenta sa validnim indeksom
     * 
     * SCENARIO: Korisnik traži studenta sa indeksom "RN1923"
     * - Indeks se parsira u [RN, 19, 23]
     * - Pronalazi se student u bazi
     * - Vraća se StudentIndeksResponse sa podacima
     * 
     * OČEKIVANI REZULTAT: StudentIndeksResponse sa ID-om 1L
     */
    @Test
    @DisplayName("1.1.1 - Pronalaženje studenta sa validnim indeksom")
    void findByIndeks_validIndeks() {
        // ===== ARRANGE (Priprema) =====
        // Ulazni parametar - indeks koji tražimo
        String indeksShort = "RN1923";
        
        // Kreiramo mock StudentIndeks objekat sa test podacima
        StudentIndeks mockStudentIndeks = new StudentIndeks();
        mockStudentIndeks.setId(1L);
        mockStudentIndeks.setBroj(23);
        mockStudentIndeks.setGodina(2019);
        mockStudentIndeks.setStudProgramOznaka("RN");
        mockStudentIndeks.setAktivan(true);
        
        // Kreiramo mock StudentIndeksResponse koji će biti vraćen
        StudentIndeksResponse mockResponse = new StudentIndeksResponse();
        mockResponse.setId(1L);
        mockResponse.setBroj(23);
        mockResponse.setGodina(2019);
        mockResponse.setStudProgramOznaka("RN");
        mockResponse.setAktivan(true);
        
        // Postavljamo ponašanje mock objekata:
        // Kada se pozove findStudentIndeks sa parametrima ("RN", 2019, 23)
        // Vraćamo mockStudentIndeks
        when(studentIndeksRepository.findStudentIndeks("RN", 2019, 23))
                .thenReturn(mockStudentIndeks);
        
        // Kada se pozove mapper za konverziju entiteta u response
        // Vraćamo mockResponse
        when(entityMappers.fromStudentIndexToResponse(mockStudentIndeks))
                .thenReturn(mockResponse);

        // ===== ACT (Izvršavanje) =====
        // Pozivamo metodu koju testiram
        StudentIndeksResponse result = studentService.fastSearch(indeksShort);

        // ===== ASSERT (Provjera) =====
        // Proveravamo da li je rezultat null
        assertNotNull(result, "Rezultat ne sme biti null");
        
        // Proveravamo da li je ID ispravan
        assertEquals(1L, result.getId(), "ID studenta mora biti 1L");
        
        // Proveravamo da li je broj indeksa ispravan
        assertEquals(23, result.getBroj(), "Broj indeksa mora biti 23");
        
        // Proveravamo da li je godina indeksa ispravan
        assertEquals(2019, result.getGodina(), "Godina indeksa mora biti 2019");
        
        // Proveravamo da li je studijski program ispravan
        assertEquals("RN", result.getStudProgramOznaka(), "Studijski program mora biti RN");
        
        // Proveravamo da li je indeks aktivan
        assertTrue(result.isAktivan(), "Indeks mora biti aktivan");
        
        // Proveravamo da je repository pozvan tačno jednom sa očekivanim parametrima
        verify(studentIndeksRepository, times(1)).findStudentIndeks("RN", 2019, 23);
        
        // Proveravamo da je mapper pozvan tačno jednom
        verify(entityMappers, times(1)).fromStudentIndexToResponse(mockStudentIndeks);
    }

    /**
     * TEST 2: Pokušaj pronalaženja studenta sa nepostojeći indeksom
     * 
     * SCENARIO: Korisnik traži studenta sa indeksom koji ne postoji
     * - Indeks se parsira u [RN, 19, 23]
     * - Repository vraća null (student ne postoji)
     * - Servis baca EntityNotFoundException
     * 
     * OČEKIVANI REZULTAT: Baca se EntityNotFoundException sa porukom
     */
    @Test
    @DisplayName("1.1.2 - Pronalaženje nepostojeći studenta baca exception")
    void findByIndeks_invalidIndeks() {
        // ===== ARRANGE =====
        String indeksShort = "RN9999"; // Indeks koji ne postoji
        
        // Postavljamo da repository vraća null (student ne postoji)
        when(studentIndeksRepository.findStudentIndeks("RN", 2099, 99))
                .thenReturn(null);

        // ===== ACT & ASSERT =====
        // Očekujemo da će biti bačena EntityNotFoundException
        // assertThrows proverava da li je exception bačen
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentService.fastSearch(indeksShort),
                "Trebalo bi baciti EntityNotFoundException za nepostojeći indeks"
        );
        
        // Proveravamo da poruka exception-a sadrži indeks
        assertTrue(
                exception.getMessage().contains("RN9999"),
                "Poruka exception-a mora sadržati indeks koji je tražen"
        );
        
        // Proveravamo da je repository pozvan
        verify(studentIndeksRepository, times(1)).findStudentIndeks("RN", 2099, 99);
    }

    /**
     * TEST 3: Case-insensitive pretraga (mala/velika slova)
     * 
     * SCENARIO: Korisnik unese indeks sa malim slovima "rn1923"
     * - ParseUtils.parseIndeks() konvertuje u velika slova
     * - Pronalazi se student sa "RN1923"
     * - Vraća se StudentIndeksResponse
     * 
     * OČEKIVANI REZULTAT: Pronalazi se student bez obzira na veličinu slova
     */
    @Test
    @DisplayName("1.1.3 - Case-insensitive pretraga (mala/velika slova)")
    void findByIndeks_caseInsensitive() {
        // ===== ARRANGE =====
        // Korisnik unese indeks sa malim slovima
        String indeksShort = "rn1923";
        
        // ParseUtils.parseIndeks() će konvertovati u [RN, 19, 23]
        // Kreiramo mock podatke
        StudentIndeks mockStudentIndeks = new StudentIndeks();
        mockStudentIndeks.setId(2L);
        mockStudentIndeks.setBroj(23);
        mockStudentIndeks.setGodina(2019);
        mockStudentIndeks.setStudProgramOznaka("RN");
        
        StudentIndeksResponse mockResponse = new StudentIndeksResponse();
        mockResponse.setId(2L);
        mockResponse.setBroj(23);
        mockResponse.setGodina(2019);
        mockResponse.setStudProgramOznaka("RN");
        
        // Postavljamo mock ponašanje
        // Napomena: ParseUtils.parseIndeks() će konvertovati "rn1923" u [RN, 19, 23]
        when(studentIndeksRepository.findStudentIndeks("RN", 2019, 23))
                .thenReturn(mockStudentIndeks);
        
        when(entityMappers.fromStudentIndexToResponse(mockStudentIndeks))
                .thenReturn(mockResponse);

        // ===== ACT =====
        StudentIndeksResponse result = studentService.fastSearch(indeksShort);

        // ===== ASSERT =====
        assertNotNull(result, "Trebalo bi pronači studenta čak i sa malim slovima");
        assertEquals(2L, result.getId(), "ID mora biti 2L");
        assertEquals("RN", result.getStudProgramOznaka(), "Studijski program mora biti RN");
        
        // Proveravamo da je repository pozvan sa VELIKIM slovima
        verify(studentIndeksRepository, times(1)).findStudentIndeks("RN", 2019, 23);
    }

    /**
     * TEST 4: Neispravan format indeksa
     * 
     * SCENARIO: Korisnik unese indeks sa neispravnim formatom
     * - ParseUtils.parseIndeks() vraća null (format nije validan)
     * - Servis baca IllegalArgumentException
     * 
     * OČEKIVANI REZULTAT: Baca se IllegalArgumentException
     */
    @Test
    @DisplayName("1.1.4 - Neispravan format indeksa baca exception")
    void findByIndeks_invalidFormat() {
        // ===== ARRANGE =====
        // Indeks sa neispravnim formatom (premalo karaktera)
        String indeksShort = "RN19"; // Trebalo bi RN1923 (minimum 5 karaktera)

        // ===== ACT & ASSERT =====
        // Očekujemo IllegalArgumentException
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.fastSearch(indeksShort),
                "Trebalo bi baciti IllegalArgumentException za neispravan format"
        );
        
        // Proveravamo poruku
        assertTrue(
                exception.getMessage().contains("Neispravan format"),
                "Poruka mora sadržati 'Neispravan format'"
        );
        
        // Proveravamo da repository NIJE pozvan (jer je format neispravan)
        verify(studentIndeksRepository, never()).findStudentIndeks(anyString(), anyInt(), anyInt());
    }

    /**
     * TEST 5: Pronalaženje neaktivnog indeksa
     * 
     * SCENARIO: Pronalazi se student čiji indeks nije aktivan
     * - Indeks se parsira u [RN, 19, 23]
     * - Pronalazi se student sa aktivan=false
     * - Vraća se StudentIndeksResponse sa aktivan=false
     * 
     * OČEKIVANI REZULTAT: Pronalazi se student čak i ako indeks nije aktivan
     */
    @Test
    @DisplayName("1.1.5 - Pronalaženje neaktivnog indeksa")
    void findByIndeks_inactiveIndex() {
        // ===== ARRANGE =====
        String indeksShort = "RN1923";
        
        // Kreiramo mock sa neaktivnim indeksom
        StudentIndeks mockStudentIndeks = new StudentIndeks();
        mockStudentIndeks.setId(3L);
        mockStudentIndeks.setBroj(23);
        mockStudentIndeks.setGodina(2019);
        mockStudentIndeks.setStudProgramOznaka("RN");
        mockStudentIndeks.setAktivan(false); // NEAKTIVAN
        
        StudentIndeksResponse mockResponse = new StudentIndeksResponse();
        mockResponse.setId(3L);
        mockResponse.setAktivan(false);
        
        when(studentIndeksRepository.findStudentIndeks("RN", 2019, 23))
                .thenReturn(mockStudentIndeks);
        
        when(entityMappers.fromStudentIndexToResponse(mockStudentIndeks))
                .thenReturn(mockResponse);

        // ===== ACT =====
        StudentIndeksResponse result = studentService.fastSearch(indeksShort);

        // ===== ASSERT =====
        assertNotNull(result, "Trebalo bi pronači studenta čak i ako je indeks neaktivan");
        assertFalse(result.isAktivan(), "Indeks mora biti neaktivan");
    }

    // ========== TESTOVI ZA EMAIL SEARCH ==========

    /**
     * TEST 6: Pronalaženje studenta preko email-a
     * 
     * SCENARIO: Korisnik traži studenta preko email-a "ppetrovic1220rn@raf.rs"
     * - Email se parsira u [rn, 20, 12]
     * - Pronalazi se student u bazi
     * - Vraća se StudentIndeksResponse
     * 
     * OČEKIVANI REZULTAT: StudentIndeksResponse sa ispravnim podacima
     */
    @Test
    @DisplayName("1.1.6 - Pronalaženje studenta preko email-a")
    void emailSearch_validEmail() {
        // ARRANGE
        String email = "ppetrovic1220rn@raf.rs";
        // Email se parsira u: [rn, 20, 12]
        
        StudentIndeks mockStudentIndeks = new StudentIndeks();
        mockStudentIndeks.setId(10L);
        mockStudentIndeks.setBroj(12);
        mockStudentIndeks.setGodina(2020);
        mockStudentIndeks.setStudProgramOznaka("RN");
        
        StudentIndeksResponse mockResponse = new StudentIndeksResponse();
        mockResponse.setId(10L);
        mockResponse.setBroj(12);
        mockResponse.setGodina(2020);
        mockResponse.setStudProgramOznaka("RN");
        
        when(studentIndeksRepository.findStudentIndeks("rn", 2020, 12))
                .thenReturn(mockStudentIndeks);
        
        when(entityMappers.fromStudentIndexToResponse(mockStudentIndeks))
                .thenReturn(mockResponse);

        // ACT
        StudentIndeksResponse result = studentService.emailSearch(email);

        // ASSERT
        assertNotNull(result, "Rezultat ne sme biti null");
        assertEquals(10L, result.getId(), "ID mora biti 10L");
        assertEquals(12, result.getBroj(), "Broj mora biti 12");
        assertEquals(2020, result.getGodina(), "Godina mora biti 2020");
        assertEquals("RN", result.getStudProgramOznaka(), "Program mora biti RN");
        
        verify(studentIndeksRepository, times(1)).findStudentIndeks("rn", 2020, 12);
    }

    /**
     * TEST 7: Pronalaženje nepostojeći studenta preko email-a
     */
    @Test
    @DisplayName("1.1.7 - Pronalaženje nepostojeći studenta preko email-a baca exception")
    void emailSearch_studentNotFound() {
        // ARRANGE
        String email = "nepostoji9999rn@raf.rs";
        
        when(studentIndeksRepository.findStudentIndeks("rn", 2099, 99))
                .thenReturn(null);

        // ACT & ASSERT
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentService.emailSearch(email),
                "Trebalo bi baciti EntityNotFoundException"
        );
        
        assertTrue(
                exception.getMessage().contains(email),
                "Poruka mora sadržati email"
        );
    }

    /**
     * TEST 8: Neispravan format email-a
     */
    @Test
    @DisplayName("1.1.8 - Neispravan format email-a baca exception")
    void emailSearch_invalidFormat() {
        // ARRANGE
        String email = "invalid@example.com"; // Nije @raf.rs

        // ACT & ASSERT
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.emailSearch(email),
                "Trebalo bi baciti IllegalArgumentException"
        );
        
        assertTrue(
                exception.getMessage().contains("Neispravan format"),
                "Poruka mora sadržati 'Neispravan format'"
        );
        
        verify(studentIndeksRepository, never()).findStudentIndeks(anyString(), anyInt(), anyInt());
    }
}

