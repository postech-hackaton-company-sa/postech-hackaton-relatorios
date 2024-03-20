package com.postechhackaton.relatorios.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PontoCalculadoDto {
    private String status;

    private LocalDate data;

    private List<PontoEletronicoDto> registros;

    private long totalHorasTrabalhadas;
}
