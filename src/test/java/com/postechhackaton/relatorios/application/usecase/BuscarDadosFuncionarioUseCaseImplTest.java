package com.postechhackaton.relatorios.application.usecase;

import com.postechhackaton.relatorios.business.entities.FuncionarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class BuscarDadosFuncionarioUseCaseImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BuscarDadosFuncionarioUseCaseImpl funcionarioGateway;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(funcionarioGateway, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(funcionarioGateway, "urlApiFuncionarios", "http://exemplo.com/api");
    }

    @Test
    void buscar_ShouldReturnListOfFuncionarioEntity_WhenCalled() {
        String usuarioRequerinte = "usuarioRequerinte";
        String usuarioBusca = "usuarioBusca";
        String urlEsperada = "http://exemplo.com/api/v1/funcionarios?username=usuarioBusca";

        FuncionarioEntity funcionario1 = new FuncionarioEntity();
        FuncionarioEntity[] funcionariosArray = {funcionario1};
        ResponseEntity<FuncionarioEntity[]> responseEntity = new ResponseEntity<>(funcionariosArray, HttpStatus.OK);
        when(restTemplate.exchange(eq(urlEsperada), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        FuncionarioEntity funcionario = funcionarioGateway.buscar(usuarioRequerinte, usuarioBusca);

        assertNotNull(funcionario);
    }
}