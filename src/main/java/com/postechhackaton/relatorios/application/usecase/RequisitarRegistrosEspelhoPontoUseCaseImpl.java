package com.postechhackaton.relatorios.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postechhackaton.relatorios.application.dto.QueryEspelhoPontoDto;
import com.postechhackaton.relatorios.domain.gateways.KafkaSenderGateway;
import com.postechhackaton.relatorios.domain.usecase.RequisitarRegistrosEspelhoPontoUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RequisitarRegistrosEspelhoPontoUseCaseImpl implements RequisitarRegistrosEspelhoPontoUseCase {

    public static final String TOPIC_BUSCAR_ESPELHO_PONTO_INPUT = "postech-hackaton-buscar-espelho-ponto-in";
    private final KafkaSenderGateway kafkaSenderGateway;
    private final ObjectMapper objectMapper;

    public RequisitarRegistrosEspelhoPontoUseCaseImpl(KafkaSenderGateway kafkaSenderGateway, ObjectMapper objectMapper) {
        this.kafkaSenderGateway = kafkaSenderGateway;
        this.objectMapper = objectMapper;
    }

    @Override
    public void requisitar(QueryEspelhoPontoDto query) throws Exception {
        String request = objectMapper.writeValueAsString(query);
        kafkaSenderGateway.enviar(TOPIC_BUSCAR_ESPELHO_PONTO_INPUT, request);
    }
}
