package com.postechhackaton.relatorios.domain.gateways;

public interface KafkaSenderGateway {
    void enviar(String pagamentoJson, String topic);
}
