package com.phabloraylan.beleza_s2.service;

import com.phabloraylan.beleza_s2.entity.Agendamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IntegracaoCalendarioService {

    public void sincronizarCalendario(Agendamento agendamento) {
        log.info("Sincronizando agendamento com calendário externo para usuário: {} e profissional: {}", 
                agendamento.getUsuario().getEmail(), agendamento.getProfissional().getNome());
    }
    
    public void removerDoCalendario(Agendamento agendamento) {
        log.info("Removendo agendamento do calendário externo para usuário: {} e profissional: {}", 
                agendamento.getUsuario().getEmail(), agendamento.getProfissional().getNome());
    }
}
