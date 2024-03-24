package com.postechhackaton.relatorios.domain.usecase;

import com.postechhackaton.relatorios.application.dto.RelatorioPeriodoPontoDto;
import com.postechhackaton.relatorios.business.entities.FuncionarioEntity;

public interface FormataRelatorioPeriodoUseCase {

    String formatar(RelatorioPeriodoPontoDto relatorio, FuncionarioEntity funcionario);
}
