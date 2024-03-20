package com.postechhackaton.relatorios.application.controller;

import com.postechhackaton.relatorios.business.enums.TipoRegistroPontoEletronico;
import com.postechhackaton.relatorios.infra.database.entities.PontoEletronico;
import com.postechhackaton.relatorios.infra.database.repositories.PontoEletronicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RelatoriosControllerTestIT {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PontoEletronicoRepository pontoEletronicoRepository;

    @BeforeEach
    void setUp() {
        // Limpar dados de teste antes de cada execução de teste
        pontoEletronicoRepository.deleteAll();
    }

    @Test
    void criarRelatorio_deveCalcularRelatorio_quandoCalculoTiverMultiplasEntradasEmUmDia() throws Exception {
        pontoEletronicoRepository.save(stubPontoEletronico(LocalDateTime.now().minusHours(10), TipoRegistroPontoEletronico.ENTRADA));
        pontoEletronicoRepository.save(stubPontoEletronico(LocalDateTime.now().minusHours(9), TipoRegistroPontoEletronico.SAIDA));
        pontoEletronicoRepository.save(stubPontoEletronico(LocalDateTime.now().minusHours(8), TipoRegistroPontoEletronico.ENTRADA));
        pontoEletronicoRepository.save(stubPontoEletronico(LocalDateTime.now().minusHours(6), TipoRegistroPontoEletronico.SAIDA));
        pontoEletronicoRepository.save(stubPontoEletronico(LocalDateTime.now().minusHours(5), TipoRegistroPontoEletronico.ENTRADA));
        pontoEletronicoRepository.save(stubPontoEletronico(LocalDateTime.now(), TipoRegistroPontoEletronico.SAIDA));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/relatorios")
                        .header("usuario", "usuario-teste")
                        .param("dataInicio", LocalDate.now().toString())
                        .param("dataFim", LocalDate.now().plusDays(1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalHorasTrabalhadas").value(8));
    }

    private static PontoEletronico stubPontoEletronico(LocalDateTime data, TipoRegistroPontoEletronico tipo) {
        return PontoEletronico.builder()
                .id(UUID.randomUUID())
                .usuario("usuario-teste")
                .data(data)
                .tipo(tipo)
                .build();
    }
}