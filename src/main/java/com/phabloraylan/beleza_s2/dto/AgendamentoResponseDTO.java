package com.phabloraylan.beleza_s2.dto;

import com.phabloraylan.beleza_s2.entity.StatusAgendamento;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendamentoResponseDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private Long profissionalId;
    private String profissionalNome;
    private Long estabelecimentoId;
    private String estabelecimentoNome;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
