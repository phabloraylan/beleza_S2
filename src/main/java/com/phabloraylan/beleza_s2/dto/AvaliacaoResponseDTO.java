package com.phabloraylan.beleza_s2.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AvaliacaoResponseDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private Long estabelecimentoId;
    private String estabelecimentoNome;
    private Long profissionalId;
    private String profissionalNome;
    private Integer nota;
    private String comentario;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
