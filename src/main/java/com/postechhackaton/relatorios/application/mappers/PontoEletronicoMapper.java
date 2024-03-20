package com.postechhackaton.relatorios.application.mappers;

import com.postechhackaton.relatorios.application.dto.PontoEletronicoDto;
import com.postechhackaton.relatorios.business.entities.PontoEletronicoEntity;
import com.postechhackaton.relatorios.infra.database.entities.PontoEletronico;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PontoEletronicoMapper {

    PontoEletronicoEntity toEntity(PontoEletronico pontoEletronico);

    List<PontoEletronicoDto> toDtoList(List<PontoEletronico> registros);
}
