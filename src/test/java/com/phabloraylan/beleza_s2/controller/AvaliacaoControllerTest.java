package com.phabloraylan.beleza_s2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phabloraylan.beleza_s2.dto.AvaliacaoRequestDTO;
import com.phabloraylan.beleza_s2.dto.AvaliacaoResponseDTO;
import com.phabloraylan.beleza_s2.service.AvaliacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvaliacaoController.class)
class AvaliacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvaliacaoService avaliacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    private AvaliacaoResponseDTO avaliacaoResponseDTO;
    private AvaliacaoRequestDTO avaliacaoRequestDTO;

    @BeforeEach
    void setUp() {
        avaliacaoResponseDTO = new AvaliacaoResponseDTO();
        avaliacaoResponseDTO.setId(1L);
        avaliacaoResponseDTO.setUsuarioId(1L);
        avaliacaoResponseDTO.setEstabelecimentoId(1L);
        avaliacaoResponseDTO.setProfissionalId(1L);
        avaliacaoResponseDTO.setNota(5);
        avaliacaoResponseDTO.setComentario("Excelente serviço!");

        avaliacaoRequestDTO = new AvaliacaoRequestDTO();
        avaliacaoRequestDTO.setUsuarioId(1L);
        avaliacaoRequestDTO.setEstabelecimentoId(1L);
        avaliacaoRequestDTO.setProfissionalId(1L);
        avaliacaoRequestDTO.setNota(5);
        avaliacaoRequestDTO.setComentario("Excelente serviço!");
    }

    @Test
    @WithMockUser
    @DisplayName("Deve listar todas as avaliações")
    void findAll_ShouldReturnListOfAvaliacoes() throws Exception {
        when(avaliacaoService.findAll()).thenReturn(Collections.singletonList(avaliacaoResponseDTO));

        mockMvc.perform(get("/api/avaliacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(avaliacaoResponseDTO.getId()))
                .andExpect(jsonPath("$[0].nota").value(avaliacaoResponseDTO.getNota()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve buscar avaliação por ID")
    void findById_ShouldReturnAvaliacao() throws Exception {
        when(avaliacaoService.findById(1L)).thenReturn(avaliacaoResponseDTO);

        mockMvc.perform(get("/api/avaliacoes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(avaliacaoResponseDTO.getId()))
                .andExpect(jsonPath("$.nota").value(avaliacaoResponseDTO.getNota()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve criar nova avaliação")
    void create_ShouldReturnCreatedAvaliacao() throws Exception {
        when(avaliacaoService.save(any(AvaliacaoRequestDTO.class))).thenReturn(avaliacaoResponseDTO);

        mockMvc.perform(post("/api/avaliacoes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avaliacaoRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(avaliacaoResponseDTO.getId()))
                .andExpect(jsonPath("$.nota").value(avaliacaoResponseDTO.getNota()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve atualizar avaliação")
    void update_ShouldReturnUpdatedAvaliacao() throws Exception {
        when(avaliacaoService.update(eq(1L), any(AvaliacaoRequestDTO.class))).thenReturn(avaliacaoResponseDTO);

        mockMvc.perform(put("/api/avaliacoes/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avaliacaoRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(avaliacaoResponseDTO.getId()))
                .andExpect(jsonPath("$.nota").value(avaliacaoResponseDTO.getNota()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve deletar avaliação")
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(avaliacaoService).delete(1L);

        mockMvc.perform(delete("/api/avaliacoes/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
