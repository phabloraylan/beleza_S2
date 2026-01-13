package com.phabloraylan.beleza_s2.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AvaliacaoRequestDTO {
    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;

    private Long estabelecimentoId;
    private Long profissionalId;

    @NotNull(message = "Nota é obrigatória")
    @Min(value = 1, message = "A nota deve ser no mínimo 1")
    @Max(value = 5, message = "A nota deve ser no máximo 5")
    private Integer nota;

    private String comentario;
}
