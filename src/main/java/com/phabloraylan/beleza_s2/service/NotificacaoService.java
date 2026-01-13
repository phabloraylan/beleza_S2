package com.phabloraylan.beleza_s2.service;

import com.phabloraylan.beleza_s2.entity.Agendamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificacaoService {

    public void enviarConfirmacaoAgendamento(Agendamento agendamento) {
        log.info("Enviando confirmação para usuário: {} para agendamento em {}", 
                agendamento.getUsuario().getEmail(), agendamento.getDataHora());
        log.info("Enviando confirmação para profissional: {} para agendamento em {}", 
                agendamento.getProfissional().getNome(), agendamento.getDataHora());
    }

    public void enviarLembreteAgendamento(Agendamento agendamento) {
        log.info("Enviando lembrete para usuário: {} para agendamento em {}", 
                agendamento.getUsuario().getEmail(), agendamento.getDataHora());
    }
}
