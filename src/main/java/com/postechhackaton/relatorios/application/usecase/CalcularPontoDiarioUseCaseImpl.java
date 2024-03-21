package com.postechhackaton.relatorios.application.usecase;

import com.postechhackaton.relatorios.application.dto.PontoCalculadoDto;
import com.postechhackaton.relatorios.business.entities.PontoEletronicoEntity;
import com.postechhackaton.relatorios.business.enums.StatusPonto;
import com.postechhackaton.relatorios.domain.usecase.CalcularPontoDiarioUseCase;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
public class CalcularPontoDiarioUseCaseImpl implements CalcularPontoDiarioUseCase {

    @Override
    public PontoCalculadoDto execute(List<PontoEletronicoEntity> registros) {
        assert registros != null;
        assert !registros.isEmpty();

        LocalDate data = registros.get(0).getData().toLocalDate();

        if (registros.size() % 2 != 0) {
            return new PontoCalculadoDto(StatusPonto.INCONSISTENTE, data, registros, 0);
        }

        registros.sort(Comparator.comparing(PontoEletronicoEntity::getData));

        long totalHorasTrabalhadas = 0;
        LocalDateTime ultimaSaida = null;

        for (int i = 0; i < registros.size(); i += 2) {
            LocalDateTime entrada = registros.get(i).getData();
            LocalDateTime saida = registros.get(i + 1).getData();

            if (ultimaSaida != null && entrada.isBefore(ultimaSaida)) {
                return new PontoCalculadoDto(StatusPonto.INCONSISTENTE, data, registros, totalHorasTrabalhadas);
            }

            Duration duracao = Duration.between(entrada, saida);
            totalHorasTrabalhadas += duracao.toHours();

            ultimaSaida = saida;
        }

        if (totalHorasTrabalhadas < 8) {
            return new PontoCalculadoDto(StatusPonto.NEGATIVO, data, registros, totalHorasTrabalhadas);
        }
        return new PontoCalculadoDto(StatusPonto.POSITIVO, data, registros, totalHorasTrabalhadas);

    }
}
