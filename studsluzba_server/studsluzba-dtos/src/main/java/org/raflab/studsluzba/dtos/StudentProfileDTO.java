package org.raflab.studsluzba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfileDTO {
    private Long idStudentPodaci;
    private Long idIndeks;
    private String ime;
    private String prezime;
    private String srednjeIme;
    private String jmbg;
    private LocalDate datumRodjenja;
    private String mestoRodjenja;
    private String drzavaRodjenja;
    private String pol;
    private String drzavljanstvo;
    private String nacionalnost;
    private String brojLicneKarte;
    
    private String adresa;
    private String mestoStanovanja;
    private String emailPrivatni;
    private String emailFakultetski;
    private String brojTelefona;
    
    private String studijskiProgram;
    private Integer brojIndeksa;
    private Integer godinaUpisa;
    private String nacinFinansiranja;
    private boolean aktivanIndeks;
    
    private String srednjaSkolaNaziv;
    private String srednjaSkolaMesto;
    private Double uspehSrednjaSkola;
    private Double uspehPrijemni;
    
    private Integer ukupnoEspb;
    private Double prosecnaOcena;
    
    private List<StudentExamDTO> polozeniIspiti;
    private List<StudentExamDTO> nepolozeniIspiti;
    private List<StudentPaymentDTO> uplate;
    private List<StudentEnrollmentDTO> upisi;
    
    private StudentIndeksDTO indeks;
    private List<SlusaPredmetDTO> slusaPredmete;
    private List<PredmetDTO> nepolozeniPredmeti;
    
    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(ime != null ? ime : "");
        if (srednjeIme != null && !srednjeIme.isEmpty()) {
            sb.append(" ").append(srednjeIme);
        }
        sb.append(" ").append(prezime != null ? prezime : "");
        return sb.toString().trim();
    }
    
    public String getBrojIndeksaFormatted() {
        if (studijskiProgram != null && brojIndeksa != null && godinaUpisa != null) {
            return studijskiProgram + " " + brojIndeksa + "/" + godinaUpisa;
        }
        return "";
    }
}
