package com.postechhackaton.relatorios.infra.database.repositories;

import com.postechhackaton.relatorios.infra.database.entities.PontoEletronico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PontoEletronicoRepository extends MongoRepository<PontoEletronico, UUID> {

    List<PontoEletronico> findByUsuarioAndDataBetween(String usuario, LocalDateTime inicioDia, LocalDateTime fimDia);

}
