package com.postechhackaton.relatorios.infra.gateway;

import com.postechhackaton.relatorios.application.mappers.PontoEletronicoMapper;
import com.postechhackaton.relatorios.domain.gateways.PontoEletronicoDatabaseGateway;
import com.postechhackaton.relatorios.infra.database.entities.PontoEletronico;
import com.postechhackaton.relatorios.infra.database.repositories.PontoEletronicoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PontoEletronicoDatabaseGatewayImpl implements PontoEletronicoDatabaseGateway {

    private final PontoEletronicoRepository pontoEletronicoRepository;

    public PontoEletronicoDatabaseGatewayImpl(PontoEletronicoRepository pontoEletronicoRepository, PontoEletronicoMapper pontoEletronicoMapper) {
        this.pontoEletronicoRepository = pontoEletronicoRepository;
    }

    @Override
    public List<PontoEletronico> findByUsuarioAndDataBetween(String usuario, LocalDateTime inicioDia, LocalDateTime fimDia) {
        return pontoEletronicoRepository.findByUsuarioAndDataBetween(usuario, inicioDia, fimDia);
    }
}
