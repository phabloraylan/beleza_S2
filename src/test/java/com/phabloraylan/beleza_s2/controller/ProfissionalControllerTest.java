package com.phabloraylan.beleza_s2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phabloraylan.beleza_s2.dto.ProfissionalRequestDTO;
import com.phabloraylan.beleza_s2.dto.ProfissionalResponseDTO;
import com.phabloraylan.beleza_s2.service.ProfissionalService;
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

@WebMvcTest(ProfissionalController.class)
class ProfissionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfissionalService profissionalService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProfissionalResponseDTO profissionalResponseDTO;
    private ProfissionalRequestDTO profissionalRequestDTO;

    @BeforeEach
    void setUp() {
        profissionalResponseDTO = new ProfissionalResponseDTO();
        profissionalResponseDTO.setId(1L);
        profissionalResponseDTO.setNome("Maria Cabeleireira");
        profissionalResponseDTO.setEspecialidades("Corte, Pintura");
        profissionalResponseDTO.setHorariosDisponiveis("09:00 - 17:00");
        profissionalResponseDTO.setTarifa(50.0);
        profissionalResponseDTO.setEstabelecimentoId(1L);

        profissionalRequestDTO = new ProfissionalRequestDTO();
        profissionalRequestDTO.setNome("Maria Cabeleireira");
        profissionalRequestDTO.setEspecialidades("Corte, Pintura");
        profissionalRequestDTO.setHorariosDisponiveis("09:00 - 17:00");
        profissionalRequestDTO.setTarifa(50.0);
        profissionalRequestDTO.setEstabelecimentoId(1L);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve listar todos os profissionais")
    void findAll_ShouldReturnListOfProfissionais() throws Exception {
        when(profissionalService.findAll()).thenReturn(Collections.singletonList(profissionalResponseDTO));

        mockMvc.perform(get("/api/profissionais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(profissionalResponseDTO.getId()))
                .andExpect(jsonPath("$[0].nome").value(profissionalResponseDTO.getNome()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve buscar profissional por ID")
    void findById_ShouldReturnProfissional() throws Exception {
        when(profissionalService.findById(1L)).thenReturn(profissionalResponseDTO);

        mockMvc.perform(get("/api/profissionais/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(profissionalResponseDTO.getId()))
                .andExpect(jsonPath("$.nome").value(profissionalResponseDTO.getNome()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve criar novo profissional")
    void create_ShouldReturnCreatedProfissional() throws Exception {
        when(profissionalService.save(any(ProfissionalRequestDTO.class))).thenReturn(profissionalResponseDTO);

        mockMvc.perform(post("/api/profissionais")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profissionalRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(profissionalResponseDTO.getId()))
                .andExpect(jsonPath("$.nome").value(profissionalResponseDTO.getNome()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve atualizar profissional")
    void update_ShouldReturnUpdatedProfissional() throws Exception {
        when(profissionalService.update(eq(1L), any(ProfissionalRequestDTO.class))).thenReturn(profissionalResponseDTO);

        mockMvc.perform(put("/api/profissionais/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profissionalRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(profissionalResponseDTO.getId()))
                .andExpect(jsonPath("$.nome").value(profissionalResponseDTO.getNome()));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve deletar profissional")
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(profissionalService).delete(1L);

        mockMvc.perform(delete("/api/profissionais/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
