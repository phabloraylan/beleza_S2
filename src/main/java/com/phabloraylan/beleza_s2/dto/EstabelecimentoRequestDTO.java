package com.phabloraylan.beleza_s2.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class EstabelecimentoRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;

    private String descricao;
    private String horarioFuncionamento;
    private List<String> servicos;
    private List<String> fotos;
}
