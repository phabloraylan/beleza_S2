package com.phabloraylan.beleza_s2.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
