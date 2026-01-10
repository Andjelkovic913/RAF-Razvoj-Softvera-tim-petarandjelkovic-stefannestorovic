package org.raflab.studsluzba.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentPaymentDTO {
    private Long id;
    private LocalDate datumUplate;
    private BigDecimal iznos;
    private String svrhaUplate;
    private String skolskaGodina;
    private String nacinPlacanja;
    private String status;
}
