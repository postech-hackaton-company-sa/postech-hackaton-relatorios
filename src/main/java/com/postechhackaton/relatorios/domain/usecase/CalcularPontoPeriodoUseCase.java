package com.postechhackaton.relatorios.domain.usecase;

import com.postechhackaton.relatorios.application.dto.RelatorioPeriodoPontoDto;
import com.postechhackaton.relatorios.business.entities.PontoEletronicoEntity;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

public interface CalcularPontoPeriodoUseCase {

    RelatorioPeriodoPontoDto execute(List<PontoEletronicoEntity> registros);

}
