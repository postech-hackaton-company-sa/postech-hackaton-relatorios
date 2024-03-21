package com.postechhackaton.relatorios.application.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class RelatoriosControllerTestIT {

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));


    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void criarRelatorio_deveRetornarErro_quandoNaoInformarOEmailDeEnvio() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/relatorios")
                        .header("usuario", "usuario-teste")
                        .param("dataInicio", LocalDate.now().toString())
                        .param("dataFim", LocalDate.now().plusDays(1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Required parameter 'email' is not present."));
    }

    @Test
    void criarRelatorio_devePostarMensagemKafka_quandoChamadaEstiverCorreta() throws Exception {
        Mockito.when(kafkaTemplate.send(Mockito.any(), Mockito.any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/relatorios")
                        .header("usuario", "usuario-teste")
                        .param("dataInicio", LocalDate.now().toString())
                        .param("dataFim", LocalDate.now().plusDays(1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Required parameter 'email' is not present."));
    }

}