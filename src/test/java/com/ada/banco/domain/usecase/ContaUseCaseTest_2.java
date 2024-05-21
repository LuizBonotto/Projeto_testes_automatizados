package com.ada.banco.domain.usecase;

import com.ada.banco.domain.exception.ContaJaExisteException;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.EmailGateway;
import com.ada.banco.domain.model.Conta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;


import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class ContaUseCaseTest_2 {

    @Mock
    private ContaGateway contaGateway;
    @Mock
    private EmailGateway emailGateway;

    @InjectMocks
    private ContaUseCase contaUseCase;

//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }

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

        // Then

        verify(contaGateway).salvar(conta);

        verify(contaGateway, times(1)).salvar(conta);
        verify(emailGateway, times(1)).send(any());
        verify(emailGateway, times(1)).send("123456789");

        Assertions.assertEquals(conta,contaSalva);

    }


}

