package com.postechhackaton.relatorios.application.usecase;

import com.postechhackaton.relatorios.application.dto.PontoCalculadoDto;
import com.postechhackaton.relatorios.application.dto.RelatorioPeriodoPontoDto;
import com.postechhackaton.relatorios.business.exceptions.NotFoundException;
import com.postechhackaton.relatorios.domain.gateways.PontoEletronicoDatabaseGateway;
import com.postechhackaton.relatorios.domain.usecase.CalcularPontoDiarioUseCase;
import com.postechhackaton.relatorios.domain.usecase.CalcularPontoPeriodoUseCase;
import com.postechhackaton.relatorios.infra.database.entities.PontoEletronico;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class CalcularPontoPeriodoUseCaseImpl implements CalcularPontoPeriodoUseCase {
    private final PontoEletronicoDatabaseGateway pontoEletronicoDatabaseGateway;
    private final CalcularPontoDiarioUseCase calcularPontoDiarioUseCase;

    public CalcularPontoPeriodoUseCaseImpl(PontoEletronicoDatabaseGateway pontoEletronicoDatabaseGateway, CalcularPontoDiarioUseCase calcularPontoDiarioUseCase) {
        this.pontoEletronicoDatabaseGateway = pontoEletronicoDatabaseGateway;
        this.calcularPontoDiarioUseCase = calcularPontoDiarioUseCase;
    }

    public RelatorioPeriodoPontoDto execute(String usuario, LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime dataInicioRegistro = dataInicio.atStartOfDay();
        LocalDateTime dataFimRegistro = dataFim.plusDays(1).atStartOfDay();
        List<PontoEletronico> registros = pontoEletronicoDatabaseGateway.findByUsuarioAndDataBetween(usuario, dataInicioRegistro, dataFimRegistro);

        if (registros == null || registros.isEmpty()) {
            throw new NotFoundException("Nenhum registro encontrado");
        }

        Map<LocalDate, List<PontoEletronico>> registrosPorDia = registros.stream()
                .collect(Collectors.groupingBy(registro -> registro.getData().toLocalDate()));

        AtomicLong horasTrabalhadas = new AtomicLong();
        List<PontoCalculadoDto> pontos = new ArrayList<>();
        registrosPorDia.forEach((data, registrosDoDia) -> {
            PontoCalculadoDto pontoCalculadoDto = calcularPontoDiarioUseCase.execute(usuario, registrosDoDia);
            horasTrabalhadas.addAndGet(pontoCalculadoDto.getTotalHorasTrabalhadas());
            pontos.add(pontoCalculadoDto);
        });

        return RelatorioPeriodoPontoDto.builder()
                .dataInicio(dataInicio)
                .usuario(usuario)
                .totalHorasTrabalhadas(horasTrabalhadas.get())
                .dataFim(dataFim)
                .registros(pontos)
                .build();
    }
}
