package com.phabloraylan.beleza_s2.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EstabelecimentoResponseDTO {
    private Long id;
    private String nome;
    private String endereco;
    private String descricao;
    private String horarioFuncionamento;
    private List<String> servicos;
    private List<String> fotos;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
