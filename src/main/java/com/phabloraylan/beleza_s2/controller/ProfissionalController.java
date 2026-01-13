package com.phabloraylan.beleza_s2.controller;

import com.phabloraylan.beleza_s2.dto.ProfissionalRequestDTO;
import com.phabloraylan.beleza_s2.dto.ProfissionalResponseDTO;
import com.phabloraylan.beleza_s2.service.ProfissionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
@RequiredArgsConstructor
@Tag(name = "Profissionais", description = "Endpoints para gerenciamento de profissionais")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @GetMapping
    @Operation(summary = "Listar todos os profissionais")
    public ResponseEntity<List<ProfissionalResponseDTO>> findAll() {
        return ResponseEntity.ok(profissionalService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar profissional por ID")
    public ResponseEntity<ProfissionalResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(profissionalService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar um novo profissional")
    public ResponseEntity<ProfissionalResponseDTO> create(@Valid @RequestBody ProfissionalRequestDTO profissionalDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalService.save(profissionalDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um profissional existente")
    public ResponseEntity<ProfissionalResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProfissionalRequestDTO profissionalDTO) {
        return ResponseEntity.ok(profissionalService.update(id, profissionalDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um profissional")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profissionalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
