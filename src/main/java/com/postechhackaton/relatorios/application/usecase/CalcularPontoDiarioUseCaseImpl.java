package com.postechhackaton.relatorios.application.usecase;

import com.postechhackaton.relatorios.application.dto.PontoCalculadoDto;
import com.postechhackaton.relatorios.application.mappers.PontoEletronicoMapper;
import com.postechhackaton.relatorios.domain.gateways.PontoEletronicoDatabaseGateway;
import com.postechhackaton.relatorios.domain.usecase.CalcularPontoDiarioUseCase;
import com.postechhackaton.relatorios.infra.database.entities.PontoEletronico;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
public class CalcularPontoDiarioUseCaseImpl implements CalcularPontoDiarioUseCase {

    private final PontoEletronicoDatabaseGateway pontoEletronicoDatabaseGateway;
    private final PontoEletronicoMapper pontoEletronicoMapper;

    public CalcularPontoDiarioUseCaseImpl(PontoEletronicoDatabaseGateway pontoEletronicoDatabaseGateway, PontoEletronicoMapper pontoEletronicoMapper) {
        this.pontoEletronicoDatabaseGateway = pontoEletronicoDatabaseGateway;
        this.pontoEletronicoMapper = pontoEletronicoMapper;
    }

    @Override
    public PontoCalculadoDto execute(String usuario, List<PontoEletronico> registros) {
        assert registros != null;
        assert !registros.isEmpty();

        LocalDate data = registros.get(0).getData().toLocalDate();

        if (registros.size() % 2 != 0) {
            var listaRegistros = pontoEletronicoMapper.toDtoList(registros);
            return new PontoCalculadoDto("inconsistente", data, listaRegistros, 0);
        }

        registros.sort(Comparator.comparing(PontoEletronico::getData));

        long totalHorasTrabalhadas = 0;
        LocalDateTime ultimaSaida = null;

        for (int i = 0; i < registros.size(); i += 2) {
            LocalDateTime entrada = registros.get(i).getData();
            LocalDateTime saida = registros.get(i + 1).getData();

            if (ultimaSaida != null && entrada.isBefore(ultimaSaida)) {
                var listaRegistros = pontoEletronicoMapper.toDtoList(registros);
                return new PontoCalculadoDto("inconsistente", data, listaRegistros, totalHorasTrabalhadas);
            }

            Duration duracao = Duration.between(entrada, saida);
            totalHorasTrabalhadas += duracao.toHours();

            ultimaSaida = saida;
        }

        var listaRegistros = pontoEletronicoMapper.toDtoList(registros);
        if (totalHorasTrabalhadas < 8) {
            return new PontoCalculadoDto("negativo", data, listaRegistros, totalHorasTrabalhadas);
        }
        return new PontoCalculadoDto("positivo", data, listaRegistros, totalHorasTrabalhadas);

    }
}
