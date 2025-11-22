package org.raflab.studsluzba.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDTO {

    private String code;
    private String date;
    private Integer number;
    private Integer parity;

    @JsonProperty("cash_buy")
    private Double cashBuy;

    @JsonProperty("cash_sell")
    private Double cashSell;

    @JsonProperty("exchange_buy")
    private Double exchangeBuy;

    @JsonProperty("exchange_middle")
    private Double exchangeMiddle;

    @JsonProperty("exchange_sell")
    private Double exchangeSell;
}
