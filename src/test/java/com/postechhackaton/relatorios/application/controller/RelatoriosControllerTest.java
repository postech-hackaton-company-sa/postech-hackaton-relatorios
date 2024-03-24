package com.postechhackaton.relatorios.application.controller;

import com.postechhackaton.relatorios.application.dto.QueryEspelhoPontoDto;
import com.postechhackaton.relatorios.domain.usecase.RequisitarRegistrosEspelhoPontoUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDateTime;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RelatoriosControllerTest {

    @Mock
    private RequisitarRegistrosEspelhoPontoUseCase requisitarRegistrosEspelhoPontoUseCase;

    @InjectMocks
    private RelatoriosController relatoriosController;

    @Test
    void criarRelatorio_deveChamarUseCase_quandoParametrosValidos() throws Exception {
        String usuarioRequerinte = "usuario-teste";
        String roles = "admin";
        String email = "teste@example.com";
        String usuario = "usuario-relatorio";
        LocalDateTime dataInicio = LocalDateTime.now().minusDays(1);
        LocalDateTime dataFim = LocalDateTime.now();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("username", usuarioRequerinte);
        request.addHeader("roles", roles);

        relatoriosController.criarRelatorio(usuarioRequerinte, roles, email, usuario, dataInicio, dataFim);

        verify(requisitarRegistrosEspelhoPontoUseCase, times(1)).requisitar(any(QueryEspelhoPontoDto.class));
    }
}
