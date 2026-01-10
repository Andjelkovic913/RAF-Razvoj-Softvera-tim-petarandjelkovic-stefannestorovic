package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import org.raflab.studsluzba.model.PredispitneObaveze;

@Data
public class PredispitneObavezeResponse {
    private Long id;
    private PredispitneObaveze.Vrsta vrsta;
    private int maxBodova;
    private int osvojeniBodovi;
    private Long slusaPredmetId;
    private Long skolskaGodinaId;
}
