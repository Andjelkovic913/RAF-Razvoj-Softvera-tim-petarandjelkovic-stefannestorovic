package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreostaliIznosResponse {

    private Double preostaloEUR;
    private Double preostaloRSD;
    private Double ukupnoUplacenoEUR;
    private Double ukupnoUplacenoRSD;

}

