package com.postechhackaton.relatorios.domain.usecase;

import com.postechhackaton.relatorios.application.dto.QueryEspelhoPontoDto;

public interface RequisitarRegistrosEspelhoPontoUseCase {
    void requisitar(QueryEspelhoPontoDto query) throws Exception;
}
