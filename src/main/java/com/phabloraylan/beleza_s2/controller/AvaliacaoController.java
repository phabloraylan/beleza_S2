package com.phabloraylan.beleza_s2.controller;

import com.phabloraylan.beleza_s2.dto.AvaliacaoRequestDTO;
import com.phabloraylan.beleza_s2.dto.AvaliacaoResponseDTO;
import com.phabloraylan.beleza_s2.service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
@Tag(name = "Avaliações", description = "Endpoints para gerenciamento de avaliações")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @GetMapping
    @Operation(summary = "Listar todas as avaliações")
    public ResponseEntity<List<AvaliacaoResponseDTO>> findAll() {
        return ResponseEntity.ok(avaliacaoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar avaliação por ID")
    public ResponseEntity<AvaliacaoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar uma nova avaliação")
    public ResponseEntity<AvaliacaoResponseDTO> create(@Valid @RequestBody AvaliacaoRequestDTO avaliacaoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoService.save(avaliacaoDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma avaliação existente")
    public ResponseEntity<AvaliacaoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AvaliacaoRequestDTO avaliacaoDTO) {
        return ResponseEntity.ok(avaliacaoService.update(id, avaliacaoDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma avaliação")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        avaliacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
