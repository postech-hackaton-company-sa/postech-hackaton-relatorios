package com.postechhackaton.relatorios.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioPeriodoPontoDto {

    private String usuario;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private long totalHorasTrabalhadas;

    private List<PontoCalculadoDto> registros;

}
