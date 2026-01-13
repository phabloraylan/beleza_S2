package com.phabloraylan.beleza_s2.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfissionalResponseDTO {
    private Long id;
    private String nome;
    private String especialidades;
    private String horariosDisponiveis;
    private Double tarifa;
    private Long estabelecimentoId;
    private String estabelecimentoNome;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
