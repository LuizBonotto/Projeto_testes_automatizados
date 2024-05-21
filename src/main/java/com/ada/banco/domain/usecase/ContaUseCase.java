package com.ada.banco.domain.usecase;

import com.ada.banco.domain.exception.ContaJaExisteException;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.EmailGateway;
import com.ada.banco.domain.model.Conta;

// USECASE - Regras de negocio
// GATEWAY - Acesso a conteudos externos (ex: DB, HTTP)

public class ContaUseCase {// Regras de negocio
    private ContaGateway contaGateway; // Acesso ao banco
    private EmailGateway emailGateway; // Envia um email

    public ContaUseCase(ContaGateway contaGateway, EmailGateway emailGateway) {
        this.contaGateway = contaGateway;
        this.emailGateway = emailGateway;
    }

    public Conta criar(Conta conta) throws Exception {
        if(contaGateway.buscarPorCpf(conta.getCpf()) != null) {
            throw new ContaJaExisteException("A conta ja existe");
        }

        emailGateway.send(conta.getCpf());

        contaGateway.salvar(conta);

        return conta;
    }
}
