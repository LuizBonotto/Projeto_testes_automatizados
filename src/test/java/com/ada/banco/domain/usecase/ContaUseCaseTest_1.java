package com.ada.banco.domain.usecase;

import com.ada.banco.domain.exception.ContaJaExisteException;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.ContaGatewayDBFake;
import com.ada.banco.domain.gateway.EmailGateway;
import com.ada.banco.domain.gateway.EmailGatewayDummy;
import com.ada.banco.domain.model.Conta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;


import java.math.BigDecimal;

public class ContaUseCaseTest_1 {
    // Ao criar uma conta ela deve ser salva no banco de dados
    // Deve lancar uma exception caso a conta ja exista

    private ContaGateway contaGateway;
    private EmailGateway emailGateway;
    private ContaUseCase contaUseCase;

    @BeforeEach
    public void beforeEach() {
        contaGateway = mock(ContaGateway.class);
        emailGateway = mock(EmailGateway.class);
        contaUseCase = new ContaUseCase(contaGateway, emailGateway);
    }

    @Test
    public void deveLancarExceptionCasoAContaJaExista() {
        // Mockito -> Mocks
        // Dado
        Conta conta =
                new Conta(1L, 2L, 3L, BigDecimal.ZERO, "Pedro", "123456789");
        when(contaGateway.buscarPorCpf("123456789")).thenReturn(conta);

        // When
        Throwable throwable = Assertions.assertThrows(ContaJaExisteException.class, () -> contaUseCase.criar(conta));

        // Then
        Assertions.assertEquals("A conta ja existe", throwable.getMessage());

        verify(contaGateway, never()).salvar(conta);
        verify(emailGateway, never()).send("123456789");
    }

    @Test
    public void deveSalvarAContaComSucesso() throws Exception {
        // Dado
        Conta conta =
                new Conta(1L, 2L, 3L, BigDecimal.ZERO, "Pedro", "123456789");
        when(contaGateway.buscarPorCpf("123456789")).thenReturn(null);
        when(contaGateway.salvar(conta)).thenReturn(conta);


        // Quando// Entao
        Conta contaSalva = contaUseCase.criar(conta);

        verify(contaGateway).salvar(conta);

        verify(contaGateway, times(1)).salvar(conta);
        verify(emailGateway, times(1)).send(any());
        verify(emailGateway, times(1)).send("123456789");

        Assertions.assertEquals(conta,contaSalva);

    }


}

