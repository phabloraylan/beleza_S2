package com.phabloraylan.beleza_s2.controller;

import com.phabloraylan.beleza_s2.dto.AgendamentoRequestDTO;
import com.phabloraylan.beleza_s2.dto.AgendamentoResponseDTO;
import com.phabloraylan.beleza_s2.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
@Tag(name = "Agendamentos", description = "Endpoints para gerenciamento de agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @GetMapping
    @Operation(summary = "Listar todos os agendamentos")
    public ResponseEntity<List<AgendamentoResponseDTO>> findAll() {
        return ResponseEntity.ok(agendamentoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar agendamento por ID")
    public ResponseEntity<AgendamentoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.findById(id));
    }

    @GetMapping("/estabelecimento/{estabelecimentoId}")
    @Operation(summary = "Listar agendamentos por estabelecimento")
    public ResponseEntity<List<AgendamentoResponseDTO>> findByEstabelecimentoId(@PathVariable Long estabelecimentoId) {
        return ResponseEntity.ok(agendamentoService.findByEstabelecimentoId(estabelecimentoId));
    }

    @PostMapping
    @Operation(summary = "Criar um novo agendamento")
    public ResponseEntity<AgendamentoResponseDTO> create(@Valid @RequestBody AgendamentoRequestDTO agendamentoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.save(agendamentoDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um agendamento existente")
    public ResponseEntity<AgendamentoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AgendamentoRequestDTO agendamentoDTO) {
        return ResponseEntity.ok(agendamentoService.update(id, agendamentoDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um agendamento")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agendamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
