package com.postechhackaton.relatorios.application.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postechhackaton.relatorios.application.dto.RelatorioPeriodoPontoDto;
import com.postechhackaton.relatorios.application.usecase.BuscarDadosFuncionarioUseCaseImpl;
import com.postechhackaton.relatorios.application.usecase.CalcularPontoDiarioUseCaseImpl;
import com.postechhackaton.relatorios.application.usecase.CalcularPontoPeriodoUseCaseImpl;
import com.postechhackaton.relatorios.application.usecase.FormataRelatorioPeriodoUseCaseImpl;
import com.postechhackaton.relatorios.business.enums.StatusPonto;
import com.postechhackaton.relatorios.domain.gateways.EmailGateway;
import com.postechhackaton.relatorios.domain.gateways.KafkaSenderGateway;
import com.postechhackaton.relatorios.domain.usecase.CalcularPontoPeriodoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ReceberEspelhoPontoSubscriberTest {
    @Spy
    private ObjectMapper objectMapper;

    private final CalcularPontoPeriodoUseCase calcularPontoPeriodoUseCase = new CalcularPontoPeriodoUseCaseImpl(new CalcularPontoDiarioUseCaseImpl());

    private final FormataRelatorioPeriodoUseCaseImpl formataRelatorioPeriodoUseCase = new FormataRelatorioPeriodoUseCaseImpl();

    @Mock
    private EmailGateway emailGateway;

    @Mock
    private KafkaSenderGateway kafkaSenderGateway;

    @Mock
    private BuscarDadosFuncionarioUseCaseImpl buscarDadosFuncionarioUseCase;

    @InjectMocks
    private ReceberEspelhoPontoSubscriber receberEspelhoPontoSubscriber;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        receberEspelhoPontoSubscriber = new ReceberEspelhoPontoSubscriber(objectMapper, kafkaSenderGateway, calcularPontoPeriodoUseCase, formataRelatorioPeriodoUseCase, buscarDadosFuncionarioUseCase, emailGateway);
    }

    @Test
    public void testExecutar_CasoDeFalha() {
        String json = "mensagem JSON válida";

        receberEspelhoPontoSubscriber.executar(json);

        verify(kafkaSenderGateway).enviar(eq(ReceberEspelhoPontoSubscriber.TOPIC_BUSCAR_ESPELHO_PONTO_OUTPUT_DLQ), eq(json));
        verifyNoInteractions(emailGateway);
    }

    @Test
    public void executar_deveGerarOsTresTiposDeStatus() throws IOException {
        String json = carregarJson("3_registros_tipos_diferentes.json");
        RelatorioPeriodoPontoDto relatorio = receberEspelhoPontoSubscriber.executar(json);

        assertEquals(3, relatorio.getRegistros().size());
        assertEquals(StatusPonto.INCONSISTENTE, relatorio.getRegistros().get(0).getStatus());
        assertEquals(StatusPonto.NEGATIVO, relatorio.getRegistros().get(1).getStatus());
        assertEquals(StatusPonto.POSITIVO, relatorio.getRegistros().get(2).getStatus());
        verify(emailGateway).enviar(anyString(), any());
        verifyNoInteractions(kafkaSenderGateway);
    }

    private String carregarJson(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource("jsons/" + path);
        byte[] jsonData = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(jsonData, StandardCharsets.UTF_8);
    }

    @Test
    public void executar_deveGerarOsRelatorioPositivo() throws IOException {
        String json = carregarJson("espelho_ponto_positivo.json");
        RelatorioPeriodoPontoDto relatorio = receberEspelhoPontoSubscriber.executar(json);

        assertEquals(1, relatorio.getRegistros().size());
        assertEquals(StatusPonto.POSITIVO, relatorio.getRegistros().get(0).getStatus());
        verify(emailGateway).enviar(anyString(), any());
        verifyNoInteractions(kafkaSenderGateway);
    }

    @Test
    public void executar_deveGerarOsRelatorioNegativo() throws IOException {
        String json = carregarJson("espelho_ponto_negativo.json");
        RelatorioPeriodoPontoDto relatorio = receberEspelhoPontoSubscriber.executar(json);

        assertEquals(1, relatorio.getRegistros().size());
        assertEquals(StatusPonto.NEGATIVO, relatorio.getRegistros().get(0).getStatus());
        verify(emailGateway).enviar(anyString(), any());
        verifyNoInteractions(kafkaSenderGateway);
    }

    @Test
    public void executar_deveGerarOsRelatorioInconsistente() throws IOException {
        String json = carregarJson("espelho_ponto_inconsistente.json");
        RelatorioPeriodoPontoDto relatorio = receberEspelhoPontoSubscriber.executar(json);

        assertEquals(1, relatorio.getRegistros().size());
        assertEquals(StatusPonto.INCONSISTENTE, relatorio.getRegistros().get(0).getStatus());
        verify(emailGateway).enviar(anyString(), any());
        verifyNoInteractions(kafkaSenderGateway);
    }
}