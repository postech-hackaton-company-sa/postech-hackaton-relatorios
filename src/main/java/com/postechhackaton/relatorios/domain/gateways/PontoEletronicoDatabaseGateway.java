package com.postechhackaton.relatorios.domain.gateways;

import com.postechhackaton.relatorios.infra.database.entities.PontoEletronico;

import java.time.LocalDateTime;
import java.util.List;

public interface PontoEletronicoDatabaseGateway {
    List<PontoEletronico> findByUsuarioAndDataBetween(String usuario, LocalDateTime startDate, LocalDateTime endDate);

}
