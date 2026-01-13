package com.phabloraylan.beleza_s2.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfissionalRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String especialidades;
    private String horariosDisponiveis;
    private Double tarifa;
    private Long estabelecimentoId;
}
