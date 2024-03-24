package com.postechhackaton.relatorios.application.usecase;

import com.postechhackaton.relatorios.business.entities.FuncionarioEntity;
import com.postechhackaton.relatorios.domain.usecase.BuscarDadosFuncionarioUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class BuscarDadosFuncionarioUseCaseImpl implements BuscarDadosFuncionarioUseCase {

    private final RestTemplate restTemplate;

    @Value(value = "${apis.funcionarios.url}")
    private String urlApiFuncionarios;
    public BuscarDadosFuncionarioUseCaseImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public FuncionarioEntity buscar(String usuarioRequerinte, String usuarioBusca) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("username", usuarioRequerinte);
        headers.add("roles", "admin");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
        String url = urlApiFuncionarios + "/v1/funcionarios?username=" + usuarioBusca;

        ResponseEntity<FuncionarioEntity[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, FuncionarioEntity[].class);
        return Objects.requireNonNull(responseEntity.getBody())[0];
    }

}
