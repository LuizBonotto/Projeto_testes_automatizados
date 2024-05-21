package com.ada.banco.domain.usecase;

import com.ada.banco.domain.exception.ContaJaExisteException;
import com.ada.banco.domain.gateway.ContaGatewayDBFake;
import com.ada.banco.domain.gateway.EmailGateway;
import com.ada.banco.domain.gateway.EmailGatewayDummy;
import com.ada.banco.domain.model.Conta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


public class ConteUseCaseTest {

    private ContaUseCase contaUseCase;
    private ContaGatewayDBFake contaGatewayDBFake;
    private EmailGatewayDummy emailGatewayDummy;

    @BeforeEach
    public void setUp() {
        contaGatewayDBFake = new ContaGatewayDBFake();
        emailGatewayDummy = new EmailGatewayDummy();
        contaUseCase = new ContaUseCase(contaGatewayDBFake, emailGatewayDummy);
    }

    @Test
    public void deveCriarContaCorretamente() throws Exception {
        Conta conta = new Conta(1L, 2L, BigDecimal.ZERO, "Luiz", "12345678941");

        contaUseCase.criar(conta);

        contaGatewayDBFake.salvar(conta);

        Conta contaSalva = contaGatewayDBFake.buscarPorCpf(conta.getCpf());

        Assertions.assertEquals(conta, contaSalva);
    }

    @Test
    public void deveLancarExceptionCasoContaJaExista() {
        Conta conta = new Conta(1L, 2L, BigDecimal.ZERO, "Luiz", "12345678941");
        contaGatewayDBFake.salvar(conta);
        Throwable throwable = Assertions.assertThrows(ContaJaExisteException.class, () -> contaUseCase.criar(conta));
        Assertions.assertEquals("A conta ja existe", throwable.getMessage());
    }
}
