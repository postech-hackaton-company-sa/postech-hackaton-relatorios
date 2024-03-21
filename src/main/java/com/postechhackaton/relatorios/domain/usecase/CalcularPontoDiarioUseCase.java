package com.postechhackaton.relatorios.domain.usecase;

import com.postechhackaton.relatorios.application.dto.PontoCalculadoDto;
import com.postechhackaton.relatorios.business.entities.PontoEletronicoEntity;

import java.util.List;

public interface CalcularPontoDiarioUseCase {

    PontoCalculadoDto execute(List<PontoEletronicoEntity> registros);

}
