package com.ada.banco.infra.gateway.http;

import com.ada.banco.domain.gateway.EmailGateway;
import org.springframework.stereotype.Component;

@Component
public class EmailGatewayHttp implements EmailGateway {
    @Override
    public void send(String cpf) {
        // CODIGO COMPLEXO PARA ENVIAR EMAIL
        System.out.println("Enviando email...");
    }
}
