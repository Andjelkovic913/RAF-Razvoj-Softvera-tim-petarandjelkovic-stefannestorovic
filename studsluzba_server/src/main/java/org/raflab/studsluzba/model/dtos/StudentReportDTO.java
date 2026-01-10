package org.raflab.studsluzba.model.dtos;

/**
 * Lightweight DTO for report generation.
 * Contains only the fields needed for the sviStudenti report.
 */
public class StudentReportDTO {
    
    private String ime;
    private String prezime;
    private Integer godinaUpisa;

    public StudentReportDTO() {}

    public StudentReportDTO(String ime, String prezime, Integer godinaUpisa) {
        this.ime = ime;
        this.prezime = prezime;
        this.godinaUpisa = godinaUpisa;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Integer getGodinaUpisa() {
        return godinaUpisa;
    }

    public void setGodinaUpisa(Integer godinaUpisa) {
        this.godinaUpisa = godinaUpisa;
    }
}
