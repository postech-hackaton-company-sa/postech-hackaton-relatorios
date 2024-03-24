package com.postechhackaton.relatorios.domain.usecase;

import com.postechhackaton.relatorios.business.entities.FuncionarioEntity;

public interface BuscarDadosFuncionarioUseCase {
    FuncionarioEntity buscar(String usuarioRequerinte, String usuarioBusca);
}
