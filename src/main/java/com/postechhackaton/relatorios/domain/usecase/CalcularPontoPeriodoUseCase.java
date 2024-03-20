package com.postechhackaton.relatorios.domain.usecase;

import com.postechhackaton.relatorios.application.dto.RelatorioPeriodoPontoDto;

import java.time.LocalDate;

public interface CalcularPontoPeriodoUseCase {

    RelatorioPeriodoPontoDto execute(String usuario, LocalDate dataInicio, LocalDate dataFim);

}
