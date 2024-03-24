package com.postechhackaton.relatorios.application.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postechhackaton.relatorios.application.dto.QueryEspelhoPontoDto;
import com.postechhackaton.relatorios.application.dto.RelatorioPeriodoPontoDto;
import com.postechhackaton.relatorios.business.entities.FuncionarioEntity;
import com.postechhackaton.relatorios.domain.gateways.EmailGateway;
import com.postechhackaton.relatorios.domain.gateways.KafkaSenderGateway;
import com.postechhackaton.relatorios.domain.usecase.BuscarDadosFuncionarioUseCase;
import com.postechhackaton.relatorios.domain.usecase.CalcularPontoPeriodoUseCase;
import com.postechhackaton.relatorios.domain.usecase.FormataRelatorioPeriodoUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReceberEspelhoPontoSubscriber {
    public static final String TOPIC_BUSCAR_ESPELHO_PONTO_OUTPUT = "postech-hackaton-buscar-espelho-ponto-out";
    public static final String TOPIC_BUSCAR_ESPELHO_PONTO_OUTPUT_DLQ = "postech-hackaton-buscar-espelho-ponto-in-out";

    private final ObjectMapper objectMapper;

    private final KafkaSenderGateway kafkaSenderGateway;

    private final CalcularPontoPeriodoUseCase calcularPontoPeriodoUseCase;

    private final FormataRelatorioPeriodoUseCase formataRelatorioPeriodoUseCase;

    private final BuscarDadosFuncionarioUseCase buscarDadosFuncionarioUseCase;

    private final EmailGateway emailGateway;

    public ReceberEspelhoPontoSubscriber(ObjectMapper objectMapper,
                                         KafkaSenderGateway kafkaSenderGateway,
                                         CalcularPontoPeriodoUseCase calcularPontoPeriodoUseCase,
                                         FormataRelatorioPeriodoUseCase formataRelatorioPeriodoUseCase,
                                         BuscarDadosFuncionarioUseCase buscarDadosFuncionarioUseCase,
                                         EmailGateway emailGateway) {
        this.objectMapper = objectMapper;
        this.kafkaSenderGateway = kafkaSenderGateway;
        this.calcularPontoPeriodoUseCase = calcularPontoPeriodoUseCase;
        this.formataRelatorioPeriodoUseCase = formataRelatorioPeriodoUseCase;
        this.buscarDadosFuncionarioUseCase = buscarDadosFuncionarioUseCase;
        this.emailGateway = emailGateway;
    }


    @KafkaListener(topics = TOPIC_BUSCAR_ESPELHO_PONTO_OUTPUT, groupId = "hackaton-group-relatorios")
    public RelatorioPeriodoPontoDto executar(String json) {
         try {
             log.debug("Received Message: " + json);
             QueryEspelhoPontoDto resposta = objectMapper.readValue(json, QueryEspelhoPontoDto.class);
             FuncionarioEntity funcionario = buscarDadosFuncionarioUseCase.buscar(resposta.getUsuarioRequerinte(), resposta.getUsuario());
             RelatorioPeriodoPontoDto relatorio = calcularPontoPeriodoUseCase.execute(resposta.getResposta());
             String emailFormatado = formataRelatorioPeriodoUseCase.formatar(relatorio, funcionario);
             emailGateway.enviar(resposta.getEmail(), emailFormatado);
             return relatorio;
         } catch (Exception e) {
            log.error("Erro ao processar a mensagem JSON: " + e.getMessage());
            kafkaSenderGateway.enviar(TOPIC_BUSCAR_ESPELHO_PONTO_OUTPUT_DLQ, json);
            return null;
        }
    }
}
