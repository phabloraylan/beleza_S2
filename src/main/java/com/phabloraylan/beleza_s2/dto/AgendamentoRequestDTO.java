package com.phabloraylan.beleza_s2.dto;

import com.phabloraylan.beleza_s2.entity.StatusAgendamento;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendamentoRequestDTO {
    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "ID do profissional é obrigatório")
    private Long profissionalId;

    @NotNull(message = "ID do estabelecimento é obrigatório")
    private Long estabelecimentoId;

    @NotNull(message = "Data e hora do agendamento são obrigatórios")
    private LocalDateTime dataHora;

    private StatusAgendamento status;
}
