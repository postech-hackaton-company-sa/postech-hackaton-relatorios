package com.postechhackaton.relatorios.domain.gateways;

public interface EmailGateway {
    void enviar(String mail, String string);
}
