package com.phabloraylan.beleza_s2.service;

import com.phabloraylan.beleza_s2.entity.Agendamento;
import com.phabloraylan.beleza_s2.entity.Profissional;
import com.phabloraylan.beleza_s2.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceTest {

    @InjectMocks
    private NotificacaoService notificacaoService;

    private Agendamento agendamento;

    @BeforeEach
    void setUp() {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@example.com");

        Profissional profissional = new Profissional();
        profissional.setNome("Profissional Teste");

        agendamento = new Agendamento();
        agendamento.setUsuario(usuario);
        agendamento.setProfissional(profissional);
        agendamento.setDataHora(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve enviar confirmação de agendamento sem erros")
    void enviarConfirmacaoAgendamento_ShouldNotThrowException() {
        assertDoesNotThrow(() -> notificacaoService.enviarConfirmacaoAgendamento(agendamento));
    }

    @Test
    @DisplayName("Deve enviar lembrete de agendamento sem erros")
    void enviarLembreteAgendamento_ShouldNotThrowException() {
        assertDoesNotThrow(() -> notificacaoService.enviarLembreteAgendamento(agendamento));
    }
}
