package com.postechhackaton.relatorios.domain.usecase;

import com.postechhackaton.relatorios.application.dto.PontoCalculadoDto;
import com.postechhackaton.relatorios.infra.database.entities.PontoEletronico;

import java.time.LocalDate;
import java.util.List;

public interface CalcularPontoDiarioUseCase {

    PontoCalculadoDto execute(String usuario, List<PontoEletronico> registros);

}
