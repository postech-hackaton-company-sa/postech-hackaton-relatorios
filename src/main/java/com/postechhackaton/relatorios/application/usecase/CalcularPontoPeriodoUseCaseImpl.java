package com.postechhackaton.relatorios.application.usecase;

import com.postechhackaton.relatorios.application.dto.PontoCalculadoDto;
import com.postechhackaton.relatorios.application.dto.RelatorioPeriodoPontoDto;
import com.postechhackaton.relatorios.business.entities.PontoEletronicoEntity;
import com.postechhackaton.relatorios.domain.usecase.CalcularPontoDiarioUseCase;
import com.postechhackaton.relatorios.domain.usecase.CalcularPontoPeriodoUseCase;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class CalcularPontoPeriodoUseCaseImpl implements CalcularPontoPeriodoUseCase {

    private final CalcularPontoDiarioUseCase calcularPontoDiarioUseCase;

    public CalcularPontoPeriodoUseCaseImpl(CalcularPontoDiarioUseCase calcularPontoDiarioUseCase) {
        this.calcularPontoDiarioUseCase = calcularPontoDiarioUseCase;
    }

    public RelatorioPeriodoPontoDto execute(List<PontoEletronicoEntity> registros) {
        assert registros != null;
        assert !registros.isEmpty();

        Map<LocalDate, List<PontoEletronicoEntity>> registrosPorDia = registros.stream()
                .sorted(Comparator.comparing(registro -> registro.getData().toLocalDate()))
                .collect(Collectors.groupingBy(registro -> registro.getData().toLocalDate()));

        AtomicLong horasTrabalhadas = new AtomicLong();
        List<PontoCalculadoDto> pontos = new ArrayList<>();
        registrosPorDia.forEach((data, registrosDoDia) -> {
            PontoCalculadoDto pontoCalculadoDto = calcularPontoDiarioUseCase.execute(registrosDoDia);
            horasTrabalhadas.addAndGet(pontoCalculadoDto.getTotalHorasTrabalhadas());
            pontos.add(pontoCalculadoDto);
        });

        String usuario = registros.get(0).getUsuario();
        LocalDate dataInicio = registrosPorDia.keySet().stream()
                .findFirst()
                .orElse(null);

        LocalDate dataFim = registrosPorDia.keySet().stream()
                .reduce((first, second) -> second)
                .orElse(null);

        return RelatorioPeriodoPontoDto.builder()
                .dataInicio(dataInicio)
                .usuario(usuario)
                .dataFim(dataFim)
                .totalHorasTrabalhadas(horasTrabalhadas.get())
                .registros(pontos)
                .build();
    }

}
