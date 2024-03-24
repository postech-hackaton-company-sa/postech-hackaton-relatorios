package com.postechhackaton.relatorios.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postechhackaton.relatorios.application.dto.QueryEspelhoPontoDto;
import com.postechhackaton.relatorios.domain.gateways.KafkaSenderGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RequisitarRegistrosEspelhoPontoUseCaseImplTest {

    @Mock
    private KafkaSenderGateway kafkaSenderGateway;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RequisitarRegistrosEspelhoPontoUseCaseImpl requisitarRegistrosEspelhoPontoUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void requisitar_DeveEnviarMensagemParaKafkaComOsParametrosCorretos() throws Exception {
        QueryEspelhoPontoDto query = QueryEspelhoPontoDto.builder()
                .dataInicio(LocalDateTime.now())
                .dataFim(LocalDateTime.now().plusDays(1))
                .usuarioRequerinte("usuario-teste")
                .usuario("usuario")
                .email("destinatario@example.com")
                .build();

        String queryJson = "{\"dataInicio\":\"2024-03-24T12:00:00\",\"dataFim\":\"2024-03-25T12:00:00\",\"usuarioRequerinte\":\"usuario-teste\",\"usuario\":\"usuario\",\"email\":\"destinatario@example.com\"}";

        when(objectMapper.writeValueAsString(query)).thenReturn(queryJson);

        requisitarRegistrosEspelhoPontoUseCase.requisitar(query);

        verify(kafkaSenderGateway).enviar(eq(RequisitarRegistrosEspelhoPontoUseCaseImpl.TOPIC_BUSCAR_ESPELHO_PONTO_INPUT), eq(queryJson));
    }
}
