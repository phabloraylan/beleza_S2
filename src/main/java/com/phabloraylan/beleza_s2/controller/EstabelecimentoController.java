package com.phabloraylan.beleza_s2.controller;

import com.phabloraylan.beleza_s2.dto.EstabelecimentoRequestDTO;
import com.phabloraylan.beleza_s2.dto.EstabelecimentoResponseDTO;
import com.phabloraylan.beleza_s2.service.EstabelecimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estabelecimentos")
@RequiredArgsConstructor
@Tag(name = "Estabelecimentos", description = "Endpoints para gerenciamento de estabelecimentos")
public class EstabelecimentoController {

    private final EstabelecimentoService estabelecimentoService;

    @GetMapping
    @Operation(summary = "Listar todos os estabelecimentos")
    public ResponseEntity<List<EstabelecimentoResponseDTO>> findAll() {
        return ResponseEntity.ok(estabelecimentoService.findAll());
    }

    @GetMapping("/busca")
    @Operation(summary = "Buscar estabelecimentos por critérios")
    public ResponseEntity<List<EstabelecimentoResponseDTO>> search(
            @Parameter(description = "Nome do estabelecimento") @RequestParam(required = false) String nome,
            @Parameter(description = "Endereço/Localização") @RequestParam(required = false) String endereco,
            @Parameter(description = "Serviço oferecido") @RequestParam(required = false) String servico,
            @Parameter(description = "Preço mínimo") @RequestParam(required = false) Double precoMinimo,
            @Parameter(description = "Preço máximo") @RequestParam(required = false) Double precoMaximo) {
        return ResponseEntity.ok(estabelecimentoService.search(nome, endereco, servico, precoMinimo, precoMaximo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar estabelecimento por ID")
    public ResponseEntity<EstabelecimentoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(estabelecimentoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar um novo estabelecimento")
    public ResponseEntity<EstabelecimentoResponseDTO> create(@Valid @RequestBody EstabelecimentoRequestDTO estabelecimentoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estabelecimentoService.save(estabelecimentoDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um estabelecimento existente")
    public ResponseEntity<EstabelecimentoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EstabelecimentoRequestDTO estabelecimentoDTO) {
        return ResponseEntity.ok(estabelecimentoService.update(id, estabelecimentoDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um estabelecimento")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        estabelecimentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
